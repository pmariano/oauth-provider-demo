package com.pmariano.oauth.webresource;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pmariano.oauth.dao.UserDAO;
import com.pmariano.oauth.webresource.view.Result;

public class UserResourceTest {
	
	private UserResource userResource;
	@Mock private UserDAO userDAO;
	@Mock private Result result;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		userResource = new UserResource(result, userDAO);
	}

	@Test
	public void shouldReturnBadRequestIfUserIsNull() {
		when(userDAO.fromToken("123ABC")).thenReturn(null);
		userResource.fromCredential("123ABC");
		verify(result).badRequest();
	}
	
	@Test
	public void shouldReturnOkIfHasAUserWithTheToken() {
		when(userDAO.fromToken("123ABC")).thenReturn("1");
		userResource.fromCredential("123ABC");
		verify(result).ok("1");
	}

}
