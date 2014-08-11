package com.ias.webide.plugin.util;

import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class ProjectUtil {

	public ProjectUtil() {

	}

	public void refreshProject(Project project) {
		refreshProject(project);
	}

	public void refreshProject(IResource iResource) throws CoreException {
		iResource.refreshLocal(IResource.DEPTH_INFINITE, null);
	}

}
