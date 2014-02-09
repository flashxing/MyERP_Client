package com.njue.mis.model;

public class CustomerCategory extends Category implements Cloneable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7896664933033894314L;

	@Override
	public CustomerCategory clone(){
		return new CustomerCategory();
	}
}