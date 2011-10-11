package com.pmariano.oauth.infra.guice;

import javax.servlet.annotation.WebFilter;

import com.google.inject.servlet.GuiceFilter;

@WebFilter(urlPatterns={GuiceJerseyListener.RESOURCE_PATTERN})
public class CustomGuiceFilter extends GuiceFilter {
	

}
