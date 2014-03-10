package com.njue.mis.model;

import java.io.Serializable;

public class MoneyItemDetail implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7442790554600569811L;
	private String moneyId;
	private String moneyItem;
	private int id;
	private double money;
	private String comment;
	
	public MoneyItemDetail(){
		
	}
	
	public MoneyItemDetail(String moneyId, int id, String moneyItem, double money, String comment){
		this.moneyId = moneyId;
		this.id = id;
		this.moneyItem = moneyItem;
		this.money = money;
		this.comment = comment;
	}
	public String getMoneyItem() {
		return moneyItem;
	}
	public void setMoneyItem(String moneyItem) {
		this.moneyItem = moneyItem;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	public MoneyItemDetail clone(){
		return new MoneyItemDetail(moneyId, id, moneyItem, money, comment);
	}

	public String getMoneyId() {
		return moneyId;
	}

	public void setMoneyId(String moneyId) {
		this.moneyId = moneyId;
	}
	@Override
	public int hashCode(){
		return (this.moneyId+this.id).hashCode();
	}
	@Override 
	public boolean equals(Object object){
		if(!(object instanceof MoneyItemDetail)){
			return false;
		}
		MoneyItemDetail item = (MoneyItemDetail) object;
		return this.moneyId.equals(item.moneyId)&&this.id==item.id;
	}
}
