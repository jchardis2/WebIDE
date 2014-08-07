package com.ias.webide.java.tools;

import org.eclipse.jdt.core.dom.Type;

public class ParameterNameValuePair {
	private Type type;
	private String name;

	public ParameterNameValuePair(Type type, String name) {
		this.type = type;
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}

}
