package vn.giki.rest.utils;

public interface Constant {

	interface USER {
		Object HINT_DEFAULT = 20;
		
		int STATE_PAYMENT_UNPAID = 0;
		int STATE_PAYMENT_PAID  = 1;
		
		
	}
	
	interface PACKAGE{
		String ID_FREE = "ANT";
	}
	
	interface PURCHASE_GOOGLE{
		//Replace your value here....
		String CLIENT_ID = "666918469015-bkrvoemjm7gngb9fmptsido9aan6e69l.apps.googleusercontent.com";
		String SERVICE_ACCOUNT = "giki-demo@api-7003935727596636686-439890.iam.gserviceaccount.com";
		
		String PRODUCT_MONTHLY = "vn.giki.***_monthly";
		String PRODUCT_FOREVER = "vn.giki.***_forever";
	}
	
	interface PURCHASE_APPLE{
		// sandbox URL
		String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
		// production URL
		String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";

		String PASS_WORD = "0ed1c2da960f4c0fb6a5b9100750e4ba";
		
		//name product inapp
		String PRODUCT_MONTHLY = "monthly";
		String PRODUCT_FOREVER = "forever";
	}

}
