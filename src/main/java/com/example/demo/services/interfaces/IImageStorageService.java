package com.example.demo.services.interfaces;

public interface IImageStorageService {
	
	public String addImage(String itemId,byte[] imageJpg);
	public String deleteImage(String itemId,String imageHash);
	public byte[] getImage(String itemId,String imageHash);
}
