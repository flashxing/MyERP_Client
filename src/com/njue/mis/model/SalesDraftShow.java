package com.njue.mis.model;

public class SalesDraftShow{
	private String time;
	private String customerName;
	private int number;
	private double totalPrice;
	private String operator;
	public SalesDraftShow(String time, String customerName, int number, double totalPrice, String operator){
		this.time = time;
		this.customerName = customerName;
		this.number = number;
		this.totalPrice = totalPrice;
		this.operator = operator;
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
}