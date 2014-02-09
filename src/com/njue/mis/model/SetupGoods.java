package com.njue.mis.model;

import java.io.Serializable;

public class SetupGoods implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3033418048436369782L;
	private String goodsId;
	private int shId;
	private int number;
	
	public SetupGoods(){
		
	}
	
	public SetupGoods(String goodsId, int shId, int number){
		this.goodsId = goodsId;
		this.shId = shId;
		this.number = number;
	}
	
	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public int getShId() {
		return shId;
	}

	public void setShId(int shId) {
		this.shId = shId;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public SetupGoods clone(){
		return new SetupGoods(goodsId, shId, number);
	}
}
