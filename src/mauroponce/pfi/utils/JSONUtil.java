package mauroponce.pfi.utils;

import java.util.ArrayList;
import java.util.List;

import mauroponce.pfi.domain.Student;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtil {	
	
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
}
