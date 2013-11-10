package com.njue.mis.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User implements Serializable{
	private int userId;
	private String userName;
	private String userPW;
	private Set<Role> roleSet = new HashSet<Role>();

	public User(){
		
	}
	
	public User clone(){
		User user = new User();
		user.setUserId(this.userId);
		user.setUserName(this.userName);
		user.setUserPW(this.userPW);
		for(Role role:this.roleSet){
			user.roleSet.add(role.clone());
		}
		return user;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserPW() {
		return userPW;
	}
	public void setUserPW(String userPW) {
		this.userPW = userPW;
	}
	public Set<Role> getRoleSet() {
		return roleSet;
	}
	public void setRoleSet(Set<Role> roleSet) {
		this.roleSet = roleSet;
	}
	public String toString(){
		return this.userName;
	}
}
