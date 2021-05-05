package com.revature.exception;


@SuppressWarnings("serial")
public class ReimbursementDenyException extends Exception {

	public ReimbursementDenyException() {
	}

	public ReimbursementDenyException(String message) {
		super(message);
	}

	public ReimbursementDenyException(Throwable cause) {
		super(cause);
	}

	public ReimbursementDenyException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReimbursementDenyException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
