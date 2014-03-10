package com.njue.mis.common;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.njue.mis.client.RemoteService;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.model.Customer;

public class CustomerUtil {
	private static Map<String, Customer> map = new HashMap<>();
	public static Map<String, Customer> getCustomers(List<String> customerIds){
		CustomerControllerInterface customerService = RemoteService.customerService;
		for(String id:customerIds){
			Customer customer =null;
			if(map.containsKey(id)){
				continue;
			}
			try {
				customer = customerService.getCustomerInfo(id);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (null != customer){
				map.put(id, customer);
			}
		}
		return map;
	}
}
