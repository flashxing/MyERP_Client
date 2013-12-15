package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.MoneyItem;

public interface MoneyItemControllerInterface extends Remote{
	/*
	 * to add a money item into db
	 */
	public String addMoneyItem(MoneyItem moneyItem) throws RemoteException;
	/*
	 * to get all the money item from the db
	 */
	public List<MoneyItem> getAll() throws RemoteException;
}
