package vn.giki.rest.utils.exception;

public class GameException extends Exception {
	private static final String DEFAULT_ERROR_MESSAGE = "You haven't learnt enough words to play this game";
	
	public GameException(){
		super(DEFAULT_ERROR_MESSAGE);
	}
	public GameException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public GameException(String message, Throwable cause) {
		super(message, cause);
	}

	public GameException(String message) {
		super(message);
	}

	public GameException(Throwable cause) {
		super(cause);
	}
	
}
