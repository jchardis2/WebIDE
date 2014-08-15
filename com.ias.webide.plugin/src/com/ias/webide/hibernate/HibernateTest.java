package com.ias.webide.hibernate;

import org.hibernate.cfg.reveng.DefaultDatabaseCollector;
import org.hibernate.cfg.reveng.dialect.JDBCMetaDataDialect;
import org.hibernate.eclipse.HibernatePlugin;

public class HibernateTest {

	public void main(String[] args) {
		HibernatePlugin hibernatePlugin = org.hibernate.eclipse.HibernatePlugin.getDefault();
		// org.hibernate.cfg.reveng.
		org.hibernate.cfg.reveng.dialect.JDBCMetaDataDialect dataDialect = new JDBCMetaDataDialect();
		org.hibernate.cfg.reveng.DefaultDatabaseCollector collector = new DefaultDatabaseCollector(dataDialect);
	}

}
