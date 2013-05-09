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
import com.googlecode.htmlcompressor.compressor.HtmlCompressor;

public class SsiRestServlet extends SSIServlet_JBossWeb {

	public static final String INIT_PARAM_HTML_COMPRESSOR = "com.github.ssi_rest.servlet.HTML_COMPRESSOR";
	public static final String INIT_PARAM_COMPRESS_CSS = "com.github.ssi_rest.servlet.COMPRESS_CSS";
	public static final String INIT_PARAM_COMPRESS_JS = "com.github.ssi_rest.servlet.COMPRESS_JAVASCRIPT";

	public static final String REQ_ATTR_INITIAL_REQUEST_STRING = "com.github.ssi_rest.servlet.INITIAL_REQUEST_STRING";

	public static final String HTML_COMMENT_BYPASS_COMPRESSING_HTML = "<!-- BYPASS_COMPRESSING_HTML -->";
	public static final String HTML_COMMENT_BYPASS_COMPRESSING_CSS = "<!-- BYPASS_COMPRESSING_CSS -->";
	public static final String HTML_COMMENT_BYPASS_COMPRESSING_JAVASCRIPT = "<!-- BYPASS_COMPRESSING_JAVASCRIPT -->";

	private static final long serialVersionUID = 4542259497206614997L;

	private String serviceDelegatorJNDIName = null;
	private boolean htmlCompressor = false;
	private boolean compressCSS = false;
	private boolean compressJS = false;

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

		if (htmlCompressor) {
			// for HTML compressor force buffered to be true
			buffered = true;
		}
	}

	@Override
	protected String processBuffered(HttpServletRequest req, String text) {
		String html = super.processBuffered(req, text);

		if (!htmlCompressor) {
			return html;
		}

		Object initialReqStr = req
				.getAttribute(REQ_ATTR_INITIAL_REQUEST_STRING);

		if (!req.toString().equals(initialReqStr)) {
			return html;
		}

		int bypassHTML = html.indexOf(HTML_COMMENT_BYPASS_COMPRESSING_HTML);

		if (bypassHTML > -1) {
			return html;
		}

		req.removeAttribute(REQ_ATTR_INITIAL_REQUEST_STRING);
		HtmlCompressor compressor = new HtmlCompressor();
		int bypassCSS = html.indexOf(HTML_COMMENT_BYPASS_COMPRESSING_CSS);

		if (compressCSS && (bypassCSS == -1)) {
			compressor.setCompressCss(true);
			// --line-break param for Yahoo YUI Compressor
			compressor.setYuiCssLineBreak(-1);
		}

		int bypassJS = html.indexOf(HTML_COMMENT_BYPASS_COMPRESSING_JAVASCRIPT);

		if (compressJS && (bypassJS == -1)) {
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

		return compressor.compress(html);
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
				((IServiceDelegator) serviceDelegator).processRequest(req,
						service, action);
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

}
