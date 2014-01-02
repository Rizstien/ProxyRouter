package com.rizstien.proxy.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigReader {
	private static Properties properties;
	private static ConfigReader instance;

	private static Logger logger = LoggerFactory.getLogger(ConfigReader.class.getSimpleName());
	private ConfigReader() {
		try {
			// Get the inputStream
			InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("application.properties");
			properties = new Properties();
			// load the inputStream using the Properties
			properties.load(inputStream);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public static ConfigReader getInstance() {
		if (instance == null)
			instance = new ConfigReader();
		return instance;
	}

	public String getProperty(String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			logger.debug("Value read from properties file for "+key+" is "+value);
		}
		return value;
	}
}
