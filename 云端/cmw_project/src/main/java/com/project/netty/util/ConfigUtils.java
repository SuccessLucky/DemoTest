package com.project.netty.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtils {

	/**
	 * 心跳数据
	 */
	public static final String HEARTBEAT_DATA = "heartbeat";

	private static final Properties pros = new Properties();
	
	static {
		InputStream input = ConfigUtils.class.getClassLoader().getResourceAsStream("properties/conf.properties");
		try {
			pros.load(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	public static String getProperty(String key) {
		return pros.getProperty(key);
	}
}
