package com.ias.webide.java;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.ThisExpression;

public class JavaBlockBuilder {

	private AST ast;
	private CompilationUnit compilationUnit;
	private Block block;

	public JavaBlockBuilder(AST ast, CompilationUnit compilationUnit) {
		this.ast = ast;
		this.compilationUnit = compilationUnit;
		block = ast.newBlock();
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
		block = ast.newBlock();
	}

	public Assignment buildAssignmentExpression(Expression lhs, Assignment.Operator operator, Expression rhs) {
		Assignment assignmentExpression = ast.newAssignment();
		assignmentExpression.setLeftHandSide(lhs);
		assignmentExpression.setOperator(Assignment.Operator.ASSIGN);
		assignmentExpression.setRightHandSide(rhs);
		return assignmentExpression;
	}

	public Assignment buildAssignmentFieldExpression(boolean includeThis, Assignment.Operator operator, String sFieldExpression, String sValueExpression) {
		Expression fieldExpression = null;
		if (includeThis) {
			FieldAccess fa = ast.newFieldAccess();
			fa.setExpression(ast.newThisExpression());
			fa.setName(ast.newSimpleName(sFieldExpression));
			fieldExpression = fa;
		} else {
			fieldExpression = ast.newSimpleName(sFieldExpression);
		}
		return buildAssignmentExpression(fieldExpression, operator, ast.newSimpleName(sValueExpression));
	}

	public FieldAccess buildFieldAccess(Expression expression, String name) {
		return buildFieldAccess(expression, ast.newSimpleName(name));
	}

	public FieldAccess buildFieldAccess(Expression expression, SimpleName name) {
		FieldAccess access = ast.newFieldAccess();
		access.setExpression(expression);
		access.setName(name);
		return access;
	}

	public boolean addStatement(Expression statement) {
		return block.statements().add((ast.newExpressionStatement(statement)));
	}

	/**
	 * 
	 * @param expression
	 *            the expression to be returned
	 * @return
	 */
	public boolean addReturnStatement(Expression expression) {
		ReturnStatement returnStatement = ast.newReturnStatement();
		returnStatement.setExpression(expression);
		return block.statements().add(returnStatement);
	}

	public void buildMethodInvocation(String name, String simpleName) {
		buildMethodInvocation(ast.newSimpleName(name), ast.newSimpleName(simpleName));
	}

	public void buildMethodInvocation(Name name, SimpleName simpleName) {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		methodInvocation.setExpression(name);
		methodInvocation.setName(simpleName);
	}

	public void addMethodBlockDemo() {
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		QualifiedName name = ast.newQualifiedName(ast.newSimpleName("System"), ast.newSimpleName("out"));
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
		ExpressionStatement expressionStatement = ast.newExpressionStatement(methodInvocation);
		block.statements().add(expressionStatement);
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

	public Block getBlock() {
		return block;
	}

	public void setBlock(Block block) {
		this.block = block;
	}

}
