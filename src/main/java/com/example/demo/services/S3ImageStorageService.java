package com.example.demo.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.DatatypeConverter;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.ImageDeleteException;
import com.example.demo.exceptions.ImageHashException;
import com.example.demo.exceptions.ImageUploadException;
import com.example.demo.models.ModelWithImages;
import com.example.demo.services.interfaces.ImageStorageService;
import com.example.demo.utils.IAwsS3Adapter;

public class S3ImageStorageService<T extends ModelWithImages> implements ImageStorageService<T>{
	private IAwsS3Adapter s3;	
	private String bucketName;
	private String baseUrl;

	public S3ImageStorageService(String bucketName,String baseUrl,IAwsS3Adapter s3) {
		this.s3=s3;
		this.baseUrl=baseUrl;
		this.bucketName=bucketName;
	}

	@Override
	public String addImage(String itemId,byte[] imageJpg) throws AuctionAppException{
		String imageHash=md5(imageJpg);
		String key=itemId+"/"+imageHash+".jpg";
		
		InputStream input=new ByteArrayInputStream(imageJpg);
		
		ObjectMetadata metadata=new ObjectMetadata();
		metadata.setContentLength(imageJpg.length);
		metadata.setContentType("image/jpg");
		try {
			s3.putObject(new PutObjectRequest(bucketName, key, input, metadata).withCannedAcl(CannedAccessControlList.PublicRead));
		}catch(AmazonServiceException ex) {
			throw new ImageUploadException();
		}
		return imageHash;
	}
	
	@Override
	public List<String> addImages(String itemId,List<byte[]> imagesJpgs) throws AuctionAppException{
		List<String> hashes=new ArrayList<>();
		for(byte[] img:imagesJpgs)
			hashes.add(addImage(itemId, img));
		return hashes;
	}

	@Override
	public String deleteImage(String itemId,String imageHash) throws AuctionAppException{
		String key=itemId+"/"+imageHash+".jpg";
		try {
			s3.deleteObject(bucketName, key);
		}catch(AmazonServiceException ex) {
			throw new ImageDeleteException();
		}
		return imageHash;
	}

	@Override
	public T loadImagesForItem(T model) {
		model.setImages(getImageUrls(model.getId()));
		return (T)model;
	}

	@Override
	public Collection<T> loadImagesForItems(Collection<T> models){
		models.stream().parallel().forEach(x->x.setImages(getImageUrls(x.getId())));
		return models;
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
