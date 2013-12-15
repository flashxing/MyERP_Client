package com.njue.mis.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Money implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4022787320093266634L;
	public String id;
	public Double money;
	public String time;
	public String operator;
	public List<MoneyItemDetail> moneyItemDetailList;
	
	public Money(){
		moneyItemDetailList = new ArrayList<>();
	}
	
	public Money(String id, Double money, String time, String operator, List<MoneyItemDetail> list){
		this.id = id;
		this.money = money;
		this.time = time;
		this.operator = operator;
		this.moneyItemDetailList = list;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Double getMoney() {
		return money;
	}
	public void setMoney(Double money) {
		this.money = money;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public List<MoneyItemDetail> getMoneyItemDetailList() {
		return moneyItemDetailList;
	}
	public void setMoneyItemDetailList(List<MoneyItemDetail> moneyItemDetailList) {
		this.moneyItemDetailList = moneyItemDetailList;
	}
	
	public Money clone(){
		List<MoneyItemDetail> list = new ArrayList<>();
		for(MoneyItemDetail moneyItemDetail:this.moneyItemDetailList){
			list.add(moneyItemDetail.clone());
		}
		return new Money(id, money, time, operator, list);
	}
	
}
