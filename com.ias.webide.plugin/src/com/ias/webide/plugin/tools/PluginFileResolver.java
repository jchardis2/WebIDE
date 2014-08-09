package com.ias.webide.plugin.tools;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class PluginFileResolver {
	public static final String COM_IAS_WEBIDE_PLUGIN = "com.ias.webide.plugin";
	Bundle bundle;

	public PluginFileResolver(String bundleName) {
		bundle = Platform.getBundle(bundleName);
	}

	public PluginFileResolver() {
		bundle = Platform.getBundle(COM_IAS_WEBIDE_PLUGIN);
	}

	public String getAbsolutePath(String fileName) throws URISyntaxException, IOException {
		URL fileURL = bundle.getEntry(fileName);
		return new File(FileLocator.resolve(fileURL).toURI()).getAbsolutePath();
	}

	public File getFile(String fileName) throws URISyntaxException, IOException {
		URL fileURL = bundle.getEntry(fileName);
		return new File(FileLocator.resolve(fileURL).toURI());
	}

}
