package com.example.demo.utils;

import java.util.Map;

import org.springframework.http.HttpMethod;

import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.HttpResponseModel;

public interface HttpClientAdapter{
	public HttpResponseModel sendHttpRequest(HttpMethod method,String urlString,Map<String,String> headers,byte[] data) throws AuctionAppException; 
}
