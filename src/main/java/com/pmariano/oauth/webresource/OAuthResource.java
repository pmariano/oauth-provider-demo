package com.pmariano.oauth.webresource;

import java.util.UUID;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.inject.Inject;
import com.pmariano.oauth.dao.CredentialDAO;
import com.pmariano.oauth.dao.ProviderDAO;
import com.pmariano.oauth.dao.UserDAO;
import com.pmariano.oauth.model.Credential;
import com.pmariano.oauth.model.Provider;
import com.pmariano.oauth.model.User;
import com.pmariano.oauth.webresource.view.Result;

@Path("oauth")
public class OAuthResource {

	private final Result result;
	private final ProviderDAO providerDAO;
	private final UserDAO userDAO;
	private final CredentialDAO credentialDAO;

	@Inject
	public OAuthResource(Result result, ProviderDAO providerDAO, UserDAO userDAO, CredentialDAO credentialDAO) {
		this.result = result;
		this.providerDAO = providerDAO;
		this.userDAO = userDAO;
		this.credentialDAO = credentialDAO;
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("access")
	public Response getCredentials(@QueryParam("client_id") String clientId, @QueryParam("redirect_uri") String callback) {
		Provider provider = providerDAO.find(clientId);

		if (provider == null) {
			return result.notFound();
		}

		User loggedUser = userDAO.find("8aeebcbb32a6392a0132a6392d880000");
		Credential credential = this.credentialDAO.findBy(provider, loggedUser);

		if (credential != null) {
			return result.redirectTo(callback + "?code=" + credential.getId());
		}

		result.put("client", provider);
		result.put("callback", callback);

		return result.render("/accept.jsp");
	}

	@Produces(MediaType.TEXT_HTML)
	@POST
	@Path("accept")
	public Response acceptOauth(@FormParam("client_id") String clientId, @FormParam("redirect_uri") String callback) {
		User user = userDAO.find("8aeebcbb32a6392a0132a6392d880000");
		Provider provider = providerDAO.find(clientId);

		Credential authorization = new Credential(user, provider, UUID.randomUUID().toString());
		credentialDAO.save(authorization);

		return result.redirectTo(callback + "?code=" + authorization.getId());
	}

	@POST
	@Path("deny")
	public Response denyOauth(@FormParam("redirect_uri") String callback) {
		return result.redirectTo(callback + "?error_reason=user_denied&error=access_denied&error_description=The+user+denied+your+reques");
	}

	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("token")
	public Response accessToken(@QueryParam("client_id") String clientId, @QueryParam("code") String code,
			@QueryParam("client_secret") String secret, @QueryParam("grant_type") String grantType) {
		
		Credential credential = null;
		
		if(!"authorization_code".equals(grantType)) {
			return result.badRequest();
		} 
		
		credential = credentialDAO.find(code);
		
		if(credential == null || !credential.validateAppSecret(secret)) {
			return result.badRequest();
		}
		
		return result.ok(credential.createNewAppToken().toJson());
		
	}
}
