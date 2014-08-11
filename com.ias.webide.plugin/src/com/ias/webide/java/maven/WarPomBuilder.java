package com.ias.webide.java.maven;

import java.io.File;
import java.util.Properties;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Element;

public class WarPomBuilder extends BasicPomBuilder {

	public WarPomBuilder(File xmlFile) throws ParserConfigurationException {
		super(xmlFile);
	}

	public void buildWarPomFile(String finalName) throws TransformerException {
		Properties sp = System.getProperties();
		appendRootElement();
		appendBasicChildren("WebIDE", "WebIDE", "0.0.1", "war");
		appendBuildElement(sp.getProperty("com.ias.webide.plugin.maven.build.sourceDirectory"), finalName, sp.getProperty("com.ias.webide.plugin.maven.build.outputDirectory"));
		appendPluginsElement();

		Element configuration = getXmlDomBuilder().createElement("configuration");
		xmlDomBuilder.appendAndCreateElement("source", sp.getProperty("com.ias.webide.plugin.maven.build.compiler.plugin.configuration.source"), configuration);
		xmlDomBuilder.appendAndCreateElement("target", sp.getProperty("com.ias.webide.plugin.maven.build.compiler.plugin.configuration.target"), configuration);
		appendPlugin(sp.getProperty("com.ias.webide.plugin.maven.build.compiler.plugin"), sp.getProperty("com.ias.webide.plugin.maven.build.compiler.plugin.version"),
				configuration);

		configuration = getXmlDomBuilder().createElement("configuration");
		xmlDomBuilder.appendAndCreateElement("warSourceDirectory", sp.getProperty("com.ias.webide.plugin.maven.build.war.plugin.warSourceDirectory"), configuration);
		xmlDomBuilder.appendAndCreateElement("failOnMissingWebXml", sp.getProperty("com.ias.webide.plugin.maven.build.war.plugin.failOnMissingWebXml"), configuration);
		appendPlugin(sp.getProperty("com.ias.webide.plugin.maven.build.war.plugin"), sp.getProperty("com.ias.webide.plugin.maven.build.war.plugin.version"), configuration);

		appendDependenciesElement();
		appendDependency("com.sun.faces", "jsf-api", "2.2.7");

		writeToFile();
	}
}
