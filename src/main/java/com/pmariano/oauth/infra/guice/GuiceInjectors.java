package com.pmariano.oauth.infra.guice;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.servlet.RequestScoped;

public class GuiceInjectors extends AbstractModule {

	@Override
	protected void configure() { 

	}
	
	@Provides 
	@RequestScoped
	@Inject
	public Session provideSession(HttpServletRequest request, SessionFactory sessionFactory) {
		Session session = sessionFactory.openSession();
		request.setAttribute("session", session);
		return session;
	}
	
	@Provides 
	@Singleton
	@Inject
	public SessionFactory provideEntityManagerFactory() {
		return new SessionFactoryProvider().getSessionFactory();
	}
}
