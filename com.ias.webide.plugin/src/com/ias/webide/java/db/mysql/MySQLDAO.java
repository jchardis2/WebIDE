package com.ias.webide.java.db.mysql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class MySQLDAO {
	MySQLDriver mDriver;

	public MySQLDAO(MySQLDriver mDriver) {
		this.mDriver = mDriver;
	}

	public ArrayList<String> getDatabases() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement statement = null;
		String showDatabasesSQL = "SHOW DATABASES";
		try {
			conn = mDriver.getConnection();
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(showDatabasesSQL);
			return getStringsFromRS(rs, "Database");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public ArrayList<String> getTables(String dbName) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement statement = null;
		String showTablesSQL = "SHOW TABLES";
		conn = mDriver.getConnection(dbName);
		statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(showTablesSQL);
		return getStringsFromRS(rs, "Tables_in_" + dbName);
	}

	private ArrayList<String> getStringsFromRS(ResultSet rs, String name) throws SQLException {
		ArrayList<String> list = new ArrayList<String>();

		while (rs.next()) {
			int count = rs.getMetaData().getColumnCount();
			for (int i = 1; i <= count; i++) {
				System.out.println(rs.getMetaData().getColumnLabel(i));
			}
			String table = rs.getString(name);
			list.add(table);
		}
		return list;
	}
}
