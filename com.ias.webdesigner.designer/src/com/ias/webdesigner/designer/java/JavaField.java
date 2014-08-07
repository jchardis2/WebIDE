package com.ias.webdesigner.designer.java;

import com.ias.webdesigner.designer.Field;

public class JavaField extends Field {
	private String name;
	private String classType;
	private String value;

	public JavaField(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public JavaField(String name, String classType, String value) {
		this(name);
		this.classType = classType;
		this.value = value;
	}

}
