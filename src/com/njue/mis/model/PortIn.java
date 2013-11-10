/**
 * ½ø»õÀà
 */
package com.njue.mis.model;



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
			String comment,int customerId)
	{
		super(id, goodsId, storeHouseId, number, price, time, operatePerson,
				comment, customerId);
		// TODO Auto-generated constructor stub
	}

}
