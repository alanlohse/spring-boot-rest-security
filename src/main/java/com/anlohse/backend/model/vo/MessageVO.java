package com.anlohse.backend.model.vo;

import com.anlohse.backend.model.types.MessageType;

public class MessageVO {

	private String message;
	private MessageType type;

	public MessageVO() {
	}

	public MessageVO(String message, MessageType type) {
		this.message = message;
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
	
}
