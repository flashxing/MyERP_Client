package com.njue.mis.model;

import java.io.Serializable;

public class Category implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5628914427144743630L;
	private int cate_id;
	private String cate_name;
	private int prefer_id;

	public int getCate_id() {
		return cate_id;
	}
	public void setCate_id(int cate_id) {
		this.cate_id = cate_id;
	}
	public String getCate_name() {
		return cate_name;
	}
	public void setCate_name(String cate_name) {
		this.cate_name = cate_name;
	}
	public int getPrefer_id() {
		return prefer_id;
	}
	public void setPrefer_id(int prefer_id) {
		this.prefer_id = prefer_id;
	}
	@Override
	public String toString(){
		return cate_name;
	}
}
