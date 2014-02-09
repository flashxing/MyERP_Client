package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.SetupCustomer;

public interface SetupCustomerControllerInterface extends Remote{
	public boolean saveOrUpdateSetupCustomerList(List<SetupCustomer> setupCustomerList) throws RemoteException;
	public List<SetupCustomer> getAllSetupCustomer(List<String> customerIdList) throws RemoteException;
}
