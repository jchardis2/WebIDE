package com.infinityappsolutions.webdesigner.eclipse.actions.java;

import org.eclipse.core.resources.IFolder;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

public class PackageGenerator {
	public IPackageFragment addPackageToSrc(IJavaProject javaProject, String packageName) throws JavaModelException {
		return addPackage(javaProject, packageName, javaProject.getProject().getFolder("src"));
	}

	public IPackageFragment addPackage(IJavaProject javaProject, String packageName, IFolder folder) throws JavaModelException {
		// Add folder to Java element
		IPackageFragmentRoot srcFolder = javaProject.getPackageFragmentRoot(folder);
		// create package fragment
		return srcFolder.createPackageFragment(packageName, false, null);
	}
}
