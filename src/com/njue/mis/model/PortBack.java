/**
 * ÕÀªı¿‡
 */

package com.njue.mis.model;

import java.util.ArrayList;
import java.util.List;


public class PortBack extends Port
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3800940854949200869L;

	public PortBack()
	{
		super();
	}

	public PortBack(String id, String goodsId, int storeHouseId,
			int number, double price, String time, String operatePerson,
			String comment,String customerId, List<GoodsItem> list)
	{
		super(id, goodsId, storeHouseId, number, price, time, operatePerson,
				comment, customerId, list);
		// TODO Auto-generated constructor stub
	}
	
	public PortBack clone() {
		List<GoodsItem> list = new ArrayList<GoodsItem>();
		for(GoodsItem item: goodsItemList){
			list.add(item);
		}
		return new PortBack(id, goodsId, storeHouseId,
			number, price, time, operatePerson,
			comment,customerId, list);
	}

}
