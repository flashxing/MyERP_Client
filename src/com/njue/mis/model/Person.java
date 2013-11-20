/**
 * 抽象类
 */

package com.njue.mis.model;

import java.io.Serializable;

import com.njue.mis.common.ValidationManager;


public abstract class Person implements Serializable
{
	public String id;  //编号
	public String name;  //全称
	public String zip;  //邮编
	public String address;  //公司地址
	public String telephone;  //公司电话
	public String fax;  //公司传真
	public String connectionPerson;  //联系人
	public String phone;  //联系电话
	public String email;  //联系人邮箱
	public String bank;  //开户银行
	public String account;  //开户账号
	public int available;  //状态 非0代表可用
	public int cateId;

	public Person()
	{
		super();
	}
	public Person(String id, String name, String zip, String address,
			String telephone, String connectionPerson, String phone,
			String bank, String account, String email, String fax)
	{
		super();
		this.id = id;
		this.name = name;
		this.zip = zip;
		this.address = address;
		this.telephone = telephone;
		this.fax = fax;
		this.connectionPerson = connectionPerson;
		this.phone = phone;
		this.email = email;
		this.bank = bank;
		this.account = account;
	}
	
	public Person(String id, String name, String zip, String address,
			String telephone, String fax, String connectionPerson,
			String phone, String email, String bank, String account,
			int available, int cateId)
	{
		super();
		this.id = id;
		this.name = name;
		this.zip = zip;
		this.address = address;
		this.telephone = telephone;
		this.fax = fax;
		this.connectionPerson = connectionPerson;
		this.phone = phone;
		this.email = email;
		this.bank = bank;
		this.account = account;
		this.available = available;
		this.cateId = cateId;
	}
	
	public void update(Person person){
		this.id = person.id;
		this.name = person.name;
		this.zip = person.zip;
		this.address = person.address;
		this.telephone = person.telephone;
		this.fax = person.fax;
		this.connectionPerson = person.connectionPerson;
		this.phone = person.phone;
		this.email = person.email;
		this.bank = person.bank;
		this.account = person.account;
		this.available = person.available;
	}
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name =name;
	}
	public String getZip()
	{
		return zip;
	}
	public void setZip(String zip)
	{
		this.zip = zip;
	}
	public String getAddress()
	{
		return address;
	}
	public void setAddress(String address)
	{
		this.address = address;
	}
	public String getTelephone()
	{
		return telephone;
	}
	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}
	public String getFax()
	{
		return fax;
	}
	public void setFax(String fax)
	{
		this.fax = fax;
	}
	public String getConnectionPerson()
	{
		return connectionPerson;
	}
	public void setConnectionPerson(String connectionPerson)
	{
		this.connectionPerson = connectionPerson;
	}
	public String getPhone()
	{
		return phone;
	}
	public void setPhone(String phone)
	{
		this.phone = phone;
	}
	public String getEmail()
	{
		return email;
	}
	public void setEmail(String email)
	{
		this.email = email;
	}
	public String getBank()
	{
		return bank;
	}
	public void setBank(String bank)
	{
		this.bank = bank;
	}
	public String getAccount()
	{
		return account;
	}
	public void setAccount(String account)
	{
		this.account = account;
	}
	public void setAvailable(int available)
	{
		this.available = available;
	}
	public int getAvailable()
	{
		return available;
	}
	public void setCateId(int cateId){
		this.cateId = cateId;
	}
	public int getCateId(){
		return cateId;
	}
	/**
	 * 表格控件调用
	 * @param columnNumber 列号
	 * @return 对应列的值
	 */
	public Object getPersonValue(int columnNumber)
	{
		switch (columnNumber)
		{
		case 0:
			return ValidationManager.changeNull(getId());
		case 1:
			return ValidationManager.changeNull(getName());
		case 2:
			return ValidationManager.changeNull(getZip());
		case 3:
			return ValidationManager.changeNull(getAddress());
		case 4:
			return ValidationManager.changeNull(getTelephone());
		case 5:
			return ValidationManager.changeNull(getConnectionPerson());
		case 6:
			return ValidationManager.changeNull(getPhone());
		case 7:
			return ValidationManager.changeNull(getBank());
		case 8:
			return ValidationManager.changeNull(getAccount());
		case 9:
			return ValidationManager.changeNull(getConnectionPerson());
		case 10:
			return ValidationManager.changeNull(getFax());
		default:
			return "";
		}
	}
}
