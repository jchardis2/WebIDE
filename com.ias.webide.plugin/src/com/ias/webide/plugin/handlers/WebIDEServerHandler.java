package com.ias.webide.plugin.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;

import com.ias.webide.jetty.IASServer;
import com.ias.webide.plugin.util.PluginFileResolver;

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
			PluginFileResolver fileResolver = new PluginFileResolver();
			try {
				iasServer = new IASServer();
				iasServer.loadSystemProperties(fileResolver.getAbsolutePath("config.properties"));
				iasServer.setJetty_home(fileResolver.getAbsolutePath("jetty"));
				iasServer.setWebbapp_home(ResourcesPlugin.getWorkspace().getRoot().getLocation().toFile().getAbsolutePath() + File.separator + "webapps");
				iasServer.configure();
//				iasServer.configureHttp();
//				iasServer.configureHttps();
				
				// ContextHandlerCollection contexts = iasServer.configure();
				// iasServer.fullWebAppDeployment(contexts);
				
				String webContentPath = fileResolver.getAbsolutePath("WebContent");
				iasServer.addPluginWebApp(webContentPath, "/");
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
		} else {
			System.out.println("Server is already running.");
		}
		return null;
	}

	public Runnable serverThread = new Runnable() {
		public void run() {

		}
	};
}
