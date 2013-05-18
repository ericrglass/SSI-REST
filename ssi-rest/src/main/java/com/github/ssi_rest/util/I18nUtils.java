package com.github.ssi_rest.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.codehaus.jackson.map.ObjectMapper;

import com.github.ssi_rest.SsiRestLogger;

public class I18nUtils {

	public static final String I18N_JSON_PROPERTY = "i18n";
	public static final String I18N_JSON_KEY_LANG = "lang";
	public static final String I18N_JSON_KEY_DIR = "dir";
	public static final String I18N_JSON_EMPTY_MAP = "{\"" + I18N_JSON_PROPERTY
			+ "\":{}}";
	public static final Locale DEFAULT_LOCALE = Locale.US;
	public static final String DEFAULT_HTML_LANG = DEFAULT_LOCALE.toString()
			.replaceFirst("_", "-");

	public static final List<String> RTL_LANGUAGES = new ArrayList<String>();

	static {
		RTL_LANGUAGES.add("ar");
		RTL_LANGUAGES.add("dv");
		RTL_LANGUAGES.add("fa");
		RTL_LANGUAGES.add("he");
		RTL_LANGUAGES.add("prs");
		RTL_LANGUAGES.add("ps");
		RTL_LANGUAGES.add("syr");
		RTL_LANGUAGES.add("ug");
		RTL_LANGUAGES.add("ur");
	}

	public static boolean isRightToLeftLanguage(String language) {
		if ((language == null) || (language.trim().length() == 0)) {
			return false;
		}

		return RTL_LANGUAGES.contains(language.trim());
	}

	public static boolean isRightToLeftLanguage(Locale locale) {
		if (locale == null) {
			return false;
		}

		return isRightToLeftLanguage(locale.getLanguage());
	}

	public static String getLanguageHTMLDirAttributeValue(String language) {
		if ((language == null) || (language.trim().length() == 0)) {
			return "ltr";
		}

		if (isRightToLeftLanguage(language.trim())) {
			return "rtl";
		}

		return "ltr";
	}

	public static String getLanguageHTMLDirAttributeValue(Locale locale) {
		if (locale == null) {
			return "ltr";
		}

		return getLanguageHTMLDirAttributeValue(locale.getLanguage());
	}

	public static String getCultureHTMLLangAttributeValue(String language,
			String country) {
		if ((language == null) || (language.trim().length() == 0)) {
			return DEFAULT_HTML_LANG;
		}

		StringBuilder htmlLang = new StringBuilder(language.trim());

		if ((country != null) && (country.trim().length() > 0)) {
			htmlLang.append("-");
			htmlLang.append(country.trim());
		}

		return htmlLang.toString();
	}

	public static String getCultureHTMLLangAttributeValue(Locale locale) {
		if (locale == null) {
			return DEFAULT_HTML_LANG;
		}

		return getCultureHTMLLangAttributeValue(locale.getLanguage(),
				locale.getCountry());
	}

	public static String getI18nJSONFromResourceBundleMap(
			Map<String, String> resBundleMap, String language, String country,
			boolean debug) {
		String json = null;
		Map<String, String> jsonMap = resBundleMap;

		if (jsonMap == null) {
			jsonMap = new HashMap<String, String>();
		}

		jsonMap.put(I18N_JSON_KEY_LANG,
				getCultureHTMLLangAttributeValue(language, country));
		jsonMap.put(I18N_JSON_KEY_DIR,
				getLanguageHTMLDirAttributeValue(language));
		I18nJSONBean i18nJSON = new I18nJSONBean();
		i18nJSON.setI18n(jsonMap);
		ObjectMapper mapper = new ObjectMapper();

		try {
			json = mapper.writeValueAsString(i18nJSON);
		} catch (Exception e) {
			json = null;
			SsiRestLogger.LOGGER.log(
					Level.SEVERE,
					"I18nUtils - getI18nJSONFromResourceBundleMap "
							+ "- had the following exception: "
							+ e.getMessage(), e);
		}

		if ((json == null) || (json.length() == 0)) {
			json = I18N_JSON_EMPTY_MAP;
		}

		if (debug) {
			SsiRestLogger.LOGGER.warning("Debug Message: I18nUtils - "
					+ "getI18nJSONFromResourceBundleMap - the i18n JSON: "
					+ json);
		}

		return json;
	}

	public static String getI18nJSONFromResourceBundleMap(
			Map<String, String> resBundleMap, Locale locale, boolean debug) {
		if (locale == null) {
			return getI18nJSONFromResourceBundleMap(resBundleMap, null, null,
					debug);
		}

		return getI18nJSONFromResourceBundleMap(resBundleMap,
				locale.getLanguage(), locale.getCountry(), debug);
	}

	public static Map<String, String> getCultureResourceBundleMap(
			String resBaseName, String language, String country,
			String resPackage, String resNameSeparator, String resNameSuffix,
			boolean debug) {
		if ((resBaseName == null) || (resBaseName.trim().length() == 0)
				|| (language == null) || (language.trim().length() == 0)) {
			return new HashMap<String, String>();
		}

		StringBuilder resBasePath = new StringBuilder();
		String baseName = resBaseName.trim();

		if ((resPackage != null) && (resPackage.trim().length() > 0)) {
			resBasePath.append(resPackage.trim().replaceAll("\\.", "/"));
			String path = resBasePath.toString();

			if ((!path.endsWith("/") || !path.endsWith("\\"))
					&& (!baseName.startsWith("/") || !baseName.startsWith("\\"))) {
				resBasePath.append("/");
			}
		}

		resBasePath.append(baseName);
		String resNameSep = resNameSeparator;

		if ((resNameSep == null) || (resNameSep.trim().length() == 0)) {
			// The default for the resource name separator: _
			resNameSep = "_";
		}

		String resNameSuf = resNameSuffix;

		if ((resNameSuf == null) || (resNameSuf.trim().length() == 0)) {
			// The default for the resource name suffix: .properties
			resNameSuf = ".properties";
		}

		if (debug) {
			SsiRestLogger.LOGGER
					.warning("Debug Message: I18nUtils - "
							+ "getCultureResourceBundleMap - is using the resource base name '"
							+ baseName + "' with the language '"
							+ language.trim() + "'.");
		}

		StringBuilder languageResPath = new StringBuilder(resBasePath);
		languageResPath.append(resNameSep);
		languageResPath.append(language.trim());
		languageResPath.append(resNameSuf);

		Map<String, String> resMap = new HashMap<String, String>();

		Properties languageProps = new Properties();
		InputStream languageIS = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream(languageResPath.toString());

		try {
			if (languageIS != null) {
				languageProps.load(languageIS);
				languageIS.close();
				languageIS = null;
			} else {
				languageProps = null;
			}
		} catch (IOException e) {
			languageProps = null;
		} finally {
			if (languageIS != null) {
				try {
					languageIS.close();
				} catch (IOException e) {
				}

				languageIS = null;
			}
		}

		if (languageProps != null) {
			for (Object key : languageProps.keySet()) {
				if (key == null) {
					continue;
				}

				Object value = languageProps.get(key);

				if ((value != null) && (value.toString().length() > 0)) {
					resMap.put(key.toString(), value.toString());
				}
			}
		}

		if (debug) {
			SsiRestLogger.LOGGER.warning("Debug Message: I18nUtils - "
					+ "getCultureResourceBundleMap - the language "
					+ "resource bundle '"
					+ languageResPath
					+ ((languageProps == null) ? "' could not be found."
							: "' was found and used."));
		}

		StringBuilder cultureResPath = new StringBuilder();

		if ((country != null) && (country.trim().length() > 0)) {
			cultureResPath.append(resBasePath);
			cultureResPath.append(resNameSep);
			cultureResPath.append(language.trim());
			cultureResPath.append(resNameSep);
			cultureResPath.append(country.trim());
			cultureResPath.append(resNameSuf);
		}

		if (cultureResPath.length() > 0) {
			Properties cultureProps = new Properties();
			InputStream cultureIS = Thread.currentThread()
					.getContextClassLoader()
					.getResourceAsStream(cultureResPath.toString());

			try {
				if (cultureIS != null) {
					cultureProps.load(cultureIS);
					cultureIS.close();
					cultureIS = null;
				} else {
					cultureProps = null;
				}
			} catch (IOException e) {
				cultureProps = null;
			} finally {
				if (cultureIS != null) {
					try {
						cultureIS.close();
					} catch (IOException e) {
					}

					cultureIS = null;
				}
			}

			if (cultureProps != null) {
				for (Object key : cultureProps.keySet()) {
					if (key == null) {
						continue;
					}

					Object value = cultureProps.get(key);

					if ((value != null) && (value.toString().length() > 0)) {
						resMap.put(key.toString(), value.toString());
					}
				}
			}

			if (debug) {
				SsiRestLogger.LOGGER.warning("Debug Message: I18nUtils - "
						+ "getCultureResourceBundleMap - the culture "
						+ "resource bundle '"
						+ cultureResPath
						+ ((cultureProps == null) ? "' could not be found."
								: "' was found and used."));
			}
		} else if (debug) {
			SsiRestLogger.LOGGER.warning("Debug Message: I18nUtils - "
					+ "getCultureResourceBundleMap - the country was not "
					+ "provided, so there is no culture resource bundle.");
		}

		if (debug) {
			SsiRestLogger.LOGGER.warning("Debug Message: I18nUtils - "
					+ "getCultureResourceBundleMap - the i18n Map: " + resMap);
		}

		return resMap;
	}

	public static Map<String, String> getCultureResourceBundleMap(
			String resBaseName, Locale locale, String resPackage,
			String resNameSeparator, String resNameSuffix, boolean debug) {
		if ((resBaseName == null) || (resBaseName.trim().length() == 0)
				|| (locale == null)) {
			return new HashMap<String, String>();
		}

		return getCultureResourceBundleMap(resBaseName, locale.getLanguage(),
				locale.getCountry(), resPackage, resNameSeparator,
				resNameSuffix, debug);
	}

	public static String getCultureResourceBundleJSON(String resBaseName,
			String language, String country, String resPackage,
			String resNameSeparator, String resNameSuffix, boolean debug) {
		if ((resBaseName == null) || (resBaseName.trim().length() == 0)
				|| (language == null) || (language.trim().length() == 0)) {
			return I18N_JSON_EMPTY_MAP;
		}

		return getI18nJSONFromResourceBundleMap(
				getCultureResourceBundleMap(resBaseName, language, country,
						resPackage, resNameSeparator, resNameSuffix, debug),
				language, country, debug);
	}

	public static String getCultureResourceBundleJSON(String resBaseName,
			Locale locale, String resPackage, String resNameSeparator,
			String resNameSuffix, boolean debug) {
		if ((resBaseName == null) || (resBaseName.trim().length() == 0)
				|| (locale == null)) {
			return I18N_JSON_EMPTY_MAP;
		}

		return getCultureResourceBundleJSON(resBaseName, locale.getLanguage(),
				locale.getCountry(), resPackage, resNameSeparator,
				resNameSuffix, debug);
	}

	public static class I18nJSONBean {

		private Map<String, String> i18n;

		public Map<String, String> getI18n() {
			return i18n;
		}

		public void setI18n(Map<String, String> i18n) {
			this.i18n = i18n;
		}

	}

}
