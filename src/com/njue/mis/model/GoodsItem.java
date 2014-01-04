package com.njue.mis.model;

import java.io.Serializable;

public class GoodsItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5676029465885142862L;
	private int id;
	private String portId;
	private String goodsId;
	private int number;
	private double unitPrice;
	private double totalPrice;
	private String comment;
	public GoodsItem(){
		
	}
	public GoodsItem(int id, String portId, String goodsId, int number,
			double unitPrice, Double totalPrice, String comment){
		this.id = id;
		this.portId = portId;
		this.goodsId = goodsId;
		this.number = number;
		this.unitPrice = unitPrice;
		this.totalPrice = totalPrice;
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
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
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
	
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
	public GoodsItem clone(){
		return new GoodsItem(id, portId, goodsId, number, unitPrice, totalPrice, comment);
	}
}
