package com.njue.mis.model;

public class GiftIn extends Gift{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4412945858824245418L;
	public GiftIn(String id, String receiver, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment)
	{
		super(id, receiver, goodsId, storeHouseId,
				number, price, time, operatePerson,
				comment);
	}
}
