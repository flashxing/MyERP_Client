package com.njue.mis.model;

public class ReceiptOut extends Receipt{
	public ReceiptOut(){
		
	}
	public ReceiptOut(String id, String customerId, Double money, String time, 
			String operator, String comment) {
		super(id, customerId, money, time, operator, comment);
	}
}
