package com.revature.exception;


@SuppressWarnings("serial")
public class NoRecieptException extends Exception {

	public NoRecieptException() {
	}

	public NoRecieptException(String message) {
		super(message);
	}

	public NoRecieptException(Throwable cause) {
		super(cause);
	}

	public NoRecieptException(String message, Throwable cause) {
		super(message, cause);
	}

	public NoRecieptException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
