package com.example.demo.services;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpMethod;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entities.Notification;
import com.example.demo.entities.PushSub;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.BadInitializatinException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.HttpResponseModel;
import com.example.demo.models.NotificationModel;
import com.example.demo.models.PushServiceSubModel;
import com.example.demo.repositories.NotificationsRepository;
import com.example.demo.repositories.PushSubsRepository;
import com.example.demo.services.interfaces.PushNotificationsService;
import com.example.demo.utils.HttpClientAdapter;
import com.example.demo.utils.PushMessageEncryptionUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class DefaultPushNotificationsService implements PushNotificationsService{
	
	private static final long HALF_DAY_MILIS=12 * 60 * 60 * 1000;
	private static final byte[] P256_HEAD= Base64.getDecoder().decode("MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgA");
	
	private ECPrivateKey privateKey;
	private ECPublicKey publicKey;
	
	private PushSubsRepository pushSubsRepo;
	private NotificationsRepository notificationsRepo;
	
	private PushMessageEncryptionUtil msgEncryptionUtil;
	private HttpClientAdapter httpClient;
	private Algorithm jwtAlgorithm;
	
	private ObjectMapper objectMapper;

	public DefaultPushNotificationsService(PushSubsRepository pushSubsRepo, NotificationsRepository notificationsRepo,
			PushMessageEncryptionUtil msgEncryptionUtil,HttpClientAdapter httpClient, String privateKey,String publicKey) throws AuctionAppException {
		this.pushSubsRepo=pushSubsRepo;
		this.notificationsRepo=notificationsRepo;
		
		this.msgEncryptionUtil=msgEncryptionUtil;
	    KeyFactory keyFactory=null;
	    
		try {
			keyFactory = KeyFactory.getInstance("EC");
		} catch (NoSuchAlgorithmException e) {
			throw new BadInitializatinException();
		}
	    
		try {
			this.privateKey = (ECPrivateKey)keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey.getBytes("UTF-8"))));
			this.publicKey = (ECPublicKey)keyFactory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKey.getBytes("UTF-8"))));
		} catch (UnsupportedEncodingException | InvalidKeySpecException e) {
			throw new BadInitializatinException();
		}
        
	    this.httpClient = httpClient;
	    this.jwtAlgorithm = Algorithm.ECDSA256(this.publicKey, this.privateKey);
	    this.objectMapper=new ObjectMapper();
	}
	
	public byte[] getPublicKeyUncompressed() {
	    byte[] result = new byte[65];
	    byte[] encoded = publicKey.getEncoded();
	    System.arraycopy(encoded, P256_HEAD.length, result, 0,
	        encoded.length - P256_HEAD.length);
	    return result;
	}
	
	public String getPublicKeyBase64() {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(this.getPublicKeyUncompressed());
	}
	
	public PushServiceSubModel subscribe(PushServiceSubModel model,User principal) throws AuctionAppException {
		if(!principal.isPushNotifications())
			throw new UnallowedOperationException();
		PushSub entity=PushSub.fromModel(model);
		entity.setUser(principal);
		return pushSubsRepo.save(entity).toModel();
	}
	
	public void sendNotification(Notification notification) {
		notificationsRepo.save(notification);
		try {
			if(notification.getUser().isPushNotifications())
				sendNotificationRequest(notification);
		} catch (AuctionAppException e) {
				System.err.println("Sending notificatin unsuccesseful\n"+
					"Recipient: "+notification.getUser().getId()+" "+notification.getUser().getEmail()+
					"Notification URL: "+notification.getLink());
		}
	}
	
	public void sendMultipleNotifications(List<Notification> notifications){
		notificationsRepo.saveAll(notifications);
		notifications.stream().forEach(t -> {
			try {
				if(t.getUser().isPushNotifications())
					sendNotificationRequest(t);
			} catch (AuctionAppException e) {
				System.err.println("Sending notificatin unsuccesseful\n"+
					"Recipient: "+t.getUser().getId()+" "+t.getUser().getEmail()+
					"Notification URL: "+t.getLink());
			}
		});
	}
	
	private void sendNotificationRequest(Notification notification) throws AuctionAppException {
		for(PushSub pushSub:notification.getUser().getPushSub()) {
			String data;
			try {
				data=objectMapper.writeValueAsString(notification.toModel());
			} catch (JsonProcessingException e) {
				return;
			}
			if(!sendRequest(pushSub,msgEncryptionUtil.encrypt(data, pushSub.getAuth(),pushSub.getP256dh()))) 
				pushSubsRepo.delete(pushSub);
		}
	}

	private boolean sendRequest(PushSub pushSub,byte[] data){
	    String audience = null;
	    try {
	      URL url = new URL(pushSub.getUrl());
	      audience = url.getProtocol() + "://" + url.getHost();
	    }
	    catch (MalformedURLException e) {
	      return false;
	    }
	    
	    Date exp = new Date((new Date()).getTime() + HALF_DAY_MILIS);
	    String token = JWT.create().withAudience(audience).withExpiresAt(exp)
	        .withSubject("mailto:auction.purple.info@gmail.com").sign(this.jwtAlgorithm);
	    Map<String,String> headers=new HashMap<String,String>();
	    headers.put("TTL", "180");
	    headers.put("Authorization", "vapid t="+token+", k="+getPublicKeyBase64());
	    
	    if(data!=null) {
	    	headers.put("Content-Type", "application/octet-stream");
	    	headers.put("Content-Encoding", "aes128gcm");
	    }
	    
	    try {
			HttpResponseModel response=httpClient.sendHttpRequest(HttpMethod.PUT, pushSub.getUrl(), headers, data);
			if(response.getStatusCode()==404||response.getStatusCode()==410)
				return false;
		} catch (AuctionAppException e) {
			return true;
		}
	    return true;
	}

	@Override
	public List<NotificationModel> getAllNotificationsForUser(User user) throws AuctionAppException {
		return notificationsRepo.findByUserEqualsOrderByTimeDesc(user,PageRequest.of(0, 10)).stream().map(x->x.toModel()).collect(Collectors.toList());
	}

	@Override
	public PushServiceSubModel unsubscribe(String link, User principal) throws AuctionAppException {
		Optional<PushSub> opt=pushSubsRepo.findOneByUrl(link);
		if(opt.isEmpty())
			throw new InvalidDataException();
		if(opt.get().getUser().getId()!=principal.getId())
			throw new UnallowedOperationException();
		pushSubsRepo.delete(opt.get());
		return opt.get().toModel();
	}
		     
}
