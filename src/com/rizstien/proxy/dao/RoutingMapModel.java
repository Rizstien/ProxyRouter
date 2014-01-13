package com.rizstien.proxy.dao;

public class RoutingMapModel {
	private String contextPath;
	private String handlerClass;
	private String destinationURL;
	
	public String getContextPath() {
		return contextPath;
	}
	public void setContextPath(String contextPath) {
		this.contextPath = contextPath;
	}
	
	public String getHandlerClass() {
		return handlerClass;
	}
	public void setHandlerClass(String handlerClass) {
		this.handlerClass = handlerClass;
	}
	
	public String getDestinationURL() {
		return destinationURL;
	}
	public void setDestinationURL(String destinationURL) {
		this.destinationURL = destinationURL;
	}
	
}
