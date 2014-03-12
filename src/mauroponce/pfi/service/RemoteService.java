package mauroponce.pfi.service;

import java.io.IOException;
import java.util.List;

import mauroponce.pfi.domain.Student;
import mauroponce.pfi.ui.R;
import mauroponce.pfi.utils.FileUtils;
import mauroponce.pfi.utils.JSONUtil;

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
	private String serverIp = "10.0.2.2"; 
	
	private static RemoteService instance = null;

	public static RemoteService GetInstance(Activity activity) {
		if (instance == null){
			instance = new RemoteService(activity);
		}
		
		return instance;
	}
	
	private RemoteService(Activity activity){
		String serverIp = FileUtils.readRawResource(activity, R.raw.ipconfig).replace("\r\n", "");
		if (!"".equals(serverIp)){
			this.serverIp = serverIp;
		}
	}
	
	/**
	 * Return a list of students from the server, by lus of students. Or an empty array.
	 * @param studentLus
	 * @return
	 */
	public List<Student> getStudents(List<Integer> studentLus) {
		List<Student> students = null;
		HttpClient httpClient = getHttpClient();
		String lusForRequest = "";
		for (int studentLu : studentLus) {
			if (!"".equals(lusForRequest)){
				lusForRequest += "_";
			}
			lusForRequest += studentLu;
		}
		HttpGet get = new HttpGet(
				"http://"+serverIp+":8080/PFI/student/batch_data/" + lusForRequest);
		get.setHeader("content-type", "application/json");
		try {
			HttpResponse resp = httpClient.execute(get);
			String respStr = EntityUtils.toString(resp.getEntity());
			students = JSONUtil.getStudents(respStr); 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return students;
	}
	
	/**
	 * Return a all students from course. Or an empty array.
	 * @param studentLus
	 * @return
	 */
	public List<Student> getCourseStudents(Integer courseId) {
		List<Student> students = null;
		HttpClient httpClient = getHttpClient();
		HttpGet get = new HttpGet(
				"http://"+serverIp+":8080/PFI/student/course_students/" + courseId);
		get.setHeader("content-type", "application/json");
		try {
			HttpResponse resp = httpClient.execute(get);
			String respStr = EntityUtils.toString(resp.getEntity());
			students = JSONUtil.getStudents(respStr); 
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return students;
	}
	    
	public void saveAttendance(Integer studentLu, Integer courseNumber) {
		try {
		    JSONObject datosJSON = new JSONObject();
				datosJSON.put("studentLU", studentLu);
			datosJSON.put("courseNumber", courseNumber);
			String url = "http://"+serverIp+":8080/PFI/attendance/save";
			this.post(url, datosJSON);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	
    public String getFacesData(String usr){    	
    	//String date = new DateTime().toString(DateTimeFormat.forPattern("yyyy-MM-dd-HH:mm"));
    	String date = "2012-10-15-09:30";
    	HttpClient httpClient = getHttpClient();
    	 
    	HttpGet get =
    	    new HttpGet("http://"+serverIp+":8080/PFI/attendance/facesdata?usr=" + usr + "&d=" + date);    	 
    	get.setHeader("content-type", "application/json");
    	String facesData = null;
		try {
			HttpResponse resp = httpClient.execute(get);
			String respStr = EntityUtils.toString(resp.getEntity());
			
//	        JSONObject respJSON = new JSONObject(respStr);
//	        facesData = respJSON.getString("facesdata");}
			facesData = respStr;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return facesData;
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
