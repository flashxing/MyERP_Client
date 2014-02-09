package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.SetupGoods;

public interface SetupGoodsControllerInterface extends Remote{
	public boolean saveOrUpdateSetupGoodsList(List<SetupGoods> setupGoodsList) throws RemoteException;
	public List<SetupGoods> getAllSetupGoods(List<String> goodsIdList, int shId) throws RemoteException;
}
