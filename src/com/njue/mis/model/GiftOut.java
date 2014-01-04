package com.njue.mis.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GiftOut extends StockOutObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4412945858824245418L;
	public GiftOut(){
		super();
	}
	public GiftOut(String id, String receiver, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment, List<StockItem> stockItems)
	{
		super(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}	
	public GiftOut clone(){
		List<StockItem> tmpList = new ArrayList<>();
		Iterator<StockItem> iterator = this.stockItems.iterator();
		while(iterator.hasNext()){
			tmpList.add(iterator.next().clone());
		}
		return new GiftOut(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}
}
