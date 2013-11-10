/**
 *用户操作接口，定义对用户进行的操作 
 */

package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import com.njue.mis.model.Customer;

public interface CustomerControllerInterface extends Remote
{
	/**
	 * 向数据库中添加新客户
	 * @param customer 封装好的客户
	 * @return 执行结果
	 */
	public String addCustomer(Customer customer) throws RemoteException;
	
	/**
	 * 从数据库中删除指定客户的信息
	 * @param id 被删除客户的编号
	 * @return 执行结果
	 */
	public boolean deleteCustomer(String id) throws RemoteException;
	/**
	 * 查询数据库中满足条件的客户
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	public Vector<Customer> searchCustomer(String field,String value) throws RemoteException;
	
	/**
	 * @param name the key word for search
	 * @return the list of the result
	 * @throws RemoteException
	 */
	public Vector<Customer> searchCustomerByName(String name) throws RemoteException;
	
	/**
	 * 更新客户信息
	 * @param customer 封装好的客户新信息
	 */
	public boolean updateCustomer(Customer customer) throws RemoteException;
	/**
	 * 获取所有客户信息
	 * @return 客户集合
	 */
	public Vector<Customer> getAllCustomer()throws RemoteException;
	/**
     * 判断用户是否存在
     * @param id 查询的用户便还
     * @return 查询结果
     */
    public boolean isExisted(String id)throws RemoteException;
    /**
     * 获取特定客户的信息
     * @param id 客户编号
     * @return 查询结果
     */
    public Customer getCustomerInfo(String id)throws RemoteException;
	public Vector<Customer> getAllCustomerByCateId(int cateId) throws RemoteException;
}
