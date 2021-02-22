package com.example.demo.utils;

public interface IHashUtil {
	public String hashPassword(String password);
	public boolean checkPassword(String password, String hash);
}
