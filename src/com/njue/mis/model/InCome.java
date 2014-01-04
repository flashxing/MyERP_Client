package com.njue.mis.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class InCome extends StockInObject {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1317690347046719287L;
	public InCome(){
		super();
	}
	public InCome(String id, String receiver, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment, List<StockItem> stockItems)
	{
		super(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}
	public InCome clone(){
		List<StockItem> tmpList = new ArrayList<>();
		Iterator<StockItem> iterator = this.stockItems.iterator();
		while(iterator.hasNext()){
			tmpList.add(iterator.next().clone());
		}
		return new InCome(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}
}
