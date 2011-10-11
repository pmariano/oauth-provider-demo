package com.pmariano.oauth.application.filters;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SessionFilterTest {
	private SessionFilter sessionFilter;

	@Mock
	private HttpServletRequest request;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.sessionFilter = new SessionFilter(request);
	}

	@Test
	public void shouldCloseTheSessionIfItsNotNullAndIsOpen() {
		Session session = mock(Session.class);
		when(request.getAttribute("session")).thenReturn(session);
		when(session.isOpen()).thenReturn(true);
		
		sessionFilter.filter(null, null);
		
		verify(session).close();
	}
	
	@Test
	public void shouldNotCloseTheSessionIfItsClosed() {
		Session session = mock(Session.class);
		when(session.isOpen()).thenReturn(false);
		when(request.getAttribute("session")).thenReturn(null);
		
		sessionFilter.filter(null, null);
		
		verify(session, never()).close();
	}
}
