/**
 * 抽象类
 */

package com.njue.mis.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public abstract class Sales implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6443419034025095969L;
	public String id;  //编号
	public String customerId;  //客户编号
	public String goodsId;  //商品编号
	public int number;  //数量
	public String time;  //时间
	public String operatePerson; //操作员  
	public String comment;  //注释
	public double discount;
	public double totalPrice;
	public double decreasePrice;
	public double price;  //价格
	public int shId;
	public int isPublished;
	public String salesMan;
	public List<SalesGoodsItem> goodsItemsList;
	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}

	public int getShId() {
		return shId;
	}

	public void setShId(int shId) {
		this.shId = shId;
	}

	public Sales()
	{
		super();
		goodsItemsList = new ArrayList<>();
	}

	public Sales(String id, String customerId, String goodsId, 
			int number, double price, String time, String operatePerson,
			String comment,double discount, double totalPrice, double decreasePrice, int shId,
			int isPublished, String salesMan, List<SalesGoodsItem> goodsItemList)
	{
		super();
		this.id = id;
		this.customerId = customerId;
		this.goodsId = goodsId;
		this.number = number;
		this.price = price;
		this.time = time;
		this.operatePerson = operatePerson;
		this.comment = comment;
		this.discount = discount;
		this.totalPrice = totalPrice;
		this.decreasePrice = decreasePrice;
		this.shId = shId;
		this.isPublished = isPublished;
		this.salesMan = salesMan;
		this.goodsItemsList = goodsItemList;
	}

	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getCustomerId()
	{
		return customerId;
	}
	public void setCustomerId(String customerId)
	{
		this.customerId = customerId;
	}
	public String getGoodsId()
	{
		return goodsId;
	}
	public void setGoodsId(String goodsId)
	{
		this.goodsId = goodsId;
	}

	public int getNumber()
	{
		return number;
	}
	public void setNumber(int number)
	{
		this.number = number;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public String getOperatePerson()
	{
		return operatePerson;
	}
	public void setOperatePerson(String operatePerson)
	{
		this.operatePerson = operatePerson;
	}
	public String getComment()
	{
		return comment;
	}
	public void setComment(String comment)
	{
		this.comment = comment;
	}
	public double getPrice()
	{
		return price;
	}
	public void setPrice(double price)
	{
		this.price = price;
	}
	public List<SalesGoodsItem> getGoodsItemsList() {
		return goodsItemsList;
	}
	public void setGoodsItemsList(List<SalesGoodsItem> goodsItemList) {
		this.goodsItemsList = goodsItemList;
	}

	public double getDecreasePrice() {
		return decreasePrice;
	}

	public void setDecreasePrice(double decreasePrice) {
		this.decreasePrice = decreasePrice;
	}

	public int getIsPublished() {
		return isPublished;
	}

	public void setIsPublished(int isPublished) {
		this.isPublished = isPublished;
	}

	public String getSalesMan() {
		return salesMan;
	}

	public void setSalesMan(String salesMan) {
		this.salesMan = salesMan;
	}
	
}
