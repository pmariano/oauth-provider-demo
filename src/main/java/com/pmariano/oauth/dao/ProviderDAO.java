package com.pmariano.oauth.dao;

import org.hibernate.Session;

import com.google.inject.Inject;
import com.pmariano.oauth.model.Provider;

public class ProviderDAO {
	
	private final Session session;

	@Inject
	public ProviderDAO(Session session) {
		this.session = session;
	}

	public Provider find(String id) {
		return (Provider) session.get(Provider.class, id);
	}

	public void save(Provider pp) {
		session.beginTransaction();
		session.save(pp);
		session.getTransaction().commit();		
	}

}
