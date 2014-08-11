package com.ias.webide.plugin.tools;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.NameValuePair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class XMLDomBuilder {
	private File xmlFile;
	DocumentBuilderFactory docFactory;
	DocumentBuilder docBuilder;
	Document doc;
	Element rootElement;

	public XMLDomBuilder(File xmlFile) throws ParserConfigurationException {
		this.xmlFile = xmlFile;
		docFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		rootElement = null;
	}

	/**
	 * Inits the Builder for reuse
	 * 
	 * @param xmlFile
	 *            the xml file to write to
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void init(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		this.xmlFile = xmlFile;
		docFactory = DocumentBuilderFactory.newInstance();
		docBuilder = docFactory.newDocumentBuilder();
		doc = docBuilder.newDocument();
		rootElement = null;
	}

	public Element appendRootElement(String name) {
		docBuilder.newDocument();
		rootElement = doc.createElement(name);
		doc.appendChild(rootElement);
		return rootElement;
	}

	public Element appendRootElement(String name, NameValuePair nameValuePair, String value) {
		appendRootElement(name);
		appendAttribute(rootElement, nameValuePair);
		appendValue(rootElement, value);
		return rootElement;
	}

	public Element appendRootElement(String name, List<NameValuePair> nameValuePairs, String value) {
		appendRootElement(name);
		appendAttributes(rootElement, nameValuePairs);
		appendValue(rootElement, value);
		return rootElement;
	}

	public Element createElement(String name) {
		Element element = doc.createElement(name);
		return element;
	}

	public Element createElement(String name, List<NameValuePair> nameValuePairs, String value) {
		Element element = createElement(name);
		if (nameValuePairs != null && nameValuePairs.size() > 0) {
			appendAttributes(element, nameValuePairs);
		}
		if (value != null) {
			appendValue(element, value);
		}
		return element;
	}

	public Element createElement(String name, String value) {
		Element element = createElement(name);
		appendValue(element, value);
		return element;
	}

	public Element appendAttributes(Element element, List<NameValuePair> nameValuePairs) {
		for (NameValuePair nameValuePair : nameValuePairs) {
			appendAttribute(element, nameValuePair);
		}
		return element;
	}

	public Element appendAttribute(Element element, NameValuePair nameValuePair) {
		element.setAttribute(nameValuePair.getName(), nameValuePair.getValue());
		return element;
	}

	public Element appendValue(Element element, String value) {
		element.appendChild(doc.createTextNode(value));
		return element;
	}

	public Node appendElement(Element element, Element parent) {
		return parent.appendChild(element);
	}

	public Element appendAndCreateElement(String name, List<NameValuePair> nameValuePairs, String value, Element parent) {
		Element element = createElement(name, nameValuePairs, value);
		appendElement(element, parent);
		return element;
	}

	public Element appendAndCreateElement(String name, String value, Element parent) {
		Element element = createElement(name, value);
		appendElement(element, parent);
		return element;
	}

	public Element appendAndCreateElement(String name, Element parent) {
		Element element = createElement(name);
		appendElement(element, parent);
		return element;
	}

	public void writeToFile() throws TransformerException {
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		System.out.println("File: ");
		StreamResult result = new StreamResult(System.out);
		transformer.transform(source, result);
	}
}
