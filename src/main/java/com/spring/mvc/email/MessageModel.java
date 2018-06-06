package com.spring.mvc.email;

public class MessageModel {
	private String message;
	private String phone;


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String execute() {

		return "SUCCESS";

	}
}
