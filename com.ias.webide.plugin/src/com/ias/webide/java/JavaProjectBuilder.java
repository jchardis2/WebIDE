package com.ias.webide.java;

import java.io.File;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

import com.ias.webide.ProjectBuilder;

public class JavaProjectBuilder extends ProjectBuilder {
	protected IJavaProject javaProject;

	public JavaProjectBuilder() {
		// TODO Auto-generated constructor stub
	}

	public IJavaProject buildBasicProject(String projectName, String outputLocation, String srcFolderName, String packageName) throws CoreException {
		createJavaProject(projectName);
		IFolder srcFolder = createSrcFolder("src");
		setOutputLocation(outputLocation);
		setClassPath(outputLocation, srcFolderName);
		createPackage(srcFolder, packageName);
		return javaProject;
	}

	public IJavaProject createJavaProject(String name) throws CoreException {
		return createJavaProject(name, new String[] { JavaCore.NATURE_ID });
	}

	public IJavaProject createJavaProject(String name, String[] natureIds) throws CoreException {
		project = super.createProject(name);
		IProjectDescription description = project.getDescription();
		description.setNatureIds(natureIds);
		project.setDescription(description, null);
		javaProject = JavaCore.create(project);
		return javaProject;
	}

	public void setOutputLocation(String path) throws CoreException {
		// specify output location
		IFolder binFolder = createIFolder(path, 0);
		javaProject.setOutputLocation(binFolder.getFullPath(), null);
	}

	public void setClassPath(String outputLocation, String srcFolderName) throws JavaModelException {
		// set the build path
		IClasspathEntry[] buildPath = { JavaCore.newSourceEntry(project.getFullPath().append(srcFolderName)), JavaRuntime.getDefaultJREContainerEntry() };

		javaProject.setRawClasspath(buildPath, project.getFullPath().append(outputLocation), null);
	}

	public IPackageFragment createPackage(IFolder srcFolder, String packageName) throws JavaModelException {
		IPackageFragment pack = javaProject.getPackageFragmentRoot(srcFolder).createPackageFragment(packageName, false, null);
		return pack;
	}

}
