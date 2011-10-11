package com.pmariano.oauth.webresource.view;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.servlet.RequestScoped;
import com.sun.jersey.api.view.Viewable;

@RequestScoped
public class DefaultResult implements Result{
	private static final Logger LOGGER = Logger.getLogger(DefaultResult.class);
	
	@Inject
	public HttpServletRequest request;

	public Result put(String paramName, Object paramValue) {
		this.request.setAttribute(paramName, paramValue);
		return this;
	}

	public Response render(String template) {
		return Response.ok(new Viewable(template)).build();
	}

	public Response redirectTo(String uri) {
		try {
			return Response.seeOther(new URI(uri)).build();
		} catch (URISyntaxException e) {
			LOGGER.error(String.format("A URI [%s] de redirecionamento não é válida", uri), e);
			return Response.serverError().build();
		}
	}
	
	public Response sendError() {
		return Response.status(500).build();
	}

	@Override
	public Response success() {
		return Response.status(200).build();
	}

	@Override
	public Response forbidden() {
		return Response.status(403).build();
	}

	@Override
	public Response notFound() {
		return Response.status(Status.NOT_FOUND).build();
	}

	@Override
	public Response badRequest() {
		return Response.status(Status.BAD_REQUEST).build();
	}

	@Override
	public Response ok(String message) {
		CacheControl cacheControl = new CacheControl();
		cacheControl.setNoCache(true);
		cacheControl.setNoStore(true);
		cacheControl.setMaxAge(0);
		cacheControl.setSMaxAge(0);
		cacheControl.setNoTransform(false);
		cacheControl.setMustRevalidate(true);
		
		return Response.ok(message).cacheControl(cacheControl).build();
	}

	@Override
	public void throwError(Status status) {
		throw new WebApplicationException(status);
	}

}
