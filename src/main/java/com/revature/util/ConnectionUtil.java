package com.revature.util;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;

import org.mariadb.jdbc.Driver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ConnectionUtil {

	private static Logger logger = LoggerFactory.getLogger(ConnectionUtil.class);
	
	public static Connection getConnection() throws SQLException {
		
		Driver mariaDBDriver = new Driver();
		DriverManager.registerDriver(mariaDBDriver);
		
		//String username = System.getenv("db_username");
		//String password = System.getenv("db_password");
		//String connectionString = System.getenv("db_url");
		
		String username = System.getenv("db_username_proj_1");
		String password = System.getenv("db_password_proj_1");
		String connectionString = System.getenv("db_url_proj_1");
		
		logger.warn("You have your username, password and url set as strings for testing purposes, please update ASAP.");
		
		return DriverManager.getConnection(connectionString, username, password);
	}

	
}
