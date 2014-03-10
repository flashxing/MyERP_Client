package com.njue.mis.model;

public class ShowSalesDetailObject implements Comparable{
	private String time;
	private String name;
	private String description;
	private int number;
	private double price;
	private double money;
	public ShowSalesDetailObject(String time, String name, String description, int number,
									double price, double money){
		this.time = time;
		this.name = name;
		this.description = description;
		this.number = number;
		this.price = price;
		this.money = money;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	@Override
	public int compareTo(Object arg0) {
		if(!(arg0 instanceof ShowSalesDetailObject)){
			return 0;
		}
		ShowSalesDetailObject object = (ShowSalesDetailObject) arg0;
		return time.compareTo(object.time);
	}
	@Override 
	public int hashCode(){
		return (time+name).hashCode();
	}
	@Override
	public boolean equals(Object object){
		if(!(object instanceof ShowSalesDetailObject)){
			return false;
		}
		ShowSalesDetailObject showSalesDetailObject = (ShowSalesDetailObject) object;
		if(time.equals(showSalesDetailObject.time) && (name.equals(showSalesDetailObject.name))){
			return true;
		}
		return false;
	}
	
}
