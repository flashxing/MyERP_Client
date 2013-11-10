package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

import com.njue.mis.model.Discount;

public interface DiscountControllerInterface extends Remote{
	/**
	 * @param discount the discount object to save into the MySQL
	 * @return the id of the discount if success
	 * 			else null
	 * @throws RemoteException
	 */
	public String addDiscount(Discount discount) throws RemoteException;
	
	/**
	 * @param id the id of discount to delete 
	 * @return if delete success
	 * @throws RemoteException
	 */
	public boolean deleteDiscount(String id) throws RemoteException;
	
	/**
	 * @param discount to discount to update
	 * @return if update success
	 * @throws RemoteException
	 */
	public boolean updateDiscount(Discount discount) throws RemoteException;
	
	/**
	 * @param id the id of the discount to get
	 * @return the discount of the id.
	 * 			null if the id isn't existed in the db
	 * @throws RemoteException
	 */
	public Discount getDiscount(String id) throws RemoteException;
}
