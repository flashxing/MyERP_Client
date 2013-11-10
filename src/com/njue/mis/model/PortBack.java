/**
 * ÕÀªı¿‡
 */

package com.njue.mis.model;


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
			String comment,int customerId)
	{
		super(id, goodsId, storeHouseId, number, price, time, operatePerson,
				comment, customerId);
		// TODO Auto-generated constructor stub
	}

}
