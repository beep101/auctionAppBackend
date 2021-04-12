package com.example.demo.exceptions;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ExceptionHandleControllerAdvice {

	private ResponseEntity<Object> buildResponse(AuctionAppException exception, HttpStatus status){
		Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("message", exception.getMessage());
        if(exception instanceof AuctionAppExtendedException)
        	if(!((AuctionAppExtendedException)exception).getErrors().isEmpty())
        		body.put("errors", ((AuctionAppExtendedException)exception).getErrors());
        return new ResponseEntity<>(body,status);
	}
	
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialException(BadCredentialsException ex, WebRequest request) {
    	return buildResponse(ex, HttpStatus.NOT_ACCEPTABLE);
    }
    
    @ExceptionHandler(ExistingUserException.class)
    public ResponseEntity<Object> handleExistingUserException(ExistingUserException ex, WebRequest request) {
    	return buildResponse(ex, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(NonExistentUserException.class)
    public ResponseEntity<Object> handleNonExistentUserException(NonExistentUserException ex, WebRequest request) {
    	return buildResponse(ex, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex, WebRequest request){
    	return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request){
    	return buildResponse(ex, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(BidAmountLowException.class)
    public ResponseEntity<Object> handleBidAmountLowException(BidAmountLowException ex, WebRequest request){
    	return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex, WebRequest request){
    	return buildResponse(ex, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InsertFailedException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InsertFailedException ex, WebRequest request){
    	return buildResponse(ex, HttpStatus.I_AM_A_TEAPOT);
    }
    
    @ExceptionHandler(UnauthenticatedException.class)
    public ResponseEntity<Object> handleUnauthenticatedException(UnauthenticatedException ex,WebRequest request){
    	return buildResponse(ex, HttpStatus.UNAUTHORIZED);    	
    }

    @ExceptionHandler(UnallowedOperationException.class)
    public ResponseEntity<Object> handleUnallowedOperationException(UnallowedOperationException ex,WebRequest request){
    	return buildResponse(ex, HttpStatus.FORBIDDEN);
    }
}
