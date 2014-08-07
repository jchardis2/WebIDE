package com.ias.webdesigner.designer;

import java.io.File;

public class Project {
	private String name;
	private File file;

	public Project() {
	}

	public Project(String name, File file) {
		this.name = name;
		this.file = file;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
