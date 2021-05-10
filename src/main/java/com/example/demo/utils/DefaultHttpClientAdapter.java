package com.example.demo.utils;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Map;

import org.springframework.http.HttpMethod;

import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.ExternalServiceException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.models.HttpResponseModel;

public class DefaultHttpClientAdapter implements HttpClientAdapter{
	private HttpClient httpClient;
	public DefaultHttpClientAdapter() {
		httpClient=HttpClient.newHttpClient();
	}
		
	public HttpResponseModel sendHttpRequest(HttpMethod method,String urlString,Map<String,String> headers,byte[] data) throws AuctionAppException {
	    Builder requestBuilder=HttpRequest.newBuilder();
	    try {
		    requestBuilder.uri(URI.create(urlString));
	    }catch(IllegalArgumentException e) {
	    	throw new InvalidDataException();
	    }
	    headers.forEach((name,value)->requestBuilder.header(name, value));
	    
	    BodyPublisher bodyPublisher;
	    if(data!=null) {
	    	bodyPublisher=BodyPublishers.ofByteArray(data);
	    }else{ 
	    	bodyPublisher=BodyPublishers.noBody();
	    }
	    switch(method) {
	    	case GET:
	    		requestBuilder.GET();
	    		break;
	    	case POST:
	    		requestBuilder.POST(bodyPublisher);
	    		break;
	    	case PUT:
	    		requestBuilder.PUT(bodyPublisher);
	    		break;
	    	case DELETE:
	    		requestBuilder.DELETE();
	    		break;
	    	default:
	    		throw new InvalidDataException();
	    }
	    HttpResponse<String> response;
		try {
			response = this.httpClient.send(requestBuilder.build(),BodyHandlers.ofString());
		} catch (IOException | InterruptedException e) {
			throw new ExternalServiceException();
		}
	    return new HttpResponseModel(response.statusCode(), response.body());
	}
}
