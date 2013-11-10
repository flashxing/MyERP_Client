package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import com.njue.mis.model.Module;
import com.njue.mis.model.Role;

public interface RoleControllerInterface extends Remote {
	/**
	 * @param role to save into the db
	 * @return the id of the user
	 * @throws RemoteException
	 */
	public int addRole(Role role) throws RemoteException;
	
	/**
	 * @param user to update into the db
	 * @return if the operation is successfull
	 * @throws RemoteException
	 */
	public boolean updateRole(Role role) throws RemoteException;
	
	/**
	 * @param role to delete from the db
	 * @return if the operation is successfull
	 * @throws RemoteException
	 */
	public boolean deleteRole(Role role) throws RemoteException;
	
	/**
	 * @param id the user's id to get from the db
	 * @return the user get from the db
	 * @throws RemoteException
	 */
	public Role getRole(int id) throws RemoteException;
	
	/**
	 * @return all the user in the db
	 * @throws RemoteException
	 */
	public Vector<Role> getAllRole() throws RemoteException;
	
	public Vector<Module> getAllModule() throws RemoteException;
	
}
