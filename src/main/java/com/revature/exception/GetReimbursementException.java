package com.revature.exception;


public class GetReimbursementException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GetReimbursementException() {
	}

	public GetReimbursementException(String message) {
		super(message);
	}

	public GetReimbursementException(Throwable cause) {
		super(cause);
	}

	public GetReimbursementException(String message, Throwable cause) {
		super(message, cause);
	}

	public GetReimbursementException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
