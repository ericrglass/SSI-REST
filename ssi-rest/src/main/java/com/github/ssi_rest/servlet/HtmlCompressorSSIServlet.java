package com.github.ssi_rest.servlet;

import javax.servlet.ServletException;

import org.apache.catalina.ssi.SSIServlet;

public class HtmlCompressorSSIServlet extends SSIServlet {

	private static final long serialVersionUID = 4542259497206614997L;

	@Override
	public void init() throws ServletException {
		super.init();

		// TODO: Add code here for HTML compressor options
		// boolean buffered =
		// Boolean.parseBoolean(getServletConfig().getInitParameter("buffered"));
	}

}
