package com.ias.webide.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.ias.webide.java.db.mysql.MySQLDAO;
import com.ias.webide.java.db.mysql.MySQLDriver;

public class DBServlet extends HttpServlet {

	private static final long serialVersionUID = 3314209468176929281L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.print(")]}',\n");
		MySQLDAO mySQLDAO = new MySQLDAO(new MySQLDriver());
		JsonObject outObj = new JsonObject();
		JsonArray dbs = null;
		Map<String, String[]> map = request.getParameterMap();
		Set<String> keys = map.keySet();
		try {
			switch (request.getParameter("action")) {
			case "listdbs":
				dbs = mySQLDAO.getDatabasesAsJSON();
				for (int i = 0; i < dbs.size(); i++) {
					JsonObject db = (JsonObject) dbs.get(i);
					String tableName = db.get("name").getAsString();
					System.out.println(tableName);
					JsonArray tables = mySQLDAO.getTablesAsJson(db.get("name").getAsString());
					db.add("tables", tables);
				}
				outObj.add("dbs", dbs);
				break;
			case "getTableData":
				dbs = mySQLDAO.getTableDataAsJson(request.getParameter("db"), request.getParameter("table"));
				outObj.add("tableData", dbs);
				break;
			case "getTableMetaData":
				dbs = mySQLDAO.getTableMetaDataAsJson(request.getParameter("db"), request.getParameter("table"));
				outObj.add("tableMeta", dbs);
				break;
			case "addNewDB":
				int rs = mySQLDAO.createNewDB(request.getParameter("db"));
				dbs = mySQLDAO.getDatabasesAsJSON();
				for (int i = 0; i < dbs.size(); i++) {
					JsonObject db = (JsonObject) dbs.get(i);
					String tableName = db.get("name").getAsString();
					System.out.println(tableName);
					JsonArray tables = mySQLDAO.getTablesAsJson(db.get("name").getAsString());
				}
				outObj.add("dbs", dbs);
				outObj.add("rs", new JsonPrimitive(rs));
				return;
			default:
				return;
			}
			out.println(outObj);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		out.print(")]}',\n");
		MySQLDAO mySQLDAO = new MySQLDAO(new MySQLDriver());
		JsonArray dbs = null;
		// Map<String, String[]> map = request.getParameterMap();
		//
		// Set<String> keys = map.keySet();
		// for (String key : keys) {
		// System.out.println("key: " + key);
		// System.out.println("Value: " + map.get(key)[0].toString());
		// }
		//
		// Enumeration<String> names = request.getAttributeNames();
		// while (names.hasMoreElements()) {
		// String name = names.nextElement();
		// System.out.println("Name: " + name);
		// System.out.println("Value: " + request.getAttribute(name));
		// }
		switch (request.getParameter("action")) {
		case "deleteDB":
			Gson gson = new Gson();
			ArrayList<String> dbNames = gson.fromJson(request.getParameter("names"), new TypeToken<ArrayList<String>>() {
			}.getType());
			for (String dbName : dbNames) {
				try {
					mySQLDAO.deleteDB(dbName);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		default:
			return;
		}
		out.println();
	}
}