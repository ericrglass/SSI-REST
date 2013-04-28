package com.github.ssi_rest.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import com.github.ssi_rest.SsiRestLogger;

@WebFilter({ "*.js", "*.css", "*.png", "*.gif", "*.ico" })
public class ResponseHeaderExpiresFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String expireAt = null;

		try {
			Calendar c = Calendar.getInstance();
			c.add(Calendar.YEAR, 1);
			String pattern = "EEE, dd MMM yyyy HH:mm:ss z";
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
			expireAt = sdf.format(c.getTime());
		} catch (Throwable th) {
			expireAt = null;
			SsiRestLogger.LOGGER.log(Level.SEVERE,
					"ResponseHeaderExpiresFilter Could not compute expires because of Exception: "
							+ th.getMessage(), th);
			throw new RuntimeException(th);
		}

		if ((expireAt != null) && (response instanceof HttpServletResponse)) {
			HttpServletResponse sr = (HttpServletResponse) response;
			sr.setHeader("Expires", expireAt);
			sr.setHeader("Cache-Control", "public");
		}

		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
