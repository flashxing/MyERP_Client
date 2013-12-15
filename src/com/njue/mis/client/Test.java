package com.njue.mis.client;

import com.njue.mis.model.Person;

public class Test {
	public static void main(String[] args){
		System.out.println(Person.class.getClasses());
		Configure.init();
		System.out.println(Configure.CategoryController);
	}
}
