package com.github.ssi_rest.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.ssi_rest.SsiRestLogger;
import com.github.ssi_rest.bean.UserProfileSessionBean;
import com.github.ssi_rest.service.IServiceDelegator;
import com.github.ssi_rest.util.I18nUtils;
import com.github.ssi_servlet.SsiServlet;

public class SsiRestServlet extends SsiServlet {

	public static final String INIT_PARAM_ENABLE_I18N_FEATURE = "com.github.ssi_rest.servlet.ENABLE_I18N_FEATURE";
	public static final String INIT_PARAM_ENABLE_I18N_RESOURCE_BUNDLE_JSON = "com.github.ssi_rest.servlet.ENABLE_I18N_RESOURCE_BUNDLE_JSON";
	public static final String INIT_PARAM_I18N_RESOURCE_BUNDLE_PACKAGE = "com.github.ssi_rest.servlet.I18N_RESOURCE_BUNDLE_PACKAGE";
	public static final String INIT_PARAM_I18N_RESOURCE_BUNDLE_JAVA_CLASS = "com.github.ssi_rest.servlet.I18N_RESOURCE_BUNDLE_JAVA_CLASS";

	public static final String REQ_ATTR_I18N_JSON = "I18N_JSON";

	private static final long serialVersionUID = 4542259497206614997L;

	private String serviceDelegatorJNDIName = null;
	private boolean i18nFeature = false;
	private boolean i18nResBundleJSON = false;
	private String i18nResBundlePackage = null;
	private boolean i18nResBundleJavaClass = false;

	@Override
	public void init() throws ServletException {
		super.init();
		serviceDelegatorJNDIName = getInitParameter(IServiceDelegator.INIT_PARAM_JNDI_NAME);
		i18nFeature = Boolean
				.parseBoolean(getInitParameter(INIT_PARAM_ENABLE_I18N_FEATURE));
		i18nResBundleJSON = Boolean
				.parseBoolean(getInitParameter(INIT_PARAM_ENABLE_I18N_RESOURCE_BUNDLE_JSON));
		i18nResBundlePackage = getInitParameter(INIT_PARAM_I18N_RESOURCE_BUNDLE_PACKAGE);
		i18nResBundleJavaClass = Boolean
				.parseBoolean(getInitParameter(INIT_PARAM_I18N_RESOURCE_BUNDLE_JAVA_CLASS));

		if (i18nFeature) {
			// For the I18N feature, force buffered to be true
			buffered = true;
		}

		if (debug > 0) {
			log("SsiRestServlet.init() started with service delegator JNDI name '"
					+ serviceDelegatorJNDIName + "'");
			log("SsiRestServlet.init() the i18n feature: "
					+ ((i18nFeature) ? "Has been enabled" : "Was not enabled"));
			log("SsiRestServlet.init() the i18n resource bundle JSON feature: "
					+ ((i18nResBundleJSON) ? "Has been enabled"
							: "Was not enabled"));

			if (i18nResBundleJSON) {
				log("SsiRestServlet.init() the i18n resource bundle package '"
						+ (((i18nResBundlePackage == null) || (i18nResBundlePackage
								.trim().length() == 0)) ? "default"
								: i18nResBundlePackage) + "'");
				log("SsiRestServlet.init() the i18n resource bundle format is: "
						+ ((i18nResBundleJavaClass) ? "Java Class"
								: "Java Properties"));
			}
		}
	}

	@Override
	protected String processBufferedBeforeCompress(HttpServletRequest req,
			String html) {
		String preCompHtml = super.processBufferedBeforeCompress(req, html);

		if (!i18nFeature) {
			return preCompHtml;
		}

		return i18nHtml(req, preCompHtml);
	}

	@Override
	protected Locale getUserLocale(HttpServletRequest req) {
		if (req == null) {
			return I18nUtils.DEFAULT_LOCALE;
		}

		Locale userLocale = req.getLocale();
		Object userProfileSession = null;

		if (req.getSession() != null) {
			userProfileSession = req.getSession().getAttribute(
					UserProfileSessionBean.NAME);
		}

		if ((userProfileSession instanceof UserProfileSessionBean)
				&& (((UserProfileSessionBean) userProfileSession).getLanguage() != null)) {
			userLocale = new Locale(
					((UserProfileSessionBean) userProfileSession).getLanguage(),
					((UserProfileSessionBean) userProfileSession).getCountry());
		}

		return userLocale;
	}

	@Override
	protected void requestHandlerInProcessSSI(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		super.requestHandlerInProcessSSI(req, res);
		String resouce = req.getServletPath();

		if (resouce.startsWith("/")) {
			if (resouce.length() > 1) {
				resouce = resouce.substring(1);
			} else {
				resouce = "";
			}
		}

		if (resouce.endsWith(".html")) {
			if (resouce.length() > 5) {
				resouce = resouce.substring(0, resouce.length() - 5);
			} else {
				resouce = "";
			}
		}

		if (i18nFeature) {
			Locale userLocale = getUserLocale(req);

			if (i18nResBundleJSON) {
				Map<String, String> i18nMap = I18nUtils
						.getCultureResourceBundleMap("global", userLocale,
								i18nResBundlePackage, i18nResBundleJavaClass,
								(debug > 0));

				if (i18nMap == null) {
					i18nMap = new HashMap<String, String>();
				}

				Map<String, String> resourceMap = I18nUtils
						.getCultureResourceBundleMap(resouce, userLocale,
								i18nResBundlePackage, i18nResBundleJavaClass,
								(debug > 0));

				if (resourceMap != null) {
					i18nMap.putAll(resourceMap);
				}

				req.setAttribute(REQ_ATTR_I18N_JSON, I18nUtils
						.getI18nJSONFromResourceBundleMap(i18nMap, userLocale,
								(debug > 0)));
			}
		}

		if ((serviceDelegatorJNDIName != null)
				&& (serviceDelegatorJNDIName.trim().length() > 0)) {
			Object serviceDelegator = null;
			NamingException namExc = null;

			try {
				InitialContext ic = new InitialContext();
				serviceDelegator = ic.lookup(serviceDelegatorJNDIName.trim());
			} catch (NamingException e) {
				serviceDelegator = null;
				namExc = e;
				SsiRestLogger.LOGGER.log(
						Level.SEVERE,
						"Service Delegator with JNDI Name ("
								+ serviceDelegatorJNDIName.trim()
								+ ") had the following NamingException: "
								+ e.getMessage(), e);
			}

			if (serviceDelegator instanceof IServiceDelegator) {
				String service = req
						.getParameter(IServiceDelegator.QUERY_PARAM_SERVICE);

				if ((service == null) || (service.trim().length() == 0)) {
					service = resouce;
				}

				String action = req
						.getParameter(IServiceDelegator.QUERY_PARAM_ACTION);

				if (debug > 0) {
					log("Calling service delegator '"
							+ serviceDelegatorJNDIName.trim()
							+ "' for resource '" + req.getServletPath()
							+ "' with service name '" + service
							+ "' and action '" + action + "'");
				}

				((IServiceDelegator) serviceDelegator).processRequest(req,
						service, action, (debug > 0));
			} else if (namExc == null) {
				SsiRestLogger.LOGGER
						.log(Level.SEVERE,
								"Service Delegator with JNDI Name ("
										+ serviceDelegatorJNDIName.trim()
										+ ") could not be called, becasue it did not implement IServiceDelegator.");
			}
		}
	}

	protected String i18nHtml(final HttpServletRequest req, final String html) {
		Locale userLocale = getUserLocale(req);

		if (debug > 0) {
			log("Internationalizing the resource '" + req.getServletPath()
					+ "' ; lang=\""
					+ I18nUtils.getCultureHTMLLangAttributeValue(userLocale)
					+ "\" ; dir=\""
					+ I18nUtils.getLanguageHTMLDirAttributeValue(userLocale)
					+ "\"");
		}

		StringBuilder i18nHtml = new StringBuilder(html);
		boolean langAttrInserted = false;
		String langAttr = "\""
				+ I18nUtils.getCultureHTMLLangAttributeValue(userLocale) + "\"";
		boolean dirAttrInserted = false;
		String dirAttr = "\""
				+ I18nUtils.getLanguageHTMLDirAttributeValue(userLocale) + "\"";
		int htmlTagPos = i18nHtml.indexOf("<html");
		int htmlTagClosePos = -1;

		if (htmlTagPos == -1) {
			htmlTagPos = i18nHtml.indexOf("<HTML");
		}

		if (htmlTagPos > -1) {
			htmlTagClosePos = i18nHtml.indexOf(">", htmlTagPos);
		}

		if ((htmlTagPos > -1) && (htmlTagClosePos > htmlTagPos)) {
			int langAttrPos = i18nHtml.indexOf(" lang=", htmlTagPos);

			if (langAttrPos == -1) {
				langAttrPos = i18nHtml.indexOf(" LANG=", htmlTagPos);
			}

			if ((langAttrPos > -1) && (langAttrPos < htmlTagClosePos)) {
				int spacePos = i18nHtml.indexOf(" ", langAttrPos + 6);

				if ((spacePos > -1) && (spacePos < htmlTagClosePos)) {
					i18nHtml.replace(langAttrPos + 6, spacePos, langAttr);
				} else {
					i18nHtml.replace(langAttrPos + 6, htmlTagClosePos, langAttr);
				}

				htmlTagClosePos = i18nHtml.indexOf(">", htmlTagPos);
				langAttrInserted = true;
			}

			int dirAttrPos = i18nHtml.indexOf(" dir=", htmlTagPos);

			if (dirAttrPos == -1) {
				dirAttrPos = i18nHtml.indexOf(" DIR=", htmlTagPos);
			}

			if ((dirAttrPos > -1) && (dirAttrPos < htmlTagClosePos)) {
				int spacePos = i18nHtml.indexOf(" ", dirAttrPos + 5);

				if ((spacePos > -1) && (spacePos < htmlTagClosePos)) {
					i18nHtml.replace(dirAttrPos + 5, spacePos, dirAttr);
				} else {
					i18nHtml.replace(dirAttrPos + 5, htmlTagClosePos, dirAttr);
				}

				htmlTagClosePos = i18nHtml.indexOf(">", htmlTagPos);
				dirAttrInserted = true;
			}
		}

		int bodyTagPos = i18nHtml.indexOf("<body");
		int bodyTagClosePos = -1;

		if (bodyTagPos == -1) {
			bodyTagPos = i18nHtml.indexOf("<BODY");
		}

		if (bodyTagPos > -1) {
			bodyTagClosePos = i18nHtml.indexOf(">", bodyTagPos);
		}

		if ((bodyTagPos > -1) && (bodyTagClosePos > bodyTagPos)) {
			if (!langAttrInserted) {
				int langAttrPos = i18nHtml.indexOf(" lang=", bodyTagPos);

				if (langAttrPos == -1) {
					langAttrPos = i18nHtml.indexOf(" LANG=", bodyTagPos);
				}

				if ((langAttrPos > -1) && (langAttrPos < bodyTagClosePos)) {
					int spacePos = i18nHtml.indexOf(" ", langAttrPos + 6);

					if ((spacePos > -1) && (spacePos < bodyTagClosePos)) {
						i18nHtml.replace(langAttrPos + 6, spacePos, langAttr);
					} else {
						i18nHtml.replace(langAttrPos + 6, bodyTagClosePos,
								langAttr);
					}

					bodyTagClosePos = i18nHtml.indexOf(">", bodyTagPos);
					langAttrInserted = true;
				}
			}

			if (!dirAttrInserted) {
				int dirAttrPos = i18nHtml.indexOf(" dir=", bodyTagPos);

				if (dirAttrPos == -1) {
					dirAttrPos = i18nHtml.indexOf(" DIR=", bodyTagPos);
				}

				if ((dirAttrPos > -1) && (dirAttrPos < bodyTagClosePos)) {
					int spacePos = i18nHtml.indexOf(" ", dirAttrPos + 5);

					if ((spacePos > -1) && (spacePos < bodyTagClosePos)) {
						i18nHtml.replace(dirAttrPos + 5, spacePos, dirAttr);
					} else {
						i18nHtml.replace(dirAttrPos + 5, bodyTagClosePos,
								dirAttr);
					}

					bodyTagClosePos = i18nHtml.indexOf(">", bodyTagPos);
					dirAttrInserted = true;
				}
			}
		}

		if (!langAttrInserted) {
			if (htmlTagPos > -1) {
				i18nHtml.insert(htmlTagPos + 5, " lang=" + langAttr);
			} else if (bodyTagPos > -1) {
				i18nHtml.insert(bodyTagPos + 5, " lang=" + langAttr);
			}
		}

		if (!dirAttrInserted) {
			if (htmlTagPos > -1) {
				i18nHtml.insert(htmlTagPos + 5, " dir=" + dirAttr);
			} else if (bodyTagPos > -1) {
				i18nHtml.insert(bodyTagPos + 5, " dir=" + dirAttr);
			}
		}

		return i18nHtml.toString();
	}

}
