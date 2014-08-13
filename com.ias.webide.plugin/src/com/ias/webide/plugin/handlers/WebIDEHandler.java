package com.ias.webide.plugin.handlers;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;

import com.ias.webide.java.JavaProjectBuilder;
import com.ias.webide.java.db.mysql.util.JavaClassGenerator;
import com.ias.webide.java.maven.MavenJavaProjectBuilder;
import com.ias.webide.java.maven.WarPomBuilder;
import com.ias.webide.plugin.util.ProjectResolver;
import com.ias.webide.plugin.util.ProjectUtil;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class WebIDEHandler extends AbstractHandler {
	public static int count = 0;

	/**
	 * The constructor.
	 */
	public WebIDEHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {

		DAOReader daoReader = new DAOReader();
		daoReader.setConnection();
		try {
			ArrayList<DatabaseTable> databaseTable = daoReader.getDatabaseTables();
			ProjectResolver projectResolver = ProjectResolver.getInstance();
			IJavaProject iJavaProject = projectResolver.getInstance().resolveProject("Test");
			iJavaProject.open(new NullProgressMonitor());
			IPackageFragmentRoot[] iPackageFragmentRoots = iJavaProject.getPackageFragmentRoots();
			IPackageFragment iPackageFragment = null;
			for (IPackageFragmentRoot iPackageFragmentRoot : iPackageFragmentRoots) {
				if (iPackageFragmentRoot.getElementName().equals("src")) {
					iPackageFragment = iPackageFragmentRoot.getPackageFragment("com.ias.test.beans");
				}
			}
			iPackageFragment.open(new NullProgressMonitor());
			// iPackageFragment.open(new NullProgressMonitor());
			System.out.println("Name: " + iPackageFragment.getElementName());
			JavaClassGenerator classGenerator = new JavaClassGenerator(iJavaProject, iPackageFragment);
			classGenerator.createClass(databaseTable.get(0), "com.ias.test.beans", "TestClass", false, false, true);
			CompilationUnit cu = classGenerator.getCu();
			IJavaElement classPath = cu.getJavaElement();
			System.out.println(classPath);
			ICompilationUnit icu = iPackageFragment.createCompilationUnit("TestClass.java", cu.toString(), true, new NullProgressMonitor());
			ProjectUtil projectUtil = new ProjectUtil();
			projectUtil.refreshProject(iJavaProject.getResource());
			System.out.println("Done building java project");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// try {
		// buildMavenProject("Test" + count++);
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ParserConfigurationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (CoreException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (TransformerException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (MavenInvocationException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		return null;
	}

	public void buildJavaProject() {
		JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
		try {
			javaProjectBuilder.buildBasicProject("Test" + count++, "bin", "src", "com.ias.test");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void buildMavenProject(String projectName) throws IOException, ParserConfigurationException, CoreException, TransformerException, MavenInvocationException {
		MavenJavaProjectBuilder javaProjectBuilder = new MavenJavaProjectBuilder();
		IJavaProject mavenJProject = javaProjectBuilder.buildBasicProject(projectName, "WebContent/WEB-INF/classes", "src", "com.ias.test");

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String folder = workspace.getRoot().getLocation().toFile().getPath().toString();
		String pomPath = folder + File.separator + mavenJProject.getElementName() + File.separator + "pom.xml";

		ProjectResolver projectResolver = ProjectResolver.getInstance();
		File xmlFile = new File(pomPath);
		if (!xmlFile.exists()) {
			xmlFile.createNewFile();
		}
		WarPomBuilder warPomBuilder = new WarPomBuilder(xmlFile);
		warPomBuilder.buildWarPomFile(projectName);
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(xmlFile);
		request.setGoals(Collections.singletonList("compile"));
		Invoker invoker = new DefaultInvoker();
		invoker.execute(request);
		ProjectUtil projectUtil = new ProjectUtil();
		projectUtil.refreshProject(mavenJProject.getResource());
		System.out.println("Finished\nupdating maven");
		mavenUpdateProject(mavenJProject);
	}

	public void refreshMaven(IJavaProject iJavaProject) throws CoreException {
		IMavenProjectFacade iMavenProjectFacade = MavenPlugin.getMavenProjectRegistry().getProject(iJavaProject.getProject());
		IFile iPomFile = iMavenProjectFacade.getPom();
		List<IFile> refreshFiles = new ArrayList<IFile>();
		refreshFiles.add(iPomFile);
		MavenPlugin.getMavenProjectRegistry().refresh(refreshFiles, null);
	}

	public void mavenUpdateProject(final IJavaProject iJavaProject) throws CoreException {
		IProjectConfigurationManager configurationManager = MavenPlugin.getProjectConfigurationManager();
		IProject project = iJavaProject.getProject();

		configurationManager.updateProjectConfiguration(project, new NullProgressMonitor());

		// Job job = new Job("My First Job") {
		// protected IStatus run(IProgressMonitor monitor) {
		// IProjectConfigurationManager configurationManager =
		// MavenPlugin.getProjectConfigurationManager();
		// try {
		// IProject project = iJavaProject.getProject();
		//
		// configurationManager.updateProjectConfiguration(project, new
		// NullProgressMonitor());
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println("Project updated");
		// return Status.OK_STATUS;
		// }
		// };
		// job.schedule();

		// Display.getDefault().asyncExec(new Runnable() {
		// public void run() {
		// try {
		// IProjectConfigurationManager configurationManager =
		// MavenPlugin.getProjectConfigurationManager();
		// configurationManager.updateProjectConfiguration(iJavaProject.getProject(),
		// null);
		// System.out.println("Project updated");
		// } catch (JavaModelException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (CoreException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });

		// IMavenProjectFacade iMavenProjectFacade =
		// MavenPlugin.getMavenProjectRegistry().getProject(iJavaProject.getProject());
		// IFile iPomFile = iMavenProjectFacade.getPom();
		// List<IFile> refreshFiles = new ArrayList<IFile>();
		// // refreshFiles.add(iPomFile);
		// MavenPlugin.getMavenProjectRegistry().refresh(refreshFiles, null);
		// ResolverConfiguration resolverConfiguration =
		// iMavenProjectFacade.getResolverConfiguration();
		// MavenImpl mavenImpl = new
		// MavenImpl(MavenPlugin.getMavenConfiguration());
		// LifecycleMappingFactory lifecycleMappingFactory = new
		// LifecycleMappingFactory();
	}

}
