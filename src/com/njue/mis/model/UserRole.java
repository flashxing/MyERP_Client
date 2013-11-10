package com.njue.mis.model;

import java.io.Serializable;

public class UserRole implements Serializable{
	private int id;
	private int userId;
	private int roleId;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public int getId(){
		return id;
	}
	public void setId(int id){
		this.id = id;
	}
}
