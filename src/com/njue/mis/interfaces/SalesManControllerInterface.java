package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.SalesMan;

public interface SalesManControllerInterface extends Remote {
	public int addSalesMan(SalesMan salesMan) throws RemoteException;
	public boolean updateSalesMan(SalesMan salesMan) throws RemoteException;
	public boolean deleteSalesMan(SalesMan salesMan) throws RemoteException;
	public SalesMan getSalesManById(int id) throws RemoteException;
	public List<SalesMan> getAllSalesMans() throws RemoteException;
	public List<SalesMan> getSalesMansByIds(List<Integer> idList) throws RemoteException;
}
