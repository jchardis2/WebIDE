package com.infinityappsolutions.webdesigner.eclipse.actions.java;

import java.util.ArrayList;

import org.apache.commons.lang.WordUtils;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

public class BeanGenerator extends ClassGenerator {

	public BeanGenerator() {
	}

	public void generateAllBeans(IJavaProject javaProject, ArrayList<DatabaseTable> databaseTables, IPackageFragment fragment, String packageName) throws JavaModelException {
		for (DatabaseTable databaseTable : databaseTables) {
			String className = WordUtils.capitalize(databaseTable.getName() + "Bean");
			createClass(javaProject, databaseTable, fragment, packageName, className, true, true, true);
		}

	}

}
