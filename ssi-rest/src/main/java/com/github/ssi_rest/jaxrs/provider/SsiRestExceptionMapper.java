package com.github.ssi_rest.jaxrs.provider;

import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class SsiRestExceptionMapper implements ExceptionMapper<Exception> {

	@Context
	private UriInfo uriInfo;

	@Context
	private HttpServletRequest request;

	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(Exception exc) {
		StringBuilder errorUriStr = new StringBuilder(uriInfo.getAbsolutePath()
				.toString());
		int contextPos = errorUriStr.indexOf(request.getContextPath());

		if ((contextPos > -1)
				&& (contextPos + request.getContextPath().length() < errorUriStr
						.length())) {
			errorUriStr.delete(contextPos + request.getContextPath().length(),
					errorUriStr.length());
		}

		errorUriStr.append("/error.html");

		try {
			return Response.seeOther(new URI(errorUriStr.toString())).build();
		} catch (URISyntaxException e) {
		}

		return Response.serverError().build();
	}

}
