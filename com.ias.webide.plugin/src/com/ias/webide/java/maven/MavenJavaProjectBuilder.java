package com.ias.webide.java.maven;

import java.util.ArrayList;
import java.util.List;

import org.apache.jasper.compiler.JavacErrorDetail;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

import com.ias.webide.java.JavaProjectBuilder;

public class MavenJavaProjectBuilder extends JavaProjectBuilder {
	public static final String MAVEN_NATURE_ID = "org.eclipse.m2e.core.maven2Nature";

	public MavenJavaProjectBuilder() {
		// TODO Auto-generated constructor stub
	}

	public IJavaProject buildBasicProject(String projectName, String outputLocation, String srcFolderName, String packageName) throws CoreException {
		createMavenProject(projectName);
		IFolder srcFolder = createSrcFolder(srcFolderName);
		setOutputLocation(outputLocation);
		setClassPath(outputLocation, srcFolderName);
		createPackage(srcFolder, packageName);
		return javaProject;
	}

	public IJavaProject createMavenProject(String name) throws CoreException {
		return createJavaProject(name, new String[] { JavaCore.NATURE_ID, MAVEN_NATURE_ID });
	}

	public void setClassPath(String outputLocation, String srcFolderName) throws JavaModelException {
		List<IClasspathEntry> entries = new ArrayList<IClasspathEntry>();
		entries.add(JavaCore.newSourceEntry(project.getFullPath().append(srcFolderName), new Path[] {}, new Path[] {}, javaProject.getOutputLocation()));
		entries.add(JavaRuntime.getDefaultJREContainerEntry());
		entries.add(JavaCore.newContainerEntry(new Path("org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER"), new IAccessRule[0],
				new IClasspathAttribute[] { JavaCore.newClasspathAttribute("org.eclipse.jst.component.dependency", "/WEB-INF/lib") }, false));
		// set the build path
		javaProject.setRawClasspath(entries.toArray(new IClasspathEntry[entries.size()]), project.getFullPath().append(outputLocation), null);

	}
}
