/**
 * ¿Í»§Àà 
 */

package com.njue.mis.model;


public class Customer extends Person
{

	public Customer()
	{
		super();
	}

	public Customer(String id, String customerName, String zip, String address,
			String telephone, String fax, String connectionPerson,
			String phone, String email, String bank, String account)
	{
		super(id, customerName, zip, address, telephone, fax, connectionPerson, phone,
				email, bank, account);
	}

	public Customer(String id, String name, String zip, String address,
			String telephone, String fax, String connectionPerson,
			String phone, String email, String bank, String account,
			int available,int cateId)
	{
		super(id, name, zip, address, telephone, fax, connectionPerson, phone, email,
				bank, account, available, cateId);
		// TODO Auto-generated constructor stub
	}
	
}
