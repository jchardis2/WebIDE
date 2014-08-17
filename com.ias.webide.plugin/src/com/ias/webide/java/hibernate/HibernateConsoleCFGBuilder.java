package com.ias.webide.java.hibernate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.internal.core.LaunchConfiguration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences.ConfigurationMode;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.utils.LaunchHelper;
import org.hibernate.eclipse.console.wizards.BestGuessConsoleConfigurationVisitor;
import org.hibernate.eclipse.launch.ConnectionProfileCtrl;
import org.hibernate.eclipse.launch.ICodeGenerationLaunchConstants;
import org.hibernate.eclipse.launch.IConsoleConfigurationLaunchConstants;
import org.hibernate.util.xpl.StringHelper;

@SuppressWarnings("restriction")
/**
 * 
 * @author Jimmy Hardison
 */
public class HibernateConsoleCFGBuilder {
	public static final String TEMPORARY_CONFIG_FLAG = "_TEMPORARY_CONFIG_"; //$NON-NLS-1$

	public static final String NO_CONNECTIN_NAME = HibernateConsoleMessages.ConnectionProfileCtrl_HibernateConfiguredConnection;
	public static final String JPA_CONNECTIN_NAME = HibernateConsoleMessages.ConnectionProfileCtrl_JPAConfiguredConnection;

	public static final String DEFAULT_HIBERNATE_VERSION = "3.5";
	public static final ConfigurationMode DEFAULT_HIBERNATE_CONFIGURATION_MODE = ConfigurationMode.CORE;

	protected BestGuessConsoleConfigurationVisitor v;
	protected ILaunchConfiguration launchConfig;
	protected ILaunchConfigurationWorkingCopy launchConfigWC;
	protected IJavaProject iJavaProject;

	public HibernateConsoleCFGBuilder(IJavaProject iJavaProject) {
		this.iJavaProject = iJavaProject;
		v = new BestGuessConsoleConfigurationVisitor();
		v.setJavaProject(iJavaProject);
	}

	public ILaunchConfiguration buildDefaultConfiguration(String launchConfigurationName, IPath configFullPath, String connectionProfileName) throws CoreException {
		launchConfig = createTemporaryLaunchConfiguration(launchConfigurationName);
		launchConfigWC = launchConfig.getWorkingCopy();
		setConfigurationFilePath(configFullPath);
		performApply(launchConfigWC, configFullPath, v.getJavaProject(), DEFAULT_HIBERNATE_CONFIGURATION_MODE, DEFAULT_HIBERNATE_VERSION, "", "", connectionProfileName);
		makeTemporaryLaunchConfigurationPermanent(launchConfigWC);
		Map<String, Object> map2 = launchConfigWC.getAttributes();
		Set<String> set = map2.keySet();
		for (String string : set) {
			System.out.println("Key: " + string);
			Object o = map2.get(string);
			System.out.println(map2.get(string));
		}
		return launchConfig;
	}

	/**
	 * 
	 * @param launchConfigWC
	 * @param iJavaProject
	 * @param configurationMode
	 *            default should be ConfigurationMode.CORE
	 * @param hibernateVersion
	 *            ex: 4.0
	 * 
	 *            default: 3.5
	 */
	private void performApply(ILaunchConfigurationWorkingCopy launchConfigWC, IPath configFullPath, IJavaProject iJavaProject, ConfigurationMode configurationMode,
			String hibernateVersion, String hibernatePropertiesPath, String persistenceUnitName, String cpName) {
		String projectName = nonEmptyTrimOrNull(iJavaProject.getProject().getName());
		launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.CONFIGURATION_FACTORY, ConfigurationMode.CORE.toString());
		launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.HIBERNATE_VERSION, hibernateVersion);
		launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, projectName);
		if (projectName != null) {
			launchConfigWC.setAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_PATHS, Collections.singletonList(nonEmptyTrimOrNull(projectName)));
			launchConfigWC.setAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_TYPES, Collections.singletonList(Integer.toString(IResource.PROJECT)));
		} else {
			launchConfigWC.removeAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_PATHS);
			launchConfigWC.removeAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_TYPES);
		}
		launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.PROPERTY_FILE, hibernatePropertiesPath);
		launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.CFG_XML_FILE, configFullPath.toOSString());
		launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.PERSISTENCE_UNIT_NAME, nonEmptyTrimOrNull(persistenceUnitName));
		if (JPA_CONNECTIN_NAME.equals(cpName)) {
			launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.USE_JPA_PROJECT_PROFILE, Boolean.toString(true));
			launchConfigWC.removeAttribute(IConsoleConfigurationLaunchConstants.CONNECTION_PROFILE_NAME);
		} else if (NO_CONNECTIN_NAME.equals(cpName)) {
			launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.CONNECTION_PROFILE_NAME, (String) null);
			launchConfigWC.removeAttribute(IConsoleConfigurationLaunchConstants.USE_JPA_PROJECT_PROFILE);
		} else {
			launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.CONNECTION_PROFILE_NAME, cpName);
			launchConfigWC.removeAttribute(IConsoleConfigurationLaunchConstants.USE_JPA_PROJECT_PROFILE);
		}
	}

	protected void setConfigurationFilePath(IPath configFullPath) {
		IPath path = configFullPath;
		IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		while (resource == null && path != null) {
			path = path.removeLastSegments(1);
			resource = ResourcesPlugin.getWorkspace().getRoot().findMember(path);
		}
		initialize();
		setPathAttribute(launchConfigWC, IConsoleConfigurationLaunchConstants.CFG_XML_FILE, configFullPath);
		setStrAttribute(launchConfigWC, IConsoleConfigurationLaunchConstants.CONFIGURATION_FACTORY, ConfigurationMode.CORE.toString());
	}

	protected void initialize() {
		setProjAttribute(launchConfigWC, IJavaLaunchConfigurationConstants.ATTR_PROJECT_NAME, v.getJavaProject());
		setPathAttribute(launchConfigWC, IConsoleConfigurationLaunchConstants.PROPERTY_FILE, v.getPropertyFile());
		setPathAttribute(launchConfigWC, IConsoleConfigurationLaunchConstants.CFG_XML_FILE, v.getConfigFile());
		if (v.getPersistencexml() != null) {
			setStrAttribute(launchConfigWC, IConsoleConfigurationLaunchConstants.CONFIGURATION_FACTORY, ConfigurationMode.JPA.toString());
		} else {
			setStrAttribute(launchConfigWC, IConsoleConfigurationLaunchConstants.CONFIGURATION_FACTORY, ConfigurationMode.CORE.toString());
		}
		if (!v.getMappings().isEmpty() && v.getConfigFile() == null && v.getPersistencexml() == null) {
			IPath[] mappings = v.getMappings().toArray(new IPath[] {});
			List<String> l = new ArrayList<String>();
			for (int i = 0; i < mappings.length; i++) {
				IPath path = mappings[i];
				l.add(path.toPortableString());
			}

			launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.FILE_MAPPINGS, l);
		} else {
			launchConfigWC.setAttribute(IConsoleConfigurationLaunchConstants.FILE_MAPPINGS, (List<String>) null);
		}
		if (!v.getClasspath().isEmpty()) {
			launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
			IPath[] custClasspath = v.getClasspath().toArray(new IPath[] {});
			List<String> mementos = new ArrayList<String>(custClasspath.length);
			for (int i = 0; i < custClasspath.length; i++) {
				mementos.add(custClasspath[i].toOSString());
			}
			launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, mementos);
		} else {
			launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, true);
			launchConfigWC.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, (List<String>) null);
		}
	}

	static protected void setProjAttribute(ILaunchConfigurationWorkingCopy currentLaunchConfig, String attr, IJavaProject proj) {
		if (proj != null) {
			currentLaunchConfig.setAttribute(attr, nonEmptyTrimOrNull(proj.getElementName()));
			currentLaunchConfig.setAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_PATHS,
					Collections.singletonList(new Path(nonEmptyTrimOrNull(proj.getElementName())).makeAbsolute().toString()));
			currentLaunchConfig.setAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_TYPES, Collections.singletonList(Integer.toString(IResource.PROJECT)));
		} else {
			currentLaunchConfig.setAttribute(attr, (String) null);
			currentLaunchConfig.removeAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_PATHS);
			currentLaunchConfig.removeAttribute(LaunchConfiguration.ATTR_MAPPED_RESOURCE_TYPES);
		}
	}

	static protected void setPathAttribute(ILaunchConfigurationWorkingCopy currentLaunchConfig, String attr, IPath path) {
		if (path != null) {
			currentLaunchConfig.setAttribute(attr, nonEmptyTrimOrNull(path.toOSString()));
		} else {
			currentLaunchConfig.setAttribute(attr, (String) null);
		}
	}

	static protected void setStrAttribute(ILaunchConfigurationWorkingCopy currentLaunchConfig, String attr, String str) {
		if (str != null) {
			currentLaunchConfig.setAttribute(attr, nonEmptyTrimOrNull(str));
		} else {
			currentLaunchConfig.setAttribute(attr, (String) null);
		}
	}

	static protected String nonEmptyTrimOrNull(String str) {
		if (StringHelper.isEmpty(str)) {
			return null;
		} else {
			return str.trim();
		}
	}

	protected ILaunchConfiguration setAttributes(ILaunchConfiguration iLaunchConfiguration) {
		IConsoleConfigurationLaunchConstants configurationLaunchConstants;

		return iLaunchConfiguration;
	}

	protected ILaunchConfiguration createTemporaryLaunchConfiguration(String launchConfigurationName) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = LaunchHelper.getHibernateLaunchConfigsType();
		String launchName = launchManager.generateLaunchConfigurationName(launchConfigurationName);
		// ILaunchConfiguration[] launchConfigurations =
		// launchManager.getLaunchConfigurations( launchConfigurationType );
		ILaunchConfigurationWorkingCopy wc = launchConfigurationType.newInstance(null, launchName);
		wc.setAttribute(TEMPORARY_CONFIG_FLAG, true);
		return wc.doSave();
	}

	protected void makeTemporaryLaunchConfigurationsPermanent() throws CoreException {
		List<ILaunchConfiguration> listTempConfigs = getTemporaryLaunchConfigurations();
		ILaunchConfigurationWorkingCopy wc;
		for (int i = 0; i < listTempConfigs.size(); i++) {
			wc = listTempConfigs.get(i).getWorkingCopy();
			// Must be set to null since it should never be in the actual saved
			// configuration!
			wc.setAttribute(TEMPORARY_CONFIG_FLAG, (String) null);
			wc.doSave();
		}
	}

	protected void makeTemporaryLaunchConfigurationPermanent(ILaunchConfigurationWorkingCopy tmpLaunchConfig) throws CoreException {
		tmpLaunchConfig.setAttribute(TEMPORARY_CONFIG_FLAG, (String) null);
		tmpLaunchConfig.doSave();
	}

	protected List<ILaunchConfiguration> getTemporaryLaunchConfigurations() throws CoreException {
		List<ILaunchConfiguration> listTempConfigs = new ArrayList<ILaunchConfiguration>();
		ILaunchConfiguration[] configs = LaunchHelper.findHibernateLaunchConfigs();
		for (int i = 0; i < configs.length; i++) {
			boolean temporary = configs[i].getAttribute(TEMPORARY_CONFIG_FLAG, false);
			if (temporary) {
				listTempConfigs.add(configs[i]);
			}
		}
		return listTempConfigs;
	}

	protected ILaunchConfiguration getTemporaryLaunchConfiguration(String name) throws CoreException {
		List<ILaunchConfiguration> listTempConfigs = new ArrayList<ILaunchConfiguration>();
		ILaunchConfiguration[] configs = LaunchHelper.findHibernateLaunchConfigs();
		for (int i = 0; i < configs.length; i++) {
			boolean temporary = configs[i].getAttribute(TEMPORARY_CONFIG_FLAG, false);
			if (temporary && configs[i].getName().equals(name)) {
				return configs[i];
			}
		}
		return null;
	}

	protected void deleteTemporaryLaunchConfigurations() throws CoreException {
		List<ILaunchConfiguration> listTempConfigs = getTemporaryLaunchConfigurations();
		for (int i = 0; i < listTempConfigs.size(); i++) {
			listTempConfigs.get(i).delete();
		}
	}

	protected void deleteTemporaryLaunchConfiguration(ILaunchConfiguration tmpLaunchConfig) throws CoreException {
		tmpLaunchConfig.delete();
	}
}
