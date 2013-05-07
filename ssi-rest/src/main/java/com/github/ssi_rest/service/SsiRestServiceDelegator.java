package com.github.ssi_rest.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import com.github.ssi_rest.jaxrs.resource.DiscountCodeBean;

@Stateless
public class SsiRestServiceDelegator implements IServiceDelegator {

	@PersistenceContext
	protected EntityManager em;

	@Override
	public void processRequest(HttpServletRequest req, String service,
			String action) {
		if ("discountCode".equalsIgnoreCase(service)) {
			DiscountCodeBean bean = new DiscountCodeBean();
			bean.processRequest(req, action, em);
		}
	}

}
