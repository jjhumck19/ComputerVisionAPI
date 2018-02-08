package com.jhu.azureapi;

/**
 * Api Util provide quick static methods for API related data checking and helper methods.
 * @author James Hu
 *
 */
public final class ApiUtil {
	private ApiUtil() {
	}
	
	public static  boolean isHttpUrl(String url) {
		boolean flag = false;
		if (url != null && url.startsWith("http")) {
			flag = true;
		}
		return flag;
	}
	
	//TODO add more rules to check the key. 
	public static  boolean isValidKey(String key) {
		boolean flag = false;
		if (key != null && key.length() > 20) {
			flag = true;
		}
		return flag;
	}

}
