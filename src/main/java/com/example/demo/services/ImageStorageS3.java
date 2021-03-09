package com.example.demo.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.exceptions.ImageDeleteException;
import com.example.demo.exceptions.ImageFetchException;
import com.example.demo.exceptions.ImageHashException;
import com.example.demo.exceptions.ImageUploadException;
import com.example.demo.models.ItemModel;
import com.example.demo.services.interfaces.IImageStorageService;

public class ImageStorageS3 implements IImageStorageService{
	private AmazonS3 s3;
	private String bucketName="auction.purple.item.pics";
	
	private String baseUrl;
	
	//aws keys for s3bucket
	public ImageStorageS3(String id, String key,String baseUrl) {
		s3=AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(id,key)))
				.withRegion(Regions.EU_CENTRAL_1)
				.build();
		this.baseUrl=baseUrl;
	}

	@Override
	public String addImage(String itemId,byte[] imageJpg) throws ImageUploadException, ImageHashException{
		String imageHash=md5(imageJpg);
		String key=itemId+"/"+imageHash+".jpg";
		
		InputStream input=new ByteArrayInputStream(imageJpg);
		
		ObjectMetadata metadata=new ObjectMetadata();
		metadata.setContentLength(imageJpg.length);
		metadata.setContentType("image/jpg");
		try {
			s3.putObject(new PutObjectRequest(bucketName, key, input, metadata));
		}catch(AmazonServiceException ex) {
			throw new ImageUploadException();
		}
		return imageHash;
	}
	
	@Override
	public byte[] getImage(String itemId,String imageHash) throws ImageFetchException{
		String key=itemId+"/"+imageHash+".jpg";
		S3Object object=s3.getObject(bucketName, key);
		byte[] img=null;
		try {
			img=object.getObjectContent().readAllBytes();
		} catch (IOException e) {
			throw new ImageFetchException();
		}
		return img;
	}

	@Override
	public String deleteImage(String itemId,String imageHash) throws ImageDeleteException{
		String key=itemId+"/"+imageHash+".jpg";
		try {
			s3.deleteObject(bucketName, key);
		}catch(AmazonServiceException ex) {
			throw new ImageDeleteException();
		}
		return imageHash;
	}

	@Override
	public ItemModel loadImagesForItem(ItemModel itemModel) {
		itemModel.setImages(getImageUrls(itemModel.getId()));
		return itemModel;
	}

	@Override
	public Collection<ItemModel> loadImagesForItems(Collection<ItemModel> itemModels){
		itemModels.stream().parallel().forEach(x->x.setImages(getImageUrls(x.getId())));
		return itemModels;
	}
	
	private List<String> getImageUrls(int id){
		ObjectListing objects=null;
		try {
			objects=s3.listObjects(bucketName, Integer.toString(id)+"/");
		}catch(SdkClientException ex) {
			return new ArrayList<String>();
		}
		List<String> imageUrls=objects.getObjectSummaries().stream().map(x->baseUrl+x.getKey()).collect(Collectors.toList());
		return imageUrls;
	}
	
	private String md5(byte[] data) throws ImageHashException{
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new ImageHashException();
		}
	    md.update(data);
	    byte[] digest = md.digest();
	    return DatatypeConverter
	      .printHexBinary(digest).toUpperCase();
	}
}
