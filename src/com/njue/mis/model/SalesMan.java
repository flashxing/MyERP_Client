package com.njue.mis.model;

import java.io.Serializable;

public class SalesMan implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2359070857421756053L;
	private int id;
	private String name;
	private String phoneNumber;
	
	public SalesMan(){
		
	}
	
	public SalesMan(int id, String name, String phoneNumber){
		this.id = id;
		this.name = name;
		this.phoneNumber = phoneNumber;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public SalesMan clone(){
		return new SalesMan(id, name, phoneNumber);
	}
}
