package com.njue.mis.model;

import java.util.ArrayList;
import java.util.List;

public class ReceiptOut extends Receipt{
	public ReceiptOut(){
		
	}
	public ReceiptOut(String id, String customerId, Double money, String time, 
			String operator, String comment, List<ReceiptItemDetail> list) {
		super(id, customerId, money, time, operator, comment, list);
	}
	public ReceiptOut clone(){
		List<ReceiptItemDetail> list = new ArrayList<ReceiptItemDetail>();
		for(ReceiptItemDetail itemDetail: receiptItemDetailList){
			list.add(itemDetail.clone());
		}
		return new ReceiptOut(id, customerId, money, time, operator, comment, list);
	}
}
