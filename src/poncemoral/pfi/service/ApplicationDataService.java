package poncemoral.pfi.service;

import java.util.List;

import poncemoral.pfi.domain.Course;
import poncemoral.pfi.domain.FacesData;
import poncemoral.pfi.domain.Student;
import poncemoral.pfi.utils.AppConstants;
import poncemoral.pfi.utils.FileUtils;
import poncemoral.pfi.utils.JSONUtil;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class ApplicationDataService {
	private static final String FACES_DATA_JSON = "facesDataJson";
	private static ApplicationDataService instance = null;
	private RemoteService remoteService;
	private SharedPreferences preferences;
	private Course courseActual;
	private FacesData facesDataActual;
	
	protected ApplicationDataService() {
		// Exists only to defeat instantiation.
	}

	public static ApplicationDataService getInstance() {
		if (instance == null) {
			instance = new ApplicationDataService();
		}
		return instance;
	}

	public void initialize(SharedPreferences preferences, Activity activity) {
		// Cargo las preferencias de la aplicacion
		this.setPreferences(preferences);
		remoteService = RemoteService.GetInstance(activity); 
	}

	private void setCourseActual(String courseJSON) {
		JSONUtil<Course> jsonUtil = new JSONUtil<Course>();
		courseActual = jsonUtil.getCourseFromJson(courseJSON);
	}

	private String getFacesDataJson(Integer courseNumber) {
		String facesDataJson = getPreferences().getString(
				FACES_DATA_JSON + "_" + courseNumber, null);
		return facesDataJson;
	}

	private void saveFacesDataJson(String facesDataJson, Integer courseNumber) {		
		Editor editor = getPreferences().edit();
		editor.putString(FACES_DATA_JSON + "_" + courseNumber, facesDataJson);
		editor.commit();
	}

	private void setFacesDataActual(String facesDataJson) {
		JSONUtil<FacesData> jsonUtil = new JSONUtil<FacesData>();
		facesDataActual = jsonUtil.getFacesDataFromJson(facesDataJson);
	}
	
	public void logIn (String usr, Activity activity){
		String courseJSON = remoteService.logIn(usr);
		this.setCourseActual(courseJSON);
		if (courseActual != null){
			Boolean hasToUpdateFacesData = Boolean.FALSE;
			Integer courseNumber = courseActual.getCourseNumber();
			String facesDataJSON = getFacesDataJson(courseNumber);
			if (facesDataJSON != null && facesDataJSON.startsWith("{")){
				this.setFacesDataActual(facesDataJSON);
				// if the faces data changed and is bigger, has to get it again 
				if (facesDataActual.getCreationDateFacesData() == null
						|| courseActual.getCreationDateFacesData() > facesDataActual
								.getCreationDateFacesData()) {
					hasToUpdateFacesData = Boolean.TRUE;
				}
			}else{
				// if the faces data doesnt exists for this course, has to get it again
				hasToUpdateFacesData = Boolean.TRUE;				
			}
			if (hasToUpdateFacesData){
				Log.i(AppConstants.TAG, "hasToUpdateFacesData");
				String newFacesDataJSON = remoteService.getTrainingData(courseNumber);
				if (newFacesDataJSON != null){
					this.saveFacesDataJson(newFacesDataJSON, courseNumber);
					this.setFacesDataActual(newFacesDataJSON);					
				}
			}
		}
		// Save the facesdata to a file, to be used on recognition
		if (facesDataActual != null){
			String facesDataString = new String(
					FileUtils.decodeFileBase64(facesDataActual
							.getEncodedFacesData()));  
			RecognitionService.saveFacesDataToInternalStorage(facesDataString, activity);
		}
	}
	
	public List<Student> getCourseStudents(){
		return courseActual.getStudents();
	}
	
	public Integer getCourseNumber(){
		return courseActual.getCourseNumber();
	}

	private SharedPreferences getPreferences() {
		return preferences;
	}

	private void setPreferences(SharedPreferences preferences) {
		this.preferences = preferences;
	}

}
