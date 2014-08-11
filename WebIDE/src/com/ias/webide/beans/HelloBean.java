package com.ias.webide.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "helloBean")
@SessionScoped
public class HelloBean implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;

	public String getName() {
		
//		Object o = FacesContext.getCurrentInstance().getExternalContext()
//				.getApplicationMap().get("workspace");
//		System.out.println(o);

		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}