package vn.giki.rest.utils;

public interface Constant {

	interface USER {
		Object HINT_DEFAULT = 20;

		int HINT_GOLD = 30;
		int DATE_UP_HINT = 30;
		int STATE_PAYMENT_UNPAID = 0;
		int STATE_PAYMENT_PAID = 1;
		int STATE_CLOSE = 2;

	}
	
	interface PLATFORM{
		String GOOGLE = "google";
		String FACEBOOK = "facebook";
		
		String ANDROID = "android";
		String IOS = "ios";
				
	}
	

	/*interface PURCHASE_GOfOGLE{
		//Replace your value here....
		String CLIENT_ID = "179101610531-1t9g6p7aibpjhu5e0kc6jmasbk2q18rr.apps.googleusercontent.com";
				//"666918469015-bkrvoemjm7gngb9fmptsido9aan6e69l.apps.googleusercontent.com";*/
	interface PACKAGE {
		String ID_FREE = "ANT";
	}

	interface PURCHASE_GOOGLE {
		// Replace your value here....
	//	String CLIENT_ID = "179101610531-1t9g6p7aibpjhu5e0kc6jmasbk2q18rr.apps.googleusercontent.com";
		String CLIENT_ID_ANDROID = "179101610531-1t9g6p7aibpjhu5e0kc6jmasbk2q18rr.apps.googleusercontent.com";
		String CLIENT_ID_IOS = "179101610531-lhetm7d28gj4brp0cdc2irl2tjln4ogs.apps.googleusercontent.com";

		String SERVICE_ACCOUNT = "test-payment@engeek2.iam.gserviceaccount.com";

		//You must replace this value. It's ID of inapp forever 
		String PRODUCT_FOREVER= "engeek.forever";
	}

	interface PURCHASE_APPLE {
		// sandbox URL
		String SANDBOX_URL = "https://sandbox.itunes.apple.com/verifyReceipt";
		// production URL
		String PRODUCTION_URL = "https://buy.itunes.apple.com/verifyReceipt";

		String PASS_WORD = "0ed1c2da960f4c0fb6a5b9100750e4ba";

		// name product inapp
		String PRODUCT_MONTHLY = "monthly";
		String PRODUCT_FOREVER = "forever";
	}

}
