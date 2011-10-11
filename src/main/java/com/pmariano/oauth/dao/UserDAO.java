package com.pmariano.oauth.dao;

import org.hibernate.Session;

import redis.clients.jedis.Jedis;

import com.google.inject.Inject;
import com.pmariano.oauth.model.User;


public class UserDAO {

	private final Session session;

	@Inject
	public UserDAO(Session session) {
		this.session = session;
	}

	public User find(String id) {
		return (User) session.get(User.class, id);
	}

	public String fromToken(String token) {
		Jedis jedis = new Jedis("localhost");
		
		return jedis.get(token);
	}
}
