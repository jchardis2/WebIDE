package com.infinityappsolutions.webdesigner.eclipse.actions.java;

import org.apache.commons.lang.WordUtils;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import com.infinityappsolutions.webdesigner.tools.database.ColumnTypeMap;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseColumn;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

public class ClassGenerator {
	private ICompilationUnit cu;
	private ColumnTypeMap columnTypeMap = new ColumnTypeMap();

	public ClassGenerator() {
		// this.className = WordUtils.capitalize(databaseTable.getName() +
		// "Bean");
	}

	public void createClass(IJavaProject javaProject, DatabaseTable databaseTable, IPackageFragment fragment, String packageName, String className) throws JavaModelException {
		// init code string and create compilation unit
		String str = "package " + fragment.getElementName() + ";\n" + "public class " + className + "  {" + "\n" + "}";

		cu = fragment.createCompilationUnit(className + ".java", str, false, null);

	}

	public void createClass(IJavaProject javaProject, DatabaseTable databaseTable, IPackageFragment fragment, String packageName, String className, boolean createConstructors, boolean createFields,
			boolean createGettersAndSetters) throws JavaModelException {
		createClass(javaProject, databaseTable, fragment, packageName, className);
		String fieldsForConstructor = "";
		if (createFields) {
			fieldsForConstructor = createFields(databaseTable, className);
		}
		if (createConstructors) {
			createConstructor(className);
			createConstructorUsingFields(className, fieldsForConstructor);
		}
		if (createFields && createConstructors) {
			createGettersAndSetters(databaseTable, className);
		}

	}

	private void createGettersAndSetters(DatabaseTable databaseTable, String className) throws JavaModelException {
		for (DatabaseColumn databaseColumn : databaseTable.getColumns()) {
			Class<?> newCLass = columnTypeMap.getClass(databaseColumn.getType());
			// create a field
			String name = WordUtils.capitalize(databaseColumn.getName());
			IType type = cu.getType(className);
			String getter = "public " + newCLass.getSimpleName() + " get" + name + "(){return " + databaseColumn.getName() + ";}";
			System.out.println("Creating Method: " + getter);
			type.createMethod(getter, null, false, null);

			String setter = "public void set" + name + "(" + newCLass.getSimpleName() + " " + databaseColumn.getName() + "){this. " + databaseColumn.getName() + "=" + databaseColumn.getName() + ";}";
			System.out.println("Creating Method: " + getter);
			type.createMethod(setter, null, false, null);
		}

	}

	private void createConstructorUsingFields(String className, String fieldsForConstructor) throws JavaModelException {
		IType type = cu.getType(className);
		if (!fieldsForConstructor.equals("")) {
			type.createMethod("public " + className + "(" + fieldsForConstructor + "){}", null, true, null);
		}
	}

	private void createConstructor(String className) throws JavaModelException {
		System.out.println(className);
		IType type = cu.getType(className);
		System.out.println("public " + className + "(){}");
		type.createMethod("public " + className + "(){}", null, false, null);
	}

	private String createFields(DatabaseTable databaseTable, String className) throws JavaModelException {
		String fieldsForConstructor = "";
		for (DatabaseColumn databaseColumn : databaseTable.getColumns()) {
			Class<?> newCLass = columnTypeMap.getClass(databaseColumn.getType());
			// create a field
			IType type = cu.getType(className);
			System.out.println(className);
			System.out.println("++++++++++++++++++");
			System.out.println("Type: " + databaseColumn.getType());
			System.out.println("++++++++++++++++++");
			String field = "private " + newCLass.getSimpleName() + " " + databaseColumn.getName() + ";";
			System.out.println("Creating Field: " + field);
			fieldsForConstructor += ", " + newCLass.getSimpleName() + " " + databaseColumn.getName();
			type.createField(field, null, true, null);
		}
		return fieldsForConstructor.replaceFirst(",", "");
	}
}
