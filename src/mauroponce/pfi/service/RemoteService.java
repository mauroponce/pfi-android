package mauroponce.pfi.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import mauroponce.pfi.ui.R;
import mauroponce.pfi.utils.FileUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;

public class RemoteService {
	
	/* The ip 10.0.2.2 Only works on the emulator */
	/*
	 * If you use a real device put the public ip of your pc on
	 * \res\raw\ipconfig and verify that the real android device are in the same
	 * network
	 */
	private String serverHost = "10.0.2.2:8080"; 
	
	private static RemoteService instance = null;

	public static RemoteService GetInstance(Activity activity) {
		if (instance == null){
			instance = new RemoteService(activity);
		}
		
		return instance;
	}
	
	private RemoteService(Activity activity){
		String appPropertiesString = FileUtils.readRawResource(activity, R.raw.app_properties);
		Properties prop = new Properties();
		try {
			prop.load(new ByteArrayInputStream(appPropertiesString.getBytes()));
			String serverHost = prop.getProperty("serverHost");
			if (!"".equals(serverHost)){
				this.serverHost = serverHost;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private DefaultHttpClient getHttpClient() {
		HttpParams httpParameters = new BasicHttpParams();
		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used. 
		int timeoutConnection = 15000;
		HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT) 
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 18000;
		HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

		return new DefaultHttpClient(httpParameters);
	}
	    
	/**
	 * Save the attendance of one student for that course
	 * @param studentLu
	 * @param courseNumber
	 */
	public void saveAttendance(Integer studentLu, Integer courseNumber) {
		try {
		    JSONObject datosJSON = new JSONObject();
				datosJSON.put("studentLU", studentLu);
			datosJSON.put("courseNumber", courseNumber);
			String url = "http://"+serverHost+"/PFI/attendance/save";
			this.post(url, datosJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Send the image taken for training
	 * @param studentLu
	 * @param encodedImageBase64
	 * @param fileExtension
	 */
	public void sendTrainingData (Integer studentLu, String encodedImageBase64, String fileExtension) {
		try {
			JSONObject datosJSON = new JSONObject();
			datosJSON.put("studentLU", studentLu);
			datosJSON.put("encodedImageBase64", encodedImageBase64);
			datosJSON.put("fileExtension", fileExtension);
			String url = "http://"+serverHost+"/PFI/course/send_training_data";
			this.post(url, datosJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
    public String logIn(String usr){    	
    	//String date = new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd-HH:mm"));
    	String date = "2012-10-15-09:30";
    	HttpClient httpClient = getHttpClient();
    	 
    	HttpGet get =
    	    new HttpGet("http://"+serverHost+"/PFI/attendance/log_in?usr=" + usr + "&d=" + date);    	 
    	get.setHeader("content-type", "application/json");
    	String logInResultJSON = null;
		try {
			HttpResponse resp = httpClient.execute(get);
			String respStr = EntityUtils.toString(resp.getEntity());
			
//	        JSONObject respJSON = new JSONObject(respStr);
//	        facesData = respJSON.getString("facesdata");}
			logInResultJSON = respStr;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return logInResultJSON;
    }
    
    public String getTrainingData(Integer courseNumber){
    	HttpClient httpClient = getHttpClient();    	
    	HttpGet get =
    			new HttpGet("http://"+serverHost+"/PFI/course/get_training_data/" + courseNumber);    	 
    	get.setHeader("content-type", "application/json");
    	String facesDataJSON = null;
    	try {
    		HttpResponse resp = httpClient.execute(get);
    		String respStr = EntityUtils.toString(resp.getEntity());
    		
//	        JSONObject respJSON = new JSONObject(respStr);
//	        facesData = respJSON.getString("facesdata");}
    		facesDataJSON = respStr;
    	} catch (ClientProtocolException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    	return facesDataJSON;
    }

    private void post(String url, JSONObject datosJSON){
    	HttpClient httpClient = getHttpClient();        
        try {
        	HttpPost post =
                new HttpPost(url);             
            post.setHeader("content-type", "application/json");            
			StringEntity entity = new StringEntity(datosJSON.toString());
			post.setEntity(entity);
			httpClient.execute(post);			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
