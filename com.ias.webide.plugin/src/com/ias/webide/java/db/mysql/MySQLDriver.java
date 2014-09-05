package com.ias.webide.java.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDriver {
	public static String CONNECTION_URL = "jdbc:mysql://localhost:3306/";
	public static String DEFAULT_CONNECTION_URL = "jdbc:mysql://localhost:3306/webide";

	public Connection getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = null;
		return DriverManager.getConnection(DEFAULT_CONNECTION_URL, "ias", "mytestpassword");
	}

	public Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = null;
		return DriverManager.getConnection(CONNECTION_URL + dbName, "ias", "mytestpassword");
	}

}
