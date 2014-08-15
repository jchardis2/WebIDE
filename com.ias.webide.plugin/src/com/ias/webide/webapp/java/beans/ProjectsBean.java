package com.ias.webide.webapp.java.beans;

import java.util.List;

import javax.faces.context.FacesContext;

import org.eclipse.core.internal.resources.Project;

import com.ias.webide.webapp.beans.ProjectBean;

public class ProjectsBean {

	public ProjectsBean() {
	}

	public List<ProjectBean> getProjects() {
		return (List<ProjectBean>) FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("projects");
	}
}
