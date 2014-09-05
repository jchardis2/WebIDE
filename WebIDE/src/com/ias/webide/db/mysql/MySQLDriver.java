package com.ias.webide.db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDriver {
	public static String connectionUrl = "jdbc:mysql://localhost:3306/webide";

	public void getConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.jdbc.Driver");
		Connection connection = null;
		connection = DriverManager.getConnection(connectionUrl, "ias", "mytestpassword");
	}

}
