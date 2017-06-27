package vn.giki.rest.controller;

import java.util.HashMap;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import vn.giki.rest.dao.PayDAO;
import vn.giki.rest.dao.UserDAO;
import vn.giki.rest.entity.Pay;
import vn.giki.rest.utils.Constant;
import vn.giki.rest.utils.pourchase.PurchaseVerifierApple;
import vn.giki.rest.utils.pourchase.PurchaseVerifierGoogle;

@RestController
public class PayController {
	
	@Autowired
	private PayDAO payDAO;
	
	@Autowired
	private UserDAO userDAO;

	@GetMapping("/check_pay")
	@ResponseBody
	public String checkPay() throws Exception{
		StringBuffer result = new StringBuffer();
		List<Pay> listPay = payDAO.getListPayByDate();
		
		for(Pay p :listPay){
			System.out.println(p.getId() + " : " + p.getProduct());
			String record = "[ID: " + p.getId() + "] - [Platform: " + p.getPlatform() + "] - [Product: " + p.getProduct() + "] - [DateStart: " + p.getDateStart() + "] - [DateEnd: " + p.getDateEnd()+"]";
			result.append(record);
			result.append("\n\r");
			if (p.getPlatform().equals(Constant.PLATFORM.ANDROID)){
				checkAndroid(p);
			}  else if (p.getPlatform().equals(Constant.PLATFORM.IOS)){
				checkIOS(p);
			}
			
			payDAO.updateLastCheck(p.getId());
		}
		
		System.out.println(result.toString());
		
		return result.toString();
	}
	
	private void checkAndroid(Pay pay) throws Exception{
		String resData = PurchaseVerifierGoogle.getData(pay.getPackages(), pay.getProduct(), pay.getReceipt());

		System.out.println(resData);

		JSONObject jsonObj = new JSONObject(resData);

		long expiryTimeMillis = jsonObj.getLong("expiryTimeMillis");
		long startTimeMillis = jsonObj.getLong("startTimeMillis");

		long timeTmp = System.currentTimeMillis();
		System.out.println(timeTmp);

		if (expiryTimeMillis > timeTmp) {
			userDAO.updatePurches(pay.getUserId(), startTimeMillis, expiryTimeMillis, Constant.USER.STATE_PAYMENT_PAID,
					pay.getProduct(), false);
		} else {
			userDAO.updatePurches(pay.getUserId(), startTimeMillis, expiryTimeMillis, Constant.USER.STATE_PAYMENT_UNPAID,
					pay.getProduct(), false);
		}
	}
	
	private void checkIOS(Pay pay) throws Exception{
		JSONObject json = new JSONObject(pay.getReceipt());
		long timeTmp = System.currentTimeMillis();

		String dataReceipt = json.getString("receipt-data");

		HashMap<String, Object> infoPurcha = PurchaseVerifierApple.getReceipt(dataReceipt);

		if (!infoPurcha.containsKey("product_id")) {
			userDAO.updatePurches(pay.getUserId(), 0, 0, Constant.USER.STATE_PAYMENT_UNPAID, "", false);
		}

		if (infoPurcha.containsKey("cancellation_date")) {
			userDAO.updatePurches(0, Long.parseLong((String) infoPurcha.get("purchase_date_ms")), 0,
					Constant.USER.STATE_CLOSE, (String) infoPurcha.get("product_id"), false);
		} else {
			if (infoPurcha.containsKey("expires_date_ms")) {
				long expires_date_ms = (long) infoPurcha.get("expires_date_ms");
				if (expires_date_ms>timeTmp){
					userDAO.updatePurches(pay.getUserId(), Long.parseLong((String) infoPurcha.get("purchase_date_ms")),
							Long.parseLong((String) infoPurcha.get("expires_date_ms")),
							Constant.USER.STATE_PAYMENT_PAID, (String) infoPurcha.get("product_id"), false);
				}  else {
					userDAO.updatePurches(pay.getUserId(), Long.parseLong((String) infoPurcha.get("purchase_date_ms")),
							Long.parseLong((String) infoPurcha.get("expires_date_ms")),
							Constant.USER.STATE_PAYMENT_UNPAID, (String) infoPurcha.get("product_id"), false);
				}
			} else {
				userDAO.updatePurches(pay.getUserId(), Long.parseLong((String) infoPurcha.get("purchase_date_ms")), 0,
						Constant.USER.STATE_PAYMENT_PAID, (String) infoPurcha.get("product_id"), false);
			}
		}
	}
}
