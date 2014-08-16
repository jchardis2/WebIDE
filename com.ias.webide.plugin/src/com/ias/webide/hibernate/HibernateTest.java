package com.ias.webide.hibernate;

import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.cfg.reveng.JDBCReader;
import org.hibernate.cfg.reveng.ReverseEngineeringRuntimeInfo;
import org.hibernate.cfg.reveng.dialect.JDBCMetaDataDialect;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences;
import org.hibernate.eclipse.HibernatePlugin;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.workbench.LazyDatabaseSchema;
import org.hibernate.eclipse.console.workbench.LazyDatabaseSchemaWorkbenchAdapter;

public class HibernateTest {

	public static void main(String[] args) {
		HibernatePlugin hibernatePlugin = org.hibernate.eclipse.HibernatePlugin.getDefault();
		ConsoleConfiguration configurations[] = 	KnownConfigurations.getInstance().getConfigurations();
//		ConsoleConfigurationPreferences configurationPreferences = 
//		ConsoleConfiguration ccfg = new ConsoleConfiguration(config);
//		LazyDatabaseSchema databaseSchema = new LazyDatabaseSchema(ccfg);
		
		org.hibernate.cfg.reveng.dialect.JDBCMetaDataDialect dataDialect = new JDBCMetaDataDialect();
		org.hibernate.cfg.reveng.DefaultDatabaseCollector collector = new DefaultDatabaseCollector(dataDialect);
	}
}
