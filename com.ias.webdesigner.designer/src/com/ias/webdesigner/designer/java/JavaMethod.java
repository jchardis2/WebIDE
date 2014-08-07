package com.ias.webdesigner.designer.java;

import java.util.ArrayList;

public class JavaMethod {
	private String name;
	public String access;// default,private,public,protected
	private boolean isStatic;
	private boolean isFinal;
	private ArrayList<JavaCodeFragment> contents;
	private String comments;

	public JavaMethod(String name, String access, boolean isStatic,
			boolean isFinal, ArrayList<JavaCodeFragment> contents,
			String comments) {
		super();
		this.name = name;
		this.access = access;
		this.isStatic = isStatic;
		this.isFinal = isFinal;
		this.contents = contents;
		this.comments = comments;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}

	public ArrayList<JavaCodeFragment> getContents() {
		return contents;
	}

	public void setContents(ArrayList<JavaCodeFragment> contents) {
		this.contents = contents;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

}
