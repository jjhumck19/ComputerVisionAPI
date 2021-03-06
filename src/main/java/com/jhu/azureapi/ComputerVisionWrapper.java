package com.jhu.azureapi;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Azure computer vision wrapper.  
 * Provide direct image analyze methods and hiding http post/get related work from user
 * 
 * @author James Hu
 *
 */
public class ComputerVisionWrapper {
	private ComputerVisionAPI api;

	public ComputerVisionWrapper(ComputerVisionAPI api) {
		this.api = api;
	}
	
	public ComputerVisionAPI getApi() {
		return api;
	}

	/*
	 * analyze image with all features and all details 
	 */
	public JSONObject analyzeImage(String imageUrlOrFileName) {
		return analyzeImage(imageUrlOrFileName, ComputerVisionAPI.ALLFEATURES, ComputerVisionAPI.ALLDETAILS);
	}
	
	/*
	 * analyze image with given features and details
	 */
	public JSONObject analyzeImage(String imageUrlOrFileName, List<String> features, List<String> details) {
		HttpClient httpclient = HttpClients.createDefault();
		JSONObject json = null;

		try {
			// azure API URL 
			URIBuilder builder = new URIBuilder(api.getAnalyzeURL());
			
			// add parameters
			addParameters(builder, features, details);
			builder.setParameter("language", "en");

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			
			//Build API post request
			HttpPost request = buildPostRequest(uri, imageUrlOrFileName);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			json = handleResponseDefault(entity, imageUrlOrFileName);

		} catch (Exception e) {
			// Display error message.
			// TODO better handle exception
			System.out.println(e.getMessage());
		}

		return json;
	}
	
	/**
	 * Description image with max candidates 1
	 */
	public JSONObject describeImage(String imageUrlOrFileName) {
		return describeImage(imageUrlOrFileName, 1);
	}
	
	/**
	 * Description image with given max candidates 
	 */
	public JSONObject describeImage(String imageUrlOrFileName,int maxCandidates) {
		HttpClient httpclient = HttpClients.createDefault();
		JSONObject json = null;

		try {
			// azure API URL 
			URIBuilder builder = new URIBuilder(api.getDscribeURL());
			
			builder.setParameter("maxCandidates", String.valueOf(maxCandidates));

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			
			//Build API post request
			HttpPost request = buildPostRequest(uri, imageUrlOrFileName);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			json = handleResponseDefault(entity, imageUrlOrFileName);

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}

		return json;
	}

	/**
	 * Analyze local an image file (Supported image file like JPEG, PNG, GIF, BMP)
	 **/
	public JSONObject analyzeImage(File localImageFile) {
		HttpClient httpclient = HttpClients.createDefault();
		JSONObject json = null;

		try {
			URIBuilder builder = new URIBuilder(api.getAnalyzeURL());

			addParameters(builder, ComputerVisionAPI.ALLFEATURES, ComputerVisionAPI.ALLDETAILS);
			builder.setParameter("language", "en");

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = new HttpPost(uri);
			request.setHeader("Ocp-Apim-Subscription-Key", api.getSubscriptionKey());

			MultipartEntityBuilder mbuilder = MultipartEntityBuilder.create();
			mbuilder.addBinaryBody("image", localImageFile);
			HttpEntity reqEntity = mbuilder.build();

			// Request body.
			request.setEntity(reqEntity);
			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			json = handleResponseDefault(entity, localImageFile.getAbsolutePath());

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}

		return json;
	}

	/**
	 * Analyze image based on the domain model. DomainModel.LANDMARKS or DomainModel.CELEBRITIES
	 * 
	 */
	public JSONObject analyzeDomainModel(String imageUrl, DomainModel model) {
		HttpClient httpclient = HttpClients.createDefault();
		JSONObject json = null;
		try {
			URIBuilder builder = new URIBuilder(api.getDomainAnalyzeURL(model));

			builder.setParameter("model", model.getName());

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = buildPostRequest(uri, imageUrl);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			json = handleResponseDefault(entity, imageUrl);

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}

		return json;
	}

	/**
	 * generate thumbnail for given image with default size 100, 100 and smartCropping
	 */
	public InputStream generateThumbnail(String imageUrl) {
		return generateThumbnail(imageUrl, 100, 100, true);
	}

	/**
	 * generate thumbnail for given image with user provide size and smartCropping flag.
	 * 
	 */
	public InputStream generateThumbnail(String imageUrl, int width, int height, boolean smartCropping) {
		HttpClient httpclient = HttpClients.createDefault();
		InputStream thumbnailInputStream = null;
		try {
			URIBuilder builder = new URIBuilder(api.getGenerateThumbnailURL());

			// Request parameters.
			builder.setParameter("width", String.valueOf(width));
			builder.setParameter("height", String.valueOf(height));
			builder.setParameter("smartCropping", String.valueOf(smartCropping));

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = buildPostRequest(uri, imageUrl);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (response.getStatusLine().getStatusCode() == 200) {
				thumbnailInputStream = entity.getContent();
				// Display the thumbnail. Comment out. Just return here let caller to handle display.
				// displayImage(entity.getContent());
			} else {
				// Format and display the JSON error message.
				String jsonString = EntityUtils.toString(entity);
				JSONObject json = new JSONObject(jsonString);
				System.out.println("Error:\n");
				System.out.println(json.toString(2));
			}

		} catch (Exception e) {
			String errorMessage = e.getMessage();
            System.out.println(errorMessage);
            JSONObject jsonError = new JSONObject(errorMessage);
            System.out.println("Error:\n");
			System.out.println(jsonError.toString(2));
            return null;
		}

		return thumbnailInputStream;
	}

	/**
	 * OCR on an image with default auto language detection and detect Orientation is true
	 * 
	 */
	public JSONObject ocrImage(String imageUrl) {
		return ocrImage(imageUrl, "unk", true);
	}

	/**
	 * OCR on an image with user provided language and detectOrientation flag
	 * 
	 */
	public JSONObject ocrImage(String imageUrl, String language, boolean detectOrientation) {
		HttpClient httpclient = HttpClients.createDefault();
		JSONObject json = null;
		try {
			URIBuilder builder = new URIBuilder(api.getOcrURL());
			builder.setParameter("language", language);
			builder.setParameter("detectOrientation", String.valueOf(detectOrientation));

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			HttpPost request = buildPostRequest(uri, imageUrl);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			json = handleResponseDefault(entity, imageUrl);

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}

		return json;
	}

	/**
	 * Recognize the hand writing text
	 * 
	 * @param imageUrl
	 * @return
	 */
	public JSONObject recognizeText(String imageUrl) {
		JSONObject json = null;
		try {
			HttpClient httpclient = HttpClients.createDefault();
			URIBuilder builder = new URIBuilder(api.getRecognizeTextURL());
			builder.setParameter("handwriting", "true");

			// Prepare the URI for the REST API call. 
			URI uri = builder.build();
			//It only support application/octet-stream not support multipart/form-data
			HttpPost textRequest = buildPostRequest(uri, imageUrl);

			// Execute the REST API call and get the response entity.
			HttpResponse textResponse = httpclient.execute(textRequest);
			// Check for success.
			if (textResponse.getStatusLine().getStatusCode() != 202) {
				// Format and display the JSON error message.
				HttpEntity entity = textResponse.getEntity();
				String jsonString = EntityUtils.toString(entity);
				json = new JSONObject(jsonString);
				System.out.println("Error:\n");
				System.out.println(json.toString(2));
				return json;
			}

			String operationLocation = null;

			// The 'Operation-Location' in the response contains the URI to retrieve the
			// recognized text.
			Header[] responseHeaders = textResponse.getAllHeaders();
			for (Header header : responseHeaders) {
				if (header.getName().equals("Operation-Location")) {
					// This string is the URI where you can get the text recognition operation result.
					operationLocation = header.getValue();
					break;
				}
			}

			// NOTE: The response may not be immediately available. Handwriting recognition
			// is an async operation that can take a variable amount of time depending on the
			// length of the text you want to recognize. You may need to wait or retry this operation.
			System.out.println("\nHandwritten text submitted. Waiting 10 seconds to retrieve the recognized text.\n");
			Thread.sleep(10000);
			// Execute the second REST API call and get the response.
			json  = getTextOperations(operationLocation);

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}

		return json;
	}
	
	/**
	 * Get supported models 
	 * @return
	 */
	public JSONObject getModels() {
		return get(api.getModelsURL());
	}
	
	/**
	 * Get text operations result from result url
	 */
	public JSONObject getTextOperations(String resultUrl) {
			return get(resultUrl);
	}
	
	/**
	 * Comment get method
	 */
	public JSONObject get(String url) {
		HttpClient httpclient = HttpClients.createDefault();
		JSONObject json = null;

		try {
			HttpGet resultRequest = new HttpGet(url);
			resultRequest.setHeader("Ocp-Apim-Subscription-Key", api.getSubscriptionKey());

			HttpResponse resultResponse = httpclient.execute(resultRequest);
			HttpEntity responseEntity = resultResponse.getEntity();

			json = handleResponseDefault(responseEntity, null);

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}
		return json;

	}
	
	/**
	 * Get image tag
	 * 
	 * @param imageUrlOrFileName
	 * @return
	 */
	public JSONObject tagImage(String imageUrlOrFileName) {
		HttpClient httpclient = HttpClients.createDefault();
		JSONObject json = null;

		try {
			// azure API URL 
			URIBuilder builder = new URIBuilder(api.getTagURL());

			// Prepare the URI for the REST API call.
			URI uri = builder.build();
			
			//Build API post request
			HttpPost request = buildPostRequest(uri, imageUrlOrFileName);

			// Execute the REST API call and get the response entity.
			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			json = handleResponseDefault(entity, imageUrlOrFileName);

		} catch (Exception e) {
			// Display error message.
			System.out.println(e.getMessage());
		}

		return json;
	}
	
	//Helper methods 
	private HttpPost buildPostRequest(URI uri, String imageUrlOrFileName) throws Exception {
		return buildPostRequest(uri, imageUrlOrFileName, false);  //default. For file not use multipart/form-data but use  application/octet-stream 
	}

	// if imageUrlOrFileName is http url the application/json
	// if imageUrlOrFileName local file  the application/octet-stream  if useMultipart flag is not true
	// Use multipart/form-data for local file if usNultipart flag is true
	private HttpPost buildPostRequest(URI uri, String imageUrlOrFileName, boolean useMultipart) throws Exception {
		HttpPost request = new HttpPost(uri);

		request.setHeader("Ocp-Apim-Subscription-Key", api.getSubscriptionKey());
		if (ApiUtil.isHttpUrl(imageUrlOrFileName)) {
			request.setHeader("Content-Type", "application/json");

			JSONObject jsonUrl = new JSONObject();
			jsonUrl.put("url", imageUrlOrFileName);

			StringEntity reqEntity = new StringEntity(jsonUrl.toString());

			// Request body.
			request.setEntity(reqEntity);
		} else if (useMultipart) {
			File localImageFile = new File(imageUrlOrFileName);
			MultipartEntityBuilder mbuilder = MultipartEntityBuilder.create();
			mbuilder.addBinaryBody("image", localImageFile);
			HttpEntity reqEntity = mbuilder.build();
			// Request body.
			request.setEntity(reqEntity);
		} else {
			File localImageFile = new File(imageUrlOrFileName);
			InputStreamEntity reqEntity = new InputStreamEntity(
	                new FileInputStream(localImageFile), -1, ContentType.APPLICATION_OCTET_STREAM);
	        reqEntity.setChunked(true);
			// Request body.
			request.setEntity(reqEntity);
		}
		return request;
	}
	
	private JSONObject handleResponseDefault(HttpEntity entity, String sourceLocation) throws Exception {
		JSONObject json = null;
		if (entity != null) {
			String jsonString = EntityUtils.toString(entity);
			json = new JSONObject(jsonString);
			json.put("imageLocation", sourceLocation);
		}
		return json;
	}

	private void addParameters(URIBuilder builder, List<String> visualFeatures, List<String> details) {
		addParameters(builder, "visualFeatures", visualFeatures);
		addParameters(builder, "details", details);
	}

	private void addParameters(URIBuilder builder, String name, List<String> list) {
		if (list != null) {
			StringBuffer sb = new StringBuffer();
			for (String item : list) {
				sb.append(item).append(",");
			}
			if (sb.length() > 0) {
				sb.setLength(sb.length() - 1);
			}
			builder.setParameter(name, sb.toString());
		}
	}

}
