package com.jhu.azureapi;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComputerVisionAPI {

	 public final static String ANALYZE ="analyze";
	 public final static String DESCRIBE ="describe";
	 public final static String TEXTOPERATIONS ="textOperations";
	 public final static String GENERATETHUMBNAIL ="generateThumbnail";
	 public final static String MODELS ="models";
	 public final static String OCR ="ocr";
	 public final static String RECOGNIZETEXT ="recognizeText";
	 public final static String TAG ="tag";
	 
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
	 
	 public final static List<String> ALLFEATURES = Arrays.asList("Categories" ,"Description","Color","Faces","Tags", "Adult");
	 public final static List<String> ALLDETAILS = Arrays.asList( "Landmarks","Celebrities");
	 
	 // Replace the subscriptionKey string value with your valid subscription key.
	 private static String DEFAULT_KEY = "3f2e48b7e47b4596b1e1ae89701915f9";
	 private static String DEFAULT_URIBASE = "https://eastus.api.cognitive.microsoft.com/vision/v1.0/";
	 
	 private String subscriptionKey = DEFAULT_KEY ;
	 private String uriBase = DEFAULT_URIBASE;
	 
	 public ComputerVisionAPI() {
	 }

	 public ComputerVisionAPI(String sKey, String uriBase) {
		 subscriptionKey = sKey;
		 this.uriBase = uriBase;
	 }
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
	
	public void changeRegion(String region) {
		if(isSupporttedRegion(region)) {
			int start = uriBase.indexOf("//")+2;
			int end = uriBase.indexOf(".");
		    uriBase = uriBase.substring(0, start) + region +uriBase.substring(end);
		} 
	}
	
	public void reset() {
		subscriptionKey = DEFAULT_KEY ;
		uriBase = DEFAULT_URIBASE;
	}
	
	private boolean isSupporttedRegion(String region) {
		return SUPPORT_REGIONS.contains(region);
	}
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

	public String getDomainAnalyzeURL(DomainModel model ) {
		return uriBase + MODELS + "/" + model.getName() + "/"  + ANALYZE;
	}
	
	public String getModelsURL() {
		return uriBase + MODELS;
	}
}
