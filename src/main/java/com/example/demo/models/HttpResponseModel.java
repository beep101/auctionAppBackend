package com.example.demo.models;

public class HttpResponseModel {
	private int statusCode;
	private String body;
	
	public HttpResponseModel(int statusCode,String body) {
		this.statusCode=statusCode;
		this.body=body;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
	
}
