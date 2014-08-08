package com.ias.webide.java;

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

public class JavaProjectBuilder {
	private IProject project;
	private IJavaProject javaProject;

	public JavaProjectBuilder() {
		// TODO Auto-generated constructor stub
	}

	public IJavaProject buildBasicProject(String projectName,
			String outputLocation, String srcFolderName, String packageName)
			throws CoreException {
		createJavaProject(projectName);
		setClassPath(outputLocation, srcFolderName);
		IFolder srcFolder = createSrcFolder("src");
		createPackage(srcFolder, "com.ias.test");

		return javaProject;
	}

	public IJavaProject createJavaProject(String name) throws CoreException {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		project = root.getProject(name);
		project.create(null);
		project.open(null);

		IProjectDescription description = project.getDescription();
		description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		project.setDescription(description, null);

		javaProject = JavaCore.create(project);

		return javaProject;
	}

	public void setOutputLocation(String path) throws CoreException {
		// specify output location
		IFolder binFolder = project.getFolder(path);
		binFolder.create(false, true, null);
		javaProject.setOutputLocation(binFolder.getFullPath(), null);
	}

	public void setClassPath(String outputLocation, String srcFolderName)
			throws JavaModelException {
		// set the build path
		IClasspathEntry[] buildPath = {
				JavaCore.newSourceEntry(project.getFullPath().append(
						srcFolderName)),
				JavaRuntime.getDefaultJREContainerEntry() };

		javaProject.setRawClasspath(buildPath,
				project.getFullPath().append(outputLocation), null);
	}

	public IFolder createSrcFolder(String src) throws CoreException {
		// create src folder
		IFolder sourceFolder = project.getFolder(src);
		sourceFolder.create(false, true, null);
		return sourceFolder;
	}

	public IPackageFragment createPackage(IFolder srcFolder, String packageName)
			throws JavaModelException {
		IPackageFragment pack = javaProject.getPackageFragmentRoot(srcFolder)
				.createPackageFragment(packageName, false, null);
		return pack;
	}
}
