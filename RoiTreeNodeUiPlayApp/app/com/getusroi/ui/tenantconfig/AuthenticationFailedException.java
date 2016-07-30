package com.getusroi.ui.tenantconfig;

public class AuthenticationFailedException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3065822264584030602L;

	public AuthenticationFailedException() {
		super();
	}

	public AuthenticationFailedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public AuthenticationFailedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationFailedException(String message) {
		super(message);
	}

	public AuthenticationFailedException(Throwable cause) {
		super(cause);
	}

}
