package com.github.ssi_rest.bean;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class LanguageCountryOptionBean {

	private String code;
	private String description;

	public LanguageCountryOptionBean() {
		super();
	}

	public LanguageCountryOptionBean(String code, String description) {
		super();
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
