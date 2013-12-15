package com.njue.mis.model;

import java.util.ArrayList;
import java.util.List;

public class ReceiptIn extends Receipt{
	public ReceiptIn(){
		
	}
	public ReceiptIn(String id, String customerId, Double money, String time, 
			String operator, String comment, List<ReceiptItemDetail> list) {
		super(id, customerId, money, time, operator, comment, list);
	}
	public ReceiptIn clone(){
		List<ReceiptItemDetail> list = new ArrayList<ReceiptItemDetail>();
		for(ReceiptItemDetail itemDetail: receiptItemDetailList){
			list.add(itemDetail.clone());
		}
		return new ReceiptIn(id, customerId, money, time, operator, comment, list);
	}
}
