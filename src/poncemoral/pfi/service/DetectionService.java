package poncemoral.pfi.service;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_DO_ROUGH_SEARCH;
import static com.googlecode.javacv.cpp.opencv_objdetect.CV_HAAR_FIND_BIGGEST_OBJECT;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;

import java.io.File;

import poncemoral.pfi.ui.R;
import poncemoral.pfi.utils.FileUtils;
import poncemoral.pfi.utils.ImageUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

/***
 * http://blog.csdn.net/ljsspace/article/details/6664011
 */
@SuppressLint("UseValueOf")
public class DetectionService {

	// The cascade definition to be used for detection.
//	private static final String CASCADE_FILE = "C:\\Users\\smoral\\Desktop\\tmp\\haarcascade_frontalface_alt.xml";
	public static final String CASCADE_FILE = "haarcascade_frontalface_alt.xml";

	public static void detectFaces(String fileInputPath, String fileOutputName, Context context) throws Exception {
		

		// Load the original image.
		IplImage originalImage = cvLoadImage(fileInputPath,1);
		
		float resizeRelation = 0.5f;
		int originalWidth = originalImage.width();
		int originalHeight = originalImage.height();
		if (originalWidth > 640 && originalHeight > 480){
			//change the picture size to minimize the recognizing time
			originalImage = ImageUtils.resizeImage(originalImage, new Float(originalWidth*resizeRelation).intValue(), new Float(originalHeight*resizeRelation).intValue());
		}

		// We need a grayscale image in order to do the recognition, so we
		// create a new image of the same size as the original one.
		IplImage grayImage = IplImage.create(originalImage.width(),
				originalImage.height(), IPL_DEPTH_8U, 1);

		// We convert the original image to grayscale.
		 cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);

		CvMemStorage storage = CvMemStorage.create();

		// We instantiate a classifier cascade to be used for detection, using
		// the cascade definition.		
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
				cvLoad(getAbsolutePathOfHaarCascadeClasifier((Activity) context)));

		// We detect the faces.
		CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, 1,
				0);

		// We iterate over the discovered faces and draw yellow rectangles
		// around them.
		
		if (faces.total()>0){
			CvRect r = new CvRect(cvGetSeqElem(faces, 1));
			IplImage imageCropped = ImageUtils.cropImage(originalImage, r);
			
//			cvRectangle(originalImage, cvPoint(r.x(), r.y()),
//					cvPoint(r.x() + r.width(), r.y() + r.height()),
//					CvScalar.YELLOW, 1, CV_AA, 0);
			
			// Save cropped image to a new file.
//			cvSaveImage(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+fileOutputName+"Cropped"+i+".jpg", imageCropped); 
//			IplImage imageResized = ImageUtils.resizeImage(imageCropped, AppConstants.WIDTH_STANDARD, AppConstants.HEIGHT_STANDARD);
			// Save resized image to a new file.
			cvSaveImage(fileInputPath, imageCropped);
		}
//
//		// Save the image to a new file.
//		cvSaveImage("C:\\Users\\smoral\\Desktop\\tmp\\"+fileOutputName+".jpg", originalImage);
		System.out.println("Printe image "+fileInputPath);
	}

	public static void saveHaarCascadeClasifierToInternalStorage(Activity activity) {
		if(!(new File(getAbsolutePathOfHaarCascadeClasifier(activity)).exists())){
			String haarcascade = FileUtils.readRawResource(activity, R.raw.haarcascade_frontalface_alt);
			String haarcascadeFileName = CASCADE_FILE;
			FileUtils.write(haarcascadeFileName, haarcascade, activity);
		}
	}

	private static String getAbsolutePathOfHaarCascadeClasifier(
			Activity activity) {
		return activity.getFilesDir().getAbsolutePath() + File.separator
						+ CASCADE_FILE;
	}
	
	public static void detectOneFace(String fileInputPath, String fileOutputName, Context context) throws Exception {
		

		// Load the original image.
		IplImage originalImage = cvLoadImage(fileInputPath,1);
		
		//change the picture size to minimize the recognizing time
		originalImage = ImageUtils.resizeImage(originalImage, 640, 480);

		// We need a grayscale image in order to do the recognition, so we
		// create a new image of the same size as the original one.
		IplImage grayImage = IplImage.create(originalImage.width(),
				originalImage.height(), IPL_DEPTH_8U, 1);

		// We convert the original image to grayscale.
		 cvCvtColor(originalImage, grayImage, CV_BGR2GRAY);

		CvMemStorage storage = CvMemStorage.create();

		// We instantiate a classifier cascade to be used for detection, using
		// the cascade definition.		
		CvHaarClassifierCascade cascade = new CvHaarClassifierCascade(
				cvLoad(getAbsolutePathOfHaarCascadeClasifier((Activity) context)));

		// We detect the faces.
		CvSeq faces = cvHaarDetectObjects(grayImage, cascade, storage, 1.1, CV_HAAR_FIND_BIGGEST_OBJECT|CV_HAAR_DO_ROUGH_SEARCH,
				0);//CV_HAAR_FIND_BIGGEST_OBJECT|CV_HAAR_DO_ROUGH_SEARCH indicates to find the biggest object and stop the process

		// We iterate over the discovered faces and draw yellow rectangles
		// around them.
		if (faces.total()>0){
			CvRect r = new CvRect(cvGetSeqElem(faces, 0));
			IplImage imageCropped = ImageUtils.cropImage(originalImage, r);
			
			 
			// Save resized image to a new file.			
			cvSaveImage(fileInputPath, imageCropped);
			// System.out.println("Saved image "+fileOutputPath);
		}
	}

}