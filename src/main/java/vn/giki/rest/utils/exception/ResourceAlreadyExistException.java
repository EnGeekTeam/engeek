package vn.giki.rest.utils.exception;

public class ResourceAlreadyExistException extends Exception {
	private static final long serialVersionUID = -6035770474792862632L;
	private static final String DEFAULT_ERROR_MESSAGE = "Resource already exist!";

	public ResourceAlreadyExistException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public ResourceAlreadyExistException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public ResourceAlreadyExistException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResourceAlreadyExistException(String message) {
		super(message);
	}

	public ResourceAlreadyExistException(Throwable cause) {
		super(cause);
	}

}
