package com.njue.mis.model;

import java.io.Serializable;

public class StockItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8984888292909372562L;
	private int id;
	private String portId;
	private String goodsId;
	private int number;
	private String comment;
	public StockItem(){
		
	}
	public StockItem(int id, String portId, String goodsId, int number, String comment){
		this.id = id;
		this.portId = portId;
		this.goodsId = goodsId;
		this.number = number;
		this.comment =comment;
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

	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public String getPortId() {
		return portId;
	}
	public void setPortId(String portId) {
		this.portId = portId;
	}
	public StockItem clone(){
		return new StockItem(id, portId, goodsId, number, comment);
	}
}
