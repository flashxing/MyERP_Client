package com.njue.mis.model;

public class GiftOut extends Gift{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4412945858824245418L;
	public GiftOut(String id, String receiver, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment)
	{
		super(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment);
	}
}
