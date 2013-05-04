package com.github.ssi_rest.jaxrs;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;

public abstract class AbstractJaxrs {

	@Context
	protected HttpServletRequest request;

	public String getResourceContextPath(String resourceUri,
			HttpServletRequest request) {
		if ((resourceUri == null) || (resourceUri.trim().length() == 0)
				|| (request == null)) {
			return "";
		}

		StringBuilder path = new StringBuilder(request.getContextPath());
		path.append(SsiRestApplication.PATH);

		if (!resourceUri.trim().startsWith("/")) {
			path.append("/");
		}

		path.append(resourceUri.trim());
		return path.toString();
	}

	protected String getResourceContextPath(String resourceUri) {
		return getResourceContextPath(resourceUri, request);
	}

}
