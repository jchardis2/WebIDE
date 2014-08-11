package com.infinityappsolutions.server.lib.dao;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.infinityappsolutions.server.lib.dao.IConnectionDriver;
import com.infinityappsolutions.server.lib.dao.AbstractProductionConnectionDriver;

/**
 * The central mediator for all Database Access Objects. The production instance
 * uses the database connection pool provided by Tomcat (so use the production
 * instance when doing stuff from JSPs in the "real code"). Both the production
 * and the test instance parses the context.xml file to get the JDBC connection.
 * 
 * Also, @see {@link EvilDAOFactory} and @see {@link TestDAOFactory}.
 * 
 * Any DAO that is added to the system should be added in this class, in the
 * same way that all other DAOs are.
 * 
 * 
 * 
 */
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
		// this.driver = (IConnectionDriver) new
		// AbstractProductionConnectionDriver();
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
