package com.njue.mis.model;

import java.io.Serializable;

public class MoneyItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2639167852541670566L;
	private String name;
	
	public MoneyItem(){
		
	}
	
	public MoneyItem(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
