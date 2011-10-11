package com.pmariano.oauth.infra.guice;

import org.apache.log4j.Logger;
import org.hibernate.SessionFactory;

public class SessionFactoryProvider {
	
	private static Logger LOGGER = Logger.getLogger(SessionFactoryProvider.class);
	private static SessionFactory sessionFactory;

	public SessionFactoryProvider() {
		if (sessionFactory == null) {
			LOGGER.debug("Inicializando sessionfactory...");
			org.hibernate.cfg.Configuration conf = new org.hibernate.cfg.Configuration();
			sessionFactory = conf.configure().buildSessionFactory();
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}
	
	public void close() {
		if (sessionFactory != null && !sessionFactory.isClosed()) {
			sessionFactory.close();
		}
		sessionFactory = null;
	}

}
