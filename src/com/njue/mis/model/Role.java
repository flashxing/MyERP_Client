package com.njue.mis.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Role implements Serializable{
	private int roleId;
	private String roleName;
	private Set<String> moduleSet = new HashSet<String>();
	public Role(){
		
	}
	public Role(Role role){
		this.roleId = role.roleId;
		this.roleName = role.roleName;
		for(String str:role.moduleSet){
			if(!this.moduleSet.contains(str)){
				this.moduleSet.add(str);
			}
		}
		for(String str:this.moduleSet){
			if(!role.moduleSet.contains(str)){
				this.moduleSet.remove(str);
			}
		}
	}
	
	public Role clone(){
		Role role = new Role();
		role.setRoleId(this.roleId);
		role.setRoleName(this.roleName);
		for(String str:this.getModuleSet()){
			role.getModuleSet().add(str);
		}
		return role;
	}
	public Set<String> getModuleSet(){
		return this.moduleSet;
	}
	public void setModuleSet(Set<String> set){
		this.moduleSet = set;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getRoleName() {
		return roleName;
	}
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
	@Override
	public String toString(){
		return this.roleName;
	}
}
