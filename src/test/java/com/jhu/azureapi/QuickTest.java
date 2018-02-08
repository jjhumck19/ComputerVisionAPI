package com.jhu.azureapi;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Properties;

import org.json.JSONObject;

import com.jhu.utils.FileUtil;
import com.jhu.utils.GUIHelper;

//Just place to do a quick tests for methods. 
public class QuickTest {
	
	public void testAnalyzeImageUrl() {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		String imageUrl = "http://cdn.onlyinyourstate.com/wp-content/uploads/2016/06/3-Cathedral.jpg";
		showResult(api.analyzeImage(imageUrl));
		
		imageUrl =  "D:/data/photo1.png";
		showResult(api.analyzeImage(imageUrl));
	}
	
	
	public void testAnalyzeImageLocalFile() {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		
		String fileName = "D:/data/photo1.png";
        File myFile=new File(fileName);
        showResult(api.analyzeImage(myFile));
	}
	
	
	public void testAnalyzeDomainModel() {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/2/23/Space_Needle_2011-07-04.jpg";
		showResult(api.analyzeDomainModel(imageUrl, DomainModel.LANDMARKS));

		imageUrl = "D:/data/photo1.png";
		showResult(api.analyzeDomainModel(imageUrl, DomainModel.LANDMARKS));
	}
	
	public void testGenerateThumbnail() {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/2/23/Space_Needle_2011-07-04.jpg";
		GUIHelper.displayImage(api.generateThumbnail(imageUrl));
	

		imageUrl = "D:/data/photo1.png";
		GUIHelper.displayImage(api.generateThumbnail(imageUrl));
		
		imageUrl = "D:/data/photo2.png";
		GUIHelper.displayImage(api.generateThumbnail(imageUrl));
		
		
		imageUrl = "D:/data/photo2.png";
		GUIHelper.displayImage(api.generateThumbnail(imageUrl, 50, 50, true));
		
		imageUrl = "D:/data/photo2.png";
		GUIHelper.displayImage(api.generateThumbnail(imageUrl, 50, 50, false));
	}
	
	
	public void testAnalyzeManytimes() {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		
		String fileName = "D:/data/photo1.png";
        File myFile=new File(fileName);
        showResult(api.analyzeImage(myFile));
		
		String imageUrl = "http://cdn.onlyinyourstate.com/wp-content/uploads/2016/06/3-Cathedral.jpg";
		showResult(api.analyzeImage(imageUrl));
		
		
		imageUrl = "http://www.planetware.com/photos-tiles/pennsyvlania-pittsburgh-city-bridge-boat.jpg";
		showResult(api.analyzeImage(imageUrl));
		
		imageUrl = "https://upload.wikimedia.org/wikipedia/commons/2/23/Space_Needle_2011-07-04.jpg";
		showResult(api.analyzeDomainModel(imageUrl, DomainModel.LANDMARKS));

	}
	
	public void testOcr()  {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Atomist_quote_from_Democritus.png/338px-Atomist_quote_from_Democritus.png";

		try {
		GUIHelper.displayImage(new URL(imageUrl).openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = api.ocrImage(imageUrl, "unk", true);
		System.out.println(json.toString(2));
	}
	
	
	public void testRecognizeText()  {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
//		String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/a/af/Atomist_quote_from_Democritus.png/338px-Atomist_quote_from_Democritus.png";
		String imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Cursive_Writing_on_Notebook_paper.jpg/800px-Cursive_Writing_on_Notebook_paper.jpg";
		try {
		GUIHelper.displayImage(new URL(imageUrl).openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONObject json = api.recognizeText(imageUrl);
		showResult(json);
	}
	
	public void testTag()  {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		String imageUrl = "http://www.planetware.com/photos-tiles/pennsyvlania-pittsburgh-city-bridge-boat.jpg";

		try {
			GUIHelper.displayImage(new URL(imageUrl).openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		showResult(api.tagImage(imageUrl));
		
		imageUrl = "D:/data/photo2.png";
		try {
			GUIHelper.displayImage(new FileInputStream( new File(imageUrl)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		showResult(api.tagImage(imageUrl));
	}
	
	
	public void testDescribeImage()  {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		String imageUrl = "http://www.planetware.com/photos-tiles/pennsyvlania-pittsburgh-city-bridge-boat.jpg";

		try {
			GUIHelper.displayImage(new URL(imageUrl).openStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		showResult(api.describeImage(imageUrl));
		
		
		imageUrl = "D:/data/photo2.png";
		try {
			GUIHelper.displayImage(new FileInputStream( new File(imageUrl)));
			} catch (Exception e) {
				e.printStackTrace();
			}
		showResult(api.describeImage(imageUrl, 3));
	}
	
	public void testChangeRegion()  {
		ComputerVisionAPI api = new ComputerVisionAPI() ;
		api.changeRegion("westus2");
		
		System.out.println("new url: " + api.getUriBase());
		api.changeRegion("northeurope");
		System.out.println("new url: " + api.getUriBase());
	}
	
	
	public void testGetModles()  {
		ComputerVisionWrapper api = new  ComputerVisionWrapper( new ComputerVisionAPI()) ;
		showResult(api.getModels());
	}
	
	
	public void testGetProperties() {
		String fileName = "Config.properties1";
		Properties prop = FileUtil.getConfigProperties(fileName);
		System.out.println("######## prop: " + prop);
		System.setProperties(prop);
		String skey = System.getProperty("CV_SubscriptionKey");
		if (ApiUtil.isValidKey(skey)) {
			System.out.println("set key:" + skey);
		}
		String uriBase = System.getProperty("CV_UriBase");
		if (ApiUtil.isHttpUrl(uriBase)) {
			System.out.println("set uribase:" + uriBase);
		}
	}
	
	public void testInitConfig() {
		ComputerVisionAPI api = new ComputerVisionAPI();
		System.out.println("######kery: " + api.getSubscriptionKey());
		System.out.println("######uriBase: " + api.getUriBase());
	}
	
	public void showResult(JSONObject json) {
		System.out.println(json.toString(2));
	}

	public static void main(String[] args) {
		
		try {
			QuickTest test = new QuickTest();
			
//			test.testAnalyzeImageUrl();
//			test.testAnalyzeImageLocalFile();
//			test.testAnalyzeManytimes();			
//			test.testAnalyzeDomainModel();		
//			test.testGenerateThumbnail();		
//			test.testOcr();
//			test.testRecognizeText();
//			test.testChangeRegion();
//			test.testTag();
//			test.testDescribeImage();
			test.testGetModles();
			
//			test.testGetProperties();
			
//			test.testInitConfig();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
