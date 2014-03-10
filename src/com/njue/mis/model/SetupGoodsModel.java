package com.njue.mis.model;

import com.njue.mis.common.CommonUtil;

public class SetupGoodsModel {
	private String productCode;
	private String goodsName;
	private String description;
	private String size;
	private int number;
	private double price;
	private double totalPrice;
	public SetupGoodsModel(){
		productCode = "";
		goodsName = "";
		description = "";
		size = "";
	}
	public SetupGoodsModel(String productCode, String goodsName, String description, String size,
							int number, double price){
		this.productCode = productCode;
		this.goodsName = goodsName;
		this.description = description;
		this.size = size;
		this.number = number;
		this.price = price;
		this.totalPrice = CommonUtil.formateDouble(number*price);
	}
	public String getProductCode() {
		return productCode;
	}
	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

}
