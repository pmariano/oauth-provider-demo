package com.pmariano.oauth.application.filters;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

import org.hibernate.Session;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponse;
import com.sun.jersey.spi.container.ContainerResponseFilter;

public class SessionFilter implements ContainerRequestFilter, ContainerResponseFilter{
	
	@Context
    private HttpServletRequest request;
	
	public SessionFilter() {
	}
	
	public SessionFilter(HttpServletRequest request) {
		this.request = request;
	}
	
	
	@Override
	public ContainerRequest filter(ContainerRequest request) {
		this.request.setAttribute("baseuri", request.getBaseUri().toString());
		return request;
	}

	@Override
	public ContainerResponse filter(ContainerRequest request, ContainerResponse response) {
		Session session = (Session) this.request.getAttribute("session");
		
		if(session != null && session.isOpen()) {
			session.close();
		}
		
		return response;
	}
	
}
