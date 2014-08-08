package com.ias.webide.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.CoreException;

import com.ias.webide.java.JavaProjectBuilder;

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
		JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder();
		try {
			javaProjectBuilder.buildBasicProject("Test" + count++, "bin",
					"src", "com.ias.test");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
