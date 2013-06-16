package com.github.ssi_rest.jaxrs.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.codehaus.jackson.map.ObjectMapper;

import com.github.ssi_rest.SsiRestLogger;
import com.github.ssi_rest.jaxrs.AbstractJaxrs;
import com.github.ssi_rest.model.DiscountCode;

@Path(DiscountCodeBean.URI_RESOURCE)
@Named
@Stateless
public class DiscountCodeBean extends AbstractJaxrs {

	public static final String SERVICE_NAME = "discountCode";
	public static final String URI_RESOURCE = "/" + SERVICE_NAME;

	public static final String REQ_ATTR_LIST_JSON = "DISCOUNT_CODE_LIST_JSON";

	@PersistenceContext
	protected EntityManager em;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<DiscountCode> getAllDiscountCodeList() {
		TypedQuery<DiscountCode> query = em.createNamedQuery(
				DiscountCode.FIND_ALL, DiscountCode.class);
		return query.getResultList();
	}

	public void processRequest(HttpServletRequest req, String action,
			EntityManager em, boolean debug) {
		this.em = em;

		if (action == null) {
			defaultAction(req, debug);
		} else if (debug) {
			SsiRestLogger.LOGGER
					.warning("Debug Message: DiscountCodeBean - "
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
					"DiscountCodeBean - processRequest (default action) "
							+ "is missing the required HttpServletRequest.");
			return;
		}

		String listJson = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			listJson = mapper.writeValueAsString(getAllDiscountCodeList());
		} catch (Exception e) {
			listJson = null;
			SsiRestLogger.LOGGER.log(
					Level.SEVERE,
					"DiscountCodeBean - processRequest (default "
							+ "action) had the following exception: "
							+ e.getMessage(), e);
		}

		if ((listJson == null) || (listJson.length() == 0)) {
			listJson = "[]";
		}

		if (debug) {
			SsiRestLogger.LOGGER.warning("Debug Message: DiscountCodeBean - "
					+ "defaultAction method has placed the JSON '"
					+ listJson
					+ "' into the request as attribute '"
					+ REQ_ATTR_LIST_JSON
					+ "' for the request resource '"
					+ ((req.getServletPath() == null) ? "N\\A" : req
							.getServletPath()) + "'.");
		}

		req.setAttribute(REQ_ATTR_LIST_JSON, listJson);
	}

}
