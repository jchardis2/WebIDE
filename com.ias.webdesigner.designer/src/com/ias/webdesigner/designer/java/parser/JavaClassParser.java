package com.ias.webdesigner.designer.java.parser;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.ias.webdesigner.designer.java.JavaClass;
import com.ias.webdesigner.designer.tools.FileReader;

public class JavaClassParser {

	/**
	 * http://docs.oracle.com/javase/tutorial/essential/regex/char_classes.html
	 * . Any character (may or may not match line terminators)
	 * 
	 * [abc] a, b, or c (simple class)
	 * 
	 * [^abc] Any character except a, b, or c (negation)
	 * 
	 * [a-zA-Z] a through z, or A through Z, inclusive (range)
	 * 
	 * [a-d[m-p]] a through d, or m through p: [a-dm-p] (union)
	 * 
	 * [a-z&&[def]] d, e, or f (intersection)
	 * 
	 * [a-z&&[^bc]] a through z, except for b and c: [ad-z] (subtraction)
	 * 
	 * [a-z&&[^m-p]] a through z, and not m through p: [a-lq-z] (subtraction
	 * 
	 * \d A digit: [0-9]
	 * 
	 * \D A non-digit: [^0-9]
	 * 
	 * \s A whitespace character: [ \t\n\x0B\f\r]
	 * 
	 * \S A non-whitespace character: [^\s]
	 * 
	 * \w A word character: [a-zA-Z_0-9]
	 * 
	 * \W A non-word character: [^\w]
	 * 
	 * 
	 * Greedy Reluctant Possessive Meaning
	 * 
	 * X? X?? X?+ X, once or not at all
	 * 
	 * X* X*? X*+ X, zero or more times
	 * 
	 * X+ X+? X++ X, one or more times
	 * 
	 * X{n} X{n}? X{n}+ X, exactly n times
	 * 
	 * X{n,} X{n,}? X{n,}+ X, at least n times
	 * 
	 * X{n,m} X{n,m}? X{n,m}+ X, at least n but not more than m times
	 * 
	 * . Any character (may or may not match line terminators) \d A digit: [0-9]
	 * 
	 * \D A non-digit: [^0-9]
	 * 
	 * \s A whitespace character: [ \t\n\x0B\f\r]
	 * 
	 * \S A non-whitespace character: [^\s]
	 * 
	 * \w A word character: [a-zA-Z_0-9]
	 * 
	 * \W A non-word character: [^\w]
	 */

	public static final String REGEX_PACKAGE_START = "\\s*package{1}\\s{1,}";

	public static void main(String args[]) {
		try {
			ParseFilesInDir();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// JavaClassParser classParser = new JavaClassParser();
		// try {
		// classParser.parse("./test.txt");
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	public JavaClassParser() {
		// TODO Auto-generated constructor stub
	}

	public JavaClass parseJava(String path) throws IOException {
		FileReader fileReader = new FileReader();
		String line = fileReader.readLine();
		System.out.println(line);
		return null;
	}

	// use ASTParse to parse string
	public static void parse(String str) {
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		cu.accept(new ASTVisitor() {

			Set names = new HashSet();

			public boolean visit(VariableDeclarationFragment node) {
				SimpleName name = node.getName();
				this.names.add(name.getIdentifier());
				System.out.println("Declaration of '" + name + "' at line"
						+ cu.getLineNumber(name.getStartPosition()));
				return false; // do not continue
			}

			public boolean visit(SimpleName node) {
				if (this.names.contains(node.getIdentifier())) {
					System.out.println("Usage of '" + node + "' at line "
							+ cu.getLineNumber(node.getStartPosition()));
				}
				return true;
			}
		});

	}

	// read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new java.io.FileReader(filePath));

		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}

		reader.close();

		return fileData.toString();
	}

	// loop directory to get file list
	public static void ParseFilesInDir() throws IOException {
		File dirs = new File(".");
//		String dirPath = dirs.getCanonicalPath() + File.separator + "src"
//				+ File.separator;
		
		String dirPath = "/home/jchardis/git/WebIDE/com.ias.webdesigner.designer/src/com/ias/webdesigner/designer/";

		File root = new File(dirPath);
		// System.out.println(rootDir.listFiles());
		File[] files = root.listFiles();
		String filePath = null;

		for (File f : files) {
			filePath = f.getAbsolutePath();
			if (f.isFile()) {
				parse(readFileToString(filePath));
			}else{
				
			}
		}
	}

}
