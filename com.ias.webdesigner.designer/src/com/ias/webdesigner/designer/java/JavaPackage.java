package com.ias.webdesigner.designer.java;

import java.io.File;

public class JavaPackage {
	String name;
	JavaProject project;
	String sourceDir;

	public JavaPackage(String name, JavaProject project, String sourceDir) {
		super();
		this.name = name;
		this.project = project;
		this.sourceDir = sourceDir;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public JavaProject getProject() {
		return project;
	}

	public void setProject(JavaProject project) {
		this.project = project;
	}

	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

}
