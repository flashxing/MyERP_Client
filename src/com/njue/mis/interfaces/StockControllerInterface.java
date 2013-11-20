package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import com.njue.mis.model.Stock;

public interface StockControllerInterface extends Remote{
	public int addStock(Stock stock) throws RemoteException;
	
	public boolean deleteStock(Stock stock) throws RemoteException;
	
	public boolean updateStock(Stock stock) throws RemoteException;
	
	public Vector<Stock> getAllStock() throws RemoteException;
	
	public int getStockId(Stock stock) throws RemoteException;
	
	public Stock getStock(int id) throws RemoteException;
	
	public Stock getStock(int shId, String goodsId) throws RemoteException;
	
}
