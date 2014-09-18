package com.ias.webide.java.db.mysql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.InputMismatchException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public JsonArray getDatabasesAsJSON() throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement statement = null;
		String showDatabasesSQL = "SHOW DATABASES";
		try {
			conn = mDriver.getConnection();
			statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(showDatabasesSQL);
			return getJSONFromRS(rs, "Database");
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {

			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}

	}

	public ArrayList<String> getTables(String dbName) throws ClassNotFoundException, SQLException {
		if (checkIfDatabaseExists(dbName)) {
			Connection conn = null;
			Statement statement = null;
			String showTablesSQL = "SHOW TABLES";
			try {
				conn = mDriver.getConnection(dbName);
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(showTablesSQL);
				return getStringsFromRS(rs, "Tables_in_" + dbName);
			} catch (ClassNotFoundException e) {
				throw e;
			} catch (SQLException e) {
				throw e;
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		}
		return null;
	}

	public JsonArray getTablesAsJson(String dbName) throws ClassNotFoundException, SQLException {
		if (checkIfDatabaseExists(dbName)) {
			Connection conn = null;
			Statement statement = null;
			String showTablesSQL = "SHOW TABLES";
			try {
				conn = mDriver.getConnection(dbName);
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(showTablesSQL);
				return getJSONFromRS(rs, "Tables_in_" + dbName);
			} catch (ClassNotFoundException e) {
				throw e;
			} catch (SQLException e) {
				throw e;
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
		}
		return null;
	}

	public JsonArray getTableDataAsJson(String dbName, String tableName) throws ClassNotFoundException, SQLException {
		JsonArray tableSchemaArray = new JsonArray();
		if (checkIfDatabaseExists(dbName) && checkIfTableExists(dbName, tableName)) {
			Connection conn = null;
			Statement statement = null;
			String showColSQL = "Select * from " + tableName;
			try {
				conn = mDriver.getConnection(dbName);
				// PreparedStatement preparedStatement =
				// conn.prepareStatement(showColSQL);
				// preparedStatement.setString(1, tableName);
				// ResultSet rs = preparedStatement.executeQuery();
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(showColSQL);
				int count = rs.getMetaData().getColumnCount();

				while (rs.next()) {
					JsonObject jsonObject = new JsonObject();
					for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
						String columnName = rs.getMetaData().getColumnLabel(i);
						jsonObject.addProperty(columnName, rs.getString(columnName));
					}
					tableSchemaArray.add(jsonObject);
				}
			} catch (ClassNotFoundException e) {
				throw e;
			} catch (SQLException e) {
				throw e;
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
			return tableSchemaArray;
		}
		return null;
	}

	public JsonArray getTableMetaDataAsJson(String dbName, String tableName) throws ClassNotFoundException, SQLException {
		JsonArray tableSchemaArray = new JsonArray();
		if (checkIfDatabaseExists(dbName) && checkIfTableExists(dbName, tableName)) {
			Connection conn = null;
			Statement statement = null;
			String showColSQL = "DESCRIBE " + tableName;
			try {
				conn = mDriver.getConnection(dbName);
				// PreparedStatement preparedStatement =
				// conn.prepareStatement(showColSQL);
				// preparedStatement.setString(1, tableName);
				// ResultSet rs = preparedStatement.executeQuery();
				statement = conn.createStatement();
				ResultSet rs = statement.executeQuery(showColSQL);
				while (rs.next()) {
					JsonObject jsonObject = new JsonObject();
					for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
						String columnName = rs.getMetaData().getColumnLabel(i);
						jsonObject.addProperty(columnName, rs.getString(columnName));
					}
					tableSchemaArray.add(jsonObject);
				}
			} catch (ClassNotFoundException e) {
				throw e;
			} catch (SQLException e) {
				throw e;
			} finally {
				if (conn != null) {
					conn.close();
				}
			}
			return tableSchemaArray;
		}
		return null;
	}

	public int createNewDB(String dbName) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement statement = null;
		String createDBSQL = "Create database " + dbName;
		try {
			conn = mDriver.getConnection();
			// PreparedStatement preparedStatement =
			// conn.prepareStatement(showColSQL);
			// preparedStatement.setString(1, tableName);
			// ResultSet rs = preparedStatement.executeQuery();
			statement = conn.createStatement();
			int rs = statement.executeUpdate(createDBSQL);
			return rs;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public int deleteDB(String dbName) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		Statement statement = null;
		String createDBSQL = "Drop database " + dbName;
		try {
			conn = mDriver.getConnection();
			statement = conn.createStatement();
			int rs = statement.executeUpdate(createDBSQL);
			return rs;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public boolean createNewTable(JsonObject tableEl, JsonArray cols) throws ClassNotFoundException, SQLException {
		Connection conn = null;
		PreparedStatement statement = null;
		String createDBSQL = getCreateTableStatement(tableEl, cols);
		try {
			conn = mDriver.getConnection();
			// PreparedStatement preparedStatement =
			// conn.prepareStatement(showColSQL);
			// preparedStatement.setString(1, tableName);
			// ResultSet rs = preparedStatement.executeQuery();
			statement = conn.prepareStatement(createDBSQL);
			statement.setString(1, tableEl.get("name").getAsString());
			boolean rs = statement.execute();
			return rs;
		} catch (ClassNotFoundException e) {
			throw e;
		} catch (SQLException e) {
			throw e;
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	private String getCreateTableStatement(JsonObject tableEl, JsonArray cols) {
		String createDBSQL = "Create table ? {";
		for (int i = 0; i < cols.size(); i++) {
			createDBSQL += "? " + validateType(((JsonObject) cols.get(i)).get("type").getAsString());
			if (i + 1 == cols.size()) {
				createDBSQL += "}";
			} else {
				createDBSQL += ",";
			}
		}
		return null;
	}

	String columnTypes[] = { "int", "varchar", "text" };

	private String validateType(String type) {
		for (String string : columnTypes) {
			if (string.equals(type))
				return string;
		}
		throw new InputMismatchException();
	}

	private ArrayList<String> getStringsFromRS(ResultSet rs, String name) throws SQLException {
		ArrayList<String> list = new ArrayList<String>();
		while (rs.next()) {
			String table = rs.getString(name);
			list.add(table);
		}
		return list;
	}

	private JsonArray getJSONFromRS(ResultSet rs, String name) throws SQLException {
		JsonArray list = new JsonArray();
		while (rs.next()) {
			String value = rs.getString(name);
			JsonObject jsonObject = new JsonObject();
			jsonObject.addProperty("name", value);
			list.add(jsonObject);
		}
		return list;
	}

	private boolean checkIfDatabaseExists(String dbName) throws ClassNotFoundException, SQLException {
		ArrayList<String> dbs = getDatabases();
		if (dbs != null && dbs.contains(dbName)) {
			return true;
		}
		return false;
	}

	private boolean checkIfTableExists(String dbName, String tableName) throws ClassNotFoundException, SQLException {
		ArrayList<String> tables = getTables(dbName);
		if (tables != null && tables.contains(tableName)) {
			return true;
		}
		return false;
	}

}
