package com.amazon.exception;

public class AmazonException extends RuntimeException {

	private String message = "";

	public AmazonException() {

	}

	public AmazonException(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return this.message;
	}

}
