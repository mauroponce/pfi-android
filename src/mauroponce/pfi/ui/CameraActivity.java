package mauroponce.pfi.ui;
import java.io.File;

import mauroponce.pfi.utils.FileUtils;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;


public class CameraActivity extends Activity {
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public final static String IMAGE_PATH = "image_path";
	public final static String STUDENTS_LUS_ARRAY = "students_lus_array";
	private String imagePath;
	private Uri fileUri;

//	Commit test para conicet
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.camera);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        File mediaFile = FileUtils.getOutputMediaFile();
        fileUri = FileUtils.getOutputMediaFileUriFromMediaFile(mediaFile); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        // start the image capture Intent
        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);

        imagePath = mediaFile.getAbsolutePath();        
    }

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE: {
			switch (resultCode) {
			case RESULT_OK: {
				Intent intent = new Intent(CameraActivity.this, ImageDetectedConfirmationActivity.class);
				intent.putExtra(IMAGE_PATH, imagePath);
		        startActivity(intent);				
				break;
			}
			case RESULT_CANCELED: {
				Toast.makeText(this, "Tome otra foto", Toast.LENGTH_LONG)
						.show();
				break;
			}
			default: {
				Toast.makeText(this, "Error!", Toast.LENGTH_LONG).show();
				break;
			}
			}
		}
        }
    }	

}