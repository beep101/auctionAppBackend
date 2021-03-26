package com.example.demo.services.interfaces;

import java.util.Collection;
import java.util.List;

import com.example.demo.exceptions.ImageDeleteException;
import com.example.demo.exceptions.ImageHashException;
import com.example.demo.exceptions.ImageUploadException;
import com.example.demo.models.ItemModel;

public interface IImageStorageService {
	
	public String addImage(String itemId,byte[] imageJpg) throws ImageUploadException,ImageHashException;
	public List<String> addImages(String itemId,List<byte[]> imagesJpgs) throws ImageUploadException, ImageHashException;
	public String deleteImage(String itemId,String imageHash)throws ImageDeleteException;
	public ItemModel loadImagesForItem(ItemModel itemModel);
	public Collection<ItemModel> loadImagesForItems(Collection<ItemModel> itemModel);
}
