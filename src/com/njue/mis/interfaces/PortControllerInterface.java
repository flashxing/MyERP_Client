/**
 * 进货服务接口
 */
package com.njue.mis.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

import com.njue.mis.model.PortBack;
import com.njue.mis.model.PortIn;

public interface PortControllerInterface extends Remote
{
	public String addPortBack(PortBack portBack) throws RemoteException;
	/**
	 * 添加新的进货记录
	 * @param portIn 封装好的PortIn对象
	 * @return 执行结果
	 */
	public String addPortIn(PortIn portIn) throws RemoteException;
	
	/**
	 * 获取所有的进货信息
	 * @return 进货信息集合
	 */
	public Vector<PortIn> getAllPortIn() throws RemoteException;
	/**
	 * 查询数据库中满足条件的进货记录
	 * @param field 查询的字段
	 * @param value 满足的值
	 * @return 查询结果
	 */
	public Vector<PortIn> searchPortIn(String field,String value) throws RemoteException;
	/**
	 * 查询数据库中满足条件的进货记录
	 * @param beginTime 查询的开始时间
	 * @param endTime  查询的结束时间
	 * @return 查询结果集
	 */
	public Vector<PortIn> searchPortInByTime(String beginTime,String endTime) throws RemoteException;
	/**
	 * 判断进货编号是否存在
	 * @param id 
	 * @return
	 */
	public PortIn getPortIn(String id) throws RemoteException;
	
	public Vector<PortBack> getAllPortBack() throws RemoteException;
	
	public Vector<PortBack> searchPortBackByTime(String beginTime, String endTime) throws RemoteException;
}
