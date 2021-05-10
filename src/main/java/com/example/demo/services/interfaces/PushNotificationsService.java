package com.example.demo.services.interfaces;

import java.util.List;

import com.example.demo.entities.Notification;
import com.example.demo.entities.User;
import com.example.demo.exceptions.AuctionAppException;
import com.example.demo.models.NotificationModel;
import com.example.demo.models.PushServiceSubModel;

public interface PushNotificationsService {
	public byte[] getPublicKeyUncompressed();
	public String getPublicKeyBase64();
	public PushServiceSubModel subscribe(PushServiceSubModel model,User principal) throws AuctionAppException;
	public PushServiceSubModel unsubscribe(String link,User principal) throws AuctionAppException;
	public void sendNotification(Notification model) throws AuctionAppException;
	public List<NotificationModel> getAllNotificationsForUser(User user) throws AuctionAppException;
	public void sendMultipleNotifications(List<Notification> notifications);
}
