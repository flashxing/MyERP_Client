package com.njue.mis.model;

import java.io.Serializable;

public class ReceiptItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7965842307646817073L;
	private String item;
	public ReceiptItem(){
		
	}
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	
}
