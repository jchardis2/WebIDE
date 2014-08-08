package com.ias.webide.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.jface.dialogs.MessageDialog;

import com.ias.webide.jetty.IASServer;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class StopServerHandler extends AbstractHandler {
	/**
	 * The constructor.
	 */
	public StopServerHandler() {
	}

	/**
	 * the command has been executed, so extract extract the needed information
	 * from the application context.
	 */
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = HandlerUtil
				.getActiveWorkbenchWindowChecked(event);
		if (WebIDEServerHandler.iasServer != null
				&& WebIDEServerHandler.iasServer.isRunning()) {
			synchronized (WebIDEServerHandler.iasServer) {
				try {
					WebIDEServerHandler.iasServer.stop();
					while (WebIDEServerHandler.iasServer.isRunning()) {
						try {
							synchronized (this) {
								this.wait(1000);
							}
							System.out.println("Still running");
							System.out.println(WebIDEServerHandler.iasServer
									.getStopTimeout());
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		} else {
			System.out.println("Server isn't running.");
		}
		return null;
	}
}
