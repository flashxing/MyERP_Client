/**
 * œ˙ €±Ì
 */

package com.njue.mis.model;

import java.util.ArrayList;
import java.util.List;


public class SalesIn extends Sales
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3586454332510730208L;

	public SalesIn()
	{
		super();
	}

	public SalesIn(String id, String customerId, String goodsId,
			int number, double price, String time,
			String operatePerson, String comment,double discount, double totalPrice,
			Double decreasePrice,int shId, int isPublished, String salesMan,
			List<SalesGoodsItem> goodsItemsList)
	{
		super(id, customerId, goodsId, number, price, time, operatePerson,
				comment, discount, totalPrice, decreasePrice, shId, isPublished, salesMan, goodsItemsList);
	}
	
	public SalesIn clone(){
		List<SalesGoodsItem> list = new ArrayList<SalesGoodsItem>();
		for(SalesGoodsItem item: goodsItemsList){
			list.add(item);
		}
		return new SalesIn(id, customerId, goodsId,
			number, price, time, operatePerson,
			comment, discount, totalPrice, decreasePrice, shId, isPublished, salesMan, list);
	}
}
