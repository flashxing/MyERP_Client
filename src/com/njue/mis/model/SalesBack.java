/**
 * ÕÀªı¿‡
 */

package com.njue.mis.model;


public class SalesBack extends Sales
{

	public SalesBack()
	{
		super();
	}

	public SalesBack(String id, String customerId, String goodsId,
			String payType, int number, double price, String time,
			String operatePerson, String comment,double discount, double totalPrice, int shId)
	{
		super(id, customerId, goodsId, number, price, time, operatePerson,
				comment, discount, totalPrice, shId);
	}

}
