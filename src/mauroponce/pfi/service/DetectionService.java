package mauroponce.pfi.service;

import static com.googlecode.javacv.cpp.opencv_core.IPL_DEPTH_8U;
import static com.googlecode.javacv.cpp.opencv_core.cvGetSeqElem;
import static com.googlecode.javacv.cpp.opencv_core.cvLoad;
import static com.googlecode.javacv.cpp.opencv_highgui.cvLoadImage;
import static com.googlecode.javacv.cpp.opencv_highgui.cvSaveImage;
import static com.googlecode.javacv.cpp.opencv_imgproc.CV_BGR2GRAY;
import static com.googlecode.javacv.cpp.opencv_imgproc.cvCvtColor;
import static com.googlecode.javacv.cpp.opencv_objdetect.cvHaarDetectObjects;
import mauroponce.pfi.utils.ImageUtils;
import android.content.Context;

import com.googlecode.javacv.cpp.opencv_core.CvMemStorage;
import com.googlecode.javacv.cpp.opencv_core.CvRect;
import com.googlecode.javacv.cpp.opencv_core.CvSeq;
import com.googlecode.javacv.cpp.opencv_core.IplImage;
import com.googlecode.javacv.cpp.opencv_objdetect.CvHaarClassifierCascade;

/***
 * http://blog.csdn.net/ljsspace/article/details/6664011
 */
public class DetectionService {

	// The cascade definition to be used for detection.
//	private static final String CASCADE_FILE = "C:\\Users\\smoral\\Desktop\\tmp\\haarcascade_frontalface_alt.xml";
	private static final String CASCADE_FILE = "/haarcascade_frontalface_alt.xml";

	public static void detectFaces(String fileInputPath, String fileOutputName, Context context) throws Exception {
		

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
				cvLoad(context.getFilesDir() + CASCADE_FILE));

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
//			IplImage imageResized = ImageUtils.resizeImage(originalImage,103,106); 
			IplImage imageResized = ImageUtils.resizeImage(imageCropped,103,106);
			// Save resized image to a new file.
			cvSaveImage(fileInputPath, imageResized);
		}
//
//		// Save the image to a new file.
//		cvSaveImage("C:\\Users\\smoral\\Desktop\\tmp\\"+fileOutputName+".jpg", originalImage);
		System.out.println("Printe image "+fileInputPath);
	}

}