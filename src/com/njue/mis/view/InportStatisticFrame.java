package com.njue.mis.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.PortControllerInterface;
import com.njue.mis.interfaces.ReceiptControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.ReceiptIn;
import com.njue.mis.model.Sales;
import com.njue.mis.model.SalesIn;

public class InportStatisticFrame extends StatisticFrame{
	private List<PortIn> portInList;
	protected String[] portInColumns={"编号","商品","仓库","单价","数量","操作人","时间"};
	protected String[] portInFields={"id","goodsId","storeHouseId","price","number","operatePerson","time"};
	
	protected PortControllerInterface portService;
	
	public InportStatisticFrame()
	{
		super("进货统计");
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		initIn();
	}
	public void initIn(){
		try {
			portService = (PortControllerInterface) Naming.lookup(Configure.PortInController);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		queryButton.addActionListener(new QueryAction());
	}
	private class QueryAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String begin = beginButton.getDateString();
			String end = endButton.getDateString();
			try {
				portInList = portService.searchPortInByTime(begin, end);
				if(portInList != null){
					CommonUtil.updateJTable(table, portInColumns, portInList.toArray(), portInFields);
					totalMoney.setText(""+totalMoney(portInList));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
		
	}
	
	private double totalMoney(List<PortIn> portList){
		double total = 0; 
		for(PortIn portIn:portList){
			total += portIn.getPrice()*portIn.getNumber();
		}
		return total;
	}
}
