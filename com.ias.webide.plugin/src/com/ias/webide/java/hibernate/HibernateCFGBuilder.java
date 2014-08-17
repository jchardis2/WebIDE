package com.ias.webide.java.hibernate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

import org.codehaus.plexus.util.IOUtil;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2x.HibernateConfigurationExporter;

public class HibernateCFGBuilder {
	public static final String HIBERNATE_CONNECTION_DRIVER_CLASS_MYSQL = "com.mysql.jdbc.Driver";
	public static final String HIBERNATE_DIALECT_MYSQL = "com.mysql.jdbc.Driver";

	public static final String DEFAULT_HIBERNATE_CONNECTION_DRIVER_CLASS_MYSQL = HIBERNATE_CONNECTION_DRIVER_CLASS_MYSQL;
	public static final String DEFAULT_HIBERNATE_DIALECT_MYSQL = HIBERNATE_DIALECT_MYSQL;
	public static final String DEFAULT_HIBERNATE_CONNECTION_URL = "jdbc:mysql://localhost:3306/";

	public static void main(String[] args) {
		HibernateCFGBuilder builder = new HibernateCFGBuilder();
		builder.buildCFG("sessionF", "dialect", "dc", "ds", "isas.com", "drov", "jchardis", "123");
	}

	public void buildCFG(String sessionFactoryName, String dialect, String defaultCatalog, String defaultSchema, String connectionURL, String driver, String username,
			String password) {
		final Properties props = new Properties();
		putIfNotNull(props, Environment.SESSION_FACTORY_NAME, sessionFactoryName);
		putIfNotNull(props, Environment.DIALECT, dialect);
		putIfNotNull(props, Environment.DEFAULT_CATALOG, defaultCatalog);
		putIfNotNull(props, Environment.DEFAULT_SCHEMA, defaultSchema);
		putIfNotNull(props, Environment.DRIVER, driver);
		putIfNotNull(props, Environment.URL, connectionURL);
		putIfNotNull(props, Environment.USER, username);
		putIfNotNull(props, Environment.PASS, password);

		StringWriter stringWriter = new StringWriter();
		HibernateConfigurationExporter hce = new HibernateConfigurationExporter();
		hce.setCustomProperties(props);
		hce.setOutput(stringWriter);
		hce.start();
		InputStream inputStream = null;
		try {
			inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes("UTF-8")); //$NON-NLS-1$
		} catch (UnsupportedEncodingException uec) {
			inputStream = new ByteArrayInputStream(stringWriter.toString().getBytes());
		}
		String string;
		try {
			string = IOUtil.toString(inputStream);
			System.out.println(string);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void putIfNotNull(Properties props, String key, String value) {
		if (value != null) {
			props.put(key, value);
		}
	}

}
