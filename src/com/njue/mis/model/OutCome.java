package com.njue.mis.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OutCome extends StockOutObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2212808464180823375L;
	public OutCome(){
		super();
	}
	public OutCome(String id, String receiver, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment, List<StockItem> stockItems)
	{
		super(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}
	public OutCome clone(){
		List<StockItem> tmpList = new ArrayList<>();
		Iterator<StockItem> iterator = this.stockItems.iterator();
		while(iterator.hasNext()){
			tmpList.add(iterator.next().clone());
		}
		return new OutCome(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}
}
