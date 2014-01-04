package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.GiftIn;
import com.njue.mis.model.GiftOut;
import com.njue.mis.model.InCome;
import com.njue.mis.model.OutCome;
import com.njue.mis.model.StockInObject;
import com.njue.mis.model.StockOutObject;

public interface StockObjectControllerInterface extends Remote{
	public String addStockIn(StockInObject stockIn) throws RemoteException;
	public String addStockOut(StockOutObject stockOut) throws RemoteException;
	public boolean deleteStockObject(StockOutObject stockObject) throws RemoteException;
	public boolean updateStockObject(StockOutObject stockObject) throws RemoteException;
	public GiftIn getGiftIn(String id) throws RemoteException;
	public GiftOut getGiftOut(String id) throws RemoteException;
	public InCome getInCome(String id) throws RemoteException;
	public OutCome getOutCome(String id) throws RemoteException;
	public List<GiftIn> getAllGiftIn() throws RemoteException;
	public List<GiftOut> getAllGiftOut() throws RemoteException;
	public List<InCome> getAllInCome() throws RemoteException;
	public List<OutCome> getAllOutCome() throws RemoteException;
}
