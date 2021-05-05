package com.revature.exception;


public class PasswordHashException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PasswordHashException() {
	}

	public PasswordHashException(String message) {
		super(message);
	}

	public PasswordHashException(Throwable cause) {
		super(cause);
	}

	public PasswordHashException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordHashException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
