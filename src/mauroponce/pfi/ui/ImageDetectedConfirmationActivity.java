package mauroponce.pfi.ui;
import java.io.IOException;
import java.util.ArrayList;

import mauroponce.pfi.service.DetectionService;
import mauroponce.pfi.service.RecognitionService;
import mauroponce.pfi.utils.ImageUtils;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class ImageDetectedConfirmationActivity extends Activity {
	public final static String IMAGE_PATH = "image_path";
	public final static String STUDENTS_LUS_ARRAY = "students_lus_array";
	private String imagePath;
	Button btnConfirmDetectedImage;
	Button btnCancelDetectedImage;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.image_view_detected_layout); 	
		
        Intent intent = getIntent();
		imagePath = intent.getStringExtra(CameraActivity.IMAGE_PATH);
		Bitmap imageBitmap = BitmapFactory.decodeFile(imagePath);

		ExifInterface exif;
		try {
			/* http://stackoverflow.com/questions/3647993/android-bitmaps-loaded-from-gallery-are-rotated-in-imageview */
			exif = new ExifInterface(imagePath);
			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
			if (orientation == 6){
				imageBitmap = ImageUtils.rotateImage(imageBitmap, 90);
				ImageUtils.saveBitmap(imageBitmap, imagePath);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			DetectionService.detectOneFace(imagePath, "nueva",
					ImageDetectedConfirmationActivity.this);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		imageBitmap = BitmapFactory.decodeFile(imagePath);
		ImageView imgView = (ImageView) findViewById(R.id.imageViewDetected);
		imgView.setImageBitmap(imageBitmap);
		
		btnConfirmDetectedImage = (Button)findViewById(R.id.btnConfirmDetectedImage);
        
		btnConfirmDetectedImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// Image captured and saved to fileUri specified in the Intent
				Toast.makeText(ImageDetectedConfirmationActivity.this, "Image saved to:\n" + imagePath,
						Toast.LENGTH_LONG).show();

				ArrayList<Integer> nearestStudents = null;
					
				nearestStudents = doRecognition(3);
				
				if (!nearestStudents.isEmpty()) {
					Intent intent = new Intent(ImageDetectedConfirmationActivity.this,
							ListViewImagesActivity.class);
					// intent.putExtra("lus", lus);
					intent.putIntegerArrayListExtra(STUDENTS_LUS_ARRAY,
							nearestStudents);
					intent.putExtra(IMAGE_PATH, imagePath);
					startActivity(intent);
				} else {
					Toast.makeText(ImageDetectedConfirmationActivity.this, "No se detectó ningún rostro, tome otra fotografía.",
							Toast.LENGTH_LONG).show();
					Intent intent = new Intent(ImageDetectedConfirmationActivity.this, CameraActivity.class);
			        startActivity(intent);
				}
			}
		});   

		btnCancelDetectedImage = (Button)findViewById(R.id.btnCancelDetectedImage);
        
		btnCancelDetectedImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ImageDetectedConfirmationActivity.this, CameraActivity.class);
		        startActivity(intent);
			}
		});
    }

	/**Returns the knn's LUs*/
	private ArrayList<Integer> doRecognition(int knn){
		RecognitionService recognitionService = new RecognitionService(ImageDetectedConfirmationActivity.this);
		
		//Hacer q devuelva la lista de LUs
		return recognitionService.recognize(imagePath, knn);
	}
    
   
	
}