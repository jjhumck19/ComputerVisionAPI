package com.jhu.azureapi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.jhu.utils.FileUtil;

/**
 * Azure Computer Vision API java class 
 * 
 * @author James Hu
 *
 */
public class ComputerVisionAPI {

	public final static String ANALYZE = "analyze";
	public final static String DESCRIBE = "describe";
	public final static String TEXTOPERATIONS = "textOperations";
	public final static String GENERATETHUMBNAIL = "generateThumbnail";
	public final static String MODELS = "models";
	public final static String OCR = "ocr";
	public final static String RECOGNIZETEXT = "recognizeText";
	public final static String TAG = "tag";

	private final static String CV_SUBSCRIPTION_KEY = "CV_SubscriptionKey";
	private final static String CV_URI_BASE = "CV_UriBase";
	private final static String CV_CONFIG_FILE_NAME = "Config.properties";

	private static final Set<String> SUPPORT_REGIONS;
	static {
		SUPPORT_REGIONS = new HashSet<String>();
		SUPPORT_REGIONS.add("eastus");
		SUPPORT_REGIONS.add("westus");
		SUPPORT_REGIONS.add("eastus2");
		SUPPORT_REGIONS.add("westus2");
		SUPPORT_REGIONS.add("southcentralus");
		SUPPORT_REGIONS.add("westeurope");
		SUPPORT_REGIONS.add("northeurope");
		SUPPORT_REGIONS.add("southeastasia");
		SUPPORT_REGIONS.add("eastasia");
		SUPPORT_REGIONS.add("australiaeast");
		SUPPORT_REGIONS.add("brazilsouth");
	}

	public final static List<String> ALLFEATURES = Arrays.asList("Categories", "Description", "Color", "Faces", "Tags",
			"Adult");
	public final static List<String> ALLDETAILS = Arrays.asList("Landmarks", "Celebrities");

	// Hard coded the subscriptionKey and uri base based on initial subscription at coding time.
	// These hard coded values may not work in the future when initial subscription expired. 
	// User should set configuration properties CV_SubscriptionKey and CV_UriBase 
	// in Config.properties file under the class root directory.  
	private static String HARD_CODED_KEY = "3f2e48b7e47b4596b1e1ae89701915f9";
	private static String HARD_CODED_URIBASE = "https://eastus.api.cognitive.microsoft.com/vision/v1.0/";

	// Handle default based on config.properties if defined otherwise use hard coded
	// values
	private static String DEFAULT_KEY;
	private static String DEFAULT_URIBASE;
	static {
		loadConfig();
	}

	private String subscriptionKey = DEFAULT_KEY;
	private String uriBase = DEFAULT_URIBASE;

	public ComputerVisionAPI() {
	}

	public ComputerVisionAPI(String sKey, String uriBase) {
		subscriptionKey = sKey;
		this.uriBase = uriBase;
	}

	private static void loadConfig() {
		Properties prop = FileUtil.getConfigProperties(CV_CONFIG_FILE_NAME);
		String skey = prop.getProperty(CV_SUBSCRIPTION_KEY);
		if (ApiUtil.isValidKey(skey)) {
			DEFAULT_KEY = skey;
		} else {
			DEFAULT_KEY = HARD_CODED_KEY;
			System.out.println(
					"Warning: please set subscription key in Config.properties. Try to use hard coded default for now");
		}

		String uriBase = prop.getProperty(CV_URI_BASE);
		if (ApiUtil.isHttpUrl(uriBase)) {
			DEFAULT_URIBASE = uriBase;
		} else {
			DEFAULT_URIBASE = HARD_CODED_URIBASE;
			System.out.println(
					"Warning: please set uri base in Config.properties.  Try to use hard coded default for now");
		}
	}

	//Normal getters and setters
	public String getSubscriptionKey() {
		return subscriptionKey;
	}

	public void setSubscriptionKey(String subscriptionKey) {
		this.subscriptionKey = subscriptionKey;
	}

	public String getUriBase() {
		return uriBase;
	}

	public void setUriBase(String uriBase) {
		this.uriBase = uriBase;
	}

	//Other methods
	public void changeRegion(String region) {
		if (isSupporttedRegion(region)) {
			int start = uriBase.indexOf("//") + 2;
			int end = uriBase.indexOf(".");
			uriBase = uriBase.substring(0, start) + region + uriBase.substring(end);
		}
	}

	public void reset() {
		subscriptionKey = DEFAULT_KEY;
		uriBase = DEFAULT_URIBASE;
	}
	
	private boolean isSupporttedRegion(String region) {
		return SUPPORT_REGIONS.contains(region);
	}

	//API urls
	public String getAnalyzeURL() {
		return uriBase + ANALYZE;
	}

	public String getDscribeURL() {
		return uriBase + DESCRIBE;
	}

	public String getGenerateThumbnailURL() {
		return uriBase + GENERATETHUMBNAIL;
	}

	public String getOcrURL() {
		return uriBase + OCR;
	}

	public String getRecognizeTextURL() {
		return uriBase + RECOGNIZETEXT;
	}

	public String getTagURL() {
		return uriBase + TAG;
	}

	public String getDomainAnalyzeURL(DomainModel model) {
		return uriBase + MODELS + "/" + model.getName() + "/" + ANALYZE;
	}

	public String getModelsURL() {
		return uriBase + MODELS;
	}
}
