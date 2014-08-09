package com.ias.webide.plugin.tools;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLDomParser {
	private File xmlFile;
	DocumentBuilderFactory dbFactory;
	DocumentBuilder dBuilder;
	Document doc;

	public XMLDomParser() {
	}

	public void init(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		this.xmlFile = xmlFile;
		dbFactory = DocumentBuilderFactory.newInstance();
		dBuilder = dbFactory.newDocumentBuilder();
		doc = dBuilder.parse(xmlFile);
		doc.getDocumentElement().normalize();
	}
}
