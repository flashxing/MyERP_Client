package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.Receipt;
import com.njue.mis.model.ReceiptIn;
import com.njue.mis.model.ReceiptOut;

public interface ReceiptControllerInterface extends Remote{
	public String addReceipt(Receipt receipt) throws RemoteException;
	public boolean updateReceipt(Receipt receipt) throws RemoteException;
	public boolean deleteReceipt(Receipt receipt) throws RemoteException;
	public ReceiptIn getReceiptIn(String id) throws RemoteException;
	public ReceiptOut getReceiptOut(String out) throws RemoteException;
	public List<ReceiptIn> getAllReceiptIn() throws RemoteException;
	public List<ReceiptOut> getAllReceiptOut() throws RemoteException;
	public List<ReceiptIn> getAllReceiptInByTime(String begin, String end) throws RemoteException;
	public List<ReceiptOut> getAllReceiptOutByTime(String begin, String end) throws RemoteException;
}
