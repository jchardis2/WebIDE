package com.ias.webide.debug;

import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;

public class LaunchConfigurationHelper {

	public void printAttributes(ILaunchConfiguration iLaunchConfiguration) throws CoreException {
		Map<String, Object> map2 = iLaunchConfiguration.getAttributes();
		Set<String> set = map2.keySet();
		for (String string : set) {
			System.out.println("Key: " + string);
			Object o = map2.get(string);
			System.out.println(map2.get(string));
		}
	}
}
