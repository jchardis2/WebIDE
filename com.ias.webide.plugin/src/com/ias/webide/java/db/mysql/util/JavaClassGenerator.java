package com.ias.webide.java.db.mysql.util;

import java.util.List;

import org.apache.commons.lang.WordUtils;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Assignment.Operator;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.ThisExpression;

import com.ias.webide.db.util.AbstractBeanGenerator;
import com.ias.webide.java.JavaClassBuilder;
import com.infinityappsolutions.webdesigner.tools.database.ColumnTypeMap;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseColumn;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

public class JavaClassGenerator extends AbstractBeanGenerator {
	protected JavaClassBuilder javaClassBuilder;
	protected IJavaProject iJavaProject;
	private IPackageFragment fragment;
	private CompilationUnit cu;
	private ColumnTypeMap columnTypeMap = new ColumnTypeMap();

	public JavaClassGenerator(IJavaProject iJavaProject, IPackageFragment fragment) {
		this.iJavaProject = iJavaProject;
		javaClassBuilder = new JavaClassBuilder();
		this.fragment = fragment;
		cu = javaClassBuilder.getCompilationUnit();
	}

	//
	// public void createClass(DatabaseTable databaseTable, IPackageFragment
	// fragment, String packageName, String className) throws JavaModelException
	// {
	// // init code string and create compilation unit
	// String str = "package " + fragment.getElementName() + ";\n" +
	// "public class " + className + "  {" + "\n" + "}";
	//
	// cu = fragment.createCompilationUnit(className + ".java", str, false,
	// null);
	//
	// }

	public void createClass(DatabaseTable databaseTable, String packageName, String className, boolean createConstructors, boolean createFields, boolean createGettersAndSetters)
			throws JavaModelException {
		javaClassBuilder.buildSimpleJavaClass(packageName, className);
		if (createGettersAndSetters) {
			createGettersAndSetters(databaseTable, className);
		}
		javaClassBuilder.generateClass();
		// createClass(databaseTable, fragment, packageName, className);
		// String fieldsForConstructor = "";
		// if (createFields) {
		// fieldsForConstructor = createFields(databaseTable, className);
		// }
		// if (createConstructors) {
		// createConstructor(className);
		// createConstructorUsingFields(className, fieldsForConstructor);
		// }
		// if (createFields && createConstructors) {
		// createGettersAndSetters(databaseTable, className);
		// }

	}

	private void createGettersAndSetters(DatabaseTable databaseTable, String className) throws JavaModelException {
		for (DatabaseColumn databaseColumn : databaseTable.getColumns()) {
			String returnType = columnTypeMap.getClass(databaseColumn.getType()).getSimpleName();
			String name = WordUtils.capitalize(databaseColumn.getName());
			AST ast = javaClassBuilder.getAst();
			SimpleType simpleType = ast.newSimpleType(ast.newName(name));

			// create getter
			MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
			methodDeclaration.setConstructor(false);
			List modifiers = methodDeclaration.modifiers();
			modifiers.add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
			methodDeclaration.setName(ast.newSimpleName("set" + name));
			methodDeclaration.setReturnType2(ast.newPrimitiveType(PrimitiveType.VOID));

			SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
			variableDeclaration.setType(ast.newSimpleType(ast.newName(returnType)));
			variableDeclaration.setName(ast.newSimpleName(databaseColumn.getName()));
			methodDeclaration.parameters().add(variableDeclaration);

			Block block = ast.newBlock();
			MethodInvocation methodInvocation = ast.newMethodInvocation();
			ThisExpression thisExpression = ast.newThisExpression();
			FieldAccess fa = ast.newFieldAccess();
			fa.setExpression(thisExpression);
			fa.setName(ast.newSimpleName(databaseColumn.getName()));
			
			Assignment assignmentExpression = ast.newAssignment();
			assignmentExpression.setLeftHandSide(fa);
			assignmentExpression.setOperator(Assignment.Operator.ASSIGN);
			assignmentExpression.setRightHandSide(ast.newSimpleName(databaseColumn.getName()));
			block.statements().add((ast.newExpressionStatement(assignmentExpression)));
			methodDeclaration.setBody(block);
			if (!javaClassBuilder.addMethod(methodDeclaration))
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
