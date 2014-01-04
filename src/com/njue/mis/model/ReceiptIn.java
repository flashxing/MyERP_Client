package com.njue.mis.model;

import java.util.ArrayList;
import java.util.List;

public class ReceiptIn extends Receipt{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4113231169618792857L;
	public ReceiptIn(){
		
	}
	public ReceiptIn(String id, String customerId, Double money, String time, 
			String operator, String cardName, String comment, List<ReceiptItemDetail> list) {
		super(id, customerId, money, time, operator, cardName, comment, list);
	}
	public ReceiptIn clone(){
		List<ReceiptItemDetail> list = new ArrayList<ReceiptItemDetail>();
		for(ReceiptItemDetail itemDetail: receiptItemDetailList){
			list.add(itemDetail.clone());
		}
		return new ReceiptIn(id, customerId, money, time, operator, cardName, comment, list);
	}
}
