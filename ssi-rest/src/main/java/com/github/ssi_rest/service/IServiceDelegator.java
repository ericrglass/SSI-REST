package com.github.ssi_rest.service;

import javax.servlet.http.HttpServletRequest;

public interface IServiceDelegator {

	public static final String INIT_PARAM_JNDI_NAME = "com.github.ssi_rest.service.DelegatorJNDIName";

	public static final String QUERY_PARAM_SERVICE = "service";
	public static final String QUERY_PARAM_ACTION = "action";

	public void processRequest(HttpServletRequest req, String service,
			String action, boolean debug);

}
