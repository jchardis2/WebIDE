import java.util.List;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Modifier.ModifierKeyword;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.QualifiedName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.ias.webide.java.JavaClassBuilder;

public class Main {
	public static void main(String argsp[]) {
		JavaClassBuilder builder = new JavaClassBuilder();
		builder.setPackage("com.stfu.stfu2.stfu3");
		builder.setClassName("JimmyRocks");
		builder.addImport("com.AwesomeImport", false);
		builder.addClassModifier(ModifierKeyword.PUBLIC_KEYWORD);
//		builder.buildMethod(methodName, modifierKeywords, returnType)
		System.out.println(builder.generateClass());
	}

	public void EclipseDemo() {
		AST ast = AST.newAST(AST.JLS4);
		CompilationUnit unit = ast.newCompilationUnit();

		PackageDeclaration packageDeclaration = ast.newPackageDeclaration();
		packageDeclaration.setName(ast.newSimpleName("example"));
		unit.setPackage(packageDeclaration);

		ImportDeclaration importDeclaration = ast.newImportDeclaration();
		QualifiedName name = ast.newQualifiedName(ast.newSimpleName("java"),
				ast.newSimpleName("util"));
		importDeclaration.setName(name);
		importDeclaration.setOnDemand(true);
		unit.imports().add(importDeclaration);

		TypeDeclaration type = ast.newTypeDeclaration();
		type.setInterface(false);
		type.modifiers().add(
				ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		type.setName(ast.newSimpleName("HelloWorld"));

		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
		methodDeclaration.setConstructor(false);
		List modifiers = methodDeclaration.modifiers();
		modifiers.add(ast.newModifier(Modifier.ModifierKeyword.PUBLIC_KEYWORD));
		modifiers.add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
		methodDeclaration.setName(ast.newSimpleName("main"));
		methodDeclaration.setReturnType2(ast
				.newPrimitiveType(PrimitiveType.VOID));
		SingleVariableDeclaration variableDeclaration = ast
				.newSingleVariableDeclaration();
		variableDeclaration.setType(ast.newArrayType(ast.newSimpleType(ast
				.newSimpleName("String"))));
		variableDeclaration.setName(ast.newSimpleName("args"));
		methodDeclaration.parameters().add(variableDeclaration);

		org.eclipse.jdt.core.dom.Block block = ast.newBlock();
		MethodInvocation methodInvocation = ast.newMethodInvocation();
		name = ast.newQualifiedName(ast.newSimpleName("System"),
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

		type.bodyDeclarations().add(methodDeclaration);

		unit.types().add(type);

		System.out.println(unit.toString());
	}
}
