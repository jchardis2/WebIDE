package com.ias.webide.webapp.beans;

import java.util.ArrayList;

public class ProjectBean implements java.io.Serializable {
	private static final long serialVersionUID = 4912349482132986988L;
	
	private String name;
	private String path;
	private ArrayList<String> natures;
	private ArrayList<String> srcFolders;
	private ArrayList<String> outputFolders;

	public ProjectBean() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public ArrayList<String> getNatures() {
		return natures;
	}

	public void setNatures(ArrayList<String> natures) {
		this.natures = natures;
	}

	public ArrayList<String> getSrcFolders() {
		return srcFolders;
	}

	public void setSrcFolders(ArrayList<String> srcFolders) {
		this.srcFolders = srcFolders;
	}

	public ArrayList<String> getOutputFolders() {
		return outputFolders;
	}

	public void setOutputFolders(ArrayList<String> outputFolders) {
		this.outputFolders = outputFolders;
	}

}
