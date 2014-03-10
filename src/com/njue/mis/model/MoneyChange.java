package com.njue.mis.model;

import java.io.Serializable;

public class MoneyChange implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3683801755995193960L;
	private String id;
	private Double money;
	private String time;
	private String operator;
	private String cardName;
	private int type; //0 to add, 1 to dec;
	private int isPublished;
	public MoneyChange(){
		
	}
	public MoneyChange(String id, Double money, String time, String operator, 
						String cardName, int type, int isPublished){
		this.id = id;
		this.money = money;
		this.time = time;
		this.operator = operator;
		this.cardName = cardName;
		this.type =type;
		this.isPublished = isPublished;
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
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getIsPublished() {
		return isPublished;
	}
	public void setIsPublished(int isPublished) {
		this.isPublished = isPublished;
	}
	public MoneyChange clone(){
		return new MoneyChange(id, money, time, operator,cardName, type, isPublished);
	}
}
