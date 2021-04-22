package com.example.demo.services.interfaces;

import java.util.Collection;
import java.util.List;

import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.ModelWithImages;

public interface ImageStorageService<T extends ModelWithImages>{
	
	public String addImage(String itemId,byte[] imageJpg) throws AuctionAppException;
	public List<String> addImages(String itemId,List<byte[]> imagesJpgs) throws AuctionAppException;
	public String deleteImage(String itemId,String imageHash)throws AuctionAppException;
	public T loadImagesForItem(T model);
	public Collection<T> loadImagesForItems(Collection<T> models);
}
