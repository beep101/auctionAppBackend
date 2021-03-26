package com.example.demo.services;

import org.easymock.EasyMockRunner;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.runner.RunWith;

import com.example.demo.utils.IAwsS3Adapter;

//@RunWith(EasyMockRunner.class)
public class ImageStorageS3Tests {
	
	//@Mock
	IAwsS3Adapter s3Adapter;
	
	//@TestSubject
	ImageStorageS3 imageStorage=new ImageStorageS3("", s3Adapter);
	
	

}
