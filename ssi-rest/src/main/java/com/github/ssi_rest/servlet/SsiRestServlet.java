package com.github.ssi_rest.servlet;

import java.io.IOException;
import java.util.logging.Level;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.Globals;
import org.apache.catalina.ssi.SSIServlet_JBossWeb;

import com.github.ssi_rest.SsiRestLogger;
import com.github.ssi_rest.service.IServiceDelegator;
import com.github.ssi_rest.util.I18nUtils;
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class SsiRestServlet extends SSIServlet_JBossWeb {

	public static final String INIT_PARAM_HTML_COMPRESSOR = "com.github.ssi_rest.servlet.HTML_COMPRESSOR";
	public static final String INIT_PARAM_COMPRESS_CSS = "com.github.ssi_rest.servlet.COMPRESS_CSS";
	public static final String INIT_PARAM_COMPRESS_JS = "com.github.ssi_rest.servlet.COMPRESS_JAVASCRIPT";
	public static final String INIT_PARAM_ENABLE_I18N_FEATURE = "com.github.ssi_rest.servlet.ENABLE_I18N_FEATURE";

	public static final String REQ_ATTR_INITIAL_REQUEST_STRING = "com.github.ssi_rest.servlet.INITIAL_REQUEST_STRING";
	public static final String REQ_ATTR_I18N_LANG = "I18N_LANG";
	public static final String REQ_ATTR_I18N_DIR = "I18N_DIR";

	public static final String HTML_COMMENT_BYPASS_COMPRESSING_HTML = "<!-- BYPASS_COMPRESSING_HTML -->";
	public static final String HTML_COMMENT_BYPASS_COMPRESSING_CSS = "<!-- BYPASS_COMPRESSING_CSS -->";
	public static final String HTML_COMMENT_BYPASS_COMPRESSING_JAVASCRIPT = "<!-- BYPASS_COMPRESSING_JAVASCRIPT -->";

	private static final long serialVersionUID = 4542259497206614997L;

	private String serviceDelegatorJNDIName = null;
	private boolean htmlCompressor = false;
	private boolean compressCSS = false;
	private boolean compressJS = false;
	private boolean i18nFeature = false;

	@Override
	public void init() throws ServletException {
		super.init();
		serviceDelegatorJNDIName = getInitParameter(IServiceDelegator.INIT_PARAM_JNDI_NAME);
		htmlCompressor = Boolean
				.parseBoolean(getInitParameter(INIT_PARAM_HTML_COMPRESSOR));
		compressCSS = Boolean
				.parseBoolean(getInitParameter(INIT_PARAM_COMPRESS_CSS));
		compressJS = Boolean
				.parseBoolean(getInitParameter(INIT_PARAM_COMPRESS_JS));
		i18nFeature = Boolean
				.parseBoolean(getInitParameter(INIT_PARAM_ENABLE_I18N_FEATURE));

		if (htmlCompressor || i18nFeature) {
			// For either the HTML compressor or the I18N feature, force
			// buffered to be true
			buffered = true;
		}

		if (debug > 0) {
			log("SsiRestServlet.init() started with service delegator JNDI name '"
					+ serviceDelegatorJNDIName + "'");
			log("SsiRestServlet.init() the I18N feature has been enabled");
		}
	}

	@Override
	protected String processBuffered(HttpServletRequest req, String text) {
		String html = super.processBuffered(req, text);

		if (!htmlCompressor && !i18nFeature) {
			return html;
		}

		Object initialReqStr = req
				.getAttribute(REQ_ATTR_INITIAL_REQUEST_STRING);

		if (!req.toString().equals(initialReqStr)) {
			return html;
		}

		req.removeAttribute(REQ_ATTR_INITIAL_REQUEST_STRING);
		String i18nHtml = html;

		if (i18nFeature) {
			i18nHtml = i18nHtml(req, html);
		}

		int bypassHTML = i18nHtml.indexOf(HTML_COMMENT_BYPASS_COMPRESSING_HTML);

		if (bypassHTML > -1) {
			return i18nHtml;
		}

		return compressHtml(req, i18nHtml);
	}

	@Override
	protected void requestHandler(HttpServletRequest req,
			HttpServletResponse res) throws IOException {
		boolean alreadyInProcessSSI = false;
		Object ssi_flag = req.getAttribute(Globals.SSI_FLAG_ATTR);

		if (ssi_flag != null) {
			alreadyInProcessSSI = Boolean.parseBoolean(ssi_flag.toString());
		}

		if (!alreadyInProcessSSI && (serviceDelegatorJNDIName != null)
				&& (serviceDelegatorJNDIName.trim().length() > 0)) {
			req.setAttribute(REQ_ATTR_INITIAL_REQUEST_STRING, req.toString());

			if (i18nFeature) {
				req.setAttribute(REQ_ATTR_I18N_LANG, I18nUtils
						.getLanguageHTMLLangAttributeValue(req.getLocale()));
				req.setAttribute(REQ_ATTR_I18N_DIR, I18nUtils
						.getLanguageHTMLDirAttributeValue(req.getLocale()));

			}

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
					service = req.getServletPath();

					if (service.startsWith("/")) {
						if (service.length() > 1) {
							service = service.substring(1);
						} else {
							service = "";
						}
					}

					if (service.endsWith(".html")) {
						if (service.length() > 5) {
							service = service
									.substring(0, service.length() - 5);
						} else {
							service = "";
						}
					}
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

		super.requestHandler(req, res);
	}

	private String i18nHtml(final HttpServletRequest req, final String html) {
		if (debug > 0) {
			log("Internationalizing the resource '"
					+ req.getServletPath()
					+ "' ; lang=\""
					+ I18nUtils.getLanguageHTMLLangAttributeValue(req
							.getLocale())
					+ "\" ; dir=\""
					+ I18nUtils.getLanguageHTMLDirAttributeValue(req
							.getLocale()) + "\"");
		}

		StringBuilder i18nHtml = new StringBuilder(html);
		boolean langAttrInserted = false;
		String langAttr = "\""
				+ I18nUtils.getLanguageHTMLLangAttributeValue(req.getLocale())
				+ "\"";
		boolean dirAttrInserted = false;
		String dirAttr = "\""
				+ I18nUtils.getLanguageHTMLDirAttributeValue(req.getLocale())
				+ "\"";
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

	private String compressHtml(final HttpServletRequest req, final String html) {
		if (debug > 0) {
			log("HTML compressing the resource '" + req.getServletPath() + "'");
		}

		HtmlCompressor compressor = new HtmlCompressor();
		int bypassCSS = html.indexOf(HTML_COMMENT_BYPASS_COMPRESSING_CSS);

		if (compressCSS && (bypassCSS == -1)) {
			if (debug > 0) {
				log("CSS minification for resource '" + req.getServletPath()
						+ "'");
			}

			compressor.setCompressCss(true);
			// --line-break param for Yahoo YUI Compressor
			compressor.setYuiCssLineBreak(-1);
		}

		int bypassJS = html.indexOf(HTML_COMMENT_BYPASS_COMPRESSING_JAVASCRIPT);

		if (compressJS && (bypassJS == -1)) {
			if (debug > 0) {
				log("JavaScript minification for resource '"
						+ req.getServletPath() + "'");
			}

			compressor.setCompressJavaScript(true);
			// --disable-optimizations param for Yahoo YUI Compressor
			compressor.setYuiJsDisableOptimizations(true);
			// --line-break param for Yahoo YUI Compressor
			compressor.setYuiJsLineBreak(-1);
			// --nomunge param for Yahoo YUI Compressor
			compressor.setYuiJsNoMunge(true);
			// --preserve-semi param for Yahoo YUI Compressor
			compressor.setYuiJsPreserveAllSemiColons(true);
		}

		long beginTime = System.currentTimeMillis();
		int sizeBefore = html.length();
		String compHtml = compressor.compress(html);

		if (debug > 0) {
			log("Compression statics for resource '" + req.getServletPath()
					+ "' - size before: " + sizeBefore + ", size after: "
					+ compHtml.length() + ", and total time: "
					+ (System.currentTimeMillis() - beginTime) + "(ms)");
		}
		return compHtml;
	}

}
