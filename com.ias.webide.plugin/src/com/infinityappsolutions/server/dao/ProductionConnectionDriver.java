package com.infinityappsolutions.server.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.infinityappsolutions.server.lib.dao.AbstractProductionConnectionDriver;
import com.infinityappsolutions.server.lib.dao.IConnectionDriver;

/**
 * Produces the JDBC connection from Tomcat's JDBC connection pool (defined in
 * context.xml). Produces and exception when running the unit tests because
 * they're not being run through Tomcat.
 * 
 * 
 * 
 */
public class ProductionConnectionDriver extends
		AbstractProductionConnectionDriver implements IConnectionDriver {
	private InitialContext initialContext;
	private static final String RESOURCE_NAME = "webide";

	// In production situations
	public ProductionConnectionDriver() {
	}

	// For our special unit test - do not use unless you know what you are doing
	public ProductionConnectionDriver(InitialContext context) {
		initialContext = context;
	}

	public Connection getConnection() throws SQLException, NamingException {
		return super.getConnection(RESOURCE_NAME);
	}

	public Connection getConnection(String dbName) throws SQLException,
			NamingException {
		return super.getConnection(dbName);
	}
}
