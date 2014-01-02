package com.rizstien.test;

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

import com.rizstien.proxy.util.ConfigReader;

public class TestProxyServer {
	private static Logger logger = LoggerFactory.getLogger(TestProxyServer.class.getSimpleName());
	
	public static void main(String[] args) throws ClientProtocolException, IOException { 
		ConfigReader reader = ConfigReader.getInstance();
		
		String xml = "test";
		String port = reader.getProperty("server.port");
		String url = "http://127.0.0.1:"+port.trim()+"/test/";
		
		HttpPost httpPost = new HttpPost(url);
		List<NameValuePair> params = new LinkedList<NameValuePair>();
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		String resStr = null;
		params.add(new BasicNameValuePair("xml", xml));
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(params));
			response = httpclient.execute(httpPost);
			HttpEntity entity = response.getEntity();
			resStr = EntityUtils.toString(entity);
		} catch (ClientProtocolException e){
			logger.error("Exceptions Occured while forwarding request", e);
		} catch (IOException e){
			logger.error("Exceptions Occured while forwarding request", e);
		} finally {
			response.close();
			 System.out.println(resStr);
		}
	}
}
