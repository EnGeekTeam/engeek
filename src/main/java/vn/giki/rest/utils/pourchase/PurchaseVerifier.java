package vn.giki.rest.utils.pourchase;

import com.badlogic.gdx.pay.Transaction;

public interface PurchaseVerifier {

	/** Returns the store name this verifier is used for. */
	String storeName();

	/** Returns true if the transaction was determined valid. */
	boolean isValid(Transaction transaction);
}
