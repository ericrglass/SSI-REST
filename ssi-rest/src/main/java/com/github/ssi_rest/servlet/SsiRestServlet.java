package com.github.ssi_rest.servlet;

import java.io.IOException;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Globals;
import org.apache.catalina.ssi.SSIServlet;

import com.github.ssi_rest.SsiRestLogger;
import com.github.ssi_rest.service.IServiceDelegator;

public class SsiRestServlet extends SSIServlet {

	private static final long serialVersionUID = 4542259497206614997L;

	private String serviceDelegatorJNDIName = null;

	@Override
	public void init() throws ServletException {
		super.init();
		serviceDelegatorJNDIName = getInitParameter(IServiceDelegator.INIT_PARAM_JNDI_NAME);

		// TODO: Add code here for HTML compressor options
		// boolean buffered =
		// Boolean.parseBoolean(getServletConfig().getInitParameter("buffered"));
	}

	@Override
	protected void requestHandler(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		boolean alreadyInProcessSSI = false;
		Object ssi_flag = req.getAttribute(Globals.SSI_FLAG_ATTR);

		if (ssi_flag != null) {
			alreadyInProcessSSI = Boolean.parseBoolean(ssi_flag.toString());
		}

		if (!alreadyInProcessSSI && (serviceDelegatorJNDIName != null)
				&& (serviceDelegatorJNDIName.trim().length() > 0)) {
			Object serviceDelegator = null;
			NamingException namExc = null;

			try {
				InitialContext ic = new InitialContext();
				serviceDelegator = ic.lookup(serviceDelegatorJNDIName.trim());
			} catch (NamingException e) {
				serviceDelegator = null;
				namExc = e;
				SsiRestLogger.LOGGER.log(
						Level.SEVERE,
						"Service Delegator with JNDI Name ("
								+ serviceDelegatorJNDIName.trim()
								+ ") had the following NamingException: "
								+ e.getMessage(), e);
			}

			if (serviceDelegator instanceof IServiceDelegator) {
				String service = req
						.getParameter(IServiceDelegator.QUERY_PARAM_SERVICE);

				if ((service == null) || (service.trim().length() == 0)) {
					service = req.getServletPath();

					if (service.startsWith("/")) {
						if (service.length() > 1) {
							service = service.substring(1);
						} else {
							service = "";
						}
					}

					if (service.endsWith(".html")) {
						if (service.length() > 5) {
							service = service
									.substring(0, service.length() - 5);
						} else {
							service = "";
						}
					}
				}

				String action = req
						.getParameter(IServiceDelegator.QUERY_PARAM_ACTION);
				((IServiceDelegator) serviceDelegator).processRequest(req,
						service, action);
			} else if (namExc == null) {
				SsiRestLogger.LOGGER
						.log(Level.SEVERE,
								"Service Delegator with JNDI Name ("
										+ serviceDelegatorJNDIName.trim()
										+ ") could not be called, becasue it did not implement IServiceDelegator.");
			}
		}

		super.requestHandler(req, res);
	}

}
