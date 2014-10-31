package poncemoral.pfi.service;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import android.os.AsyncTask;

public class PostDataService extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		HttpClient httpClient = RemoteService.GetInstance(null).getHttpClient();        
        try {
        	String url = params[0];
        	String jsonString = params[1];
			HttpPost post =
                new HttpPost(url);             
            post.setHeader("content-type", "application/json");            
			StringEntity entity = new StringEntity(jsonString);
			post.setEntity(entity);
			httpClient.execute(post);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
        return "";
	}

}
