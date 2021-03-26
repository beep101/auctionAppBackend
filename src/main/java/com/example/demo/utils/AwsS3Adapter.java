package com.example.demo.utils;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

public class AwsS3Adapter implements IAwsS3Adapter{
	private AmazonS3 s3;
	
	public AwsS3Adapter(String id, String key) {
		s3=AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(id,key)))
				.withRegion(Regions.EU_CENTRAL_1)
				.build();
	}

	@Override
	public PutObjectResult putObject(PutObjectRequest request) {
		return s3.putObject(request);
	}

	@Override
	public S3Object getObject(String bucket, String key) {
		return s3.getObject(bucket, key);
	}

	@Override
	public void deleteObject(String bucket, String key) {
		s3.deleteObject(bucket, key);
	}

	@Override
	public ObjectListing listObjects(String bucket, String key) {
		return s3.listObjects(bucket, key);
	}

}
