package com.github.ssi_rest.jaxrs.provider;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.github.ssi_rest.SsiRestLogger;

@Provider
public class SsiRestExceptionMapper implements
		ExceptionMapper<WebApplicationException> {

	@Context
	private UriInfo uriInfo;

	@Context
	private HttpServletRequest request;

	/**
	 * @see javax.ws.rs.ext.ExceptionMapper#toResponse(java.lang.Throwable)
	 */
	@Override
	public Response toResponse(WebApplicationException exc) {
		if ((exc.getResponse() != null)
				&& (exc.getResponse().getStatus() != Response.Status.NOT_FOUND
						.getStatusCode())) {
			Response.Status status = null;
			for (Response.Status aStatus : Response.Status.values()) {
				if (aStatus.getStatusCode() == exc.getResponse().getStatus()) {
					status = aStatus;
					break;
				}
			}
			String statusMessage = "N/A";
			if (status != null) {
				statusMessage = status.toString();
			} else if (exc.getResponse().getStatus() == 405) {
				statusMessage = "(" + request.getMethod()
						+ ") Method Not Allowed";
			}
			String msg = "HTTP status code (" + exc.getResponse().getStatus()
					+ ") for URI (" + request.getRequestURI()
					+ ") and content type (" + request.getContentType()
					+ "); with Media Data (" + exc.getResponse().getMetadata()
					+ "), Entity (" + exc.getResponse().getEntity()
					+ "), and message: " + statusMessage;
			SsiRestLogger.LOGGER.log(Level.SEVERE, msg, exc);
			URI requestUri = null;
			try {
				requestUri = new URI(request.getRequestURI());
			} catch (URISyntaxException e) {
			}
			return Response.serverError().entity(msg).location(requestUri)
					.build();
		}

		SsiRestLogger.LOGGER.log(Level.SEVERE, "The mapping rule for the URI ("
				+ request.getRequestURI()
				+ ") could not be found, because JAX-RS did not "
				+ "have it registered with the Java @Path annotation.", exc);
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
