package com.example.demo.utils;

import com.example.demo.exceptions.AuctionAppException;

public interface PushMessageEncryptionUtil {
	public byte[] encrypt(String plainText, String authSecret, String publicKey) throws AuctionAppException;
}
