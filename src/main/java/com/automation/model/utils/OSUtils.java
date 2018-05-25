package com.automation.model.utils;

public class OSUtils {
	
	public static String getOsName() {
		return System.getProperty("os.name");
	}
	
	public static boolean isWindows() {
		return getOsName().startsWith("Windows");
	}
	
	public static boolean isUnix() {
		return getOsName().startsWith("Unix");
	}
	
	public static boolean isLinux() {
		return getOsName().startsWith("Linux");
	}
}
