package com.example.demo.services;

import com.paypal.base.rest.APIContext;

public class DefaultPayPalTransactionService {
	
	APIContext payPalApiContext;
	
	public DefaultPayPalTransactionService(String id,String secret,String mode) {
		payPalApiContext=new APIContext(id, secret, mode);
	}

	public int recievePayment() {
		//fetch data
		//check if auction is end and nofinished
		//check if user who paid is winner
		//set finished
		//setPaid
		//approve transaction
		return 0;
	}
}
