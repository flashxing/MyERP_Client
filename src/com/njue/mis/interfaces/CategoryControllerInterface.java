package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.Category;

public interface CategoryControllerInterface extends Remote {
	/*
	 * @params category the category to add
	 * @returns true if save the category in the db success
	 */
	public int addCategory(Category category) throws RemoteException;
	/*
	 * @params category the category to delete
	 * @returns true if delete hte category in the db success
	 */
	public boolean delCategory(Category category) throws RemoteException;
	/*
	 * @params category the category to update
	 * @returns true if update the category in the db success
	 */
	public boolean updateCategory(Category category) throws RemoteException;
	public List<Category> getAllGoodsCategory() throws RemoteException;
	public List<Category> getAllCustomerCategory() throws RemoteException;
	public Category getGoodsCategory(int cate_id) throws RemoteException;
	public Category getCustomerCategory(int cate_id) throws RemoteException;
	
	public boolean categoryHasCustomer(int cate_id) throws RemoteException;
	
	/*
	 * to check if the category has goods.
	 */
	public boolean categoryHasGoods(int cateId) throws RemoteException;
}
