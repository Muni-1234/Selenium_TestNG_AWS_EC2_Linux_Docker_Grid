package com.sauce;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Common_Utilities {
	
	public String getpropertyvalue(String keyvalue, String FileName) throws IOException {
		
		Properties prop = new Properties();
		try {
			prop.load(new FileInputStream(FileName));
			
		} catch (FileNotFoundException e ) {
			e.printStackTrace();
		}
		return prop.getProperty(keyvalue);
	}

}
