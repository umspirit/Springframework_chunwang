package com.java.myproject.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseUtil {
	public static Connection getConnection() {
		try {
			String dbURL = "jdbc:mysql://localhost:3306/chunwang";
			String dbID = "root";
			String dbPassword = "djawjdrl12";
			Class.forName("com.mysql.jdbc.Driver");
			
			return DriverManager.getConnection(dbURL, dbID, dbPassword);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
}
