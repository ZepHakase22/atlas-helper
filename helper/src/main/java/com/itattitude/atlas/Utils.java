package com.itattitude.atlas;

public class Utils {
	public static boolean isDebug() {
		return java.lang.management.ManagementFactory.getRuntimeMXBean().
		    getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;
	}
}

