package com.njue.mis.model;

import java.io.Serializable;

public class StoreHouse implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1854061874899484634L;
	private int id;
	private String name;
	private String type;
	private String mnemonics;
	public StoreHouse(){
		
	}
	public StoreHouse(StoreHouse storeHouse){
		this.id = storeHouse.id;
		this.name = storeHouse.name;
		this.type = storeHouse.type;
		this.mnemonics = storeHouse.mnemonics;
	}
	
	public void update(StoreHouse storeHouse){
		this.name = storeHouse.name;
		this.type = storeHouse.type;
		this.mnemonics = storeHouse.mnemonics;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getMnemonics() {
		return mnemonics;
	}
	public void setMnemonics(String mnemonics) {
		this.mnemonics = mnemonics;
	}
	@Override
	public String toString(){
		return this.getName();
	}
}
