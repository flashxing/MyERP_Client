package com.njue.mis.model;

import java.io.Serializable;

public class ReceiptItemDetail implements Serializable {
	private String receiptId;
	private String receiptItem;
	private int id;
	private double money;
	private String comment;
	
	public ReceiptItemDetail(){
		
	}
	
	public ReceiptItemDetail(String receiptId, int id, String receiptItem, double money, String comment){
		this.receiptId = receiptId;
		this.id = id;
		this.receiptItem = receiptItem;
		this.money = money;
		this.comment = comment;
	}
	public String getReceiptItem() {
		return receiptItem;
	}
	public void setReceiptItem(String receiptItem) {
		this.receiptItem = receiptItem;
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

	public ReceiptItemDetail clone(){
		return new ReceiptItemDetail(receiptId, id, receiptItem, money, comment);
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
}
