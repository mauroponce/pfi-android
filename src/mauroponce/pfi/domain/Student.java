package mauroponce.pfi.domain;

import java.io.Serializable;


public class Student implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String firstName;
	private String lastName;
	private Integer LU;
	private String encodedImage;
	
	public Student() {}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public Integer getLU() {
		return LU;
	}
	public void setLU(Integer lU) {
		LU = lU;
	}
	
	public String getEncodedImage() {
		return encodedImage;
	}
	public void setEncodedImage(String encodedImage) {
		this.encodedImage = encodedImage;
	}
	public String getFullName(){
		return lastName + ", " + firstName;
	}
}
