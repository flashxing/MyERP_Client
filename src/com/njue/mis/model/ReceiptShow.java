package com.njue.mis.model;

public class ReceiptShow {
	private String time;
	private String customerName;
	private double money;
	private String operator;
	public ReceiptShow(String time, String customerName, double money, String operator){
		this.time = time;
		this.customerName = customerName;
		this.money = money;
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
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	
}
