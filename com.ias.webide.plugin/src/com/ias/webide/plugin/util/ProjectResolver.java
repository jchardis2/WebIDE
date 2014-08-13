package com.ias.webide.plugin.util;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

public class ProjectResolver {
	private static ProjectResolver projectResolver;

	IWorkspaceRoot root;

	private ProjectResolver() {
		root = ResourcesPlugin.getWorkspace().getRoot();
	}

	public static ProjectResolver getInstance() {
		if (projectResolver == null) {
			projectResolver = new ProjectResolver();
		}
		return projectResolver;
	}

	public String resolveProjectPath(String projectName) {
		return root.getProject(projectName).getRawLocation().toString();
	}

	public IJavaProject resolveProject(String projectName) {
		IJavaModel iJavaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		return iJavaModel.getJavaProject(projectName);
	}

	public IJavaProject[] getJavaProjects(String projectName) throws JavaModelException {
		IJavaModel iJavaModel = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot());
		return iJavaModel.getJavaProjects();
	}
}
