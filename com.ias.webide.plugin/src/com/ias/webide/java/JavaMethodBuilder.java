package com.ias.webide.java;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.VariableDeclaration;

public class JavaMethodBuilder {
	private AST ast;
	private CompilationUnit compilationUnit;
	private MethodDeclaration methodDeclaration;

	public JavaMethodBuilder(AST ast, CompilationUnit compilationUnit) {
		this.ast = ast;
		this.compilationUnit = compilationUnit;
		methodDeclaration = ast.newMethodDeclaration();
	}

	/**
	 * Called to reinitialize the class for reuse
	 * 
	 * @param ast
	 * @param compilationUnit
	 */
	public void init(AST ast, CompilationUnit compilationUnit) {
		this.ast = ast;
		this.compilationUnit = compilationUnit;
		methodDeclaration = ast.newMethodDeclaration();
	}
	

	/**
	 * Sets the return type of the method
	 * 
	 * @param returnType
	 *            ex: String
	 */
	public void setReturnType(String returnType) {
		setReturnType(ast.newSimpleType(ast.newSimpleName(returnType)));
	}

	/**
	 * Sets the return type of the method
	 * 
	 * @param returnType
	 *            ex: ast.newType("String")
	 */
	public void setReturnType(Type returnType) {
		methodDeclaration.setReturnType2(returnType);
	}

	/**
	 * Sets the return type of the method
	 * 
	 * @param primitiveCode
	 *            ex: PrimitiveType.VOID
	 */
	public void setPrimitiveReturnType(PrimitiveType.Code primitiveCode) {
		setReturnType(ast.newPrimitiveType(primitiveCode));
	}

	/**
	 * Sets whether this is a constructor or not
	 * 
	 * @param constructor
	 */
	public void setConstructor(boolean constructor) {
		methodDeclaration.setConstructor(constructor);
	}

	public void setMethodName(String methodName) {
		setMethodName(ast.newSimpleName(methodName));
	}

	public void setMethodName(SimpleName methodName) {
		methodDeclaration.setName(methodName);
	}

	public void addKeyword(Modifier.ModifierKeyword modifierKeyword) {
		methodDeclaration.modifiers().add(ast.newModifier(modifierKeyword));
	}

	public void addKeywords(Collection<Modifier> modifierKeywords) {
		methodDeclaration.modifiers().addAll(modifierKeywords);
	}

	public void setBody(Block block) {
		methodDeclaration.setBody(block);
	}

	public void addVariableDeclaration(VariableDeclaration variableDeclaration) {
		methodDeclaration.parameters().add(variableDeclaration);
	}

	public void addVariableDeclaration(String name, String type) {
		addVariableDeclaration(ast.newSimpleName(name), ast.newSimpleType(ast.newName(type)));
	}

	public void addVariableDeclaration(SimpleName name, Type type) {
		SingleVariableDeclaration variableDeclaration = ast.newSingleVariableDeclaration();
		variableDeclaration.setType(type);
		variableDeclaration.setName(name);
		addVariableDeclaration(variableDeclaration);
	}

	public MethodDeclaration buildSimpleMethod(boolean constructor, String methodName, ArrayList<Modifier> modifiers, String returnType, Block block) {
		methodDeclaration.setBody(block);
		return buildSimpleMethod(constructor, methodName, modifiers, returnType);
	}

	public MethodDeclaration buildSimpleMethod(boolean constructor, String methodName, ArrayList<Modifier> modifiers, String returnType) {
		setConstructor(constructor);
		setMethodName(methodName);
		addKeywords(modifiers);
		setReturnType(returnType);
		return methodDeclaration;
	}

	public MethodDeclaration buildSimpleMethod(boolean constructor, String methodName, Modifier.ModifierKeyword modifierKeyword, String returnType, Block block) {
		methodDeclaration.setBody(block);
		return buildSimpleMethod(constructor, methodName, modifierKeyword, returnType);
	}

	public MethodDeclaration buildSimpleMethod(boolean constructor, String methodName, Modifier.ModifierKeyword modifierKeyword, String returnType) {
		setConstructor(constructor);
		setMethodName(methodName);
		addKeyword(modifierKeyword);
		setReturnType(returnType);
		return methodDeclaration;
	}

	public MethodDeclaration buildSimpleMethod(boolean constructor, String methodName, ArrayList<Modifier> modifiers, Type returnType, Block block) {
		methodDeclaration.setBody(block);
		return buildSimpleMethod(constructor, methodName, modifiers, returnType);
	}

	public MethodDeclaration buildSimpleMethod(boolean constructor, String methodName, ArrayList<Modifier> modifiers, Type returnType) {
		setConstructor(constructor);
		setMethodName(methodName);
		addKeywords(modifiers);
		setReturnType(returnType);
		return methodDeclaration;
	}

	public MethodDeclaration buildSimpleMethod(boolean constructor, String methodName, Modifier.ModifierKeyword modifierKeyword, Type returnType, Block block) {
		methodDeclaration.setBody(block);
		return buildSimpleMethod(constructor, methodName, modifierKeyword, returnType);
	}

	public MethodDeclaration buildSimpleMethod(boolean constructor, String methodName, Modifier.ModifierKeyword modifierKeyword, Type returnType) {
		setConstructor(constructor);
		setMethodName(methodName);
		addKeyword(modifierKeyword);
		setReturnType(returnType);
		return methodDeclaration;
	}

	public AST getAst() {
		return ast;
	}

	public void setAst(AST ast) {
		this.ast = ast;
	}

	public CompilationUnit getCompilationUnit() {
		return compilationUnit;
	}

	public void setCompilationUnit(CompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
	}

	public MethodDeclaration getMethodDeclaration() {
		return methodDeclaration;
	}

	public void setMethodDeclaration(MethodDeclaration methodDeclaration) {
		this.methodDeclaration = methodDeclaration;
	}
}
