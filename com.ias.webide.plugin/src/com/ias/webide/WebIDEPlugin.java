package com.ias.webide;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Plugin;

public class WebIDEPlugin {
	public static Plugin getInstance() {
		return ResourcesPlugin.getPlugin();
	}
}
