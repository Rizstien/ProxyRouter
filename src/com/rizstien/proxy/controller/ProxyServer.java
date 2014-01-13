package com.rizstien.proxy.controller;

import java.lang.reflect.Constructor;
import java.util.List;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rizstien.proxy.dao.ProxyJDBCManager;
import com.rizstien.proxy.dao.RoutingMapModel;
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
		List<RoutingMapModel> routingList = manager.loadRoutingMap();
		Handler[] handlers = new Handler[routingList.size()];
		ContextHandlerCollection contexts = new ContextHandlerCollection();
		int i = 0;
		
		for(RoutingMapModel route : routingList) {
			ContextHandler context = new ContextHandler();
			context.setContextPath(route.getContextPath());
			
			@SuppressWarnings("unchecked")
			Class<Handler> _tempClass = (Class<Handler>) Class.forName(route.getHandlerClass());
			Constructor<Handler> ctor = _tempClass.getConstructor(String.class);
			Handler handler = (Handler) ctor.newInstance(route.getDestinationURL());
			
			context.setHandler(handler);
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
