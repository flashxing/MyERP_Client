package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.model.CardItem;

public interface CardItemControllerInterface extends Remote{
	public String addCardItem(CardItem cardItem) throws RemoteException;
	public boolean deleteCardItem(CardItem cardItem) throws RemoteException;
	public boolean updateCardItem(CardItem cardItem) throws RemoteException;
	public List<CardItem> getAllCardItems() throws RemoteException;
	public boolean saveOrUpdateCardItems(List<CardItem> cardItemList) throws RemoteException;
}
