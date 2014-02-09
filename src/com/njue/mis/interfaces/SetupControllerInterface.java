package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SetupControllerInterface extends Remote{
	public boolean setUp() throws RemoteException;
}
