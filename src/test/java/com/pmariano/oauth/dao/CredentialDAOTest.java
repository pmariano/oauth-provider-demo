package com.pmariano.oauth.dao;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import com.pmariano.oauth.model.Credential;
import com.pmariano.oauth.model.Provider;
import com.pmariano.oauth.model.User;

public class CredentialDAOTest extends AbstractDAOTest {

	private CredentialDAO credentialDAO;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		credentialDAO = new CredentialDAO(session);
	}

	@Test
	public void shouldReturnTheCredentialByUserAndProvider() {
		Provider provider = new Provider();
		session.save(provider);
		
		User user = new User();
		session.save(user);
		
		Credential credential = new Credential();
		credential.setUser(user);
		credential.setProvider(provider);
		
		credentialDAO.save(credential);
		
		Credential loadedCredential = credentialDAO.findBy(provider, user);
		
		Assert.assertEquals(loadedCredential, credential);
	}
	
	@Test
	public void shouldReturnTheCredentialByCodeAndRefreshToken() {
		Provider provider = new Provider();
		session.save(provider);
		
		User user = new User();
		session.save(user);
		
		Credential credential = new Credential();
		credential.setUser(user);
		credential.setRefreshToken("BLABLA");
		credential.setProvider(provider);
		
		credentialDAO.save(credential);
		
		Credential loadedCredential = credentialDAO.findBy(credential.getId(), "BLABLA");
		
		Assert.assertEquals(loadedCredential, credential);
	}

}
