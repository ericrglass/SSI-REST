package com.github.ssi_rest.jaxrs.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.github.ssi_rest.bean.LanguageCountryOptionBean;
import com.github.ssi_rest.bean.UserProfileFormBean;
import com.github.ssi_rest.bean.UserProfileSessionBean;
import com.github.ssi_rest.jaxrs.AbstractJaxrs;
import com.github.ssi_rest.util.I18nUtils;

@Path(UserProfileFormResource.URI_RESOURCE)
@Named
@Stateless
public class UserProfileFormResource extends AbstractJaxrs {

	public static final String URI_RESOURCE = "/userProfileForm";

	public static final String REQ_ATTR_USER_PROFILE_FORM_GET_URL = "USER_PROFILE_FORM_GET_URL";
	public static final String REQ_ATTR_USER_PROFILE_FORM_PUT_URL = "USER_PROFILE_FORM_PUT_URL";

	private UserProfileSessionBean userProfileSession;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public UserProfileFormBean view() {
		getUserProfileSession();

		if (userProfileSession.getLanguage() == null) {
			userProfileSession.setLanguage(request.getLocale().getLanguage());
		}

		if (userProfileSession.getCountry() == null) {
			userProfileSession.setCountry(request.getLocale().getCountry());
		}

		return createUserProfileFormBean(true);
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UserProfileFormBean update(UserProfileFormBean userProfForm) {
		getUserProfileSession();

		if ((userProfForm != null)
				&& (userProfForm.getUserProfileSession() != null)
				&& (userProfForm.getUserProfileSession().getLanguage() != null)) {
			userProfileSession.setLanguage(userProfForm.getUserProfileSession()
					.getLanguage());
		} else {
			userProfileSession.setLanguage(request.getLocale().getLanguage());
		}

		if ((userProfForm != null)
				&& (userProfForm.getUserProfileSession() != null)
				&& (userProfForm.getUserProfileSession().getCountry() != null)) {
			userProfileSession.setCountry(userProfForm.getUserProfileSession()
					.getCountry());
		} else {
			userProfileSession.setCountry(request.getLocale().getCountry());
		}

		request.getSession(true).setAttribute(UserProfileSessionBean.NAME,
				userProfileSession);
		return new UserProfileFormBean(true);
	}

	private void getUserProfileSession() {
		if (request.getSession() != null) {
			userProfileSession = (UserProfileSessionBean) request.getSession()
					.getAttribute(UserProfileSessionBean.NAME);
		}

		if (userProfileSession == null) {
			userProfileSession = new UserProfileSessionBean();
		}
	}

	private UserProfileFormBean createUserProfileFormBean(boolean success) {
		UserProfileFormBean bean = new UserProfileFormBean(success);
		bean.setUserProfileSession((UserProfileSessionBean) userProfileSession
				.clone());
		Locale curLocale = new Locale(userProfileSession.getLanguage(),
				userProfileSession.getCountry());
		Map<String, String> langsMap = I18nUtils
				.getCurrentSupportedLanguagesMap(curLocale);
		List<LanguageCountryOptionBean> langOptions = new ArrayList<LanguageCountryOptionBean>(
				langsMap.size());

		for (String code : langsMap.keySet()) {
			LanguageCountryOptionBean langOptBean = new LanguageCountryOptionBean(
					code, langsMap.get(code));
			langOptions.add(langOptBean);
		}

		bean.setLanguageOptions(langOptions);

		Map<String, Map<String, String>> langCountriesMap = I18nUtils
				.getCurrentSupportedLanguageCountriesMap(curLocale);
		Map<String, List<LanguageCountryOptionBean>> langCountryOptions = new HashMap<String, List<LanguageCountryOptionBean>>();

		for (String lang : langCountriesMap.keySet()) {
			Map<String, String> countryMap = langCountriesMap.get(lang);
			List<LanguageCountryOptionBean> countryOptions = new ArrayList<LanguageCountryOptionBean>();

			for (String code : countryMap.keySet()) {
				LanguageCountryOptionBean countryOptBean = new LanguageCountryOptionBean(
						code, countryMap.get(code));
				countryOptions.add(countryOptBean);
			}

			langCountryOptions.put(lang, countryOptions);
		}

		bean.setLanguageCountryOptions(langCountryOptions);
		return bean;
	}
}
