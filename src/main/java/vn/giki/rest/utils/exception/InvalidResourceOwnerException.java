package vn.giki.rest.utils.exception;

public class InvalidResourceOwnerException extends Exception {
	private static final long serialVersionUID = -6035770474792862632L;
	private static final String DEFAULT_ERROR_MESSAGE = "Invalid resource owner!";

	public InvalidResourceOwnerException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public InvalidResourceOwnerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidResourceOwnerException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidResourceOwnerException(String message) {
		super(message);
	}

	public InvalidResourceOwnerException(Throwable cause) {
		super(cause);
	}

}
