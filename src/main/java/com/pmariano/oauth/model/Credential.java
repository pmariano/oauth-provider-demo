package com.pmariano.oauth.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;

import redis.clients.jedis.Jedis;

@Entity
public class Credential {
	
	@Id
	@GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name = "system-uuid", strategy = "uuid")
	private String id;
	
	@Column(unique = true)
	private String refreshToken;
	
	@ManyToOne
	private Provider provider;
	@ManyToOne
	private User user;
	
	public Credential() {
	}
	
	public Credential(User user, Provider provider, String refreshToken) {
		this.user = user;
		this.provider = provider;
		this.refreshToken = refreshToken;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Provider getProvider() {
		return provider;
	}
	
	public void setProvider(Provider provider) {
		this.provider = provider;
	}
	
	public User getUser() {
		return user;
	}
	
	public void setUser(User user) {
		this.user = user;
	}

	public boolean validateAppSecret(String secret) {
		return this.provider.getAppSecret().equals(secret);
	}

	public Token createNewAppToken() {
		Jedis jedis = new Jedis("localhost");
		
		String key = UUID.randomUUID().toString();
		jedis.setex(key, 500, this.user.toJson());
		
		return new Token(key, this.refreshToken);
	}

	public static void fromToken(String token) {
		
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
