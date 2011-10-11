package com.pmariano.oauth.webresource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.pmariano.oauth.dao.UserDAO;
import com.pmariano.oauth.webresource.view.Result;

@Path("user")
public class UserResource {
	
	private final Result result;
	private final UserDAO userDAO;

	@Inject
	public UserResource(Result result, UserDAO userDAO) {
		this.result = result;
		this.userDAO = userDAO;
	}
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response fromCredential(@QueryParam("access_token") String token) {
		String user = userDAO.fromToken(token);
		if (user == null) {
			return result.badRequest();
		}
		return result.ok(user);
	}

}
