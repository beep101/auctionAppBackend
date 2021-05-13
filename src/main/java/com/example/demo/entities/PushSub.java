package com.example.demo.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.example.demo.models.PushServiceSubKeysModel;
import com.example.demo.models.PushServiceSubModel;

@Entity
@Table(name="pushsubs")
@NamedQuery(name="PushSub.findAll", query="SELECT p FROM PushSub p")
public class PushSub  implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;

	@JoinColumn(name="url")
	private String url;

	@JoinColumn(name="auth")
	private String auth;

	@JoinColumn(name="p256dh")
	private String p256dh;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="userid")
	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
		
	public String getP256dh() {
		return p256dh;
	}

	public void setP256dh(String p256dh) {
		this.p256dh = p256dh;
	}

	public static PushSub fromModel(PushServiceSubModel model) {
		PushSub entity=new PushSub();
		
		entity.setAuth(model.getKeys().getAuth());
		entity.setP256dh(model.getKeys().getP256dh());
		entity.setUrl(model.getEndpoint());
		
		return entity;
	}
	
	public PushServiceSubModel toModel() {
		PushServiceSubModel model=new PushServiceSubModel();
		model.setEndpoint(this.getUrl());
		PushServiceSubKeysModel keys=new PushServiceSubKeysModel();
		keys.setAuth(this.getAuth());
		keys.setP256dh(this.getP256dh());
		model.setKeys(keys);
		return model;
	}
}
