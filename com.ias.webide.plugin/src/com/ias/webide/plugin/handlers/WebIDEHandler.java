package com.ias.webide.plugin.handlers;

import java.io.File;
import java.util.Collections;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import com.ias.webide.java.JavaProjectBuilder;
import com.ias.webide.java.maven.MavenJavaProjectBuilder;
import com.ias.webide.plugin.tools.ProjectResolver;

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
		buildMavenProject();
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

	public void buildMavenProject() {
		MavenJavaProjectBuilder javaProjectBuilder = new MavenJavaProjectBuilder();
		try {
			IJavaProject mavenJProject = javaProjectBuilder.buildBasicProject("Test" + count++, "WebContent/WEB-INF/classes", "src", "com.ias.test");
			InvocationRequest request = new DefaultInvocationRequest();
			ProjectResolver projectResolver = new ProjectResolver();
			System.out.println("MavenProject:" + mavenJProject.getJavaProject().getProject().getRawLocation());
			request.setPomFile(new File(mavenJProject.getProject().getRawLocation().toFile().getAbsoluteFile() + File.separator + "pom.xml"));
			request.setGoals(Collections.singletonList("build"));
			Invoker invoker = new DefaultInvoker();
			invoker.execute(request);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MavenInvocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
