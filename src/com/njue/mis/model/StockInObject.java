package com.njue.mis.model;

public class StockInObject extends StockObject{
	public StockInObject(String id, String receiver, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment)
	{
		super(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment);
	}
}
