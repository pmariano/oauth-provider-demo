package com.pmariano.oauth.dao;

import org.hibernate.Query;
import org.hibernate.Session;

import com.google.inject.Inject;
import com.pmariano.oauth.model.Credential;
import com.pmariano.oauth.model.Provider;
import com.pmariano.oauth.model.User;


public class CredentialDAO {

	private final Session session;

	@Inject
	public CredentialDAO(Session session) {
		this.session = session;
	}

	public void save(Credential authorization) {
		session.beginTransaction();
		session.save(authorization);
		session.getTransaction().commit();
	}

	public Credential find(String id) {
		return (Credential) session.get(Credential.class, id);
	}

	public Credential findBy(Provider provider, User user) {
		Query query = session.createQuery("from Credential where user = :user and provider = :provider");
		query.setParameter("user", user);
		query.setParameter("provider", provider);
		return (Credential) query.uniqueResult();
	}

	public Credential findBy(String code, String refreshToken) {
		Query query = session.createQuery("from Credential where id = :code and refreshToken = :refresh");
		query.setParameter("code", code);
		query.setParameter("refresh", refreshToken);
		return (Credential) query.uniqueResult();
	}
}
