package com.example.demo.services;

import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.ImageDeleteException;
import com.example.demo.exceptions.ImageUploadException;
import com.example.demo.models.ModelWithImages;
import com.example.demo.utils.IAwsS3Adapter;

@RunWith(EasyMockRunner.class)
public class S3ImageStorageServiceTests extends EasyMockSupport{
	
	@Mock
	IAwsS3Adapter s3Adapter;
	
	@TestSubject
	S3ImageStorageService<ModelWithImages> imageStorage=new S3ImageStorageService<>("","", s3Adapter);
	
	@Test
	public void addImageShouldCreateValidPutRequest() throws AuctionAppException, IOException {
		
		Capture<PutObjectRequest> putReqCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(s3Adapter.putObject(capture(putReqCapture))).andReturn(null).anyTimes();
		replayAll();
		String key="key";
		byte[] bytes=new byte[] {1,2,3,4,5,6};
		imageStorage.addImage(key,bytes);
		assertEquals(putReqCapture.getValue().getKey().subSequence(0, key.length()),key);
		assertEquals(putReqCapture.getValue().getKey().subSequence(putReqCapture.getValue().getKey().length()-4, putReqCapture.getValue().getKey().length()),".jpg");
		assertEquals(putReqCapture.getValue().getKey().length(),key.length()+"/7E9744F12DBFDE6BE0F1DE7996D97C37.jpg".length());
		assertArrayEquals(putReqCapture.getValue().getInputStream().readAllBytes(),bytes);
		
		verifyAll();
	}
	
	@Test
	public void addImagesShouldReturnValidHashList() throws AuctionAppException{
		expect(s3Adapter.putObject(anyObject())).andReturn(null).anyTimes();
		replayAll();
		
		List<byte[]> imgs=new ArrayList<>();
		imgs.add(new byte[] {1,2,3,4,5,6});
		imgs.add(new byte[] {7,8,9,10});
		imgs.add(new byte[] {1});
		
		List<String> hashes=imageStorage.addImages("", imgs);
		assertEquals(imgs.size(), hashes.size());
		assertEquals(hashes.get(0).length(), "7E9744F12DBFDE6BE0F1DE7996D97C37".length());
		assertEquals(hashes.get(1).length(), "7E9744F12DBFDE6BE0F1DE7996D97C37".length());
		assertEquals(hashes.get(2).length(), "7E9744F12DBFDE6BE0F1DE7996D97C37".length());
		
		verifyAll();
	}
	
	@Test(expected = ImageUploadException.class)
	public void addImageErrorOnWriteShouldThrowException() throws AuctionAppException{
		
		expect(s3Adapter.putObject(anyObject())).andThrow(new AmazonServiceException("msg"));
		
		replayAll();
		
		String key="key";
		byte[] bytes=new byte[] {1,2,3,4,5,6};
		imageStorage.addImage(key,bytes);

		
		verifyAll();
	}
	
	
	@Test
	public void deleteImageCreateValidRequest() throws AuctionAppException {
		
		Capture<String> reqCapture=Capture.newInstance();
		s3Adapter.deleteObject(anyString(), capture(reqCapture));
		expectLastCall();
		replayAll();

		String key="key";
		String hash="7E9744F12DBFDE6BE0F1DE7996D97C37";
		String validRequest=key+"/"+hash+".jpg";
		
		imageStorage.deleteImage(key, hash);	
		assertEquals(validRequest, reqCapture.getValue());
		
		verifyAll();
	}
	
	@Test
	public void deleteImageReturnsHash() throws AuctionAppException {
		
		s3Adapter.deleteObject(anyString(), anyString());
		expectLastCall();
		replayAll();

		String key="key";
		String hash="7E9744F12DBFDE6BE0F1DE7996D97C37";
		
		String resHash=imageStorage.deleteImage(key, hash);	
		assertEquals(hash, resHash);
		
		verifyAll();
	}
	
	@Test(expected = ImageDeleteException.class)
	public void deleteImageThrowsException() throws AuctionAppException {
		
		s3Adapter.deleteObject(anyString(), anyString());
		expectLastCall().andThrow(new AmazonServiceException("")).anyTimes();
		replayAll();

		String key="key";
		String hash="7E9744F12DBFDE6BE0F1DE7996D97C37";
		
		imageStorage.deleteImage(key, hash);
		
		verifyAll();
	}
	 
	
}
