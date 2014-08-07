package com.ias.webdesigner.designer.builders;

import java.io.File;
import java.io.IOException;

import com.ias.webdesigner.designer.exception.FileCreationFailedException;

public class FileBuilder {
	/**
	 * 
	 * @param pathname
	 * @return
	 * @throws IOException
	 *             - If an I/O error occurred
	 * @throws SecurityException
	 *             - If a security manager exists and its
	 *             SecurityManager.checkWrite(java.lang.String) method denies
	 *             write access to the
	 * @throws FileCreationFailedException
	 */
	public static File buildFile(String pathname) throws IOException,
			SecurityException, FileCreationFailedException {
		File file = new File(pathname);
		boolean success = false;
		if (file.exists() && file.isFile()) {
			return file;
		} else {
			success = file.createNewFile();
		}
		if (success)
			return file;
		throw new FileCreationFailedException();
	}

	/**
	 * 
	 * @param pathname
	 * @return
	 * @throws SecurityException
	 *             - If a security manager exists and its
	 *             SecurityManager.checkWrite(java.lang.String) method denies
	 *             write access to the
	 * @throws FileCreationFailedException
	 */
	public static File buildDir(String pathname) throws SecurityException,
			FileCreationFailedException {
		File file = new File(pathname);
		boolean success = false;
		if (file.exists() && file.isDirectory()) {
			return file;
		} else {
			success = file.mkdir();
		}
		if (success)
			return file;
		throw new FileCreationFailedException();
	}
}
