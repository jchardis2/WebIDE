package com.infinityappsolutions.server.lib.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

public abstract class AbstractDAOFactory {
	private static AbstractDAOFactory productionInstance = null;
	private IConnectionDriver driver;

	/**
	 * 
	 */
	public static AbstractDAOFactory getProductionInstance() {
		return productionInstance;
	}

	/**
	 * Protected constructor. Call getProductionInstance to get an instance of
	 * the AbstractDAOFactory
	 */
	protected AbstractDAOFactory() {
	}

	/**
	 * 
	 * @return this AbstractDAOFactory's Connection
	 * @throws SQLException
	 * @throws NamingException
	 */
	public Connection getConnection() throws SQLException, NamingException {
		try {
			return driver.getConnection();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
