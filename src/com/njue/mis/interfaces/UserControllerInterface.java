package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import com.njue.mis.model.User;

public interface UserControllerInterface extends Remote {
	/**
	 * @param user to save into the db
	 * @return the id of the user
	 * @throws RemoteException
	 */
	public int addUser(User user) throws RemoteException;
	
	/**
	 * @param user to update into the db
	 * @return if the operation is successfull
	 * @throws RemoteException
	 */
	public boolean updateUser(User user) throws RemoteException;
	
	/**
	 * @param user to delete from the db
	 * @return if the operation is successfull
	 * @throws RemoteException
	 */
	public boolean deleteUser(User user) throws RemoteException;
	
	/**
	 * @param id the user's id to get from the db
	 * @return the user get from the db
	 * @throws RemoteException
	 */
	public User getUser(int id) throws RemoteException;
	
	/**
	 * @return all the user in the db
	 * @throws RemoteException
	 */
	public Vector<User> getAllUser() throws RemoteException;
	
	public User validUser(String userName, String passWord) throws RemoteException;
	
}
