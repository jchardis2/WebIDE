package com.ias.webide.java.db.mysql.util;

import java.util.ArrayList;

import org.apache.commons.lang.WordUtils;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import com.ias.webide.java.JavaClassBuilder;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

public class JavaBeanGenerator extends JavaClassGenerator {
	protected IJavaProject ijavaProject;

	public JavaBeanGenerator(IJavaProject ijavaProject) {
		super(ijavaProject);
	}

	public void generateAllBeans(IJavaProject javaProject, ArrayList<DatabaseTable> databaseTables, IPackageFragment fragment, String packageName) throws JavaModelException {

		JavaClassBuilder classBuilder = new JavaClassBuilder();
		for (DatabaseTable databaseTable : databaseTables) {
			String className = WordUtils.capitalize(databaseTable.getName() + "Bean");

			classBuilder.buildSimpleJavaClass(packageName, className);
			createClass(databaseTable, fragment, packageName, className, true, true, true);
		}
	}
}
