package com.ias.webide.java.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.jdt.core.IJavaProject;
import org.hibernate.eclipse.console.ExtensionManager;
import org.hibernate.eclipse.console.model.impl.ExporterDefinition;
import org.hibernate.eclipse.console.model.impl.ExporterFactory;
import org.hibernate.eclipse.console.utils.LaunchHelper;
import org.hibernate.eclipse.console.wizards.BestGuessConsoleConfigurationVisitor;
import org.hibernate.eclipse.launch.ExporterAttributes;
import org.hibernate.eclipse.launch.HibernateLaunchConstants;

public class HibernateCodeCFGGenerationBuilder {
	public static final String TEMPORARY_CONFIG_FLAG = "_TEMPORARY_CONFIG_"; //$NON-NLS-1$
	protected BestGuessConsoleConfigurationVisitor v;
	protected ILaunchConfiguration launchConfig;
	protected ILaunchConfigurationWorkingCopy launchConfigWC;
	private IJavaProject iJavaProject;

	public HibernateCodeCFGGenerationBuilder(IJavaProject iJavaProject) {
		this.iJavaProject = iJavaProject;
	}

	public ILaunchConfiguration buildDefaultConfiguration(String launchConfigurationName, String consoleConfigurationName, String outputPackage) throws CoreException {
		launchConfig = createTemporaryLaunchConfiguration(launchConfigurationName);
		launchConfigWC = launchConfig.getWorkingCopy();
		setAttributes(iJavaProject.getPath().append("src").toOSString(), true, true, true, true, true, "", "", false, "", consoleConfigurationName, outputPackage, false, false,
				false);
		setDefaultExporterAttributes();
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

	protected ILaunchConfiguration createTemporaryLaunchConfiguration(String launchConfigurationName) throws CoreException {
		ILaunchManager launchManager = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType launchConfigurationType = LaunchHelper.getCodeGenerationType();
		String launchName = launchManager.generateLaunchConfigurationName(launchConfigurationName);
		// ILaunchConfiguration[] launchConfigurations =
		// launchManager.getLaunchConfigurations( launchConfigurationType );
		ILaunchConfigurationWorkingCopy wc = launchConfigurationType.newInstance(null, launchName);
		wc.setAttribute(TEMPORARY_CONFIG_FLAG, true);
		return wc.doSave();
	}

	protected void makeTemporaryLaunchConfigurationPermanent(ILaunchConfigurationWorkingCopy tmpLaunchConfig) throws CoreException {
		tmpLaunchConfig.setAttribute(TEMPORARY_CONFIG_FLAG, (String) null);
		tmpLaunchConfig.doSave();
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

	public void setAttributes(String outputDir, boolean preferRawCompositeIds, boolean autoManyToMany, boolean autoOneToOne, boolean autoVersioning,
			boolean isReverseEngineerEnabled, String reverseEngineeringStrategy, String reverseEngineeringSettings, boolean useOwnTemplates, String templatedir,
			String consoleConfigurationName, String outputPackage, boolean useExternalProcess, boolean enableEJB3annotations, boolean enableJDK5) {
		// CodeGenerationSettingTab
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_OUTPUT_DIR, strOrNull(outputDir));
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_PREFER_BASIC_COMPOSITE_IDS, preferRawCompositeIds);
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_AUTOMATIC_MANY_TO_MANY, autoManyToMany);
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_AUTOMATIC_ONE_TO_ONE, autoOneToOne);
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_AUTOMATIC_VERSIONING, autoVersioning);
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_REVERSE_ENGINEER, isReverseEngineerEnabled);
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_REVERSE_ENGINEER_STRATEGY, strOrNull(reverseEngineeringStrategy));
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_REVERSE_ENGINEER_SETTINGS, strOrNull(reverseEngineeringSettings));

		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_USE_OWN_TEMPLATES, useOwnTemplates);
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_TEMPLATE_DIR, strOrNull(templatedir));

		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_CONSOLE_CONFIGURATION_NAME, strOrNull(consoleConfigurationName));
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_PACKAGE_NAME, strOrNull(outputPackage));

		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_USE_EXTERNAL_PROCESS, useExternalProcess);

		// ExporterSettingsTab
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_ENABLE_EJB3_ANNOTATIONS, enableEJB3annotations);
		launchConfigWC.setAttribute(HibernateLaunchConstants.ATTR_ENABLE_JDK5, enableJDK5);

		// List<ExporterFactory> exporterFactories = ((ObservableFactoryList)
		// getExporterTable().getInput()).getList();
		// ExporterAttributes.saveExporterFactories(config, exporterFactories,
		// selectedExporters, deletedExporterIds);
	}

	public void setExporterAttributes(ILaunchConfigurationWorkingCopy config, List<ExporterFactory> exporterFactories, Set<ExporterFactory> selectedExporters,
			Set<String> deletedExporterIds) {
		ExporterAttributes.saveExporterFactories(config, exporterFactories, selectedExporters, deletedExporterIds);
	}

	public void setDefaultExporterAttributes() {
		// ILaunchConfigurationWorkingCopy config) {
		Set<ExporterFactory> selectedExporters = new HashSet<ExporterFactory>();
		Set<String> deletedExporterIds = new HashSet<String>();
		Map<String, ExporterDefinition> map = getExporterDefinitions();
		Collection<ExporterDefinition> exporterDefinitions = map.values();
		List<ExporterFactory> exporterFactories = new ArrayList<ExporterFactory>();
		for (ExporterDefinition exporterDefinition : exporterDefinitions) {
			ExporterFactory exporterFactory = new ExporterFactory(exporterDefinition, exporterDefinition.getId());
			if (isDefaultFactory(exporterDefinition.getId())) {
				selectedExporters.add(exporterFactory);
			}
			exporterFactories.add(exporterFactory);
			// deletedExporterIds.add(exporterDefinition.getId());
		}
		// ExporterDefinition exporterDefinition =
		// map.get("org.hibernate.tools.hbm2dao");
		// selectedExporters.add(getExporterFactory(exporterDefinition));
		// exporterDefinition = map.get("org.hibernate.tools.hbm2hbmxml");
		// selectedExporters.add(getExporterFactory(exporterDefinition));
		// exporterDefinition = map.get("org.hibernate.tools.hbm2java");
		// selectedExporters.add(getExporterFactory(exporterDefinition));
		ExporterAttributes.saveExporterFactories(launchConfigWC, exporterFactories, selectedExporters, deletedExporterIds);
	}

	private boolean isDefaultFactory(String id) {
		if (id.equals("org.hibernate.tools.hbm2dao") || id.equals("org.hibernate.tools.hbm2java") | id.equals("org.hibernate.tools.hbm2hbmxml"))
			return true;
		return false;
	}

	public ExporterFactory getExporterFactory(ExporterDefinition exporterDefinition) {
		String exporterID = ExporterAttributes.getLaunchAttributePrefix(exporterDefinition.getId()) + ".extension_id";
		System.out.println(exporterDefinition.getId());
		System.out.println(exporterID);
		ExporterFactory exporterFactory = new ExporterFactory(exporterDefinition, exporterDefinition.getId());
		exporterFactory.setEnabled(true);
		return exporterFactory;
	}

	public Map<String, ExporterDefinition> getExporterDefinitions() {
		return ExtensionManager.findExporterDefinitionsAsMap();
	}

	private String strOrNull(String text) {
		if (text == null || text.trim().length() == 0) {
			text = null;
		}
		return text;
	}

}
