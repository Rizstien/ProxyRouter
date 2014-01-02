package com.rizstien.proxy.controller;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProxyController {
	
	private static Logger logger = LoggerFactory.getLogger(ProxyController.class.getSimpleName());

	@SuppressWarnings("finally")
	public static String forwardRequest(String xml, String redirectUrl) throws IOException, ClientProtocolException {
		HttpPost httpPost = new HttpPost(redirectUrl);
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resStr = null;
		params.add(new BasicNameValuePair("message", xml));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			resStr = EntityUtils.toString(entity);
			logger.debug("Response =>  "+ resStr);
		} catch (ClientProtocolException e){
			logger.error("Exceptions Occured while forwarding request", e);
		} catch (IOException e){
			logger.error("Exceptions Occured while forwarding request", e);
		} finally {
			response.close();
			return resStr;
		}
	}
}