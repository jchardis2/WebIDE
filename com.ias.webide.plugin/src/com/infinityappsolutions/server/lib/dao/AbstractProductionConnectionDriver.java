package com.infinityappsolutions.server.lib.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Produces the JDBC connection from Tomcat's JDBC connection pool (defined in
 * context.xml). Produces and exception when running the unit tests because
 * they're not being run through Tomcat.
 * 
 * 
 * 
 */
public abstract class AbstractProductionConnectionDriver implements
		IConnectionDriver {
	private InitialContext initialContext;
	private static final String RESOURCE_NAME = null;

	// In production situations
	public AbstractProductionConnectionDriver() {
	}

	// For our special unit test - do not use unless you know what you are doing
	public AbstractProductionConnectionDriver(InitialContext context) {
		initialContext = context;
	}

	public Connection getConnection(String dbName) throws SQLException,
			NamingException {
		if (initialContext == null)
			initialContext = new InitialContext();
		InitialContext ic = new InitialContext();
		DataSource myDS = (DataSource) ic
				.lookup("java:comp/env/jdbc/" + dbName);
		return myDS.getConnection();
	}
}
