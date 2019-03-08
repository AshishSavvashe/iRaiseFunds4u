package com.appbell.iraisefund4u.common.exception;

public class ValidationException extends Exception {

	private static final long serialVersionUID = 9L;

	private int msgId;
	private String msg;

	public ValidationException(int messageId) {
		msgId = messageId;
	}

	public ValidationException(String message) {
		msg = message;
	}

	public int getMessageId() {
		return msgId;
	}

	public String getMessage() {
		return msg;
	}
}