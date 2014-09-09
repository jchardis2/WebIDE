package com.ias.webide.server.rest;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.ias.webide.java.db.mysql.MySQLDAO;
import com.ias.webide.java.db.mysql.MySQLDriver;

@Path("/db")
public class MysqlDBControllerService {

	MySQLDAO mySQLDAO;
	JsonObject outObj;

	public MysqlDBControllerService() {
		mySQLDAO = new MySQLDAO(new MySQLDriver());
		outObj = new JsonObject();
	}

	@POST
	@Path("getDbs")
	public Response getDbs() {
		MySQLDAO mySQLDAO = new MySQLDAO(new MySQLDriver());
		JsonObject outObj = new JsonObject();
		try {
			JsonArray dbs = mySQLDAO.getDatabasesAsJSON();
			for (int i = 0; i < dbs.size(); i++) {
				JsonObject jsonDb = (JsonObject) dbs.get(i);
				JsonArray tables = mySQLDAO.getTablesAsJson(jsonDb.get("name").getAsString());
				jsonDb.add("tables", tables);
			}
			outObj.add("dbs", dbs);
			String out = outObj.toString();
			return Response.status(RestServiceUtil.STATUS_OK).entity(out).build();
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getAlertMessage(e, outObj, RestServiceUtil.JSON_ERROR_PROPERTY);
		}
	}

	@POST
	@Path("getTableMetaData")
	public Response getTableMetaData(@QueryParam("db") String db, @QueryParam("table") String table) {

		JsonArray dbs;
		try {
			dbs = mySQLDAO.getTableMetaDataAsJson(db, table);
			outObj.add("tableMeta", dbs);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getAlertMessage(e, outObj, RestServiceUtil.JSON_ERROR_PROPERTY);
		}

		String out = outObj.toString();
		return Response.status(RestServiceUtil.STATUS_OK).entity(out).build();
	}

	@POST
	@Path("getTableData")
	public Response getTableData(@QueryParam("db") String db, @QueryParam("table") String table) {

		try {
			JsonArray dbs = mySQLDAO.getTableDataAsJson(db, table);
			outObj.add("tableData", dbs);
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getAlertMessage(e, outObj, RestServiceUtil.JSON_ERROR_PROPERTY);
		}
		String out = outObj.toString();
		return Response.status(RestServiceUtil.STATUS_OK).entity(out).build();
	}

	@POST
	@Path("addNewDB")
	public Response addNewDB(@QueryParam("db") String db) {

		try {
			int rs = mySQLDAO.createNewDB(db);
			JsonArray dbs = mySQLDAO.getDatabasesAsJSON();
			for (int i = 0; i < dbs.size(); i++) {
				JsonObject jsonDb = (JsonObject) dbs.get(i);
				JsonArray tables = mySQLDAO.getTablesAsJson(jsonDb.get("name").getAsString());
			}
			outObj.add("dbs", dbs);
			outObj.add("rs", new JsonPrimitive(rs));
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return getAlertMessage(e, outObj, RestServiceUtil.JSON_ERROR_PROPERTY);
		}

		String out = outObj.toString();
		return Response.status(RestServiceUtil.STATUS_OK).entity(out).build();
	}

	@POST
	@Path("deleteDB")
	public Response deleteDB(@QueryParam("names") String names) {
		boolean error = false;
		Gson gson = new Gson();
		ArrayList<String> dbNames = gson.fromJson(names, new TypeToken<ArrayList<String>>() {
		}.getType());
		for (String dbName : dbNames) {
			try {
				mySQLDAO.deleteDB(dbName);
			} catch (ClassNotFoundException | SQLException e) {
				// TODO Auto-generated catch block
				error = true;
				e.printStackTrace();
				outObj = addAlertMessage(e, outObj, RestServiceUtil.JSON_ERROR_PROPERTY);
			}
		}
		if (error) {
			return Response.status(RestServiceUtil.STATUS_BAD_REQUEST).entity(outObj.toString()).build();
		}

		String out = outObj.toString();
		return Response.status(RestServiceUtil.STATUS_OK).entity(out).build();
	}

	private Response getAlertMessage(Exception e, JsonObject outObj, String alertID) {
		outObj = addAlertMessage(e, outObj, RestServiceUtil.JSON_ERROR_PROPERTY);
		return Response.status(RestServiceUtil.STATUS_BAD_REQUEST).entity(outObj.toString()).build();
	}

	private JsonObject addAlertMessage(Exception e, JsonObject outObj, String type) {
		if (!outObj.has(RestServiceUtil.JSON_ALERTS_PROPERTY)) {
			outObj.add(RestServiceUtil.JSON_ALERTS_PROPERTY, new JsonArray());
		}
		JsonArray alerts = outObj.get(RestServiceUtil.JSON_ALERTS_PROPERTY).getAsJsonArray();
		JsonObject alert = new JsonObject();
		alert.add(RestServiceUtil.JSON_TYPE_PROPERTY, new JsonPrimitive(type));
		alert.add(RestServiceUtil.JSON_ALERTS_PROPERTY, new JsonPrimitive(e.getMessage()));
		alerts.add(alert);
		outObj.add(RestServiceUtil.JSON_ALERTS_PROPERTY, alerts);
		return outObj;
	}
	// @Path("smooth")
	// @GET
	// public Response smooth(@DefaultValue("2") @QueryParam("step") int step,
	// @DefaultValue("true") @QueryParam("min-m") boolean hasMin,
	// @DefaultValue("true") @QueryParam("max-m") boolean hasMax,
	// @DefaultValue("true") @QueryParam("last-m") boolean hasLast) {
	// return null;
	// }

	// @GET
	// @Path("/{param}")
	// public Response getMsg(@PathParam("param") String msg) {
	//
	// String output = "Jersey say : " + msg;
	//
	// return Response.status(RestServiceUtil.STATUS_OK).entity(output).build();
	//
	// }
}