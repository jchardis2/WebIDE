package com.ias.webide.java.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Element;

import com.ias.webide.plugin.util.XMLDomBuilder;

/**
 * Builds a maven pom
 * 
 * Must call init
 * 
 * @author Jimmy Hardison
 *
 */
public class BasicPomBuilder {
	protected XMLDomBuilder xmlDomBuilder;
	private Element rootElement;
	private Element buildElement;
	private Element plugins;
	private Element dependencies;
	private Element repositories;

	public static void main(String args[]) throws TransformerException, IOException {
		String name = "WebIDE";
		FileInputStream propFile = new FileInputStream("maven.config.properties");
		Properties p = new Properties(System.getProperties());
		p.load(propFile);
		System.setProperties(p);
		Properties sp = System.getProperties();
		try {
			BasicPomBuilder basicPomBuilder = new BasicPomBuilder(new File("test.pom.xml"));
			XMLDomBuilder xmlDomBuilder = basicPomBuilder.getXmlDomBuilder();
			basicPomBuilder.appendRootElement();
			basicPomBuilder.appendBasicChildren("WebIDE", "WebIDE", "0.0.1", "war");
			basicPomBuilder.appendBuildElement(sp.getProperty("com.ias.webide.plugin.maven.build.sourceDirectory"), name,
					sp.getProperty("com.ias.webide.plugin.maven.build.outputDirectory"));
			basicPomBuilder.appendPluginsElement();

			Element configuration = basicPomBuilder.getXmlDomBuilder().createElement("configuration");
			xmlDomBuilder.appendAndCreateElement("source", sp.getProperty("com.ias.webide.plugin.maven.build.compiler.plugin.configuration.source"), configuration);
			xmlDomBuilder.appendAndCreateElement("target", sp.getProperty("com.ias.webide.plugin.maven.build.compiler.plugin.configuration.target"), configuration);
			basicPomBuilder.appendPlugin(sp.getProperty("com.ias.webide.plugin.maven.build.compiler.plugin"),
					sp.getProperty("com.ias.webide.plugin.maven.build.compiler.plugin.version"), configuration);

			configuration = basicPomBuilder.getXmlDomBuilder().createElement("configuration");
			xmlDomBuilder.appendAndCreateElement("warSourceDirectory", sp.getProperty("com.ias.webide.plugin.maven.build.war.plugin.warSourceDirectory"), configuration);
			xmlDomBuilder.appendAndCreateElement("failOnMissingWebXml", sp.getProperty("com.ias.webide.plugin.maven.build.war.plugin.failOnMissingWebXml"), configuration);
			basicPomBuilder.appendPlugin(sp.getProperty("com.ias.webide.plugin.maven.build.war.plugin"), sp.getProperty("com.ias.webide.plugin.maven.build.war.plugin.version"),
					configuration);

			basicPomBuilder.appendDependenciesElement();
			basicPomBuilder.appendDependency("com.sun.faces", "com.sun.faces", "2.2.7");

			basicPomBuilder.writeToFile();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public BasicPomBuilder(File xmlFile) throws ParserConfigurationException {
		xmlDomBuilder = new XMLDomBuilder(xmlFile);
	}

	public Element appendRootElement() {
		Properties sp = System.getProperties();
		rootElement = xmlDomBuilder.appendRootElement("project");
		xmlDomBuilder.appendAttribute(rootElement, new BasicNameValuePair("xmlns", sp.getProperty("com.ias.webide.plugin.maven.xmlns")));
		xmlDomBuilder.appendAttribute(rootElement, new BasicNameValuePair("xmlns:xsi", sp.getProperty("com.ias.webide.plugin.maven.xmlns.xsi")));
		xmlDomBuilder.appendAttribute(rootElement, new BasicNameValuePair("xsi:schemaLocation", sp.getProperty("com.ias.webide.plugin.maven.xmlns.xsi.schemaLocation")));
		return rootElement;
	}

	public void appendBasicChildren(String groupID, String artifactId, String version) {
		xmlDomBuilder.appendAndCreateElement("modelVersion", System.getProperty("com.ias.webide.plugin.maven.model.version"), rootElement);
		xmlDomBuilder.appendAndCreateElement("groupId", groupID, rootElement);
		xmlDomBuilder.appendAndCreateElement("artifactId", artifactId, rootElement);
		xmlDomBuilder.appendAndCreateElement("version", version, rootElement);
	}

	public void appendBasicChildren(String groupID, String artifactId, String version, String packaging) {
		appendBasicChildren(groupID, artifactId, version);
		xmlDomBuilder.appendAndCreateElement("packaging", packaging, rootElement);
	}

	public void appendBuildElement(String sourceDirectory, String finalName, String outputDirectory) {
		buildElement = xmlDomBuilder.createElement("build");
		if (sourceDirectory != null)
			xmlDomBuilder.appendAndCreateElement("sourceDirectory", sourceDirectory, buildElement);
		if (finalName != null)
			xmlDomBuilder.appendAndCreateElement("finalName", finalName, buildElement);
		if (outputDirectory != null)
			xmlDomBuilder.appendAndCreateElement("outputDirectory", outputDirectory, buildElement);
		xmlDomBuilder.appendElement(buildElement, rootElement);
	}

	public void appendPluginsElement() {
		plugins = xmlDomBuilder.appendAndCreateElement("plugins", buildElement);
	}

	public Element appendPlugin(String artifactId, String version) {
		Element plugin = xmlDomBuilder.appendAndCreateElement("plugin", plugins);
		xmlDomBuilder.appendAndCreateElement("artifactId", artifactId, plugin);
		xmlDomBuilder.appendAndCreateElement("version", version, plugin);
		return plugin;
	}

	public Element appendPlugin(String artifactId, String version, Element configuration) {
		Element plugin = appendPlugin(artifactId, version);
		xmlDomBuilder.appendElement(configuration, plugin);
		return plugin;
	}

	public Element appendDependenciesElement() {
		dependencies = xmlDomBuilder.appendAndCreateElement("dependencies", rootElement);
		return dependencies;
	}

	public Element appendDependency(String groupId, String artifactId, String version) {
		Element dependency = xmlDomBuilder.appendAndCreateElement("dependency", dependencies);
		if (groupId != null)
			xmlDomBuilder.appendAndCreateElement("groupId", groupId, dependency);
		if (artifactId != null)
			xmlDomBuilder.appendAndCreateElement("artifactId", artifactId, dependency);
		if (version != null)
			xmlDomBuilder.appendAndCreateElement("version", version, dependency);
		return dependency;
	}

	public Element appendDependency(Element dependency) {
		xmlDomBuilder.appendElement(dependency, dependencies);
		return dependency;
	}

	public Element appendRepositoriesElement() {
		repositories = xmlDomBuilder.appendAndCreateElement("repositories", rootElement);
		return repositories;
	}

	public Element appendRepository(String id, String name, String url, String layout) {
		Element repository = xmlDomBuilder.appendAndCreateElement("dependency", repositories);
		if (id != null)
			xmlDomBuilder.appendAndCreateElement("id", id, repositories);
		if (name != null)
			xmlDomBuilder.appendAndCreateElement("name", name, repositories);
		if (url != null)
			xmlDomBuilder.appendAndCreateElement("url", url, repositories);
		if (layout != null)
			xmlDomBuilder.appendAndCreateElement("layout", layout, repositories);
		return repositories;
	}

	public Element appendRepository(Element repository) {
		xmlDomBuilder.appendElement(repository, repositories);
		return repository;
	}

	public void writeToFile() throws TransformerException {
		xmlDomBuilder.writeToFile();
	}

	public XMLDomBuilder getXmlDomBuilder() {
		return xmlDomBuilder;
	}

}
