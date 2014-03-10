package com.njue.mis.model;

import java.io.Serializable;

public class ReceiptChange implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7765910073834261942L;
	private String id;
	private String customerId;
	private Double money;
	private String time;
	private String operator;
	private String comment;
	private int type; //0 is receive to add; 1 is receive to dec; 2 is give to add; 3 is give to dec;
	private int isPublished;
	public ReceiptChange(){
		
	}
	public ReceiptChange(String id, String customerId, Double money, String time,
							String operator, String comment, int type, int isPublished){
		this.id = id;
		this.customerId = customerId;
		this.money = money;
		this.time = time;
		this.operator = operator;
		this.comment = comment;
		this.type = type;
		this.isPublished = isPublished;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
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
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public ReceiptChange clone(){
		return new ReceiptChange(id, customerId, money, time, operator, comment, type, isPublished);
	}
}
