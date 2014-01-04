package com.njue.mis.model;

import java.io.Serializable;

public class CardItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6437249864144914108L;
	private String name;
	private String number;
	
	public CardItem(){
		
	}
	
	public CardItem(String name, String number){
		this.name = name;
		this.number = number;
	}
	
	public CardItem(String name){
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	public CardItem clone(){
		return new CardItem(name,number);
	}
}
