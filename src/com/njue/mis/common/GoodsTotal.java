package com.njue.mis.common;

public class GoodsTotal {
	private String categoryName;
	private int totalNumber;
	private double totalMoney;
	public GoodsTotal(String categoryName, int totalNumber, double totalMoney){
		this.categoryName = categoryName;
		this.totalNumber = totalNumber;
		this.totalMoney = totalMoney;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public int getTotalNumber() {
		return totalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		this.totalNumber = totalNumber;
	}
	public double getTotalMoney() {
		return totalMoney;
	}
	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}
	
}
