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

	public static final String URI_RESOURCE = "/discountCode";

	public static final String REQ_ATTR_LIST_JSON = "DISCOUNT_CODE_LIST_JSON";

	public static final String JS_VAR_NAME_LIST_DATA = "discountCodeListData";

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
			EntityManager em) {
		this.em = em;

		if (action == null) {
			String listJson = null;
			ObjectMapper mapper = new ObjectMapper();

			try {
				listJson = mapper.writeValueAsString(getAllDiscountCodeList());
			} catch (Exception e) {
				listJson = null;
				SsiRestLogger.LOGGER.log(Level.SEVERE, "", e);
			}

			if ((listJson == null) || (listJson.length() == 0)) {
				listJson = "var " + JS_VAR_NAME_LIST_DATA + " = [];";
			} else {
				listJson = "var " + JS_VAR_NAME_LIST_DATA + " = " + listJson
						+ ";";
			}

			req.setAttribute(REQ_ATTR_LIST_JSON, listJson);
		}
	}

}
