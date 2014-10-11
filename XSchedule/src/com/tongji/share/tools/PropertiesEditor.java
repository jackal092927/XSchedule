package com.tongji.share.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class PropertiesEditor {
	private static String PROPERTIES_PATH_PRE = "./resources/com/tongji/jackal/";
	private static String PROPERTIES_PATH_POST = ".properties";
	
	private static Properties getProperties(String propertiesName) {
		try {
			Properties properties = new Properties();
			
			FileInputStream fis = new FileInputStream(PROPERTIES_PATH_PRE + propertiesName + PROPERTIES_PATH_POST);
			properties.load(fis);
			return properties;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getProperty(String propertiesName, String key){
		return getProperties(propertiesName).getProperty(key);
	}
	
	public static void setProperty(String propertiesName, String key, String value){
		getProperties(propertiesName).setProperty(key, value);
	}
	
	
}
