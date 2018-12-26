package com.kingfisher.deployment.audit.exception.model;

public class AuditError {
	private int code;
	private String type;
	private String message;

	public AuditError() {
		super();
	}

	public AuditError(int code, String type, String message) {
		super();
		this.code = code;
		this.type = type;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
