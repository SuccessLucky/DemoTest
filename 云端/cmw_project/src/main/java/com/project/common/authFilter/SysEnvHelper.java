package com.project.common.authFilter;

public class SysEnvHelper {
	private static final String ACTION_SUFFIX = "do/ajax/action/json";
	
	
	public static String[] getActionSuffix(){
		return ACTION_SUFFIX.split("/");
	}
}
