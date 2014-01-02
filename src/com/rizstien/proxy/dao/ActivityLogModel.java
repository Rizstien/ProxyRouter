package com.rizstien.proxy.dao;

public class ActivityLogModel {
	private java.sql.Timestamp requestTime;
	private java.sql.Timestamp responseTime;
	private String requestXML;
	private String responseXML;
	private String sourceIP;
	private String destinationURL;
	
	public java.sql.Timestamp getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(java.sql.Timestamp requestTime) {
		this.requestTime = requestTime;
	}
	
	public java.sql.Timestamp getResponseTime() {
		return responseTime;
	}
	public void setResponseTime(java.sql.Timestamp responseTime) {
		this.responseTime = responseTime;
	}
	
	public String getRequestXML() {
		return requestXML;
	}
	public void setRequestXML(String requestXML) {
		this.requestXML = requestXML;
	}
	
	public String getResponseXML() {
		return responseXML;
	}
	public void setResponseXML(String responseXML) {
		this.responseXML = responseXML;
	}
	
	public String getSourceIP() {
		return sourceIP;
	}
	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}
	
	public String getDestinationURL() {
		return destinationURL;
	}
	public void setDestinationURL(String destinationURL) {
		this.destinationURL = destinationURL;
	}
	
}
