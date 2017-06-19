package vn.giki.rest.utils;

public interface Constant {

	interface USER {
		Object HINT_DEFAULT = 20;

		int STATE_PAYMENT_UNPAID = 0;
		int STATE_PAYMENT_PAID = 1;
		int STATE_CLOSE = 2;

	}
	
	interface PLATFORM{
		String GOOGLE = "google";
		String FACEBOOK = "facebook";
	}
	

	interface PACKAGE {
		String ID_FREE = "ANT";
	}

	interface PURCHASE_GOOGLE {
		// Replace your value here....
		String CLIENT_ID_ANDROID = "274987452778-vu3hblfqc2r3ln0j8u7ddh0dcjmsedke.apps.googleusercontent.com";
		String CLIENT_ID_IOS = "274987452778-fvigei0jp376lo9cvgb87tso4eepkci9.apps.googleusercontent.com";

		String SERVICE_ACCOUNT = "giki-demo@api-7003935727596636686-439890.iam.gserviceaccount.com";

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
