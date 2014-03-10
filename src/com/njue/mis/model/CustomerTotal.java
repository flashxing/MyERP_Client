package com.njue.mis.model;

public class CustomerTotal {
	private String categoryName;
	private double totalGive;
	private double totalReceive;
	public CustomerTotal(String categoryName, double totalGive, double totalReceive){
		this.categoryName = categoryName;
		this.totalGive = totalGive;
		this.totalReceive = totalReceive;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public double getTotalGive() {
		return totalGive;
	}
	public void setTotalGive(double totalGive) {
		this.totalGive = totalGive;
	}
	public double getTotalReceive() {
		return totalReceive;
	}
	public void setTotalReceive(double totalReceive) {
		this.totalReceive = totalReceive;
	}
}
