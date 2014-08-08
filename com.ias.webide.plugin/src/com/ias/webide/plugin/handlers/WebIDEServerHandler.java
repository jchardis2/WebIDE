package com.ias.webide.plugin.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.jasper.compiler.JspRuntimeContext;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.osgi.framework.Bundle;

import com.ias.webide.jetty.IASServer;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class WebIDEServerHandler extends AbstractHandler {
	public static IASServer iasServer;

	/**
	 * The constructor.
	 */
	public WebIDEServerHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (iasServer == null || !iasServer.isRunning()) {
			Bundle bundle = Platform.getBundle("com.ias.webide.plugin");
			URL fileURL = bundle.getEntry("config.properties");
			File file = null;
			try {
				file = new File(FileLocator.resolve(fileURL).toURI());
				iasServer = new IASServer();
				iasServer.loadSystemProperties(file.getAbsolutePath());
				ContextHandlerCollection contexts = iasServer.configure();
				iasServer.fullWebAppDeployment(contexts);
				// iasServer.setLoginService(contexts);
				iasServer.start();
				// iasServer.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e1) {
				e1.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			System.out.println("Server is already running.");
		}
		return null;
	}

	public Runnable serverThread = new Runnable() {
		public void run() {

		}
	};
}
