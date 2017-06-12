package vn.giki.rest.utils.pourchase;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import vn.giki.rest.utils.Constant;

public class PurchaseVerifierApple {

	public static HashMap<String, Object> getReceipt(String receiptData) throws Exception {

		HashMap<String, Object> result = new HashMap<>();
		String json = String.format("{\"receipt-data\": \"%s\", \"password\":\"%s\" }", receiptData,
				Constant.PURCHASE_APPLE.PASS_WORD);

		final URL url = new URL(Constant.PURCHASE_APPLE.SANDBOX_URL);
		final HttpURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setDoOutput(true);
		conn.setRequestProperty("Content-Type", "application/json");
		conn.setRequestProperty("Accept", "application/json");
		final OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
		wr.write(json);
		wr.flush();

		// obtain the response
		final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		StringBuffer stringBuffer = new StringBuffer();

		String line;

		while ((line = rd.readLine()) != null) {
			stringBuffer.append(line);
		}

		wr.close();
		rd.close();

		JSONObject jsonData = new JSONObject(stringBuffer.toString());
		int status = jsonData.getInt("status");
		if (status == 0) {
			JSONObject jsonReceipt = jsonData.getJSONObject("receipt");
			JSONArray jsonInApp = jsonReceipt.getJSONArray("in_app");
			JSONObject jsonPay = (JSONObject) jsonInApp.get(0);

			System.out.println(jsonPay);

			result.put("product_id", jsonPay.get("product_id"));
			result.put("purchase_date_ms", jsonPay.get("purchase_date_ms"));
			if (jsonPay.has("expires_date_ms")) {
				result.put("expires_date_ms", jsonPay.get("expires_date_ms"));
			}
		} else {
			throw new Exception("Error code: " + status);
		}

		return result;
	}

	public static void main(String[] args) throws Exception {
		getReceipt(
				"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");
	}

}
