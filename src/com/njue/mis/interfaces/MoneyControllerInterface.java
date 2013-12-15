package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.Money;

public interface MoneyControllerInterface extends Remote{
	public String addMoney(Money money) throws RemoteException;
	public boolean deleteMoney(Money money) throws RemoteException;
	public boolean updateMoney(Money money) throws RemoteException;
	public Money getMoney(String id) throws RemoteException;
	public List<Money> getMoneysByTime(String begin, String end) throws RemoteException;
}
