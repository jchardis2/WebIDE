package com.ias.webide.plugin.tools;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

public class ProjectResolver {
	IWorkspaceRoot root;

	public ProjectResolver() {
		root = ResourcesPlugin.getWorkspace().getRoot();
	}

	public String resolveProject(String projectName) {
		return root.getProject("WebIDE").getRawLocation().toString();
	}
}
