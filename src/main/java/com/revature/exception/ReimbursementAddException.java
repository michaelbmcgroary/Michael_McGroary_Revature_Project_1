package com.revature.exception;


public class ReimbursementAddException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ReimbursementAddException() {
	}

	public ReimbursementAddException(String message) {
		super(message);
	}

	public ReimbursementAddException(Throwable cause) {
		super(cause);
	}

	public ReimbursementAddException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReimbursementAddException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
