/**
 * œ˙ €±Ì
 */

package com.njue.mis.model;

import java.util.ArrayList;
import java.util.List;


public class SalesIn extends Sales
{

	public SalesIn()
	{
		super();
	}

	public SalesIn(String id, String customerId, String goodsId,
			int number, double price, String time,
			String operatePerson, String comment,double discount, double totalPrice, int shId,
			List<SalesGoodsItem> goodsItemsList)
	{
		super(id, customerId, goodsId, number, price, time, operatePerson,
				comment, discount, totalPrice, shId, goodsItemsList);
	}
	
	public SalesIn clone(){
		List<SalesGoodsItem> list = new ArrayList<SalesGoodsItem>();
		for(SalesGoodsItem item: goodsItemsList){
			list.add(item);
		}
		return new SalesIn(id, customerId, goodsId,
			number, price, time, operatePerson,
			comment, discount, totalPrice, shId, list);
	}
}
