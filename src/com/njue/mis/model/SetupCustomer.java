package com.njue.mis.model;

import java.io.Serializable;

public class SetupCustomer implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8140722140779803727L;
	private String customerId;
	private double toGive;
	private double toReceive;
	
	public SetupCustomer(){
		
	}
	
	public SetupCustomer(String customerId, double toGive, double toReceive){
		this.customerId = customerId;
		this.toGive = toGive;
		this.toReceive = toReceive;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public double getToGive() {
		return toGive;
	}

	public void setToGive(double toGive) {
		this.toGive = toGive;
	}

	public double getToReceive() {
		return toReceive;
	}

	public void setToReceive(double toReceive) {
		this.toReceive = toReceive;
	}

	public SetupCustomer clone(){
		return new SetupCustomer(customerId, toGive, toReceive);
	}
	
}
