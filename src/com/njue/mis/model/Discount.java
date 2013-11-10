package com.njue.mis.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Discount implements Serializable {
	private String customerId;
	private double discount;
	private Map<String, Double> goodsDiscount = new HashMap<String,Double>();
	public Discount(){
		
	}
	
	public Discount(Discount discount){
		this.customerId = discount.customerId;
		this.discount = discount.discount;
		for(Entry<String, Double> entry:discount.goodsDiscount.entrySet()){
			this.goodsDiscount.put(entry.getKey(), entry.getValue());
		}
		for(String goods:this.goodsDiscount.keySet()){
			if(!discount.goodsDiscount.containsKey(goods)){
				this.goodsDiscount.remove(goods);
			}
		}
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public double getDiscount() {
		return discount;
	}
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	public Map<String, Double> getGoodsDiscount() {
		return goodsDiscount;
	}
	public void setGoodsDiscount(Map<String, Double> goodsDiscount) {
		this.goodsDiscount = goodsDiscount;
	}
	public void update(Discount discount){
		this.discount = discount.discount;
		this.goodsDiscount = discount.goodsDiscount;
	}

}
