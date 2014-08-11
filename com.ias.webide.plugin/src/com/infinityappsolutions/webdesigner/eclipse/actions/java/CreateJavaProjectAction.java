package com.infinityappsolutions.webdesigner.eclipse.actions.java;

import java.util.ArrayList;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;

import com.infinityappsolutions.webdesigner.eclipse.actions.CreateProjectAction;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

public class CreateJavaProjectAction extends CreateProjectAction {

	public IProject createProject(String orgName, String repositoryName, String projectName, ArrayList<DatabaseTable> databaseTables) {

		// description.setNatureIds(new String[] { JavaCore.NATURE_ID });
		IProject generalProject = super.createProject(orgName, repositoryName, projectName);
		IJavaProject javaProject = JavaCore.create(generalProject);
		IClasspathEntry[] buildPath = { JavaCore.newSourceEntry(generalProject.getFullPath().append("src")), JavaRuntime.getDefaultJREContainerEntry() };
		try {
			javaProject.setRawClasspath(buildPath, generalProject.getFullPath().append("bin"), null);
			IFolder folder = generalProject.getFolder("src");
			folder.create(true, true, null);

			PackageGenerator packageGenerator = new PackageGenerator();

			String mainPackageName = "com." + orgName.toLowerCase() + "." + projectName.toLowerCase();
			String beanPackageName = mainPackageName + ".beans";
			String loaderPackageName = mainPackageName + ".loaders";
			String mysqlDaoPackageName = mainPackageName + ".mysql.dao";
			String actionsPackageName = mainPackageName + ".actions";
			// create package fragments
			IPackageFragment mainPackageFragment = packageGenerator.addPackage(javaProject, mainPackageName, folder);
			IPackageFragment beanPackageFragment = packageGenerator.addPackage(javaProject, beanPackageName, folder);
			IPackageFragment loaderPackageFragment = packageGenerator.addPackage(javaProject, loaderPackageName, folder);
			IPackageFragment mysqlDaoPackageFragment = packageGenerator.addPackage(javaProject, mysqlDaoPackageName, folder);
			IPackageFragment actionsPackageFragment = packageGenerator.addPackage(javaProject, mysqlDaoPackageName, folder);

			// create beans
			BeanGenerator beanGenerator = new BeanGenerator();
			beanGenerator.generateAllBeans(javaProject, databaseTables, beanPackageFragment, beanPackageName);
			
			//create loaders
			
			//create daos
			

		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return generalProject;
	}

}
