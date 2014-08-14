package com.ias.webide.java.db.mysql.util;

import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.ThisExpression;

import com.ias.webide.db.util.AbstractBeanGenerator;
import com.ias.webide.java.JavaBlockBuilder;
import com.ias.webide.java.JavaClassBuilder;
import com.ias.webide.java.JavaMethodBuilder;
import com.infinityappsolutions.webdesigner.tools.database.ColumnTypeMap;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseColumn;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

public class JavaClassGenerator extends AbstractBeanGenerator {
	protected JavaClassBuilder javaClassBuilder;
	protected IJavaProject iJavaProject;
	private IPackageFragment fragment;
	private AST ast;
	private CompilationUnit cu;
	private ColumnTypeMap columnTypeMap = new ColumnTypeMap();

	public JavaClassGenerator(IJavaProject iJavaProject, IPackageFragment fragment) {
		this.iJavaProject = iJavaProject;
		javaClassBuilder = new JavaClassBuilder();
		this.fragment = fragment;
		ast = javaClassBuilder.getAst();
		cu = javaClassBuilder.getCompilationUnit();
	}

	public void createClass(DatabaseTable databaseTable, String packageName, String className, boolean createConstructors, boolean createFields, boolean createGettersAndSetters)
			throws JavaModelException {
		javaClassBuilder.buildSimpleJavaClass(packageName, className);
		if (createFields) {
			createFields(databaseTable);
			if (createGettersAndSetters) {
				createGettersAndSetters(databaseTable);
			}
		}
		javaClassBuilder.generateClass();
	}

	public void createFields(DatabaseTable databaseTable) {
		for (DatabaseColumn databaseColumn : databaseTable.getColumns()) {
			String type = columnTypeMap.getClass(databaseColumn.getType()).getSimpleName();
			javaClassBuilder.addField(Modifier.ModifierKeyword.PRIVATE_KEYWORD, databaseColumn.getName(), type);
		}
	}

	private void createGettersAndSetters(DatabaseTable databaseTable) throws JavaModelException {
		
		JavaMethodBuilder javaMethodBuilder = null;
		JavaBlockBuilder blockBuilder = null;
		for (DatabaseColumn databaseColumn : databaseTable.getColumns()) {
			String returnType = columnTypeMap.getClass(databaseColumn.getType()).getSimpleName();
			String methodName = WordUtils.capitalize(databaseColumn.getName());
			String fieldName = databaseColumn.getName();

			javaMethodBuilder = new JavaMethodBuilder(ast, cu);
			blockBuilder = new JavaBlockBuilder(ast, cu);

			// create setter
			javaMethodBuilder.buildSimpleMethod(false, "set" + methodName, Modifier.ModifierKeyword.PUBLIC_KEYWORD, ast.newPrimitiveType(PrimitiveType.VOID));
			javaMethodBuilder.addVariableDeclaration(fieldName, returnType);
			FieldAccess fa = blockBuilder.buildFieldAccess(ast.newThisExpression(), fieldName);
			Assignment assignmentExpression = blockBuilder.buildAssignmentExpression(fa, Assignment.Operator.ASSIGN, ast.newSimpleName(fieldName));
			if (!blockBuilder.addStatement(assignmentExpression))
				throw new JavaModelException(new Exception(), 0);
			javaMethodBuilder.setBody(blockBuilder.getBlock());
			if (!javaClassBuilder.addMethod(javaMethodBuilder.getMethodDeclaration()))
				throw new JavaModelException(new Exception(), 0);

			// create getter
			javaMethodBuilder.init(ast, cu);
			blockBuilder.init(ast, cu);
			javaMethodBuilder.buildSimpleMethod(false, "get" + methodName, Modifier.ModifierKeyword.PUBLIC_KEYWORD, returnType);
			blockBuilder.addReturnStatement(ast.newSimpleName(fieldName));
			javaMethodBuilder.setBody(blockBuilder.getBlock());
			if (!javaClassBuilder.addMethod(javaMethodBuilder.getMethodDeclaration()))
				throw new JavaModelException(new Exception(), 0);
		}

	}

	public CompilationUnit getCu() {
		return cu;
	}

	public void setCu(CompilationUnit cu) {
		this.cu = cu;
	}

	// private void createConstructorUsingFields(String className, String
	// fieldsForConstructor) throws JavaModelException {
	// IType type = cu.getType(className);
	// if (!fieldsForConstructor.equals("")) {
	// type.createMethod("public " + className + "(" + fieldsForConstructor +
	// "){}", null, true, null);
	// }
	// }
	//
	// private void createConstructor(String className) throws
	// JavaModelException {
	// System.out.println(className);
	// IType type = cu.getType(className);
	// System.out.println("public " + className + "(){}");
	// type.createMethod("public " + className + "(){}", null, false, null);
	// }
	//
	// private String createFields(DatabaseTable databaseTable, String
	// className) throws JavaModelException {
	// String fieldsForConstructor = "";
	// for (DatabaseColumn databaseColumn : databaseTable.getColumns()) {
	// Class<?> newCLass = columnTypeMap.getClass(databaseColumn.getType());
	// // create a field
	// IType type = cu.getType(className);
	// System.out.println(className);
	// System.out.println("++++++++++++++++++");
	// System.out.println("Type: " + databaseColumn.getType());
	// System.out.println("++++++++++++++++++");
	// String field = "private " + newCLass.getSimpleName() + " " +
	// databaseColumn.getName() + ";";
	// System.out.println("Creating Field: " + field);
	// fieldsForConstructor += ", " + newCLass.getSimpleName() + " " +
	// databaseColumn.getName();
	// type.createField(field, null, true, null);
	// }
	// return fieldsForConstructor.replaceFirst(",", "");
	// }

}
