/**
 * 商品类
 */

package com.njue.mis.model;

import java.io.Serializable;

import com.njue.mis.common.ValidationManager;

public class Goods implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6982905753217184200L;
	private String id;  
	private String productCode;  //商品编码
	private String goodsName;  //商品名称
	private String producePlace;  //商品产地
	private String size;  //规格
	private String _package; //包装
	private String promitCode;  //批转文号
	private double price;  //价格
	private String description;  //描述
	private String providerId;  //供应商编号
	private int cateId;  //状态 非0代表可用
	private double lastSalePrice;	//最近一次售价
	private double lastStockPrice; //最近一次进价
	private int goodsNum;	//商品数量
	public Goods()
	{
		super();
	}
	public Goods(Goods good){
		this.id = good.getId();
		this.goodsName = good.getGoodsName();
		this.producePlace = good.getProducePlace();
		this.size = good.getSize();
		this._package = good.get_package();
		this.productCode = good.getProductCode();
		this.promitCode = good.getPromitCode();
		this.price = good.getPrice();
		this.description = good.getDescription();
		this.providerId = good.getProviderId();
		this.cateId = good.getCateId();
	}
	public void update(Goods good){
		this.id = good.getId();
		this.goodsName = good.getGoodsName();
		this.producePlace = good.getProducePlace();
		this.size = good.getSize();
		this._package = good.get_package();
		this.productCode = good.getProductCode();
		this.promitCode = good.getPromitCode();
		this.price = good.getPrice();
		this.description = good.getDescription();
		this.providerId = good.getProviderId();
		this.cateId = good.getCateId();
	}
	public Goods(String id, String goodsName, String producePlace, String size,
			String _package, String productCode, String promitCode,
			String description,double price, String providerId)
	{
		super();
		this.id = id;
		this.goodsName = goodsName;
		this.producePlace = producePlace;
		this.size = size;
		this._package = _package;
		this.productCode = productCode;
		this.promitCode = promitCode;
		this.price = price;
		this.description = description;
		this.providerId = providerId;
	}
	
	public Goods(String id, String goodsName, String producePlace, String size,
			String _package, String productCode, String promitCode,
			String description,double price, String providerId,
			int cateId)
	{
		super();
		this.id = id;
		this.goodsName = goodsName;
		this.producePlace = producePlace;
		this.size = size;
		this._package = _package;
		this.productCode = productCode;
		this.promitCode = promitCode;
		this.price = price;
		this.description = description;
		this.providerId = providerId;
		this.cateId = cateId;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getGoodsName()
	{
		return goodsName;
	}
	public void setGoodsName(String goodsName)
	{
		this.goodsName = goodsName;
	}
	public String getProducePlace()
	{
		return producePlace;
	}
	public void setProducePlace(String producePlace)
	{
		this.producePlace = producePlace;
	}
	public String getSize()
	{
		return size;
	}
	public void setSize(String size)
	{
		this.size = size;
	}
	public String get_package()
	{
		return _package;
	}
	public void set_package(String _package)
	{
		this._package = _package;
	}
	public String getProductCode()
	{
		return productCode;
	}
	public void setProductCode(String productCode)
	{
		this.productCode = productCode;
	}
	public String getPromitCode()
	{
		return promitCode;
	}
	public void setPromitCode(String promitCode)
	{
		this.promitCode = promitCode;
	}
	public double getPrice()
	{
		return price;
	}
	public void setPrice(double price)
	{
		this.price = price;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getProviderId()
	{
		return providerId;
	}
	public void setProviderId(String providerId)
	{
		this.providerId = providerId;
	}
	public int getCateId()
	{
		return cateId;
	}
	public void setCateId(int cateId)
	{
		this.cateId = cateId;
	}
	
	public Object getGoodsValue(int columnNumber)
	{
		switch (columnNumber)
		{
		case 0:
			return ValidationManager.changeNull(getId());
		case 1:
			return ValidationManager.changeNull(getGoodsName());
		case 2:
			return ValidationManager.changeNull(getProducePlace());
		case 3:
			return ValidationManager.changeNull(getSize());
		case 4:
			return ValidationManager.changeNull(get_package());
		case 5:
			return ValidationManager.changeNull(getProductCode());
		case 6:
			return ValidationManager.changeNull(getPromitCode());
		case 7:
			return ValidationManager.changeNull(getDescription());
		case 8:
			return ValidationManager.changeNull(getPrice());
		case 9:
			return ValidationManager.changeNull(getProviderId());
		default:
			return "";
		}
	}
	public double getLastSalePrice() {
		return lastSalePrice;
	}
	public void setLastSalePrice(double lastSalePrice) {
		this.lastSalePrice = lastSalePrice;
	}
	public double getLastStockPrice() {
		return lastStockPrice;
	}
	public void setLastStockPrice(double lastStockPrice) {
		this.lastStockPrice = lastStockPrice;
	}
	public int getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}
}
