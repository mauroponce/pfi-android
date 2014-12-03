package poncemoral.pfi.service;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import poncemoral.pfi.ui.SettingsActivity;
import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class RemoteService {
	
	/* The ip 10.0.2.2 Only works on the emulator */
	/*
	 * If you use a real device put the public ip of your pc on
	 * \res\raw\ipconfig and verify that the real android device are in the same
	 * network
	 */
	private static RemoteService instance = null;

	public static RemoteService GetInstance() {
		if (instance == null){
			instance = new RemoteService();
		}
		
		return instance;
	}
	
	private RemoteService(){}

	public DefaultHttpClient getHttpClient() {
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
	
	private String getServerHost(Activity activity) throws Exception {
		SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(activity);
		String serverHost = sharedPref.getString(SettingsActivity.KEY_PREF_SERVER_IP, null);
		if (serverHost == null){
			throw new Exception("Server Ip is null");
		}
		return serverHost;
	}
	    
	/**
	 * Save the attendance of one student for that course
	 * @param studentLu
	 * @param courseNumber
	 */
	public void saveAttendance(Integer studentLu, Integer courseNumber, Activity activity) {
		try {
		    JSONObject datosJSON = new JSONObject();
				datosJSON.put("studentLU", studentLu);
			datosJSON.put("courseNumber", courseNumber);
			String url = "http://"+this.getServerHost(activity)+"/PFI/attendance/save";
			this.post(url, datosJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send the image taken for training
	 * @param studentLu
	 * @param encodedImageBase64
	 * @param fileExtension
	 */
	public void sendTrainingData (Integer studentLu, String encodedImageBase64, String fileExtension, Activity activity) {
		try {
			JSONObject datosJSON = new JSONObject();
			datosJSON.put("studentLU", studentLu);
			datosJSON.put("encodedImageBase64", encodedImageBase64);
			datosJSON.put("fileExtension", fileExtension);
			String url = "http://"+this.getServerHost(activity)+"/PFI/course/send_training_data";
			this.post(url, datosJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public String logIn(String usr, Activity activity){    	
    	//String date = new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd-HH:mm"));
    	String date = "2012-10-15-09:30";
    	HttpClient httpClient = getHttpClient();
    	 
    	String logInResultJSON = null;
		try {
			HttpGet get =
					new HttpGet("http://"+this.getServerHost(activity)+"/PFI/attendance/log_in?usr=" + usr + "&d=" + date);    	 
			get.setHeader("content-type", "application/json");
			HttpResponse resp = httpClient.execute(get);
			String respStr = EntityUtils.toString(resp.getEntity());
			
//	        JSONObject respJSON = new JSONObject(respStr);
//	        facesData = respJSON.getString("facesdata");}
			logInResultJSON = respStr;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logInResultJSON;
    }
    
    public String getTrainingData(Integer courseNumber, Activity activity){
    	HttpClient httpClient = getHttpClient();    	
    	String facesDataJSON = null;
    	try {
    		HttpGet get =
    				new HttpGet("http://"+this.getServerHost(activity)+"/PFI/course/get_training_data/" + courseNumber);    	 
    		get.setHeader("content-type", "application/json");
    		HttpResponse resp = httpClient.execute(get);
    		String respStr = EntityUtils.toString(resp.getEntity());
    		
//	        JSONObject respJSON = new JSONObject(respStr);
//	        facesData = respJSON.getString("facesdata");}
    		facesDataJSON = respStr;
    	} catch (ClientProtocolException e) {
    		e.printStackTrace();
    	} catch (IOException e) {
    		e.printStackTrace();
    	} catch (Exception e) {
			e.printStackTrace();
		}
    	return facesDataJSON;
    }

    private void post(String url, JSONObject datosJSON){
    	String[] params = new String[2];
    	params[0] = url;
    	params[1] = datosJSON.toString();
    	PostDataService post = new PostDataService();
    	post.execute(params);
    }
}
