/**
 * 商品操作接口
 */

package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Vector;

import com.njue.mis.model.Goods;
import com.njue.mis.model.StorageGoods;

public interface GoodsControllerInterface extends Remote
{
	
	/**
	 * 向数据库中添加新商品
	 * @param goods  封装好的goods对象
	 * @return  the goodsId of the goods
	 */
	public String addGoods(Goods goods) throws RemoteException;
	/**
	 * 从数据库中删除商品
	 * @param id  被删除商品的编号
	 * @return  执行结果
	 */
	public boolean deleteGoods(Goods goods) throws RemoteException;
	/**
	 * 查询数据库中满足条件的商品
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	public Vector<Goods> searchGoods(String field,String value)throws RemoteException;
	/**
	 * 修改商品的价格
	 * @param goods the goods to update
	 * @return 执行结果
	 */
	public boolean updateGoods(Goods goods) throws RemoteException;
	/**
	 * 获取所有的商品
	 * @return 商品的集合
	 */
	public Vector<Goods> getAllGoods()throws RemoteException;
	/**
     * 判断商品是否存在
     * @param id 查询的商品编号
     * @return 查询结果
     */
    public boolean isExisted(String id) throws RemoteException;
    /**
     * 获取商品的信息
     * @param id 被查询的商品的编号
     * @return 查询结果
     */
    public Goods getGoods(String id)throws RemoteException;
    /**
	 * 查询库存的商品信息
	 * @return  查询结果集
	 */
	public Vector<StorageGoods> getAllStorageGoods()throws RemoteException;
	/*
	 * to get all the goods of this category.
	 * @params int cateId the id of the category
	 * @return Vector<Goods> all the goods.
	 */
	public Vector<Goods> getAllGoodsByCateId(int cateId)throws RemoteException;
	public List<Goods> getAllGoodsByCateId(List<Integer> cateIds) throws RemoteException;
	
	public Vector<Goods> getAllGoodsByGoodsName(String goodsName) throws RemoteException;
	public List<Goods> getGoods(List<String> goodsIdList) throws RemoteException;

}
