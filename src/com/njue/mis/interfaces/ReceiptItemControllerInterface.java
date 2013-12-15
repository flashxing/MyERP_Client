package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.ReceiptItem;

public interface ReceiptItemControllerInterface extends Remote{
	public String addReceiptItem(ReceiptItem receiptItem) throws RemoteException;
	public boolean updateReceiptItem(ReceiptItem receiptItem) throws RemoteException;
	public boolean deleteReceiptItem(ReceiptItem receiptItem) throws RemoteException;
	public ReceiptItem getReceiptItem(String item) throws RemoteException;
	public List<ReceiptItem> getAll() throws RemoteException;
}
