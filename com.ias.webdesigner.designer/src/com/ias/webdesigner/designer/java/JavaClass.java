package com.ias.webdesigner.designer.java;

import java.io.File;
import java.util.ArrayList;

import com.ias.webdesigner.designer.Field;

public class JavaClass {
	private String name;
	private File classFile;
	private String packageName;
	private ArrayList<String> importArrayList;
	private ArrayList<JavaMethod> methodArrayList;
	private ArrayList<Field> fieldArrayList;

	public JavaClass(String name, File classFile, String packageName,
			ArrayList<String> importArrayList,
			ArrayList<JavaMethod> methodArrayList,
			ArrayList<Field> fieldArrayList) {
		super();
		this.name = name;
		this.classFile = classFile;
		this.packageName = packageName;
		this.importArrayList = importArrayList;
		this.methodArrayList = methodArrayList;
		this.fieldArrayList = fieldArrayList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public File getClassFile() {
		return classFile;
	}

	public void setClassFile(File classFile) {
		this.classFile = classFile;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public ArrayList<String> getImportArrayList() {
		return importArrayList;
	}

	public void setImportArrayList(ArrayList<String> importArrayList) {
		this.importArrayList = importArrayList;
	}

	public ArrayList<JavaMethod> getMethodArrayList() {
		return methodArrayList;
	}

	public void setMethodArrayList(ArrayList<JavaMethod> methodArrayList) {
		this.methodArrayList = methodArrayList;
	}

	public ArrayList<Field> getFieldArrayList() {
		return fieldArrayList;
	}

	public void setFieldArrayList(ArrayList<Field> fieldArrayList) {
		this.fieldArrayList = fieldArrayList;
	}

}
