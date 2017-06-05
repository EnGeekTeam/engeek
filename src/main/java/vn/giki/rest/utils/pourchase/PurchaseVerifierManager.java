package vn.giki.rest.utils.pourchase;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.pay.Transaction;

public class PurchaseVerifierManager {
	/** Default if no verifier was found for a store. */
	private boolean defaultIfNoVerifierFound;

	/** The verifier implementations. */
	private Map<String, PurchaseVerifier> verifiers;

	public PurchaseVerifierManager () {
		this(false);
	}

	public PurchaseVerifierManager (boolean defaultIfNoVerifierFound) {
		this.defaultIfNoVerifierFound = defaultIfNoVerifierFound;
		this.verifiers = new HashMap<String, PurchaseVerifier>(16);
	}

	public void addVerifier (PurchaseVerifier verifier) {
		verifiers.put(verifier.storeName(), verifier);
	}

	public void removeVerifier (PurchaseVerifier verifier) {
		verifiers.remove(verifier.storeName());
	}

	/** Returns true if a transaction is deemed valid.
	 * <p>
	 * IMPORTANT: will return "defaultIfNoVerifierFound" if no verifier was found for the given transaction.
	 * 
	 * @param transaction The transaction to verify.
	 * @return True for considered valid. */
	public boolean isValid (Transaction transaction) {
		// find the verifier and verify via verifier if a purchase is valid
		PurchaseVerifier verifier = verifiers.get(transaction.getStoreName());
		if (verifier == null) {
			return defaultIfNoVerifierFound;
		} else {
			return verifier.isValid(transaction);
		}
	}
}
