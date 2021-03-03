package com.example.demo.services.interfaces;

import com.example.demo.exceptions.ImageDeleteException;
import com.example.demo.exceptions.ImageFetchException;
import com.example.demo.exceptions.ImageHashException;
import com.example.demo.exceptions.ImageUploadException;

public interface IImageStorageService {
	
	public String addImage(String itemId,byte[] imageJpg) throws ImageUploadException,ImageHashException;
	public String deleteImage(String itemId,String imageHash)throws ImageDeleteException;
	public byte[] getImage(String itemId,String imageHash) throws ImageFetchException;
}
