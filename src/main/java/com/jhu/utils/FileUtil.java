package com.jhu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
/**
 * FileUtil to provide quick methods to reading write data from files.
 * @author James Hu
 *
 */
public final class FileUtil {
	
	//Get properties based of properties file name
	//The file name is relative to the root class directory.
	public static Properties getConfigProperties(String fileName)  {
		Properties prop = new Properties();
		InputStream input = null;
		
		try {
			input = ClassLoader.getSystemResourceAsStream(fileName);
			if (input != null ) {
				prop.load(input);
			} 
		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return prop;
	}
	
}
