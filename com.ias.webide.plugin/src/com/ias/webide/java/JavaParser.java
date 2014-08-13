package com.ias.webide.java;

import java.io.IOException;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.ias.webide.java.tools.FileReader;

public class JavaParser {
	private AST ast;
	private CompilationUnit unit;
	private TypeDeclaration classType;// defines the modifiers

	public static void main(String args[]) {
		JavaParser javaParser = new JavaParser();
		FileReader fileReader = new FileReader();
		String classContent;
		try {
			classContent = fileReader
					.getContent("src/com/ias/webide/java/JavaParser.java");
			javaParser.init(classContent, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public JavaParser() {
	}

	public void init(String classContent, boolean resolveBindings) {

		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		astParser.setSource(classContent.toCharArray());
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);
		unit = (CompilationUnit) astParser.createAST(null);
		System.out.println(unit.toString());
	}

}
