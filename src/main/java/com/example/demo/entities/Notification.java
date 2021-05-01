package com.example.demo.entities;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.example.demo.models.NotificationModel;

@Entity
@Table(name="notifications")
@NamedQuery(name="Notification.findAll", query="SELECT n FROM Notification n")
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="title")
	private String title;

	@Column(name="body")
	private String body;
	
	@Column(name="time")
	private Timestamp time;
	
	@ManyToOne
	@JoinColumn(name="userid")
	private User user;

	@Column(name="link")
	private String link;
	
	public Notification() {
		super();
	}
	
	public Notification(String title,String body,String link) {
		super();
		this.title=title;
		this.body=body;
		this.link=link;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp time) {
		this.time = time;
	}
	
	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public NotificationModel toModel() {
		NotificationModel model=new NotificationModel();
		
		model.setTitle(this.getTitle());
		model.setBody(this.getBody());
		model.setTime(this.getTime());
		model.setLink(this.link==null?"":this.link);
		
		return model;
	}
}
