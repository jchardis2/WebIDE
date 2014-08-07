package com.ias.webide.java;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;

public class JavaBlockBuilder {

	private AST ast;
	private CompilationUnit unit;
	private Block block;

	public JavaBlockBuilder(AST ast, CompilationUnit unit) {
		this.ast = ast;
		this.unit = unit;
		block = ast.newBlock();
	}

	public void buildMethodInvocation(String name, String simpleName) {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		buildMethodInvocation(ast.newSimpleName(name),
				ast.newSimpleName(simpleName));
	}

	public void buildMethodInvocation(Name name, SimpleName simpleName) {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(name);
		methodInvocation.setName(simpleName);
	}

	public void addMethodBlockDemo() {
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
	}

}
