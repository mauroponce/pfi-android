package poncemoral.pfi.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class Course implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer courseNumber;
	private Long creationDateFacesData;  
	private List<Student> students = new ArrayList<Student>();
			
	public Course() {}
	
	public Integer getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(Integer courseNumber) {
		this.courseNumber = courseNumber;
	}
	public Long getCreationDateFacesData() {
		return creationDateFacesData;
	}
	public void setCreationDateFacesData(Long creationDateFacesData) {
		this.creationDateFacesData = creationDateFacesData;
	}
	public List<Student> getStudents() {
		return students;
	}
	public void setStudents(List<Student> students) {
		this.students = students;
	}

}
