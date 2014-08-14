package com.ias.webide.plugin.java.util;

import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class JavaClassFormatter {
	CodeFormatter factory;

	public JavaClassFormatter() {
		Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();

		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		factory = ToolFactory.createCodeFormatter(options);
	}

	public String format(String source) throws MalformedTreeException, BadLocationException {
		final TextEdit edit = factory.format(CodeFormatter.K_COMPILATION_UNIT, source, 0, source.length(), 0, System.getProperty("line.separator"));

		IDocument document = new Document(source);
		edit.apply(document);
		return document.get();
	}
}
