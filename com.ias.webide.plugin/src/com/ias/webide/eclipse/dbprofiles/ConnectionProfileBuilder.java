package com.ias.webide.eclipse.dbprofiles;

import java.util.Enumeration;
import java.util.Properties;

import org.eclipse.datatools.connectivity.ConnectionProfileConstants;
import org.eclipse.datatools.connectivity.ConnectionProfileException;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.ProfileConnectionManager;
import org.eclipse.datatools.connectivity.ProfileManager;
import org.eclipse.datatools.connectivity.drivers.IDriverMgmtConstants;
import org.eclipse.datatools.connectivity.drivers.jdbc.IJDBCConnectionProfileConstants;

public class ConnectionProfileBuilder {
	public static final String DEFAULT_MYSQL_DRIVER_DEFINITION_ID = "DriverDefn.org.eclipse.datatools.enablement.mysql.5_1.driverTemplate.MySQL JDBC Driver";
	public static final String DEFAULT_MYSQL_DRIVER_DEFINITION_TYPE = "org.eclipse.datatools.enablement.mysql.5_1.driverTemplate";
	public static final String DEFAULT_MYSQL_PROVIDER_ID = "org.eclipse.datatools.enablement.mysql.connectionProfile";
	public static final String DEFAULT_MYSQL_VERSION = "5.1";
	public static final String DEFAULT_MYSQL_VENDOR = "MySql";
	public static final String DEFAULT_MYSQL_JAR_LIST = "/home/jchardis/ds/tmp/WebclipseDemo/Workspace/HibernateTestProject/libs/mysql-connector-java-5.1.29.jar";
	public static final String DEFAULT_MYSQL_DRIVER_CLASS = "com.mysql.jdbc.Driver";
	public static final String MYSQL__DRIVER_URL = "jdbc:mysql://localhost:3306/";

	public void getConnectionProfiles() {
		ProfileManager connectionManager = ProfileManager.getInstance();
		IConnectionProfile profile = connectionManager.getProfileByName("myprofile");
		System.out.println(profile.getProviderId());
		System.out.println(profile.getInstanceID());
		Properties properties = profile.getBaseProperties();
		Enumeration<Object> keys = properties.keys();
		while (keys.hasMoreElements()) {
			Object object = (Object) keys.nextElement();
			System.out.println("Key: " + object + " value: " + properties.getProperty((String) object));

		}
		System.out.println(profile.getBaseProperties());
	}

	public void buildConnectionProfile() {
		ProfileConnectionManager connectionManager = ProfileConnectionManager.getProfileConnectionManagerInstance();
	}

	public IConnectionProfile createProfile(String name, String description, String driverDefinitionID, String defnType, String vendor, String version, String jarList,
			String username, String password, String driverClass, String driverURL, String databaseName, String savePwd) throws Exception {
		ProfileManager pm = ProfileManager.getInstance();
		IConnectionProfile profile = ProfileManager.getInstance().createProfile(name, description, DEFAULT_MYSQL_PROVIDER_ID,
				generateProperties(driverDefinitionID, defnType, vendor, version, jarList, username, password, driverClass, driverURL, databaseName, savePwd));
		return profile;
	}

	public IConnectionProfile createMysqlProfile(String name, String description, String username, String password, String driverURL, String databaseName, String savePwd)
			throws ConnectionProfileException {
		ProfileManager pm = ProfileManager.getInstance();
		IConnectionProfile profile = ProfileManager.getInstance().createProfile(
				name,
				description,
				DEFAULT_MYSQL_PROVIDER_ID,
				generateProperties(DEFAULT_MYSQL_DRIVER_DEFINITION_ID, DEFAULT_MYSQL_DRIVER_DEFINITION_TYPE, DEFAULT_MYSQL_VENDOR, DEFAULT_MYSQL_VERSION, DEFAULT_MYSQL_JAR_LIST,
						username, password, DEFAULT_MYSQL_DRIVER_CLASS, driverURL, databaseName, savePwd));
		return profile;
	}

	public Properties generateProperties(String driverDefinitionID, String defnType, String vendor, String version, String jarList, String username, String password,
			String driverClass, String driverURL, String databaseName, String savePwd) {
		Properties baseProperties = new Properties();
		baseProperties.setProperty(ConnectionProfileConstants.PROP_DRIVER_DEFINITION_ID, driverDefinitionID);
		baseProperties.setProperty(IDriverMgmtConstants.PROP_DEFN_TYPE, defnType);
		baseProperties.setProperty(IDriverMgmtConstants.PROP_DEFN_JARLIST, jarList);
		baseProperties.setProperty(IJDBCConnectionProfileConstants.DRIVER_CLASS_PROP_ID, driverClass);
		baseProperties.setProperty(IJDBCConnectionProfileConstants.URL_PROP_ID, driverURL);
		baseProperties.setProperty(IJDBCConnectionProfileConstants.USERNAME_PROP_ID, username);
		baseProperties.setProperty(IJDBCConnectionProfileConstants.PASSWORD_PROP_ID, password);
		baseProperties.setProperty(IJDBCConnectionProfileConstants.DATABASE_NAME_PROP_ID, databaseName);
		baseProperties.setProperty(IJDBCConnectionProfileConstants.DATABASE_VENDOR_PROP_ID, vendor);
		baseProperties.setProperty(IJDBCConnectionProfileConstants.DATABASE_VERSION_PROP_ID, version);
		baseProperties.setProperty(IJDBCConnectionProfileConstants.SAVE_PASSWORD_PROP_ID, savePwd);
		return baseProperties;
	}
}
