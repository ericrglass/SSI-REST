package com.github.ssi_rest.jpa;

import org.eclipse.persistence.config.SessionCustomizer;
import org.eclipse.persistence.sessions.Session;

public class SchemaNameSessionCustomizer implements SessionCustomizer {

	public static final String SYSTEM_PROPERTY = "com.github.ssi_rest.jpa.SchemaName";

	private String schemaName = "ssi_rest";

	public SchemaNameSessionCustomizer() {
		super();
		schemaName = System.getProperty(SYSTEM_PROPERTY);

		if ((schemaName == null) || (schemaName.trim().length() == 0)) {
			schemaName = "ssi_rest";
		}
	}

	@Override
	public void customize(Session session) throws Exception {
		session.getLogin().setTableQualifier(schemaName);
	}

}
