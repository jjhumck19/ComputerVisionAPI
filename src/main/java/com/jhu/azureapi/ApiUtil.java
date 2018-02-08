package com.jhu.azureapi;

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
