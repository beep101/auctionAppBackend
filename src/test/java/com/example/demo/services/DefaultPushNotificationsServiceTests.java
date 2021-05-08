package com.example.demo.services;

import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpMethod;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.example.demo.entities.Notification;
import com.example.demo.entities.PushSub;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.HttpResponseModel;
import com.example.demo.models.NotificationModel;
import com.example.demo.models.PushServiceSubKeysModel;
import com.example.demo.models.PushServiceSubModel;
import com.example.demo.repositories.NotificationsRepository;
import com.example.demo.repositories.PushSubsRepository;
import com.example.demo.utils.HttpClientAdapter;
import com.example.demo.utils.PushMessageEncryptionUtil;

@RunWith(EasyMockRunner.class)
public class DefaultPushNotificationsServiceTests extends EasyMockSupport {
	@Mock
	PushSubsRepository pushSubsRepo;
	@Mock
	NotificationsRepository notificationsRepo;
	@Mock
	PushMessageEncryptionUtil msgEncryptionUtil;
	@Mock
	HttpClientAdapter httpClient;
			
	String privateKey="MEECAQAwEwYHKoZIzj0CAQYIKoZIzj0DAQcEJzAlAgEBBCAYCtLcMGOmXjE4IRGBtdbRxC0dPcjedJ3IuioIsmNTVw==";
	String publicKey="MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEVElQvMCqADkEPfKkpjdMUWP7rFw8eAuedsDSFwTwnB3rT27wBLsdfa6CSSu30sz8HFeHbswxgKGePsV9f4vcVA==";
	
	DefaultPushNotificationsService pushSubService;
	
	@Before
	public void setup() throws AuctionAppException {
		pushSubService=new DefaultPushNotificationsService(pushSubsRepo, notificationsRepo, msgEncryptionUtil,httpClient, privateKey, publicKey);

	}
	@Test(expected = InvalidDataException.class)
	public void testUnsubscribeUnexistingSubscriptionShouldThrowException() throws AuctionAppException {
		expect(pushSubsRepo.findOneByUrl(anyString())).andReturn(Optional.empty());
		replayAll();
		
		pushSubService.unsubscribe("", new User());
		
		verifyAll();
	}
	
	@Test(expected = UnallowedOperationException.class)
	public void testUnsubscribeOtherUserShoultThrowException() throws AuctionAppException {
		User principal=new User();
		principal.setId(13);

		User subscriber=new User();
		subscriber.setId(27);
		
		PushSub ps=new PushSub();
		ps.setUser(subscriber);
		
		expect(pushSubsRepo.findOneByUrl(anyString())).andReturn(Optional.of(ps));
		replayAll();
		
		pushSubService.unsubscribe("", principal);
		
		verifyAll();
	}
	

	@Test
	public void testUnsubscribeOtherUserHappyFlow() throws AuctionAppException {
		User principal=new User();
		principal.setId(13);

		User subscriber=new User();
		subscriber.setId(13);
		
		PushSub ps=new PushSub();
		ps.setUser(subscriber);
		ps.setAuth("auth");
		ps.setP256dh("p256dh");
		ps.setUrl("url");
		
		expect(pushSubsRepo.findOneByUrl(anyString())).andReturn(Optional.of(ps));
		pushSubsRepo.delete(ps);
		expectLastCall();
		replayAll();
		
		PushServiceSubModel model=pushSubService.unsubscribe("", principal);
		
		assertEquals("url", model.getEndpoint());
		assertEquals("auth", model.getKeys().getAuth());
		assertEquals("p256dh", model.getKeys().getP256dh());
		
		verifyAll();
	}
	
	@Test
	public void testGetAllNotificationsForUser() throws AuctionAppException {
		List<Notification> notifications=new ArrayList<>();
		
		Notification ntf=new Notification();
		ntf.setTitle("title");
		ntf.setBody("body");
		ntf.setLink("link");
		ntf.setUser(new User());
		notifications.add(ntf);
		expect(notificationsRepo.findByUserEqualsOrderByTimeDesc(anyObject(),anyObject())).andReturn(notifications);
		replayAll();
		
		List<NotificationModel> models=pushSubService.getAllNotificationsForUser(new User());
		NotificationModel model=models.get(0);
		
		assertEquals("title", model.getTitle());
		assertEquals("body", model.getBody());
		assertEquals("link", model.getLink());
		
		verifyAll();
	}
	
	@Test
	public void testSubscribe() throws AuctionAppException {
		PushServiceSubModel requestModel=new PushServiceSubModel();
		requestModel.setEndpoint("url");
		requestModel.setKeys(new PushServiceSubKeysModel());
		requestModel.getKeys().setAuth("auth");
		requestModel.getKeys().setP256dh("p256dh");
		
		User user=new User();
		user.setId(13);
		user.setPushNotifications(true);

		Capture<PushSub> psCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(pushSubsRepo.save(capture(psCapture))).andAnswer(()->psCapture.getValue());
		replayAll();
		
		PushServiceSubModel responseModel=pushSubService.subscribe(requestModel, user);
		
		assertEquals("url", responseModel.getEndpoint());
		assertEquals("auth", responseModel.getKeys().getAuth());
		assertEquals("p256dh", responseModel.getKeys().getP256dh());
		assertEquals(13, psCapture.getValue().getUser().getId());
		
		verifyAll();
	}
	
	@Test
	public void testSendNotificationHappyFlow() throws AuctionAppException {
		
		List<PushSub> pushSubs=new ArrayList<PushSub>();
		PushSub pushSub=new PushSub();
		pushSub.setAuth("auth");
		pushSub.setP256dh("p256dh");
		pushSub.setUrl("https://notifications.service/path");
		pushSubs.add(pushSub);
		
		User user=new User();
		user.setId(13);
		user.setPushSub(pushSubs);
		user.setPushNotifications(true);
		
		Notification ntf=new Notification();
		ntf.setLink("linkText");
		ntf.setBody("bodyText");
		ntf.setTitle("titleText");
		ntf.setUser(user);
		ntf.setTime(new Timestamp(System.currentTimeMillis()));
		
		byte[] byteArr=new byte[]{1,2,3,4,5,6};
		Capture<String> dataCapture=EasyMock.newCapture(CaptureType.ALL);
		Capture<Map<String,String>> headersCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(notificationsRepo.save(anyObject())).andReturn(ntf).anyTimes();
		expect(msgEncryptionUtil.encrypt(capture(dataCapture),cmpEq( "auth"), cmpEq("p256dh"))).andReturn(byteArr).anyTimes();
		expect(httpClient.sendHttpRequest(cmpEq(HttpMethod.PUT),cmpEq("https://notifications.service/path"), capture(headersCapture), aryEq(byteArr))).andReturn(new HttpResponseModel(200, "")).anyTimes();
		replayAll();
		
		pushSubService.sendNotification(ntf);
		assertEquals(4,headersCapture.getValue().size());
		assertThat(dataCapture.getValue().contains("linkText"));
		assertThat(dataCapture.getValue().contains("bodyText"));
		assertThat(dataCapture.getValue().contains("titleText"));
		
		verifyAll();
	}
	
	@Test
	public void testSendMultipleNotificationsHappyFlow() throws AuctionAppException {
		
		List<PushSub> pushSubs=new ArrayList<PushSub>();
		PushSub pushSub=new PushSub();
		pushSub.setAuth("auth");
		pushSub.setP256dh("p256dh");
		pushSub.setUrl("https://notifications.service/path");
		pushSubs.add(pushSub);
		
		User user=new User();
		user.setId(13);
		user.setPushSub(pushSubs);
		user.setPushNotifications(true);
		
		Notification ntf1=new Notification();
		ntf1.setLink("linkText");
		ntf1.setBody("bodyText");
		ntf1.setTitle("titleText");
		ntf1.setUser(user);
		ntf1.setTime(new Timestamp(System.currentTimeMillis()));

		Notification ntf2=new Notification();
		ntf2.setLink("linkText");
		ntf2.setBody("bodyText");
		ntf2.setTitle("titleText");
		ntf2.setUser(user);
		ntf2.setTime(new Timestamp(System.currentTimeMillis()));
		
		List<Notification> ntfs=new ArrayList<>();
		ntfs.add(ntf1);
		ntfs.add(ntf2);
		
		byte[] byteArr=new byte[]{1,2,3,4,5,6};
		Capture<String> dataCapture=EasyMock.newCapture(CaptureType.LAST);
		Capture<Map<String,String>> headersCapture=EasyMock.newCapture(CaptureType.LAST);
		expect(notificationsRepo.saveAll(anyObject())).andReturn(ntfs).anyTimes();
		expect(msgEncryptionUtil.encrypt(capture(dataCapture),cmpEq( "auth"), cmpEq("p256dh"))).andReturn(byteArr).anyTimes();
		expect(httpClient.sendHttpRequest(cmpEq(HttpMethod.PUT),cmpEq("https://notifications.service/path"), capture(headersCapture), aryEq(byteArr))).andReturn(new HttpResponseModel(200, "")).anyTimes();
		replayAll();
		
		pushSubService.sendMultipleNotifications(ntfs);
		assertEquals(4,headersCapture.getValue().size());
		assertThat(dataCapture.getValue().contains("linkText"));
		assertThat(dataCapture.getValue().contains("bodyText"));
		assertThat(dataCapture.getValue().contains("titleText"));
		
		verifyAll();
	}
	
	@Test
	public void testSendNotificationMalformedUrl() throws AuctionAppException {
		
		List<PushSub> pushSubs=new ArrayList<PushSub>();
		PushSub pushSub=new PushSub();
		pushSub.setAuth("auth");
		pushSub.setP256dh("p256dh");
		pushSub.setUrl("https://notifications.service/path");
		pushSubs.add(pushSub);
		
		User user=new User();
		user.setId(13);
		user.setPushSub(pushSubs);
		user.setPushNotifications(true);
		
		Notification ntf=new Notification();
		ntf.setLink("linkText");
		ntf.setBody("bodyText");
		ntf.setTitle("titleText");
		ntf.setUser(user);
		ntf.setTime(new Timestamp(System.currentTimeMillis()));
		
		byte[] byteArr=new byte[]{1,2,3,4,5,6};
		Capture<String> dataCapture=EasyMock.newCapture(CaptureType.ALL);
		Capture<Map<String,String>> headersCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(notificationsRepo.save(anyObject())).andReturn(ntf).anyTimes();
		expect(msgEncryptionUtil.encrypt(capture(dataCapture),cmpEq( "auth"), cmpEq("p256dh"))).andReturn(byteArr).anyTimes();
		expect(httpClient.sendHttpRequest(cmpEq(HttpMethod.PUT),cmpEq("https://notifications.service/path"), capture(headersCapture), aryEq(byteArr))).andReturn(new HttpResponseModel(404, "")).anyTimes();
		pushSubsRepo.delete(anyObject());
		expectLastCall().once();
		replayAll();
		
		pushSubService.sendNotification(ntf);
		
		verifyAll();
	}
	
	
	@Test
	public void testSendNotificationUnsuccessfulRequest() throws AuctionAppException {
		
		List<PushSub> pushSubs=new ArrayList<PushSub>();
		PushSub pushSub=new PushSub();
		pushSub.setAuth("auth");
		pushSub.setP256dh("p256dh");
		pushSub.setUrl("url");
		pushSubs.add(pushSub);
		
		User user=new User();
		user.setId(13);
		user.setPushSub(pushSubs);
		user.setPushNotifications(true);
		
		Notification ntf=new Notification();
		ntf.setLink("linkText");
		ntf.setBody("bodyText");
		ntf.setTitle("titleText");
		ntf.setUser(user);
		ntf.setTime(new Timestamp(System.currentTimeMillis()));
		
		byte[] byteArr=new byte[]{1,2,3,4,5,6};
		Capture<String> dataCapture=EasyMock.newCapture(CaptureType.ALL);
		Capture<Map<String,String>> headersCapture=EasyMock.newCapture(CaptureType.ALL);
		expect(notificationsRepo.save(anyObject())).andReturn(ntf).anyTimes();
		expect(msgEncryptionUtil.encrypt(capture(dataCapture),cmpEq( "auth"), cmpEq("p256dh"))).andReturn(byteArr).anyTimes();
		expect(httpClient.sendHttpRequest(cmpEq(HttpMethod.PUT),cmpEq("url"), capture(headersCapture), aryEq(byteArr))).andReturn(new HttpResponseModel(200, "")).anyTimes();
		pushSubsRepo.delete(anyObject());
		expectLastCall().once();
		replayAll();
		
		pushSubService.sendNotification(ntf);
		
		verifyAll();
	}
}
