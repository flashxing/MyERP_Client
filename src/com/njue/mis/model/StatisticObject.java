package com.njue.mis.model;

public class StatisticObject {
	public String itemName;
	public String subItemName;
	public double number ;
	public StatisticObject(){
		itemName = "";
		subItemName = "";
		number = 0;
	}
	
	public StatisticObject(String itemName, String subItemName, double number){
		this.itemName = itemName;
		this.subItemName = subItemName;
		this.number = number;
	}
}
