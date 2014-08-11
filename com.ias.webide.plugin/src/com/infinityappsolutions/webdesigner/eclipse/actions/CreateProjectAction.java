package com.infinityappsolutions.webdesigner.eclipse.actions;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;

public class CreateProjectAction {

	public static final String PROPERTY_PROJECTS_HOME = "projects.home";

	public CreateProjectAction() {

	}

	public IProject createProject(String orgName, String repositoryName, String projectName) {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IProject project = root.getProject(projectName);
		IProjectDescription description = workspace.newProjectDescription(project.getName());
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		Path path = new Path(System.getProperty(PROPERTY_PROJECTS_HOME) + File.separator + orgName + File.separator + projectName);
		System.out.println("Path: " + path.toString());
		description.setLocation(path);
		try {
			project.create(description, null);
			project.open(null);
			return project;
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
