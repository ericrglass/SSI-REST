package com.github.ssi_rest.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebFilter({ "/coffee/*", "/includes/*", "/sass/*" })
public class ResourceSecurityFilter implements Filter {

	@Override
	public void destroy() {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		boolean responseSent = false;

		if (request instanceof HttpServletRequest) {
			try {
				RequestDispatcher reqDisp = ((HttpServletRequest) request)
						.getRequestDispatcher("/notFound.html");

				if (reqDisp != null) {
					reqDisp.forward(request, response);
					responseSent = true;
				}
			} catch (Exception e) {
			}
		}

		if (!responseSent && (response instanceof HttpServletResponse)) {
			((HttpServletResponse) response).setStatus(404);
		}
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
	}

}
