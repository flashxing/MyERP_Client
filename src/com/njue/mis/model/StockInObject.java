package com.njue.mis.model;

import java.util.List;

public class StockInObject extends StockObject{
	public StockInObject(){
		super();
	}
	public StockInObject(String id, String receiver, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment, List<StockItem> stockItems)
	{
		super(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment, stockItems);
	}
}
