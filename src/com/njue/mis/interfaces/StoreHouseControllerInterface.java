package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.StoreHouse;

public interface StoreHouseControllerInterface extends Remote{
	/*
	 * @params storeHouse the object to add
	 * @return the id of the object
	 */
	public int saveStoreHouse(StoreHouse storeHouse) throws RemoteException;
	
	/*
	 * @params storeHouse the object to delete
	 * @return true if the operation success
	 * 		else return false;
	 */
	public boolean deleteStoreHouse(StoreHouse storeHouse) throws RemoteException;
	
	/*
	 * @params storeHouse the object to update
	 * @return true if the operation success
	 * 		else return false;
	 */
	public boolean updateStoreHouse(StoreHouse storeHouse) throws RemoteException;
	
	/*
	 * @params id the id of the object to get
	 * @return the storeHouse to get
	 */
	public StoreHouse getStoreHouse(int id) throws RemoteException;
	
	/*
	 * @return all the storeHouse list
	 */
	public List<StoreHouse> getAll() throws RemoteException;
}
