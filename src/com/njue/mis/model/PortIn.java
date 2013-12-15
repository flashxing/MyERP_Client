/**
 * ½ø»õÀà
 */
package com.njue.mis.model;

import java.util.ArrayList;
import java.util.List;



public class PortIn extends Port
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1168557249353855869L;

	public PortIn()
	{
		super();
	}

	public PortIn(String id, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment,String customerId, List<GoodsItem> list)
	{
		super(id, goodsId, storeHouseId, number, price, time, operatePerson,
				comment, customerId, list);
		// TODO Auto-generated constructor stub
	}
	public PortIn clone() {
		List<GoodsItem> list = new ArrayList<GoodsItem>();
		for(GoodsItem item: goodsItemList){
			list.add(item);
		}
		return new PortIn(id, goodsId, storeHouseId,
			number, price, time, operatePerson,
			comment,customerId, list);
	}
}
