/**
 * 抽象类
 */

package com.njue.mis.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Port implements Serializable
{
	/**
	 * 
	 */
	protected static final long serialVersionUID = -6974161517485100177L;
	protected String id;  //编号
	protected String goodsId;  //商品编号
	protected int storeHouseId;  //仓库
	protected int number;  //数量
	protected double price; //价格
	protected String time;  //支付时间
	protected String operatePerson;  //操作员
	protected String comment;  //注释
	protected String customerId; //客户id
	protected List<GoodsItem> goodsItemList;

	public Port()
	{
		super();
		goodsItemList = new ArrayList<GoodsItem>();
		// TODO Auto-generated constructor stub
	}
	public Port(String id, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment,String customerId, List<GoodsItem> goodsItemList)
	{
		super();
		this.id = id;
		this.goodsId = goodsId;
		this.number = number;
		this.price = price;
		this.time = time;
		this.operatePerson = operatePerson;
		this.comment = comment;
		this.storeHouseId = storeHouseId;
		this.customerId = customerId;
		this.goodsItemList = goodsItemList;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public int getStoreHouseId()
	{
		return this.storeHouseId;
	}
	public void setStoreHouseId(int storeHouseId)
	{
		this.storeHouseId = storeHouseId;
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
	public void setInportTime(String time)
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
	public void setTime(String time)
	{
		this.time = time;
	}
	public void setCustomerId(String customerId){
		this.customerId = customerId;
	}
	public String getCustomerId(){
		return this.customerId;
	}
	public List<GoodsItem> getGoodsItemList() {
		return goodsItemList;
	}
	public void setGoodsItemList(List<GoodsItem> goodsItemList) {
		this.goodsItemList = goodsItemList;
	}
	
	public Port clone(){
		List<GoodsItem> list = new ArrayList<GoodsItem>();
		for(GoodsItem item: goodsItemList){
			list.add(item);
		}
		return new Port(id, goodsId, storeHouseId,
			number, price, time, operatePerson,
			comment,customerId, list);
	}
}
