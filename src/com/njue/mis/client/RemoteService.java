package com.njue.mis.client;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import com.njue.mis.interfaces.CardItemControllerInterface;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.DiscountControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.GoodsItemControllerInterface;
import com.njue.mis.interfaces.MoneyControllerInterface;
import com.njue.mis.interfaces.PortControllerInterface;
import com.njue.mis.interfaces.ReceiptControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.SalesManControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;

public class RemoteService {
	public static DiscountControllerInterface discountService;
	public static StockControllerInterface stockService;
	public static CardItemControllerInterface cardItemService;
	public static CustomerControllerInterface customerService;
	public static GoodsControllerInterface goodsService;
	public static SalesControllerInterface salesService;
	public static StoreHouseControllerInterface storeHouseService;
	public static PortControllerInterface portService;
	public static ReceiptControllerInterface receiptService;
	public static MoneyControllerInterface moneyService;
	public static GoodsItemControllerInterface goodsItemService;
	public static SalesManControllerInterface salesManService;
	public static int load(){
		try {
			cardItemService = (CardItemControllerInterface) Naming.lookup(Configure.CardItemController);
			stockService = (StockControllerInterface) Naming.lookup(Configure.StockController);
			discountService = (DiscountControllerInterface) Naming.lookup(Configure.DiscountController);
			customerService = (CustomerControllerInterface) Naming.lookup(Configure.CustomerController);
			goodsService = (GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
			salesService = (SalesControllerInterface) Naming.lookup(Configure.SalesController);
			storeHouseService = (StoreHouseControllerInterface) Naming.lookup(Configure.StoreHouseController);
			portService = (PortControllerInterface) Naming.lookup(Configure.PortInController);
			receiptService = (ReceiptControllerInterface) Naming.lookup(Configure.ReceiptController);
			moneyService = (MoneyControllerInterface) Naming.lookup(Configure.MoneyController);
			goodsItemService = (GoodsItemControllerInterface)Naming.lookup(Configure.GoodsItemController);
			salesManService = (SalesManControllerInterface) Naming.lookup(Configure.SalesManController);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		return 1;
	}
}
