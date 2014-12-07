package com.phoenix.dal.hibernate;


import com.phoenix.exceptions.ChainedException;

public class ConverterException extends ChainedException {

	/**
	 *
	 */
	private static final long serialVersionUID = -1982581470342054105L;

	public ConverterException() {
		super();
	}

	public ConverterException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConverterException(String message) {
		super(message);
	}

	public ConverterException(Throwable cause) {
		super(cause);
	}

}
