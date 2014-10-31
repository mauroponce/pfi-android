package poncemoral.pfi.domain;

import java.io.Serializable;



public class FacesData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer courseNumber;
	private Long creationDateFacesData;  
	private String encodedFacesData;
			
	public FacesData() {}
	
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

	public String getEncodedFacesData() {
		return encodedFacesData;
	}

	public void setEncodedFacesData(String encodedFacesData) {
		this.encodedFacesData = encodedFacesData;
	}

}
