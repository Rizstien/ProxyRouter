package com.rizstien.proxy.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rizstien.proxy.dao.ActivityLogModel;
import com.rizstien.proxy.dao.ProxyJDBCManager;
import com.rizstien.proxy.util.ConfigReader;

public class ProxyHandler extends AbstractHandler {

	private static Logger logger = LoggerFactory.getLogger(ProxyHandler.class.getSimpleName());
	private static ProxyJDBCManager manager;
	private static String redirectUrl;

	public ProxyHandler() {
		ConfigReader reader = ConfigReader.getInstance();
		redirectUrl = reader.getProperty("redirect.url");
	}
	
	public void handle(String arg0, Request request, HttpServletRequest httpRequest, HttpServletResponse response) throws IOException, ServletException {
		logger.info("Forwarding Request to " + redirectUrl);
		
		String req = httpRequest.getParameter("xml");
		manager = ProxyJDBCManager.instance();
		ActivityLogModel log = new ActivityLogModel();
		try {
			log.setSourceIP(httpRequest.getRemoteAddr());
			log.setDestinationURL(redirectUrl);
			log.setRequestXML(req);
			log.setRequestTime(new Timestamp(new Date().getTime()));
				
			String resStr = ProxyController.forwardRequest(req, redirectUrl);
				
			log.setResponseTime(new Timestamp(new Date().getTime()));
			log.setResponseXML(resStr);
			manager.logActivity(log);
			response.setContentType("text/html;charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			request.setHandled(true);
			request.getRequestURL();
			response.getWriter().println(resStr);	
		} catch (Exception e){
			logger.error("[ProxyHandler] Exceptions Occured ", e);
		}

	}
}
