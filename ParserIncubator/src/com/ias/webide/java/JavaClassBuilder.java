package com.ias.webide.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.ias.webide.java.tools.ParameterNameValuePair;

public class JavaClassBuilder {
	private AST ast;
	private CompilationUnit unit;
	private TypeDeclaration classType;// defines the modifiers

	public JavaClassBuilder() {
		ast = AST.newAST(AST.JLS3);
		unit = ast.newCompilationUnit();
		classType = ast.newTypeDeclaration();
		classType.setInterface(false);
	}

	public void setPackage(String packageName) {
		String packageArray[] = packageName.split("\\.");
		Name name;
		if (packageArray.length == 1) {
			name = ast.newSimpleName(packageName);
		} else {
			name = ast.newSimpleName(packageArray[0]);
			for (int i = 1; i < packageArray.length; i++) {
				name = ast.newQualifiedName(name,
						ast.newSimpleName(packageArray[i]));
			}
		}
		PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
		packageDeclaration.setName(name);
		unit.setPackage(packageDeclaration);
	}

	/**
	 * Adds a single import to the class
	 * 
	 * @param qualifier
	 *            The package of the imported class
	 * @param simpleName
	 *            The name of the imported class
	 * @return true if successfully added
	 */
	public boolean addImport(String importName, boolean onDemand) {
		ImportDeclaration importDeclaration = ast.newImportDeclaration();
		String packageArray[] = importName.split("\\.");
		Name name;
		if (packageArray.length == 1) {
			name = ast.newSimpleName(importName);
		} else {
			name = ast.newSimpleName(packageArray[0]);
			for (int i = 1; i < packageArray.length; i++) {
				name = ast.newQualifiedName(name,
						ast.newSimpleName(packageArray[i]));
			}
		}
		// ast.newSimpleName("java"),ast.newSimpleName("util"));
		importDeclaration.setName(name);
		importDeclaration.setOnDemand(onDemand);
		return unit.imports().add(importDeclaration);
	}

	public void setIsInterface(boolean isInterface) {
		classType.setInterface(isInterface);
	}

	/**
	 * 
	 * @param keyword
	 *            a modifier keyword ex: ModifierKeyword.
	 * @return ModifierKeyword.PUBLIC_KEYWORD
	 */
	public boolean addClassModifier(ModifierKeyword keyword) {
		return classType.modifiers().add(ast.newModifier(keyword));
	}

	/**
	 * Set the name of the class
	 * 
	 * @param name
	 *            of the class
	 */
	public void setClassName(String name) {
		classType.setName(ast.newSimpleName(name));
	}

	/**
	 * 
	 * @param modifierKeywords
	 *            ex: ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD)
	 */
	public void addConstructor(ArrayList<ModifierKeyword> modifierKeywords) {
		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		methodDeclaration.setConstructor(true);
		// methodDeclaration.setName(ast.newSimpleName(constructorName));
		methodDeclaration.modifiers().addAll(modifierKeywords);
	}

	/**
	 * 
	 * @param methodName
	 *            the name of the method
	 * @param modifierKeywords
	 *            ex: ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD)
	 * @param returnType
	 *            the return type of the method ex:
	 *            ast.newPrimitiveType(PrimitiveType.VOID));
	 * @return the method that was created
	 */
	public MethodDeclaration buildMethod(String methodName,
			ArrayList<ModifierKeyword> modifierKeywords, Type returnType) {
		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		methodDeclaration.setConstructor(false);
		methodDeclaration.setName(ast.newSimpleName(methodName));
		methodDeclaration.modifiers().addAll(modifierKeywords);
		methodDeclaration.setReturnType2(returnType);
		// ast.newPrimitiveType(PrimitiveType.VOID));

		return methodDeclaration;

	}

	/**
	 * 
	 * @param methodName
	 *            the name of the method
	 * @param modifierKeywords
	 *            ex: ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD)
	 * @param returnType
	 *            the return type of the method ex:
	 *            ast.newPrimitiveType(PrimitiveType.VOID));
	 * @param parameterNVPList
	 *            the list of types and names of the parameters to add
	 * @return the method that was created
	 */
	public MethodDeclaration buildMethod(String methodName,
			ArrayList<ModifierKeyword> modifierKeywords, Type returnType,
			List<ParameterNameValuePair> parameterNVPList) {
		MethodDeclaration methodDeclaration = buildMethod(methodName,
				modifierKeywords, returnType);

		// set the variables
		for (ParameterNameValuePair parameterNVP : parameterNVPList) {
			SingleVariableDeclaration variableDeclaration = ast
					.newSingleVariableDeclaration();
			variableDeclaration.setType(parameterNVP.getType());
			variableDeclaration.setName(ast.newSimpleName(parameterNVP
					.getName()));
			methodDeclaration.parameters().add(variableDeclaration);
		}
		return methodDeclaration;

	}

	public void addMethodBlock(MethodDeclaration methodDeclaration) {
		org.eclipse.jdt.core.dom.Block block = ast.newBlock();
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		QualifiedName name = ast.newQualifiedName(ast.newSimpleName("System"),
				ast.newSimpleName("out"));
		methodInvocation.setExpression(name);
		methodInvocation.setName(ast.newSimpleName("println"));
		InfixExpression infixExpression = ast.newInfixExpression();
		infixExpression.setOperator(InfixExpression.Operator.PLUS);
		StringLiteral literal = ast.newStringLiteral();
		literal.setLiteralValue("Hello");
		infixExpression.setLeftOperand(literal);
		literal = ast.newStringLiteral();
		literal.setLiteralValue(" world");
		infixExpression.setRightOperand(literal);
		methodInvocation.arguments().add(infixExpression);
		ExpressionStatement expressionStatement = ast
				.newExpressionStatement(methodInvocation);
		block.statements().add(expressionStatement);
		methodDeclaration.setBody(block);
	}

	public boolean addMethod(MethodDeclaration methodDeclaration) {
		return classType.bodyDeclarations().add(methodDeclaration);
	}

	public String generateClass() {
		unit.types().add(classType);
		return unit.toString();
	}

	public AST getAst() {
		return ast;
	}

	public void setAst(AST ast) {
		this.ast = ast;
	}

	public CompilationUnit getUnit() {
		return unit;
	}

	public void setUnit(CompilationUnit unit) {
		this.unit = unit;
	}

	public TypeDeclaration getClassType() {
		return classType;
	}

	public void setClassType(TypeDeclaration classType) {
		this.classType = classType;
	}

}
