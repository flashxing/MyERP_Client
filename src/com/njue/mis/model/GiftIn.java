package com.njue.mis.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GiftIn extends StockInObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4412945858824245418L;
	public GiftIn(){
		super();
	}
	public GiftIn(String id, String receiver, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment, List<StockItem> stockItems)
	{
		super(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}
	
	public GiftIn clone(){
		List<StockItem> tmpList = new ArrayList<>();
		Iterator<StockItem> iterator = this.stockItems.iterator();
		while(iterator.hasNext()){
			tmpList.add(iterator.next().clone());
		}
		return new GiftIn(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}
}
