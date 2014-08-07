package com.ias.webdesigner.designer.builders;

import java.io.File;
import java.io.IOException;

import com.ias.webdesigner.designer.Project;

public abstract class ProjectBuilder {

	public ProjectBuilder() {
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
	public Project buildProject(String name, String path)
			throws IOException, SecurityException {
		File file = FileBuilder.buildFile(path);
		return new Project(name, file);
	}
}
