package com.github.ssi_rest.jaxrs.resource;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Path;

import com.github.ssi_rest.jaxrs.AbstractJaxrs;
import com.github.ssi_rest.model.DiscountCode;

@Path(DiscountCodeBean.URI_RESOURCE)
@Named
@Stateless
public class DiscountCodeBean extends AbstractJaxrs {

	public static final String URI_RESOURCE = "/discountCode";

	@PersistenceContext
	protected EntityManager em;

	public List<DiscountCode> getAllDiscountCodeList() {
		TypedQuery<DiscountCode> query = em.createNamedQuery(
				DiscountCode.FIND_ALL, DiscountCode.class);
		return query.getResultList();
	}

}
