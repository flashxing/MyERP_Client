package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import com.njue.mis.model.UserRole;

public interface UserRoleControllerInterface extends Remote {
	/**
	 * @param user to save into the db
	 * @return the id of the user
	 * @throws RemoteException
	 */
	public int addUserRole(UserRole userRole) throws RemoteException;
	
	/**
	 * @param user to update into the db
	 * @return if the operation is successfull
	 * @throws RemoteException
	 */
	public boolean updateUserRole(UserRole userRole) throws RemoteException;
	
	/**
	 * @param user to delete from the db
	 * @return if the operation is successfull
	 * @throws RemoteException
	 */
	public boolean deleteUserRole(UserRole userRole) throws RemoteException;
	
	/**
	 * @param id the user's id to get from the db
	 * @return the user get from the db
	 * @throws RemoteException
	 */
	public UserRole getUserRole(int id) throws RemoteException;
	
	/**
	 * @return all the user in the db
	 * @throws RemoteException
	 */
	public Vector<UserRole> getAllUserRoles() throws RemoteException;
	
}
