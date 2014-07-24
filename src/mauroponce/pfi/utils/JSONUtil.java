package mauroponce.pfi.utils;

import java.util.ArrayList;
import java.util.List;

import mauroponce.pfi.domain.Course;
import mauroponce.pfi.domain.FacesData;
import mauroponce.pfi.domain.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JSONUtil<T> {	
	
	/**
	 * Return the list of students from json, or an empty array 
	 * @param jsonString
	 * @return
	 * @throws JSONException
	 */
	public static List<Student> getStudents (String jsonString) throws JSONException{
		List<Student> students = new ArrayList<Student>();
		JSONArray jsonArray = new JSONArray(jsonString);
		for (int i = 0; i < jsonArray.length(); i++) {
			Student student = new Student();
			JSONObject json = jsonArray.getJSONObject(i);
			student.setLU(Integer.parseInt(json.getString("lu")));
			student.setFirstName(json.getString("firstName"));
			student.setLastName(json.getString("lastName"));
			student.setEncodedImage(json.getString("encodedImage"));
			students.add(student);
		}
		return students;
	}
	
	public Course getCourseFromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<Course>(){}.getType());
	}
	
	public FacesData getFacesDataFromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<FacesData>(){}.getType());
	}
	
	public List<T> getListFromJson(String json) {
		Gson gson = new Gson();
		return gson.fromJson(json, new TypeToken<List<T>>(){}.getType());
	}
}
