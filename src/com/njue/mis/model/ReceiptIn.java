package com.njue.mis.model;

public class ReceiptIn extends Receipt{
	public ReceiptIn(){
		
	}
	public ReceiptIn(String id, String customerId, Double money, String time, 
			String operator, String comment) {
		super(id, customerId, money, time, operator, comment);
	}
}
