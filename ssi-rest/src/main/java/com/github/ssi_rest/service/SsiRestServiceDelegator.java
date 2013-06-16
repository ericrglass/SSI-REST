package com.github.ssi_rest.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import com.github.ssi_rest.SsiRestLogger;
import com.github.ssi_rest.jaxrs.resource.DiscountCodeBean;
import com.github.ssi_rest.jaxrs.resource.WelcomeBean;

@Stateless
public class SsiRestServiceDelegator implements IServiceDelegator {

	@PersistenceContext
	protected EntityManager em;

	@Override
	public void processRequest(HttpServletRequest req, String service,
			String action, boolean debug) {
		if (WelcomeBean.SERVICE_NAME.equalsIgnoreCase(service)) {
			WelcomeBean bean = new WelcomeBean();
			bean.processRequest(req, action, em, debug);
		} else if (DiscountCodeBean.SERVICE_NAME.equalsIgnoreCase(service)) {
			DiscountCodeBean bean = new DiscountCodeBean();
			bean.processRequest(req, action, em, debug);
		} else if (debug) {
			SsiRestLogger.LOGGER
					.warning("Debug Message: SsiRestServiceDelegator "
							+ "was called with service name '"
							+ service
							+ "' and action '"
							+ action
							+ "', but does not have a process for "
							+ "the service; and the request resource '"
							+ ((req.getServletPath() == null) ? "N\\A" : req
									.getServletPath()) + "'.");
		}
	}

}
