package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.GoodsItem;
import com.njue.mis.model.SalesGoodsItem;

public interface GoodsItemControllerInterface extends Remote{
	public List<List<GoodsItem>> getPortGoodsItemByTimeAndGoods(String beginTime, String endTime, String[] goodsIds)
		throws RemoteException;
	public List<List<SalesGoodsItem>> getSalesGoodsItemByTimeAndGoods(String beginTime, String endTime, String[] goodsIds)
		throws RemoteException;
}
