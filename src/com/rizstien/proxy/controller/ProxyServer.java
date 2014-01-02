package com.rizstien.proxy.controller;

import java.util.Map;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rizstien.proxy.dao.ProxyJDBCManager;
import com.rizstien.proxy.util.ConfigReader;

public class ProxyServer {
	private static Logger logger = LoggerFactory.getLogger(ProxyServer.class.getSimpleName());
	private static ProxyJDBCManager manager;
	
	public static void main(String[] args) throws Exception {
		ConfigReader reader = ConfigReader.getInstance();
		String port = reader.getProperty("server.port");
		Server server = new Server(Integer.parseInt(port.trim()));
		logger.info("SERVER START AT PORT : "+port);
		
		manager = ProxyJDBCManager.instance();
		Map<String, String> routingMap = manager.loadRoutingMap();
		Handler[] handlers = new Handler[routingMap.size()];
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		int i = 0;
		
		for(String key : routingMap.keySet()) {
			ContextHandler context = new ContextHandler();
			context.setContextPath(key);
			context.setHandler((Handler) Class.forName(routingMap.get(key)).newInstance());
			handlers[i] = context;
			i++;
		}
		
		contexts.setHandlers(handlers);
		server.setHandler(contexts);
		
		logger.debug("Starting Proxy Server");
		server.start();
		
		logger.debug("Joining Proxy Server");
		server.join();
	}
}
