package mauroponce.pfi.service;

import java.io.IOException;
import java.util.List;

import mauroponce.pfi.domain.Student;
import mauroponce.pfi.utils.JSONUtil;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class RemoteService {
	
	private String serverIp = "192.168.1.102";

	/**
	 * Return a list of students from the server, by lus of students. Or an empty array.
	 * @param studentLus
	 * @return
	 */
	public List<Student> getStudents(List<Integer> studentLus) {
		List<Student> students = null;
		HttpClient httpClient = new DefaultHttpClient();
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
    	HttpClient httpClient = new DefaultHttpClient();
    	 
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
//		} catch (JSONException e) {
//			e.printStackTrace();
		}
		return facesData;
    }

    private void post(String url, JSONObject datosJSON){
    	HttpClient httpClient = new DefaultHttpClient();        
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
