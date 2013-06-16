package com.github.ssi_rest.bean;

import java.util.List;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class UserProfileFormBean {

	private boolean success;
	private UserProfileSessionBean userProfileSession;
	private List<LanguageCountryOptionBean> languageOptions;
	private Map<String, List<LanguageCountryOptionBean>> languageCountryOptions;

	public UserProfileFormBean() {
		super();
	}

	public UserProfileFormBean(boolean success) {
		super();
		this.success = success;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public UserProfileSessionBean getUserProfileSession() {
		return userProfileSession;
	}

	public void setUserProfileSession(UserProfileSessionBean userProfileSession) {
		this.userProfileSession = userProfileSession;
	}

	public List<LanguageCountryOptionBean> getLanguageOptions() {
		return languageOptions;
	}

	public void setLanguageOptions(
			List<LanguageCountryOptionBean> languageOptions) {
		this.languageOptions = languageOptions;
	}

	public Map<String, List<LanguageCountryOptionBean>> getLanguageCountryOptions() {
		return languageCountryOptions;
	}

	public void setLanguageCountryOptions(
			Map<String, List<LanguageCountryOptionBean>> languageCountryOptions) {
		this.languageCountryOptions = languageCountryOptions;
	}
}
