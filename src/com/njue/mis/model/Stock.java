package com.njue.mis.model;

import java.io.Serializable;

public class Stock implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4673557627828948056L;
	private int id;
	private String goodsId;
	private int number;
	private int shId;
	
	public void update(Stock stock){
		this.id = stock.getId();
		this.goodsId = stock.getGoodsId();
		this.number = stock.getNumber();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	
	@Override
	public String toString(){
		return this.getGoodsId();
	}

	public int getShId() {
		return shId;
	}

	public void setShId(int shId) {
		this.shId = shId;
	}
}
