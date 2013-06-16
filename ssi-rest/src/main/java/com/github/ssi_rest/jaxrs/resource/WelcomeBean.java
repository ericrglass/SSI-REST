package com.github.ssi_rest.jaxrs.resource;

import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import com.github.ssi_rest.SsiRestLogger;
import com.github.ssi_rest.jaxrs.AbstractJaxrs;

public class WelcomeBean extends AbstractJaxrs {

	public static final String SERVICE_NAME = "welcome";
	public static final String URI_RESOURCE = "/" + SERVICE_NAME;

	public void processRequest(HttpServletRequest req, String action,
			EntityManager em, boolean debug) {
		if (action == null) {
			defaultAction(req, debug);
		} else if (debug) {
			SsiRestLogger.LOGGER
					.warning("Debug Message: WelcomeBean - "
							+ "processRequest method was called with action '"
							+ action
							+ "', but does not have support for it; and the request resource '"
							+ (((req == null) || (req.getServletPath() == null)) ? "N\\A"
									: req.getServletPath()) + "'.");
		}
	}

	private void defaultAction(HttpServletRequest req, boolean debug) {
		if (req == null) {
			SsiRestLogger.LOGGER.log(Level.SEVERE,
					"WelcomeBean - processRequest (default action) "
							+ "is missing the required HttpServletRequest.");
			return;
		}

		req.setAttribute(
				UserProfileFormResource.REQ_ATTR_USER_PROFILE_FORM_GET_URL,
				getResourceContextPath(UserProfileFormResource.URI_RESOURCE,
						req));
		req.setAttribute(
				UserProfileFormResource.REQ_ATTR_USER_PROFILE_FORM_PUT_URL,
				getResourceContextPath(UserProfileFormResource.URI_RESOURCE,
						req));
	}

}
