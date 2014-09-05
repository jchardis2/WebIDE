package com.infinityappsolutions.server.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ias.webide.java.db.mysql.MySQLDAO;
import com.ias.webide.java.db.mysql.MySQLDriver;

public class DBServlet extends HttpServlet {

	private static final long serialVersionUID = 3314209468176929281L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		MySQLDAO mySQLDAO = new MySQLDAO(new MySQLDriver());
		ArrayList<String> dbs = null;
		Map<String, String[]> map = request.getParameterMap();
		Set<String> keys = map.keySet();
		for (String string : keys) {
			System.out.println(keys);
		}
		try {
			switch (request.getParameter("action")) {
			case "listdbs":
				dbs = mySQLDAO.getDatabases();
				break;

			default:
				return;
			}
			PrintWriter out = response.getWriter();
			for (String string : dbs) {
				out.println(string);
			}
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}