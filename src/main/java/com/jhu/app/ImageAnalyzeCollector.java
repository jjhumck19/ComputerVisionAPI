package com.jhu.app;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.jhu.azureapi.ComputerVisionAPI;
import com.jhu.azureapi.ComputerVisionWrapper;
import com.jhu.utils.FileUtil;

/**
 * Image information collector. Scan all image files under given directory 
 * and get image analyze result using Computer Vision API and save JSON result in a file.
 * 
 * @author jhu
 *
 */
public class ImageAnalyzeCollector {
	
	//TODo add more control for append or new and image file fileter etc. 
	File resultFile;
	boolean includeSubDir;
	boolean isAppendMode;
	int batchSize = 5;  
	ComputerVisionWrapper api = new ComputerVisionWrapper (new ComputerVisionAPI());
	
	public void collectImageAnalyzeResults(File rootDir, boolean includeSubDir, File resultFile) throws Exception {
		if (resultFile == null ) {
			System.out.println("No output file return");
			return;
		}
		
		if (resultFile.exists()) {
			resultFile.createNewFile();
		}
		
		int count = 0;
		List<JSONObject> results = new ArrayList<JSONObject>() ;
		for (File f :rootDir.listFiles()) {
			if (f.isDirectory()) {
				if (includeSubDir) {
					collectImageAnalyzeResults(f, includeSubDir, resultFile);
				}
			} else if (f.isFile()) {
				//If the f is supported image file 
				JSONObject json = api.analyzeImage(f);
				results.add(json);
				System.out.println(json);
				if (count > batchSize) {
					try {
						FileUtil.writeToFile(results, resultFile.getAbsolutePath(), null, null);
					} catch (Exception e) {
						e.printStackTrace();
					}
					count=0;
					results.clear();
				}
			} 
		}
		if (!results.isEmpty()) {
			try {
				FileUtil.writeToFile(results, resultFile.getAbsolutePath(), null, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) {
	   try {

		if (args.length > 0 ) {
			String path = args[0];
			System.out.println("root: " +path);
			File root = new File (path);
			if (root.isDirectory()) {
				ImageAnalyzeCollector  collector = new ImageAnalyzeCollector();
				String resultFileName = "result" + System.currentTimeMillis() + ".json";
				if (args.length > 1) {
					resultFileName = args[1];
				}
				
				System.out.println("Result File Name: " +resultFileName);
				collector.collectImageAnalyzeResults(root, false, new File(resultFileName));
				System.out.println("collectImageAnalyzeResults done at:  " +System.currentTimeMillis());
			} else {
				System.out.println(String.format("Input parameter %s is not a file path", root));
			}
		}
	   }catch (Exception e) {
		   e.printStackTrace();
	   }
	}
}
