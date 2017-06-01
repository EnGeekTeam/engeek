package vn.giki.rest.utils.exception;

public class ResourceNotFoundException extends Exception {
	private static final long serialVersionUID = 4850948941093541600L;
	private static final String DEFAULT_ERROR_MESSAGE = "Resource not found";

	public ResourceNotFoundException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public ResourceNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceNotFoundException(String message) {
		super(message);
	}

	public ResourceNotFoundException(Throwable cause) {
		super(cause);
	}

}
