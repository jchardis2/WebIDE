package com.ias.webide.plugin.handlers;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.IProjectConfigurationManager;
import org.eclipse.osgi.util.NLS;
import org.eclipse.text.edits.MalformedTreeException;
import org.hibernate.HibernateException;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.HibernateConsoleRuntimeException;
import org.hibernate.console.KnownConfigurations;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.eclipse.HibernatePlugin;
import org.hibernate.eclipse.console.HibernateConsoleMessages;
import org.hibernate.eclipse.console.HibernateConsolePlugin;
import org.hibernate.eclipse.console.ext.ConsoleExtension;
import org.hibernate.eclipse.console.ext.ConsoleExtensionManager;
import org.hibernate.eclipse.console.model.impl.ExporterFactory;
import org.hibernate.eclipse.console.utils.LaunchHelper;
import org.hibernate.eclipse.launch.CodeGenerationStrings;
import org.hibernate.eclipse.launch.CodeGenerationUtils;
import org.hibernate.eclipse.launch.ExporterAttributes;
import org.hibernate.eclipse.launch.PathHelper;
import org.hibernate.tool.hbm2x.ArtifactCollector;
import org.hibernate.tool.hbm2x.Exporter;
import org.hibernate.util.xpl.ReflectHelper;

import com.ias.webide.java.JavaProjectBuilder;
import com.ias.webide.java.db.mysql.util.JavaClassGenerator;
import com.ias.webide.java.maven.MavenJavaProjectBuilder;
import com.ias.webide.java.maven.WarPomBuilder;
import com.ias.webide.plugin.java.util.JavaClassFormatter;
import com.ias.webide.plugin.util.ProjectResolver;
import com.ias.webide.plugin.util.ProjectUtil;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader;
import com.infinityappsolutions.webdesigner.tools.database.DAOReader.DatabaseTable;

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
		try {
			ILaunchConfiguration[] iLaunchConfigurations = LaunchHelper.findCodeGenerationConfigs();
			ILaunchConfiguration iLaunchConfiguration = iLaunchConfigurations[0];
			final ExporterAttributes attributes = new ExporterAttributes(iLaunchConfiguration);

			List<ExporterFactory> exporterFactories = attributes.getExporterFactories();
			for (Iterator<ExporterFactory> iter = exporterFactories.iterator(); iter.hasNext();) {
				ExporterFactory exFactory = iter.next();
				if (!exFactory.isEnabled(iLaunchConfiguration)) {
					iter.remove();
				}
			}

			try {
				Set<String> outputDirectories = new HashSet<String>();
				ExporterFactory[] exporters = exporterFactories.toArray(new ExporterFactory[exporterFactories.size()]);
				ArtifactCollector collector = runExporters(attributes, exporters, outputDirectories, new NullProgressMonitor());

				for (String path : outputDirectories) {
					CodeGenerationUtils.refreshOutputDir(path);
				}

				// RefreshTab.refreshResources(configuration, monitor);

				// code formatting needs to happen *after* refresh to make sure
				// eclipse will format the uptodate files!
				Map<String, File[]> map = null;
				if (collector != null) {
					map = new HashMap<String, File[]>();
					Set<String> types = collector.getFileTypes();
					for (String type : types) {
						File[] files = collector.getFiles(type.toString());
						map.put(type, files);
					}
				}
				if (map != null) {
					map.keySet();
				}
			} catch (Exception e) {
				throw new CoreException(HibernateConsolePlugin.throwableToStatus(e, 666));
			} catch (NoClassDefFoundError e) {
				throw new CoreException(HibernateConsolePlugin.throwableToStatus(new HibernateConsoleRuntimeException(
						HibernateConsoleMessages.CodeGenerationLaunchDelegate_received_noclassdeffounderror, e), 666));
			}

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	private ArtifactCollector runExporters(final ExporterAttributes attributes, final ExporterFactory[] exporterFactories, final Set<String> outputDirectories,
			final IProgressMonitor monitor) throws CoreException {

		monitor.beginTask(HibernateConsoleMessages.CodeGenerationLaunchDelegate_generating_code_for + attributes.getConsoleConfigurationName(), exporterFactories.length + 1);

		if (monitor.isCanceled())
			return null;

		ConsoleConfiguration cc = KnownConfigurations.getInstance().find(attributes.getConsoleConfigurationName());
		if (attributes.isReverseEngineer()) {
			monitor.subTask(HibernateConsoleMessages.CodeGenerationLaunchDelegate_reading_jdbc_metadata);
		}
		final Configuration cfg = buildConfiguration(attributes, cc, ResourcesPlugin.getWorkspace().getRoot());

		monitor.worked(1);

		if (monitor.isCanceled())
			return null;

		return (ArtifactCollector) cc.execute(new Command() {

			public Object execute() {
				ArtifactCollector artifactCollector = new ArtifactCollector();

				// Global properties
				Properties props = new Properties();
				props.put(CodeGenerationStrings.EJB3, "" + attributes.isEJB3Enabled()); //$NON-NLS-1$
				props.put(CodeGenerationStrings.JDK5, "" + attributes.isJDK5Enabled()); //$NON-NLS-1$

				for (int i = 0; i < exporterFactories.length; i++) {
					monitor.subTask(exporterFactories[i].getExporterDefinition().getDescription());

					Properties globalProperties = new Properties();
					globalProperties.putAll(props);

					Exporter exporter;
					try {
						exporter = exporterFactories[i].createConfiguredExporter(cfg, attributes.getOutputPath(), attributes.getTemplatePath(), globalProperties,
								outputDirectories, artifactCollector);
					} catch (CoreException e) {
						throw new HibernateConsoleRuntimeException(HibernateConsoleMessages.CodeGenerationLaunchDelegate_error_while_setting_up
								+ exporterFactories[i].getExporterDefinition(), e);
					}

					try {
						exporter.start();
					} catch (HibernateException he) {
						throw new HibernateConsoleRuntimeException(HibernateConsoleMessages.CodeGenerationLaunchDelegate_error_while_running
								+ exporterFactories[i].getExporterDefinition().getDescription(), he);
					}
					monitor.worked(1);
				}
				return artifactCollector;
			}

		});

	}

	private Configuration buildConfiguration(final ExporterAttributes attributes, ConsoleConfiguration cc, IWorkspaceRoot root) {
		final boolean reveng = attributes.isReverseEngineer();
		final String reverseEngineeringStrategy = attributes.getRevengStrategy();
		final boolean preferBasicCompositeids = attributes.isPreferBasicCompositeIds();
		final IResource revengres = PathHelper.findMember(root, attributes.getRevengSettings());

		if (reveng) {
			Configuration configuration = null;
			if (cc.hasConfiguration()) {
				configuration = cc.getConfiguration();
			} else {
				configuration = cc.buildWith(null, false);
			}

			final JDBCMetaDataConfiguration cfg = new JDBCMetaDataConfiguration();
			Properties properties = configuration.getProperties();
			cfg.setProperties(properties);
			cc.buildWith(cfg, false);

			cfg.setPreferBasicCompositeIds(preferBasicCompositeids);

			cc.execute(new Command() { // need to execute in the
				// consoleconfiguration to let it handle
				// classpath stuff!

				public Object execute() {
					// todo: factor this setup of revengstrategy to core
					ReverseEngineeringStrategy res = new DefaultReverseEngineeringStrategy();

					OverrideRepository repository = null;

					if (revengres != null) {
						File file = PathHelper.getLocation(revengres).toFile();
						repository = new OverrideRepository();
						repository.addFile(file);
					}

					if (repository != null) {
						res = repository.getReverseEngineeringStrategy(res);
					}

					if (reverseEngineeringStrategy != null && reverseEngineeringStrategy.trim().length() > 0) {
						res = loadreverseEngineeringStrategy(reverseEngineeringStrategy, res);
					}

					ReverseEngineeringSettings qqsettings = new ReverseEngineeringSettings(res).setDefaultPackageName(attributes.getPackageName())
							.setDetectManyToMany(attributes.detectManyToMany()).setDetectOneToOne(attributes.detectOneToOne())
							.setDetectOptimisticLock(attributes.detectOptimisticLock());

					res.setSettings(qqsettings);

					cfg.setReverseEngineeringStrategy(res);

					cfg.readFromJDBC();
					cfg.buildMappings();
					return null;
				}
			});

			return (Configuration) cfg;
		} else {
			cc.build();
			cc.buildMappings();
			return cc.getConfiguration();
		}
	}

	public void buildMavenProject(String projectName) throws IOException, ParserConfigurationException, CoreException, TransformerException, MavenInvocationException {
		MavenJavaProjectBuilder javaProjectBuilder = new MavenJavaProjectBuilder();
		IJavaProject mavenJProject = javaProjectBuilder.buildBasicProject(projectName, "WebContent/WEB-INF/classes", "src", "com.ias.test");

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		String folder = workspace.getRoot().getLocation().toFile().getPath().toString();
		String pomPath = folder + File.separator + mavenJProject.getElementName() + File.separator + "pom.xml";

		ProjectResolver projectResolver = ProjectResolver.getInstance();
		File xmlFile = new File(pomPath);
		if (!xmlFile.exists()) {
			xmlFile.createNewFile();
		}
		WarPomBuilder warPomBuilder = new WarPomBuilder(xmlFile);
		warPomBuilder.buildWarPomFile(projectName);
		InvocationRequest request = new DefaultInvocationRequest();
		request.setPomFile(xmlFile);
		request.setGoals(Collections.singletonList("compile"));
		Invoker invoker = new DefaultInvoker();
		invoker.execute(request);
		ProjectUtil projectUtil = new ProjectUtil();
		projectUtil.refreshProject(mavenJProject.getResource());
		System.out.println("Finished\nupdating maven");
		mavenUpdateProject(mavenJProject);
	}

	// TODO: merge with revstrategy load in JDBCConfigurationTask
	@SuppressWarnings("unchecked")
	private ReverseEngineeringStrategy loadreverseEngineeringStrategy(final String className, ReverseEngineeringStrategy delegate) {
		try {
			Class<ReverseEngineeringStrategy> clazz = ReflectHelper.classForName(className);
			Constructor<ReverseEngineeringStrategy> constructor = clazz.getConstructor(new Class[] { ReverseEngineeringStrategy.class });
			return constructor.newInstance(new Object[] { delegate });
		} catch (NoSuchMethodException e) {
			try {
				Class<?> clazz = ReflectHelper.classForName(className);
				ReverseEngineeringStrategy rev = (ReverseEngineeringStrategy) clazz.newInstance();
				return rev;
			} catch (Exception eq) {
				String out = NLS.bind(HibernateConsoleMessages.CodeGenerationLaunchDelegate_could_not_create_or_find_with_default_noarg_constructor, className);
				throw new HibernateConsoleRuntimeException(out, eq);
			}
		} catch (Exception e) {
			String out = NLS.bind(HibernateConsoleMessages.CodeGenerationLaunchDelegate_could_not_create_or_find_with_one_argument_delegate_constructor, className);
			throw new HibernateConsoleRuntimeException(out, e);
		}
	}

	public void refreshMaven(IJavaProject iJavaProject) throws CoreException {
		IMavenProjectFacade iMavenProjectFacade = MavenPlugin.getMavenProjectRegistry().getProject(iJavaProject.getProject());
		IFile iPomFile = iMavenProjectFacade.getPom();
		List<IFile> refreshFiles = new ArrayList<IFile>();
		refreshFiles.add(iPomFile);
		MavenPlugin.getMavenProjectRegistry().refresh(refreshFiles, null);
	}

	public void mavenUpdateProject(final IJavaProject iJavaProject) throws CoreException {
		IProjectConfigurationManager configurationManager = MavenPlugin.getProjectConfigurationManager();
		IProject project = iJavaProject.getProject();

		configurationManager.updateProjectConfiguration(project, new NullProgressMonitor());

		// Job job = new Job("My First Job") {
		// protected IStatus run(IProgressMonitor monitor) {
		// IProjectConfigurationManager configurationManager =
		// MavenPlugin.getProjectConfigurationManager();
		// try {
		// IProject project = iJavaProject.getProject();
		//
		// configurationManager.updateProjectConfiguration(project, new
		// NullProgressMonitor());
		// } catch (Exception e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// System.out.println("Project updated");
		// return Status.OK_STATUS;
		// }
		// };
		// job.schedule();

		// Display.getDefault().asyncExec(new Runnable() {
		// public void run() {
		// try {
		// IProjectConfigurationManager configurationManager =
		// MavenPlugin.getProjectConfigurationManager();
		// configurationManager.updateProjectConfiguration(iJavaProject.getProject(),
		// null);
		// System.out.println("Project updated");
		// } catch (JavaModelException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (CoreException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		// });

		// IMavenProjectFacade iMavenProjectFacade =
		// MavenPlugin.getMavenProjectRegistry().getProject(iJavaProject.getProject());
		// IFile iPomFile = iMavenProjectFacade.getPom();
		// List<IFile> refreshFiles = new ArrayList<IFile>();
		// // refreshFiles.add(iPomFile);
		// MavenPlugin.getMavenProjectRegistry().refresh(refreshFiles, null);
		// ResolverConfiguration resolverConfiguration =
		// iMavenProjectFacade.getResolverConfiguration();
		// MavenImpl mavenImpl = new
		// MavenImpl(MavenPlugin.getMavenConfiguration());
		// LifecycleMappingFactory lifecycleMappingFactory = new
		// LifecycleMappingFactory();
	}

	public void buildProjectsFromMysql() {
		DAOReader daoReader = new DAOReader();
		daoReader.setConnection();
		JavaClassFormatter classFormatter = new JavaClassFormatter();
		try {
			ArrayList<DatabaseTable> databaseTables = daoReader.getDatabaseTables();
			for (DatabaseTable databaseTable2 : databaseTables) {

			}

			ProjectResolver projectResolver = ProjectResolver.getInstance();
			IJavaProject iJavaProject = projectResolver.getInstance().resolveProject("Test");
			iJavaProject.open(new NullProgressMonitor());
			IPackageFragmentRoot[] iPackageFragmentRoots = iJavaProject.getPackageFragmentRoots();
			IPackageFragment iPackageFragment = null;
			for (IPackageFragmentRoot iPackageFragmentRoot : iPackageFragmentRoots) {
				if (iPackageFragmentRoot.getElementName().equals("src")) {
					iPackageFragment = iPackageFragmentRoot.getPackageFragment("com.ias.test.beans");
				}
			}
			iPackageFragment.open(new NullProgressMonitor());
			// iPackageFragment.open(new NullProgressMonitor());
			System.out.println("Name: " + iPackageFragment.getElementName());

			for (DatabaseTable databaseTable : databaseTables) {

				JavaClassGenerator classGenerator = new JavaClassGenerator(iJavaProject, iPackageFragment);
				classGenerator.createClass(databaseTable, "com.ias.test.beans", databaseTable.getName(), true, true, true);
				CompilationUnit cu = classGenerator.getCu();
				IJavaElement classPath = cu.getJavaElement();
				System.out.println(classPath);
				String source = classFormatter.format(cu.toString());
				ICompilationUnit icu = iPackageFragment.createCompilationUnit(databaseTable.getName() + ".java", source, true, new NullProgressMonitor());
				ProjectUtil projectUtil = new ProjectUtil();
				projectUtil.refreshProject(iJavaProject.getResource());
			}
			System.out.println("Done building java project");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedTreeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (org.eclipse.jface.text.BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
