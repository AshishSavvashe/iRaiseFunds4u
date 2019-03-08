package com.appbell.iraisefund4u.common.exception;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = 10L;
	private int msgId;
	private String msg;

	public ApplicationException(int messageId) {
		msgId = messageId;
	}

	public ApplicationException(String message) {
		msg = message;
	}

	public int getMessageId() {
		return msgId;
	}

	public String getMessage() {
		return msg;
	}
}