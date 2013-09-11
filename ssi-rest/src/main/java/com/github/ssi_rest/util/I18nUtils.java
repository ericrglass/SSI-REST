package com.github.ssi_rest.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.github.ssi_servlet.util.I18nJsonUtils;
import com.github.ssi_servlet.util.I18nResourceUtils;

public class I18nUtils extends I18nResourceUtils {

	public static final String I18N_JSON_KEY_LANG = "lang";
	public static final String I18N_JSON_KEY_DIR = "dir";

	public static final List<String> CURRENT_SUPPORTED_LANGUAGES = new ArrayList<String>();
	public static final Map<String, List<String>> CURRENT_SUPPORTED_LANGUAGE_COUNTRIES = new HashMap<String, List<String>>();

	static {
		CURRENT_SUPPORTED_LANGUAGES.add("ar");
		CURRENT_SUPPORTED_LANGUAGES.add("de");
		CURRENT_SUPPORTED_LANGUAGES.add("en");
		CURRENT_SUPPORTED_LANGUAGES.add("es");
		CURRENT_SUPPORTED_LANGUAGES.add("fr");
		CURRENT_SUPPORTED_LANGUAGES.add("he");
		CURRENT_SUPPORTED_LANGUAGES.add("hi");
		CURRENT_SUPPORTED_LANGUAGES.add("it");
		CURRENT_SUPPORTED_LANGUAGES.add("iw");
		CURRENT_SUPPORTED_LANGUAGES.add("ja");
		CURRENT_SUPPORTED_LANGUAGES.add("ko");
		CURRENT_SUPPORTED_LANGUAGES.add("zh");

		List<String> arCountries = new ArrayList<String>();
		// Arabic - Algeria
		arCountries.add("DZ");
		// Arabic - Bahrain
		arCountries.add("BH");
		// Arabic - Egypt
		arCountries.add("EG");
		// Arabic - Iraq
		arCountries.add("IQ");
		// Arabic - Jordan
		arCountries.add("JO");
		// Arabic - Kuwait
		arCountries.add("KW");
		// Arabic - Lebanon
		arCountries.add("LB");
		// Arabic - Libya
		arCountries.add("LY");
		// Arabic - Morocco
		arCountries.add("MA");
		// Arabic - Oman
		arCountries.add("OM");
		// Arabic - Qatar
		arCountries.add("QA");
		// Arabic - Saudi Arabia
		arCountries.add("SA");
		// Arabic - Syria
		arCountries.add("SY");
		// Arabic - Tunisia
		arCountries.add("TN");
		// Arabic - United Arab Emirates
		arCountries.add("AE");
		// Arabic - Yemen
		arCountries.add("YE");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("ar", arCountries);

		List<String> deCountries = new ArrayList<String>();
		// German - Austria
		deCountries.add("AT");
		// German - Germany
		deCountries.add("DE");
		// German - Liechtenstein
		deCountries.add("LI");
		// German - Luxembourg
		deCountries.add("LU");
		// German - Switzerland
		deCountries.add("CH");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("de", deCountries);

		List<String> enCountries = new ArrayList<String>();
		// English - Australia
		enCountries.add("AU");
		// English - Belize
		enCountries.add("BZ");
		// English - Canada
		enCountries.add("CA");
		// English - Caribbean
		enCountries.add("CB");
		// English - Ireland
		enCountries.add("IE");
		// English - Jamaica
		enCountries.add("JM");
		// English - New Zealand
		enCountries.add("NZ");
		// English - Philippines
		enCountries.add("PH");
		// English - South Africa
		enCountries.add("ZA");
		// English - Trinidad and Tobago
		enCountries.add("TT");
		// English - United Kingdom
		enCountries.add("GB");
		// English - United States
		enCountries.add("US");
		// English - Zimbabwe
		enCountries.add("ZW");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("en", enCountries);

		List<String> esCountries = new ArrayList<String>();
		// Spanish - Argentina
		esCountries.add("AR");
		// Spanish - Bolivia
		esCountries.add("BO");
		// Spanish - Chile
		esCountries.add("CL");
		// Spanish - Colombia
		esCountries.add("CO");
		// Spanish - Costa Rica
		esCountries.add("CR");
		// Spanish - Dominican Republic
		esCountries.add("DO");
		// Spanish - Ecuador
		esCountries.add("EC");
		// Spanish - El Salvador
		esCountries.add("SV");
		// Spanish - Guatemala
		esCountries.add("GT");
		// Spanish - Honduras
		esCountries.add("HN");
		// Spanish - Mexico
		esCountries.add("MX");
		// Spanish - Nicaragua
		esCountries.add("NI");
		// Spanish - Panama
		esCountries.add("PA");
		// Spanish - Paraguay
		esCountries.add("PY");
		// Spanish - Peru
		esCountries.add("PE");
		// Spanish - Puerto Rico
		esCountries.add("PR");
		// Spanish - Spain
		esCountries.add("ES");
		// Spanish - Uruguay
		esCountries.add("UY");
		// Spanish - Venezuela
		esCountries.add("VE");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("es", esCountries);

		List<String> frCountries = new ArrayList<String>();
		// French - Belgium
		frCountries.add("BE");
		// French - Canada
		frCountries.add("CA");
		// French - France
		frCountries.add("FR");
		// French - Luxembourg
		frCountries.add("LU");
		// French - Monaco
		frCountries.add("MC");
		// French - Switzerland
		frCountries.add("CH");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("fr", frCountries);

		List<String> heCountries = new ArrayList<String>();
		// Hebrew - Israel
		heCountries.add("IL");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("he", heCountries);
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("iw", heCountries);

		List<String> hiCountries = new ArrayList<String>();
		// Hindi - India
		hiCountries.add("IN");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("hi", hiCountries);

		List<String> itCountries = new ArrayList<String>();
		// Italian - Italy
		itCountries.add("IT");
		// Italian - Switzerland
		itCountries.add("CH");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("it", itCountries);

		List<String> jaCountries = new ArrayList<String>();
		// Japanese - Japan
		jaCountries.add("JP");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("ja", jaCountries);

		List<String> koCountries = new ArrayList<String>();
		// Korean - Korea
		koCountries.add("KR");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("ko", koCountries);

		List<String> zhCountries = new ArrayList<String>();
		// Chinese - Hong Kong SAR
		zhCountries.add("HK");
		// Chinese - Macao SAR
		zhCountries.add("MO");
		// Chinese - China
		zhCountries.add("CN");
		// Chinese - Singapore
		zhCountries.add("SG");
		// Chinese - Taiwan
		zhCountries.add("TW");
		CURRENT_SUPPORTED_LANGUAGE_COUNTRIES.put("zh", zhCountries);
	}

	protected I18nUtils() {
		super();
	}

	public static Map<String, String> getCurrentSupportedLanguagesMap(
			Locale curLocale) {
		Map<String, String> curSupLangsMap = new LinkedHashMap<String, String>(
				CURRENT_SUPPORTED_LANGUAGES.size());
		Locale descrLocale = curLocale;

		if (curLocale == null) {
			descrLocale = DEFAULT_LOCALE;
		}

		Map<String, String> langDescrMap = new HashMap<String, String>(
				CURRENT_SUPPORTED_LANGUAGES.size());

		for (String curSupLang : CURRENT_SUPPORTED_LANGUAGES) {
			langDescrMap.put(curSupLang,
					new Locale(curSupLang).getDisplayLanguage(descrLocale));
		}

		List<String> descrList = new ArrayList<String>(langDescrMap.values());
		Collections.sort(descrList);

		for (String descr : descrList) {
			if (!langDescrMap.containsValue(descr)) {
				continue;
			}

			String lang = null;

			for (String curLang : langDescrMap.keySet()) {
				String curDescr = langDescrMap.get(curLang);

				if (descr.equals(curDescr)) {
					lang = curLang;
					break;
				}
			}

			if (lang == null) {
				continue;
			}

			curSupLangsMap.put(lang, descr);
		}

		return curSupLangsMap;
	}

	public static Map<String, Map<String, String>> getCurrentSupportedLanguageCountriesMap(
			Locale curLocale) {
		Map<String, Map<String, String>> curSupLangCountriesMap = new HashMap<String, Map<String, String>>(
				CURRENT_SUPPORTED_LANGUAGES.size());
		Locale descrLocale = curLocale;

		if (curLocale == null) {
			descrLocale = DEFAULT_LOCALE;
		}

		for (String curSupLang : CURRENT_SUPPORTED_LANGUAGES) {
			List<String> countriesList = CURRENT_SUPPORTED_LANGUAGE_COUNTRIES
					.get(curSupLang);

			if ((countriesList == null) || (countriesList.size() == 0)) {
				continue;
			}

			Map<String, String> countriesDescrMap = new HashMap<String, String>();

			for (String curCountry : countriesList) {
				countriesDescrMap.put(curCountry, new Locale(curSupLang,
						curCountry).getDisplayCountry(descrLocale));
			}

			List<String> descrList = new ArrayList<String>(
					countriesDescrMap.values());
			Collections.sort(descrList);

			Map<String, String> countriesMap = new LinkedHashMap<String, String>(
					descrList.size());

			for (String descr : descrList) {
				if (!countriesDescrMap.containsValue(descr)) {
					continue;
				}

				String country = null;

				for (String curCountry : countriesDescrMap.keySet()) {
					String curDescr = countriesDescrMap.get(curCountry);

					if (descr.equals(curDescr)) {
						country = curCountry;
						break;
					}
				}

				if (country == null) {
					continue;
				}

				countriesMap.put(country, descr);
			}

			curSupLangCountriesMap.put(curSupLang, countriesMap);
		}

		return curSupLangCountriesMap;
	}

	public static String getCultureResourceBundleJSONFromMap(
			Map<String, String> resBundleMap, String language, String country,
			boolean debug) {
		Map<String, String> resMap = resBundleMap;

		if (resMap == null) {
			resMap = new HashMap<String, String>();
		}

		resMap.put(I18N_JSON_KEY_LANG,
				getCultureHTMLLangAttributeValue(language, country));
		resMap.put(I18N_JSON_KEY_DIR,
				getLanguageHTMLDirAttributeValue(language));
		return I18nJsonUtils.getI18nJsonStringFromResourceBundleMap(resMap,
				debug);
	}

	public static String getCultureResourceBundleJSONFromMap(
			Map<String, String> resBundleMap, Locale locale, boolean debug) {
		if (locale == null) {
			return getCultureResourceBundleJSONFromMap(resBundleMap, null,
					null, debug);
		}

		return getCultureResourceBundleJSONFromMap(resBundleMap,
				locale.getLanguage(), locale.getCountry(), debug);
	}

	public static String getCultureResourceBundleJSON(String resBaseName,
			String language, String country, String resPackage,
			boolean javaClass, boolean debug) {
		if ((resBaseName == null) || (resBaseName.trim().length() == 0)
				|| (language == null) || (language.trim().length() == 0)) {
			return I18nJsonUtils.I18N_JSON_EMPTY_MAP;
		}

		return getCultureResourceBundleJSONFromMap(
				getCultureResourceBundleMap(resBaseName, language, country,
						resPackage, javaClass, debug), language, country, debug);
	}

	public static String getCultureResourceBundleJSON(String resBaseName,
			Locale locale, String resPackage, boolean javaClass, boolean debug) {
		if ((resBaseName == null) || (resBaseName.trim().length() == 0)
				|| (locale == null)) {
			return I18nJsonUtils.I18N_JSON_EMPTY_MAP;
		}

		return getCultureResourceBundleJSON(resBaseName, locale.getLanguage(),
				locale.getCountry(), resPackage, javaClass, debug);
	}

}
