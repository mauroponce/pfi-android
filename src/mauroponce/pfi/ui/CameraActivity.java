package mauroponce.pfi.ui;
import java.util.ArrayList;
import java.util.Date;

import mauroponce.pfi.service.DetectionService;
import mauroponce.pfi.service.RecognitionService;
import mauroponce.pfi.utils.FileUtils;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;


public class CameraActivity extends Activity {

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public final static String STUDENTS_LUS_ARRAY = "students_lus_array";
	private String imagePath;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        ContentValues values = new ContentValues();
        String fileName = String.valueOf(new Date().getTime());
        values.put(MediaStore.Images.Media.TITLE, fileName);
        
        Uri mCapturedImageURI = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intentPicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intentPicture.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
        
        startActivityForResult(intentPicture,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        imagePath = FileUtils.getRealPathFromURI(mCapturedImageURI, CameraActivity.this);
    }

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
            	
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                		imagePath, Toast.LENGTH_LONG).show();
				//int [] lus = doRecognition(2);
                //if(lus == null){//no se encontro un proximo con cierta certidumbre
                	//mostrar dialogo para tomar nueva fotografia
                //}else{
//                //Despues de hacer el reconocimiento, borro la imagen
//                FileUtils.deleteFileInPath(imagePath);//NO ANDA!
                
                ArrayList<Integer> nearestStudents = null;
				try {
					DetectionService.detectFaces(imagePath, "nueva", CameraActivity.this);
					nearestStudents = doRecognition(3);
			        
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
				if (!nearestStudents.isEmpty()){
					Intent intent = new Intent(CameraActivity.this, ListViewImagesActivity.class);
					//intent.putExtra("lus", lus);
				    intent.putIntegerArrayListExtra(STUDENTS_LUS_ARRAY, nearestStudents);
			        startActivity(intent);
				}else{
					//TODO hacer que vuelva a pedir la foto
				}

            } else if (resultCode == RESULT_CANCELED) {

            	Toast.makeText(this, "Good bye!", Toast.LENGTH_LONG).show();
            } else {

            	Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
            }

        }
    }	

	/**Returns the knn's LUs*/
	private ArrayList<Integer> doRecognition(int knn){
		RecognitionService recognitionService = new RecognitionService();
		
		//Hacer q devuelva la lista de LUs
		return recognitionService.recognize(imagePath, knn);
	}
    
   
	
}