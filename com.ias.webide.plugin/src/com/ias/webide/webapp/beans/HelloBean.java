package com.ias.webide.webapp.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.eclipse.core.internal.resources.Workspace;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;

import com.ias.webide.jetty.handlers.ProjectHandler;

@ManagedBean(name = "helloBean")
@SessionScoped
public class HelloBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
//		Object o = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("jp");
//		System.out.println(o);
//		IJavaProject iJavaProject = (IJavaProject) o;
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}