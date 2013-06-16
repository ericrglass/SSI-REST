package com.github.ssi_rest.bean;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@Named(UserProfileSessionBean.NAME)
@SessionScoped
@JsonAutoDetect
public class UserProfileSessionBean implements Serializable, Cloneable {

	public static final String NAME = "com.github.ssi_rest.bean.UserProfileSession";

	private static final long serialVersionUID = 8952880919172929715L;

	private String language;
	private String country;

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Override
	public Object clone() {
		UserProfileSessionBean clone = new UserProfileSessionBean();
		clone.setLanguage(language);
		clone.setCountry(country);
		return clone;
	}

	@Override
	public String toString() {
		StringBuilder beanInfo = new StringBuilder();
		beanInfo.append("UserProfileSessionBean {");
		beanInfo.append("language: ");
		beanInfo.append(language);
		beanInfo.append(" , country: ");
		beanInfo.append(country);
		beanInfo.append("}");
		return beanInfo.toString();
	}

}
