package com.example.demo.utils;

import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;

public interface IAwsS3Adapter {
	public PutObjectResult putObject(PutObjectRequest request);
	public S3Object getObject(String bucket,String key);
	public void deleteObject(String bucket,String key);
	public ObjectListing listObjects(String bucket,String key);
}
