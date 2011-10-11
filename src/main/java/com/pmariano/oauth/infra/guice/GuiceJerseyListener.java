package com.pmariano.oauth.infra.guice;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.pmariano.oauth.application.filters.SessionFilter;
import com.sun.jersey.api.core.PackagesResourceConfig;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;

@WebListener
public class GuiceJerseyListener extends GuiceServletContextListener {

	public static final String RESOURCE_PACKAGES = "com.pmariano.oauth";
	public static final String RESOURCE_PATTERN = "*";
	
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		super.contextInitialized(servletContextEvent);
	}

	protected Injector getInjector() {
		return Guice.createInjector(
			new JerseyServletModule() {
				@Override
				protected void configureServlets() {
					Map<String, String> params = new HashMap<String, String>();
					params.put(PackagesResourceConfig.PROPERTY_PACKAGES, RESOURCE_PACKAGES);
					params.put("com.sun.jersey.spi.container.ContainerRequestFilters", SessionFilter.class.getName());
					params.put("com.sun.jersey.spi.container.ContainerResponseFilters", SessionFilter.class.getName());
					params.put("com.sun.jersey.config.property.JSPTemplatesBasePath", "/WEB-INF/jsp/");
					serve(RESOURCE_PATTERN).with(GuiceContainer.class, params);
				}
			},
			new GuiceInjectors()
		);
	}

}
