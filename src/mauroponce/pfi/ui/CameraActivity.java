package mauroponce.pfi.ui;
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
                

				try {
					DetectionService.detectFaces(imagePath, "nueva");
			        doRecognition(3);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                
				Intent intent = new Intent(CameraActivity.this, ListViewImagesActivity.class);
				//intent.putExtra("lus", lus);
		        startActivity(intent);

            } else if (resultCode == RESULT_CANCELED) {

            	Toast.makeText(this, "Good bye!", Toast.LENGTH_LONG).show();
            } else {

            	Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
            }

        }
    }	
	/**Returns the knn's LUs*/
	private int [] doRecognition(int knn){
		RecognitionService recognitionService = new RecognitionService();
		
		//Hacer q devuelva la lista de LUs
		recognitionService.recognize(imagePath, knn);
		
		return null;
	}
	
}