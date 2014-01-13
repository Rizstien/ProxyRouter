package com.rizstien.proxy.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;
import com.rizstien.proxy.util.ConfigReader;

public class ProxyJDBCManager {
	private static ProxyJDBCManager INSTANCE;
	private static BoneCP connectionPool = null;
	private static Logger logger = LoggerFactory.getLogger(ProxyJDBCManager.class.getSimpleName());

	private static final String INSERT_ACTIVITY_LOG = "insert into ACTIVITY_LOG (ACTIVITY_LOG_ID, REQUEST_TIME, RESPONSE_TIME, REQUEST_XML, RESPONSE_XML, SOURCE_IP, DESTINATION_URL) values (ACTIVITY_LOG_SEQ.nextval, ?, ?, ?, ?, ?, ?)";
	private static final String LOAD_ROUTING_MAP  = "select CONTEXT_PATH, HANDLER_CLASS, DESTINATION_URL from REQUEST_ROUTER where IS_ACTIVE = 1";
	
	public static void cleanup() {
		try {
			if (connectionPool != null)
				connectionPool.shutdown();
			logger.info("Connection Pooling shutdown");
		} catch (Exception e) {
			logger.error("Exceptions Occured while shuting down Connection Pooling", e);
		}
	}

	private ProxyJDBCManager() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");

			ConfigReader reader = ConfigReader.getInstance();
			String dbserver = reader.getProperty("db.server");
			String dbUsername = reader.getProperty("db.username");
			String dbPassword = reader.getProperty("db.password");
			String dbSID = reader.getProperty("db.sid");
			String dbPort = reader.getProperty("db.port");
			
			BoneCPConfig config = new BoneCPConfig();
			StringBuilder stringBuilder = new StringBuilder("jdbc:oracle:thin:@");
			stringBuilder.append(dbserver);
			stringBuilder.append(":");
			stringBuilder.append(dbPort);
			stringBuilder.append(":");
			stringBuilder.append(dbSID);

			logger.info("Database URL: " + stringBuilder.toString());
			logger.info("USERNAME: " + dbUsername);
			logger.info("PASSWORD: " + dbPassword);
			
			config.setJdbcUrl(stringBuilder.toString());
			config.setUsername(dbUsername);
			config.setPassword(dbPassword);
			config.setMinConnectionsPerPartition(1);
			config.setMaxConnectionsPerPartition(1);
			config.setPartitionCount(1);
			config.setAcquireIncrement(5);
			config.setDefaultAutoCommit(true);
			config.setInitSQL("SELECT 1 FROM DUAL");
			// setup the connection pool
			connectionPool = new BoneCP(config);
		} catch (Exception e) {
			logger.error("Exceptions Occured while Connection Pooling", e);
		}
	}

	public static ProxyJDBCManager instance() {
		if (INSTANCE == null) {
			INSTANCE = new ProxyJDBCManager();
		}
		return INSTANCE;
	}

	private Connection connection = null;
	
	public List<RoutingMapModel> loadRoutingMap() {
		RoutingMapModel routingMap;
		List<RoutingMapModel> routingList = new ArrayList<RoutingMapModel>();
		try {
			connection = connectionPool.getConnection();
			PreparedStatement stmt = connection.prepareStatement(LOAD_ROUTING_MAP);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				routingMap = new RoutingMapModel();
				routingMap.setContextPath(rs.getString("CONTEXT_PATH"));
				routingMap.setHandlerClass(rs.getString("HANDLER_CLASS"));
				routingMap.setDestinationURL(rs.getString("DESTINATION_URL"));
				routingList.add(routingMap);
			}
		} catch (Exception e) {
			logger.error("Exceptions Occured while Fetching Routing Details", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Exceptions Occured while Closing Connection", e);
				}
			}
		}
		return routingList;
	}
	
	public void logActivity(ActivityLogModel log) {
		try {
			connection = connectionPool.getConnection();
			PreparedStatement stmt = connection.prepareStatement(INSERT_ACTIVITY_LOG);
			stmt.setTimestamp(1, log.getRequestTime());
			stmt.setTimestamp(2, log.getResponseTime());
			stmt.setString(3, log.getRequestXML());
			stmt.setString(4, log.getResponseXML());
			stmt.setString(5, log.getSourceIP());
			stmt.setString(6, log.getDestinationURL());
			stmt.executeQuery();
		} catch (Exception e) {
			logger.error("Exceptions Occured while Fetching Routing Details", e);
		} finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Exceptions Occured while Closing Connection", e);
				}
			}
		}
	}

}
