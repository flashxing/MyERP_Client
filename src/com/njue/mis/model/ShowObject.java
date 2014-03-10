package com.njue.mis.model;

public class ShowObject{
	private String time;
	private String type;
	private String customerName;
	private int number;
	private double totalPrice;
	private String operator;
	private String id;
	public ShowObject(String id,String time, String type, String customerName, int number, double totalPrice, String operator){
		this.id = id;
		this.time = time;
		this.customerName = customerName;
		this.number = number;
		this.totalPrice = totalPrice;
		this.operator = operator;
		this.type = type;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
}