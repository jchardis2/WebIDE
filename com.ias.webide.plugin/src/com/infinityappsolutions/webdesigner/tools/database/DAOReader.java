package com.infinityappsolutions.webdesigner.tools.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DAOReader {
	private ArrayList<String> tableNames;
	private ArrayList<DatabaseTable> databaseTables;
	private Connection connection = null;
	private DatabaseMetaData dbMetaData;

	public DAOReader() {
		tableNames = new ArrayList<String>();
		databaseTables = new ArrayList<DAOReader.DatabaseTable>();
	}

	/**
	 * this is just for testing
	 */
	public void setConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");

			// System.out.println("MySQL JDBC Driver Registered!");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/webdesigner", "root", "mytestpassword");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your MySQL JDBC Driver?");
			e.printStackTrace();
			return;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void printTableNames() {
		for (String string : tableNames) {
			System.out.println(string);
		}
	}

	public ArrayList<DatabaseTable> getDatabaseTables() throws SQLException {
		dbMetaData = (DatabaseMetaData) connection.getMetaData();
		String tableTypes[] = { "TABLE", "VIEW", "LOCAL TEMPORARY" };
		ResultSet allTables = dbMetaData.getTables(null, null, null, tableTypes);
		while (allTables.next()) {
			String table_name = allTables.getString("TABLE_NAME");
			tableNames.add(table_name);
		}
		allTables.close();

		for (String string : tableNames) {
			databaseTables.add(loadSingleTable(string));
		}
		return databaseTables;
	}

	public DatabaseTable loadSingleTable(String tableName) throws SQLException {
		Statement stmt = connection.createStatement();
		String query = "SELECT * FROM " + tableName;
		ResultSet rs = stmt.executeQuery(query);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		ArrayList<DatabaseColumn> columns = new ArrayList<DatabaseColumn>();
		for (int col = 1; col <= columnCount; col++) {
			// System.out.print(rsmd.getColumnLabel(col));
			// System.out.print(" (" + rsmd.getColumnTypeName(col) + ")" + "("
			// + rsmd.getColumnDisplaySize(col) + ")");
			// if (col < columnCount)
			// System.out.print(", ");
			columns.add(new DatabaseColumn(rsmd.getColumnLabel(col), rsmd.getColumnTypeName(col), null));
		}
		// System.out.println();
		return new DatabaseTable(tableName, columns);
	}

	public class DatabaseTable {
		private String name;
		private ArrayList<DatabaseColumn> columns;

		public DatabaseTable() {

		}

		public DatabaseTable(String name, ArrayList<DatabaseColumn> columns) {
			super();
			this.name = name;
			this.columns = columns;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public ArrayList<DatabaseColumn> getColumns() {
			return columns;
		}

		public void setColumns(ArrayList<DatabaseColumn> columns) {
			this.columns = columns;
		}

	}

	public class DatabaseColumn {
		private String name;
		private String type;
		private String foreignKey;

		public DatabaseColumn() {
		}

		public DatabaseColumn(String name, String type, String foreignKey) {
			super();
			this.name = name;
			this.type = type;
			this.foreignKey = foreignKey;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getForeignKey() {
			return foreignKey;
		}

		public void setForeignKey(String foreignKey) {
			this.foreignKey = foreignKey;
		}

	}
}
