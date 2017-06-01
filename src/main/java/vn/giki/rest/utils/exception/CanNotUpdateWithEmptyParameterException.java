package vn.giki.rest.utils.exception;

public class CanNotUpdateWithEmptyParameterException extends Exception {
	private static final long serialVersionUID = -6035770474792862632L;
	private static final String DEFAULT_ERROR_MESSAGE = "Sorry! No parameter to update";

	public CanNotUpdateWithEmptyParameterException() {
		super(DEFAULT_ERROR_MESSAGE);
	}

	public CanNotUpdateWithEmptyParameterException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CanNotUpdateWithEmptyParameterException(String message, Throwable cause) {
		super(message, cause);
	}

	public CanNotUpdateWithEmptyParameterException(String message) {
		super(message);
	}

	public CanNotUpdateWithEmptyParameterException(Throwable cause) {
		super(cause);
	}

}
