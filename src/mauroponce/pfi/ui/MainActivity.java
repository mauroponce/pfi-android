package mauroponce.pfi.ui;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import mauroponce.pfi.service.DetectionService;
import mauroponce.pfi.service.RecognitionService;
import mauroponce.pfi.service.RemoteService;
import mauroponce.pfi.utils.FileUtils;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
    
	EditText editTextUsr;
	Button btnAccept;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //When the appliction starts we need to save in the storage the haar classifier to can get it for the detection
		DetectionService.saveHaarCascadeClasifierToInternalStorage(MainActivity.this);
        
        editTextUsr = (EditText)findViewById(R.id.editTextUsr);
        btnAccept = (Button)findViewById(R.id.buttonAccept);
        
        btnAccept.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//Intent intent = new Intent(MainActivity.this, ResultadoActivity.class);
				String facesData = getFacesData(editTextUsr.getText().toString().trim());//mmiralles
				RecognitionService.saveFacesDataToInternalStorage(facesData, MainActivity.this);
				
//				String facesData = FileUtils.readRawResource(MainActivity.this, R.raw.facedata);
//				String fileName = "facesData.xml";
//				FileUtils.write(fileName, facesData, MainActivity.this);
				
				//String read = FileUtils.read(fileName, MainActivity.this);
				//Open from FaceRecognizer
				
				//start camera
				Intent intent = new Intent(MainActivity.this, CameraActivity.class);
				//intent.putExtra("lus", lus);
		        startActivity(intent);
			}   
			
//			@Override
//			public void onClick(View v) {
//				try {
//					DetectionService.detectFaces(Environment.getExternalStorageDirectory().getAbsolutePath()+"/moralalbino.jpg", "nueva");
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
//		        new RecognitionService().recognize(Environment.getExternalStorageDirectory().getAbsolutePath()+"/nuevaResized0.jpg", 3);
//			}
        });
        //post();
               
    }

    private void post(){
    	HttpClient httpClient = new DefaultHttpClient();        
        try {
        	HttpPost post =
                new HttpPost("http://10.0.2.2:8080/PFI/attendance/postput");
             
            post.setHeader("content-type", "application/json");
            JSONObject dato = new JSONObject();
			dato.put("nombre", "Abu Reina!");
			StringEntity entity = new StringEntity(dato.toString());
			post.setEntity(entity);
			httpClient.execute(post);
			
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    private String getFacesData(String usr){    	
    	RemoteService remoteService= new RemoteService();
    	return remoteService.getFacesData(usr);
    }
}