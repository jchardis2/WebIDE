package com.ias.webdesigner.designer.java.builders;

import java.io.IOException;

import com.ias.webdesigner.designer.builders.ProjectBuilder;
import com.ias.webdesigner.designer.java.JavaProject;

public class JavaProjectBuilder extends ProjectBuilder {

	public JavaProjectBuilder() {
		super();
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
	@Override
	public JavaProject buildProject(String name, String path)
			throws IOException, SecurityException {
		return (JavaProject) super.buildProject(name, path);
	}
}
