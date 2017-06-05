package vn.giki.rest.utils.exception;

public class TokenInvalidException extends Exception {
	private static final long serialVersionUID = -6035770474792862632L;
	private static final String DEFAULT_ERROR_MESSAGE = "Token invalid";

	public TokenInvalidException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public TokenInvalidException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TokenInvalidException(String message, Throwable cause) {
		super(message, cause);
	}

	public TokenInvalidException(String message) {
		super(message);
	}

	public TokenInvalidException(Throwable cause) {
		super(cause);
	}

}
