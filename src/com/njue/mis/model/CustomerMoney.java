package com.njue.mis.model;

import java.io.Serializable;

public class CustomerMoney implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7617388663870169238L;
	private String customerId;
	private double give;
	private double receive;
	public CustomerMoney(){
		
	}
	
	public CustomerMoney(String customerId, double give, double receive){
		this.customerId = customerId;
		this.give = give;
		this.receive = receive;
	}
	
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public double getGive() {
		return give;
	}

	public void setGive(double give) {
		this.give = give;
	}

	public double getReceive() {
		return receive;
	}

	public void setReceive(double receive) {
		this.receive = receive;
	}

	public CustomerMoney clone(){
		return new CustomerMoney(customerId,give,receive);
	}
}
