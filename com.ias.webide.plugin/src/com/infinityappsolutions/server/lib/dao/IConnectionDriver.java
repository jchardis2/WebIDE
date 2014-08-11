package com.infinityappsolutions.server.lib.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

/**
 * Used by AbstractDAOFactory to abstract away different ways of getting our
 * JDBC connection
 */
public interface IConnectionDriver {
	public Connection getConnection() throws SQLException, ClassNotFoundException, NamingException;
}
