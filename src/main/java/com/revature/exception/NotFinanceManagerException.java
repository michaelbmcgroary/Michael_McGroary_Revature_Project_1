package com.revature.exception;


public class NotFinanceManagerException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NotFinanceManagerException() {
	}

	public NotFinanceManagerException(String message) {
		super(message);
	}

	public NotFinanceManagerException(Throwable cause) {
		super(cause);
	}

	public NotFinanceManagerException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFinanceManagerException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
