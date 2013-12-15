/**
 * �����࣬��������������Ķ���
 */
package com.njue.mis.common;

import com.njue.mis.interfaces.CustomerServicesHandler;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.OperatorControllerInterface;
import com.njue.mis.interfaces.SalesBackServicesHandler;
import com.njue.mis.interfaces.SalesInServicesHandler;

public class CommonFactory
{
	/**
	 * ��ȡGoodsService��Ķ���
	 * @return GoodsService��Ķ���
	 */
	public static GoodsControllerInterface getGoodsServices()
	{
		try
		{
			return (GoodsControllerInterface)Class.forName(Constants.GOODS_SERVICES_CLASS).newInstance();
		}
		catch (Exception e)
		{
			ErrorManager.printError("CommontFactory.getGoodsServices", e);
		}
		return null;
	}
	
	/**
	 * ��ȡCustomerServices��Ķ���
	 * @return CustomerServices��Ķ���
	 */
	public static CustomerServicesHandler getCustomerServices()
	{
		try
		{
			return (CustomerServicesHandler)Class.forName(Constants.CUSTOMER_SERVICES_CLASS).newInstance();
		}
		catch (Exception e)
		{
			ErrorManager.printError("CommontFactory.getCustomerServices", e);
		}
		return null;
	}
	/**
	 * ��ȡProviderServices��Ķ���
	 * @return ProviderServices��Ķ���
	 */

	/**
	 * ��ȡOperatorServices��Ķ���
	 * @return OperatorServices��Ķ���
	 */
	public static OperatorControllerInterface getOperatorServices()
	{
		try
		{
			return (OperatorControllerInterface)Class.forName(Constants.OPERATOR_SERVICES_CLASS).newInstance();
		}
		catch (Exception e)
		{
			ErrorManager.printError("CommontFactory.getOperatorServices", e);
		}
		return null;
	}
	/**
	 * ��ȡPortInServices��Ķ���
	 * @return PortInServices��Ķ���
	 */

	/**
	 * ��ȡPortOutServices��Ķ���
	 * @return PortOutServices��Ķ���
	 */

	
	/**
	 * ��ȡSalesBackServices��Ķ���
	 * @return SalesBackServices��Ķ���
	 */
	public static SalesInServicesHandler getSalesInServices()
	{
		try
		{
			return (SalesInServicesHandler)Class.forName(Constants.SALESIN_SERVICES_CLASS).newInstance();
		}
		catch (Exception e)
		{
			ErrorManager.printError("CommontFactory.SalesInServicesHandler", e);
		}
		return null;
	}
	/**
	 * ��ȡSalesBackServices��Ķ���
	 * @return SalesBackServices��Ķ���
	 */
	public static SalesBackServicesHandler getSalesBackServices()
	{
		try
		{
			return (SalesBackServicesHandler)Class.forName(Constants.SALESBACK_SERVICES_CLASS).newInstance();
		}
		catch (Exception e)
		{
			ErrorManager.printError("CommontFactory.getSalesBackServices", e);
		}
		return null;
	}
}
