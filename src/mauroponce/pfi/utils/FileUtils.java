package mauroponce.pfi.utils;

import static com.googlecode.javacv.cpp.opencv_core.cvConvertScale;
import static com.googlecode.javacv.cpp.opencv_core.cvCreateImage;
import static com.googlecode.javacv.cpp.opencv_core.cvMinMaxLoc;
import static com.googlecode.javacv.cpp.opencv_core.cvSize;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.googlecode.javacv.cpp.opencv_core.CvPoint;
import com.googlecode.javacv.cpp.opencv_core.IplImage;



public class FileUtils {
	
	public static void write(String fileName, String data, Activity activity) {
		FileOutputStream fos;
		try {
			fos = activity.openFileOutput(fileName, Context.MODE_PRIVATE);
			fos.write(data.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static String readRawResource(Activity activity, int resourceId) {
		BufferedReader filereader = new BufferedReader(new InputStreamReader(
				activity.getResources().openRawResource(resourceId)));

		String lineread = "";
		StringBuffer buffer = new StringBuffer();
		try {
			while ((lineread = filereader.readLine()) != null) {
				buffer.append(lineread+"\r\n");				
			}
			filereader.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return buffer.toString();
	}
	
	public static String read(String fileName, Activity activity){
		FileInputStream in;
		StringBuilder sb = new StringBuilder();
		try {
			in = activity.openFileInput(fileName);
			InputStreamReader inputStreamReader = new InputStreamReader(in);
		    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		    String line;
		    while ((line = bufferedReader.readLine()) != null) {
		        sb.append(line);
		    }
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return sb.toString();
	}
	public static String encodeFileBase64(String inputPath){
		File file = new File(inputPath);
		byte[] fileData = new byte[(int) file.length()];
        try {
        	FileInputStream fileInputStream = new FileInputStream(file);
	        fileInputStream.read(fileData);
	        fileInputStream.close();	        	        
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Base64.encodeToString(fileData,  Base64.DEFAULT);
	}
	
	public static byte[] decodeFileBase64(String base64String){
		return Base64.decode(base64String, Base64.DEFAULT);
	}
	
	public static IplImage convertFloatImageToUcharImage(
			final IplImage sourceIplImage) {
		IplImage destinationIplImage;
		if ((sourceIplImage != null)
				&& (sourceIplImage.width() > 0 && sourceIplImage.height() > 0)) {
			// Spread the 32bit floating point pixels to fit within 8bit pixel
			// range.
			final CvPoint minloc = new CvPoint();
			final CvPoint maxloc = new CvPoint();
			final double[] minVal = new double[1];
			final double[] maxVal = new double[1];
			cvMinMaxLoc(sourceIplImage, minVal, maxVal, minloc, maxloc, null);
			// Deal with NaN and extreme values, since the DFT seems to give
			// some NaN results.
			if (minVal[0] < -1e30) {
				minVal[0] = -1e30;
			}
			if (maxVal[0] > 1e30) {
				maxVal[0] = 1e30;
			}
			if (maxVal[0] - minVal[0] == 0.0f) {
				maxVal[0] = minVal[0] + 0.001; // remove potential divide by
												// zero errors.
			} // Convert the format
			destinationIplImage = cvCreateImage(
					cvSize(sourceIplImage.width(), sourceIplImage.height()), 8,
					1);
			cvConvertScale(sourceIplImage, destinationIplImage,
					255.0 / (maxVal[0] - minVal[0]), -minVal[0] * 255.0
							/ (maxVal[0] - minVal[0]));
			return destinationIplImage;
		}
		return null;
	}
	
	public static void deleteFileInPath(String absolutePath){
		File file = new File(absolutePath);
		if (file != null) {
	        file.delete();
	    }
	}

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUriFromMediaFile(File mediaFile) {
	      return Uri.fromFile(mediaFile);	
	}	

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "PFI");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("PFI", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "PFI_"+ timeStamp + ".jpg");
	    return mediaFile;
	}
	
}
