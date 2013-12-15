/**
 * ¿Í»§Àà 
 */

package com.njue.mis.model;


public class Customer extends Person
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4847685193901495437L;

	public Customer()
	{
		super();
	}

	public Customer(String id, String customerName, String zip, String address,
			String telephone, String fax, String connectionPerson,
			String phone, String email, String bank, String account, CustomerMoney customerMoney)
	{
		super(id, customerName, zip, address, telephone, fax, connectionPerson, phone,
				email, bank, account, customerMoney);
	}

	public Customer(String id, String name, String zip, String address,
			String telephone, String fax, String connectionPerson,
			String phone, String email, String bank, String account,
			int available,int cateId, CustomerMoney customerMoney)
	{
		super(id, name, zip, address, telephone, fax, connectionPerson, phone, email,
				bank, account, available, cateId, customerMoney);
		// TODO Auto-generated constructor stub
	}
	
}
