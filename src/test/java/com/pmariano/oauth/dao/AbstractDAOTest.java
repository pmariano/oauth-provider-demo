package com.pmariano.oauth.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class AbstractDAOTest {
	
	private static SessionFactory factory;
	protected Session session;

	@BeforeClass
	public static void beforeClass() {
		Configuration configuration = new Configuration();
		configuration.configure(AbstractDAOTest.class.getResource("/hibernate.cfg.xml"));
		factory = configuration.buildSessionFactory();
	}
	
	@Before
	public void beforeEach() {
		this.session = factory.openSession();
	}
	
	@After
	public void afterEach() {
		this.session.close();
	}

}
