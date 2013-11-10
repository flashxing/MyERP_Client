/**
 * œ˙ €±Ì
 */

package com.njue.mis.model;


public class SalesIn extends Sales
{

	public SalesIn()
	{
		super();
	}

	public SalesIn(String id, String customerId, String goodsId,
			int number, double price, String time,
			String operatePerson, String comment,double discount, double totalPrice, int shId)
	{
		super(id, customerId, goodsId, number, price, time, operatePerson,
				comment, discount, totalPrice, shId);
	}

}
