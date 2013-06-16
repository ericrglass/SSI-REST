package com.github.ssi_rest.jaxrs;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.github.ssi_rest.jaxrs.provider.SsiRestExceptionMapper;
import com.github.ssi_rest.jaxrs.resource.DiscountCodeBean;
import com.github.ssi_rest.jaxrs.resource.UserProfileFormResource;

@ApplicationPath(SsiRestApplication.PATH)
public class SsiRestApplication extends Application {

	public static final String PATH = "/resource";

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> jaxrsClasses = new HashSet<Class<?>>();

		// JAX-RS Resources
		jaxrsClasses.add(DiscountCodeBean.class);
		jaxrsClasses.add(UserProfileFormResource.class);

		// JAX-RS Providers, MessageBodyWriters, and MessageBodyReaders
		jaxrsClasses.add(JsonMessageProvider.class);
		jaxrsClasses.add(SsiRestExceptionMapper.class);

		return jaxrsClasses;
	}

}
