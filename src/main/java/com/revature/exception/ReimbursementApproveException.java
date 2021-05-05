package com.revature.exception;

@SuppressWarnings("serial")
public class ReimbursementApproveException extends Exception {

	public ReimbursementApproveException() {
	}

	public ReimbursementApproveException(String message) {
		super(message);
	}

	public ReimbursementApproveException(Throwable cause) {
		super(cause);
	}

	public ReimbursementApproveException(String message, Throwable cause) {
		super(message, cause);
	}

	public ReimbursementApproveException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
