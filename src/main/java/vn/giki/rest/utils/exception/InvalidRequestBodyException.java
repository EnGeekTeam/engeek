package vn.giki.rest.utils.exception;

public class InvalidRequestBodyException extends Exception {
	private static final long serialVersionUID = -6035770474792862632L;
	private static final String DEFAULT_ERROR_MESSAGE = "Invalid request body! Check id field";

	public InvalidRequestBodyException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public InvalidRequestBodyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public InvalidRequestBodyException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidRequestBodyException(String message) {
		super(message);
	}

	public InvalidRequestBodyException(Throwable cause) {
		super(cause);
	}

}
