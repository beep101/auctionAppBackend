package com.example.demo.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;

import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.example.demo.services.interfaces.IImageStorageService;

public class ImageStorageS3 implements IImageStorageService{
	private AmazonS3 s3;
	private String bucketName="auction.purple.item.pics";
	
	@Value("${s3.id}")
	private String id;
	@Value("${s3.key}")
	private String key;
	
	//aws keys for s3bucket
	public ImageStorageS3() {
		s3=AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(id,key)))
				.withRegion(Regions.EU_CENTRAL_1)
				.build();
	}

	@Override
	public String addImage(String itemId,byte[] imageJpg) {
		String imageHash=md5(imageJpg);
		String key=itemId+"/"+imageHash+".jpg";
		
		InputStream input=new ByteArrayInputStream(imageJpg);
		
		ObjectMetadata metadata=new ObjectMetadata();
		metadata.setContentLength(imageJpg.length);
		metadata.setContentType("image/jpg");
		try {
			s3.putObject(new PutObjectRequest(bucketName, key, input, metadata));
		}catch(AmazonServiceException ex) {
			return "";
		}
		return imageHash;
	}
	
	@Override
	public byte[] getImage(String itemId,String imageHash) {
		String key=itemId+"/"+imageHash+".jpg";
		S3Object object=s3.getObject(bucketName, key);
		byte[] img=null;
		try {
			img=object.getObjectContent().readAllBytes();
		} catch (IOException e) {
			return null;
		}
		return img;
	}

	@Override
	public String deleteImage(String itemId,String imageHash) {
		String key=itemId+"/"+imageHash+".jpg";
		try {
			s3.deleteObject(bucketName, key);
		}catch(AmazonServiceException ex) {
			return "";
		}
		return imageHash;
	}
	
	private String md5(byte[] data) {
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	    md.update(data);
	    byte[] digest = md.digest();
	    return DatatypeConverter
	      .printHexBinary(digest).toUpperCase();
	}
}
