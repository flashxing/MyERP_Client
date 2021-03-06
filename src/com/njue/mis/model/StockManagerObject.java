package com.njue.mis.model;

public class StockManagerObject {
	private String goodsName;
	private int salesNumber;
	private double salesPrice;
	private double totalSalesMoney;
	private int portNumber;
	private double portPrice;
	private double totalPortMoney;
	
	private int stockNumber;
	private double stockPrice;
	private double stockMoney;
	
	public StockManagerObject(String goodsName, int saleNumber, double salesPrice, double totalSalesMoney,
			int portNumber, double portPrice, double totalPortMoney,
			int stockNumber, double stockPrice, double stockMoney){
		this.goodsName = goodsName;
		this.salesNumber = saleNumber;
		this.salesPrice = salesPrice;
		this.totalSalesMoney = totalSalesMoney;
		this.portNumber = portNumber;
		this.portPrice = portPrice;
		this.totalPortMoney = totalPortMoney;
		this.stockMoney = stockMoney;
		this.stockNumber = stockNumber;
		this.stockPrice = stockPrice;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public int getSalesNumber() {
		return salesNumber;
	}

	public void setSalesNumber(int salesNumber) {
		this.salesNumber = salesNumber;
	}

	public double getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	public double getTotalSalesMoney() {
		return totalSalesMoney;
	}

	public void setTotalSalesMoney(double totalSalesMoney) {
		this.totalSalesMoney = totalSalesMoney;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public double getPortPrice() {
		return portPrice;
	}

	public void setPortPrice(double portPrice) {
		this.portPrice = portPrice;
	}

	public double getTotalPortMoney() {
		return totalPortMoney;
	}

	public void setTotalPortMoney(double totalPortMoney) {
		this.totalPortMoney = totalPortMoney;
	}

	public int getStockNumber() {
		return stockNumber;
	}

	public void setStockNumber(int stockNumber) {
		this.stockNumber = stockNumber;
	}

	public double getStockPrice() {
		return stockPrice;
	}

	public void setStockPrice(double stockPrice) {
		this.stockPrice = stockPrice;
	}

	public double getStockMoney() {
		return stockMoney;
	}

	public void setStockMoney(double stockMoney) {
		this.stockMoney = stockMoney;
	}
	
}
