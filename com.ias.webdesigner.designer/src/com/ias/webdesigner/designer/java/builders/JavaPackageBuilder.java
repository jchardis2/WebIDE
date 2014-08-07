package com.ias.webdesigner.designer.java.builders;

import java.io.IOException;

import com.ias.webdesigner.designer.builders.FileBuilder;
import com.ias.webdesigner.designer.java.JavaPackage;
import com.ias.webdesigner.designer.java.JavaProject;

public class JavaPackageBuilder {
	private JavaProject javaProject;

	public JavaPackageBuilder(JavaProject javaProject) {
		this.javaProject = javaProject;
	}

	/**
	 * @return
	 * @throws IOException
	 *             - If an I/O error occurred
	 * 
	 * @throws SecurityException
	 *             - If a security manager exists and its
	 *             SecurityManager.checkWrite(java.lang.String) method denies
	 *             write access to the
	 */
	public JavaPackage buildPackage(String packageName, String srcDir)
			throws IOException, SecurityException {
		FileBuilder.buildDir(javaProject.getFile().getPath() + srcDir
				+ packageName);
		return new JavaPackage(packageName, javaProject, srcDir);
	}
}
