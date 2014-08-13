package com.ias.webide.java.db.mysql.util;

import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;

import com.ias.webide.java.JavaClassBuilder;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

public class JavaBeanGenerator extends JavaClassGenerator {
	protected IJavaProject ijavaProject;

	public JavaBeanGenerator(IJavaProject ijavaProject, IPackageFragment fragment) {
		super(ijavaProject, fragment);
	}

	public void generateAllBeans(List<DatabaseTable> databaseTables, String packageName, String className, boolean createConstructors, boolean createFields,
			boolean createGettersAndSetters) throws JavaModelException {
		JavaClassBuilder classBuilder = new JavaClassBuilder();
		for (DatabaseTable databaseTable : databaseTables) {
			className = WordUtils.capitalize(databaseTable.getName() + "Bean");

			classBuilder.buildSimpleJavaClass(packageName, className);
			createClass(databaseTable, packageName, className, true, true, true);
		}
	}
}
