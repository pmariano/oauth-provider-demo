package com.pmariano.oauth.model;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class Token {

	private final String token;

	public Token(String token, String refreshToken) {
		this.token = token;
	}
	
	
	public String toJson() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("access_token", this.token);
			jsonObject.put("expires_in", "500");
		} catch (JSONException e) {
			throw new IllegalArgumentException(e);
		}
		return "access_token=" + token;
	}

}
