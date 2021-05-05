package com.revature.exception;


@SuppressWarnings("serial")
public class ReimbursementNotFoundException extends Exception {

	public ReimbursementNotFoundException() {
	}

	public ReimbursementNotFoundException(String message) {
		super(message);
	}

	public ReimbursementNotFoundException(Throwable cause) {
		super(cause);
	}

	public ReimbursementNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReimbursementNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
