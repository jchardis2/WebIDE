package com.ias.webide;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

public class ProjectBuilder {
	protected IProject project;

	public ProjectBuilder() {
		// TODO Auto-generated constructor stub
	}

	public IProject buildBasicProject(String projectName, String srcFolderName, String packageName) throws CoreException {
		createProject(projectName);
		IFolder srcFolder = createSrcFolder("src");
		return project;
	}

	public IProject createProject(String name) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project = root.getProject(name);
		project.create(null);
		project.open(null);
		return project;
	}

	public IFolder createSrcFolder(String src) throws CoreException {
		// create src folder
		IFolder sourceFolder = project.getFolder(src);
		sourceFolder.create(false, true, null);
		return sourceFolder;
	}

	protected IFolder createIFolder(String path, int offset) throws CoreException {
		int nextOffset = path.indexOf(File.separator, offset);
		if (nextOffset == -1) {
			IFolder iFolder = project.getFolder(path);
			if (!iFolder.exists()) {
				iFolder.create(false, true, null);
			}
			return iFolder;
		} else {
			String pathPart = path.substring(0, nextOffset);
			IFolder iFolder = project.getFolder(pathPart);
			if (!iFolder.exists())
				iFolder.create(false, true, null);
			return createIFolder(path, nextOffset+1);
		}
	}

}
