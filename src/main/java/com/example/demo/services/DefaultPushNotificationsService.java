package com.example.demo.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpRequest.Builder;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.demo.entities.Notification;
import com.example.demo.entities.PushSub;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.exceptions.BadInitializatinException;
import com.example.demo.exceptions.InvalidDataException;
import com.example.demo.exceptions.UnallowedOperationException;
import com.example.demo.models.NotificationModel;
import com.example.demo.models.PushServiceSubModel;
import com.example.demo.repositories.NotificationsRepository;
import com.example.demo.repositories.PushSubsRepository;
import com.example.demo.services.interfaces.PushNotificationsService;
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
	private HttpClient httpClient;
	private Algorithm jwtAlgorithm;
	
	private ObjectMapper objectMapper;

	public DefaultPushNotificationsService(PushSubsRepository pushSubsRepo, NotificationsRepository notificationsRepo, PushMessageEncryptionUtil msgEncryptionUtil,String privateKey,String publicKey) throws AuctionAppException {
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
        
	    this.httpClient = HttpClient.newHttpClient();
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
	
	public PushServiceSubModel subscribe(PushServiceSubModel model,User principal) {
		PushSub entity=PushSub.fromModel(model);
		entity.setUser(principal);
		return pushSubsRepo.save(entity).toModel();
	}
	
	public void notifyUser(User user,Notification notification) throws AuctionAppException {
		notification.setUser(user);
		notification.setTime(new Timestamp(System.currentTimeMillis()));
		notificationsRepo.save(notification);
		sendNotification(notification);
	}
	
	public void sendMultipleNotifications(List<Notification> notifications){
		notificationsRepo.saveAll(notifications);
		notifications.stream().forEach(t -> {
			try {
				sendNotification(t);
			} catch (AuctionAppException e) {}
		});
	}
	
	private void sendNotification(Notification notification) throws AuctionAppException {
		for(PushSub pushSub:notification.getUser().getPushSub()) {
			String data;
			try {
				data=objectMapper.writeValueAsString(notification.toModel());
			} catch (JsonProcessingException e) {
				throw new InvalidDataException();
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
	    
	    Builder requestBuilder=HttpRequest.newBuilder();
	    requestBuilder.uri(URI.create(pushSub.getUrl()))
	    	.header("TTL", "180")
	    	.header("Authorization", "vapid t="+token+", k="+getPublicKeyBase64());
	    
	    if(data!=null) {
	    	requestBuilder.POST(BodyPublishers.ofByteArray(data))
	          .header("Content-Type", "application/octet-stream")
	          .header("Content-Encoding", "aes128gcm");
	    }else{ 
	    	requestBuilder.POST(BodyPublishers.noBody());
	    }
	    try {
	    	HttpResponse<Void> response = this.httpClient.send(requestBuilder.build(),BodyHandlers.discarding());
	    	System.out.println(response.statusCode());
	    	if(response.statusCode()==404||response.statusCode()==410)
	    		return false;
	    }catch (IOException | InterruptedException e) {	    
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
