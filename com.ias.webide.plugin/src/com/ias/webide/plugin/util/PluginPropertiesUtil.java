package com.ias.webide.plugin.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import com.ias.webide.jetty.ConfigPropertiesUtil;

public class PluginPropertiesUtil {
	public static final String COM_IAS_WEBIDE_PLUGIN_WEBAPPS = "com.ias.webide.plugin.webapps";

	public static void addProperties() {
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		System.setProperty("com.ias.webide.plugin.webapps", root.getRawLocation() + File.separator + "webapps");
	}

	public static void loadSystemProperties(String filename) throws IOException, URISyntaxException {
		PluginFileResolver fileResolver = new PluginFileResolver();
		String path = fileResolver.getAbsolutePath(filename);

		ConfigPropertiesUtil configPropertiesUtil = new ConfigPropertiesUtil();
		configPropertiesUtil.resolveProperties(new File(path));

		FileInputStream propFile = new FileInputStream(path);
		Properties p = new Properties(System.getProperties());
		p.load(propFile);
		System.setProperties(p);
	}

}
