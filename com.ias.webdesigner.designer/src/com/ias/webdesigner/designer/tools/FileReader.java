package com.ias.webdesigner.designer.tools;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileReader {
	private BufferedReader reader;

	public FileReader() {
	}

	public void init(String path) throws FileNotFoundException {
		reader = new BufferedReader(new java.io.FileReader(path));
	}

	public String readLine() throws IOException {
		return reader.readLine();
	}

	public String getRemainingContent() throws IOException {
		String content = "";
		String line = null;
		while ((line = reader.readLine()) != null) {
			content += line;
		}
		return content;
	}

	public String getContent(String path) throws IOException {
		BufferedReader reader = new BufferedReader(new java.io.FileReader(path));
		String content = "";
		String line = null;
		while ((line = reader.readLine()) != null) {
			content += line;
		}
		return content;
	}
}
