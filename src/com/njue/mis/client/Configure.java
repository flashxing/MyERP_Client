package com.njue.mis.client;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import com.njue.mis.common.CommonUtil;


public class Configure {
	private static Properties propertie;
	private static InputStream inputFile;
	public static String CategoryController =null;
	public static String OperatorController =null;
	public static String GoodsController =null;
	public static String StoreHouseController =null;
	public static String PortInController =null;
	public static String CustomerController =null;
	public static String DiscountController =null;
	public static String SalesController =null;
	public static String RoleController =null;
	public static String UserController =null;
	public static String UserRoleController =null;
	public static String StockObjectController =null;
	public static String StockController =null;
	public static String ReceiptController =null;
	public static String ReceiptItemController = null;
	public static String CustomerMoneyController = null;
	public static String GoodsItemController = null;
	public static String MoneyItemController = null;
	public static String MoneyController = null;
//	public final static String IP = "localhost:3333";
	public final static int init(){
		propertie = new Properties();
		try{
			inputFile = Configure.class.getClassLoader().getResourceAsStream("erp.property");
			propertie.load(inputFile);
			ReceiptController = "rmi://"+getValue("URL")+"/ReceiptController";
			StockController = "rmi://"+getValue("URL")+"/StockController";
			StockObjectController = "rmi://"+getValue("URL")+"/StockObjectController";
			UserController = "rmi://"+getValue("URL")+"/UserController";
			UserRoleController = "rmi://"+getValue("URL")+"/UserRoleController";
			RoleController = "rmi://"+getValue("URL")+"/RoleController";
			SalesController = "rmi://"+getValue("URL")+"/SalesController";
			DiscountController = "rmi://"+getValue("URL")+"/DiscountController";
			CustomerController = "rmi://"+getValue("URL")+"/CustomerController";
			PortInController = "rmi://"+getValue("URL")+"/PortInController";
			StoreHouseController = "rmi://"+getValue("URL")+"/StoreHouseController";
			GoodsController= "rmi://"+getValue("URL")+"/GoodsController";
			OperatorController = "rmi://"+getValue("URL")+"/OperatorController";
			CategoryController = "rmi://"+getValue("URL")+"/CategoryController";
			ReceiptItemController = "rmi://"+getValue("URL")+"/ReceiptItemController";
			CustomerMoneyController = "rmi://"+getValue("URL")+"/CustomerMoneyController";
			GoodsItemController = "rmi://"+getValue("URL")+"/GoodsItemController";
			MoneyItemController = "rmi://"+getValue("URL")+"/MoneyItemController";
			MoneyController = "rmi://"+getValue("URL")+"/MoneyController";
			inputFile.close();
			return 1;
		}catch(Exception ex){
			ex.printStackTrace();
			return 0;
		}
	}
	public static String getValue(String key){
		if(propertie.containsKey(key)){
			String value = propertie.getProperty(key);
			return value;
		}else
			return "";
	}
//	public final static String IP = "192.168.1.108:3333";
//	public final static int PORT = 3333;

}
