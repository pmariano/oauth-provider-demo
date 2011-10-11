package com.pmariano.oauth.webresource;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.pmariano.oauth.dao.CredentialDAO;
import com.pmariano.oauth.dao.ProviderDAO;
import com.pmariano.oauth.dao.UserDAO;
import com.pmariano.oauth.model.Credential;
import com.pmariano.oauth.model.Provider;
import com.pmariano.oauth.model.Token;
import com.pmariano.oauth.model.User;
import com.pmariano.oauth.webresource.view.Result;

public class OAuthResourceTest {
	private OAuthResource oAuthResource;
	
	@Mock private CredentialDAO credentialDAO;
	@Mock private Result result;
	@Mock private ProviderDAO providerDAO;
	@Mock private UserDAO userDAO;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.oAuthResource = new OAuthResource(result, providerDAO, userDAO, credentialDAO);
	}

	@Test
	public void shouldrenderNotFoundIfThereIsNoProviderWhenTryingToGrantAcess() {
		when(providerDAO.find("1ABC")).thenReturn(null);
		
		oAuthResource.getCredentials("1ABC", "http://meusite.com");
		
		verify(result).notFound();
	}
	
	@Test
	public void shouldRedirectToCallbackURWithTheCredentialLIfExistsCredentialsForTheGivenUserOnThisProvider() {
		Provider provider = new Provider();
		User user = new User();
		Credential credential = new Credential();
		credential.setId("ABCDEFG1234");
		
		when(userDAO.find(Mockito.anyString())).thenReturn(user);
		when(providerDAO.find("1ABC")).thenReturn(provider);
		when(credentialDAO.findBy(provider, user)).thenReturn(credential);

		oAuthResource.getCredentials("1ABC", "http://meusite.com");
		
		verify(result).redirectTo("http://meusite.com?code=ABCDEFG1234");
	}
	
	@Test
	public void shouldRenderTheUserAcceptPageIfEverythingsIsOkWhenTryingToGrantAcessAndThereIsNoCredentialsToTheCurrentUser() {
		Provider provider = new Provider();
		User user = new User();
		
		when(userDAO.find(Mockito.anyString())).thenReturn(user);
		when(providerDAO.find("1ABC")).thenReturn(provider);
		when(credentialDAO.findBy(provider, user)).thenReturn(null);
		
		oAuthResource.getCredentials("1ABC", "http://meusite.com");
		
		verify(result).put("client", provider);
		verify(result).put("callback", "http://meusite.com");
		
		verify(result).render("/accept.jsp");
	}
	
	@Test
	public void shouldThrowBadRequestIfTheGrantTypeIsNotAuthorizationCodeOnGetAnAccessToken() {
		oAuthResource.accessToken(null, null, null, "other_grant_type");
		
		verify(result).badRequest();
	}
	
	@Test
	public void shouldThrowBadRequestIfTheThereIsNoCredentialWithTheGivenCodeOnGetAnAccessToken() {
		when(credentialDAO.find("ABCDE123")).thenReturn(null);
		oAuthResource.accessToken(null, "ABCDE123", null, "authorization_code");
		
		verify(result).badRequest();
	}
	
	@Test
	public void shouldThrowBadRequestIfTheAppSecretIsNotTheSameOfCredentialsOnGetAnAccessToken() {
		Provider provider = new Provider();
		provider.setAppSecret("OTHERSECRET123SECRET");
		
		Credential credential = new Credential();
		credential.setProvider(provider);
		
		when(credentialDAO.find("ABCDE123")).thenReturn(credential);
		oAuthResource.accessToken("CLIENTID", "ABCDE123", "SECRET123SECRET", "authorization_code");
		
		verify(result).badRequest();
	}
	
	@Test
	public void shouldRenderTheJsonWithTheNewApplicationTokenIfTheApplicationSuccessfulGrantAccess() {
		Token accessToken = new Token("ACESSTOKEN123", "NOREFRESHTOKEN");
		
		Provider provider = new Provider();
		provider.setAppSecret("SECRET123SECRET");
		
		Credential spy = spy(new Credential());
		spy.setProvider(provider);
		doReturn(accessToken).when(spy).createNewAppToken();
		
		when(credentialDAO.find("ABCDE123")).thenReturn(spy);
		
		oAuthResource.accessToken("CLIENTID", "ABCDE123", "SECRET123SECRET", "authorization_code");
		
		verify(result).ok(accessToken.toJson());
	}

}
