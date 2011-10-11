package com.pmariano.oauth.webresource.view;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultResult.class)
public interface Result {
	Response render(String template);
	
	Result put(String name, Object obj);

	Response redirectTo(String uri);

	Response sendError();

	Response success();

	Response forbidden();

	Response notFound();

	Response badRequest();

	Response ok(String message);

	void throwError(Status notFound);

}
