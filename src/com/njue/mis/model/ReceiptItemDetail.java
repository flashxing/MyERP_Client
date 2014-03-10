package com.njue.mis.model;

import java.io.Serializable;

public class ReceiptItemDetail implements Serializable {
	private String receiptId;
	private String cardName;
	private int id;
	private double money;
	private String comment;
	
	public ReceiptItemDetail(){
		
	}
	
	public ReceiptItemDetail(String receiptId, int id, String cardName, double money, String comment){
		this.receiptId = receiptId;
		this.id = id;
		this.cardName = cardName;
		this.money = money;
		this.comment = comment;
	}
	public String getCardName() {
		return cardName;
	}
	public void setCardName(String cardName) {
		this.cardName = cardName;
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
		return new ReceiptItemDetail(receiptId, id, cardName, money, comment);
	}

	public String getReceiptId() {
		return receiptId;
	}

	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}
	@Override
	public int hashCode(){
		return (this.receiptId+this.id).hashCode();
	}
	@Override 
	public boolean equals(Object object){
		if(!(object instanceof ReceiptItemDetail)){
			return false;
		}
		ReceiptItemDetail item = (ReceiptItemDetail) object;
		return this.receiptId.equals(item.receiptId)&&this.id==item.id;
	}
}
