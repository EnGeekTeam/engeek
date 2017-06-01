package vn.giki.rest.utils.exception;

public class TokenClientException extends Exception {
	private static final long serialVersionUID = -6035770474792862632L;
	private static final String DEFAULT_ERROR_MESSAGE = "Token client invalid";

	public TokenClientException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public TokenClientException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TokenClientException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenClientException(String message) {
		super(message);
	}

	public TokenClientException(Throwable cause) {
		super(cause);
	}

}
