package com.ias.webide.webapp.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IWorkbench;

@ManagedBean(name = "helloBean")
@SessionScoped
public class HelloBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		Object o = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap().get("workspace");
		System.out.println(o);

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}