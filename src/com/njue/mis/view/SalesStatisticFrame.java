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
import com.njue.mis.interfaces.ReceiptControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.model.ReceiptIn;
import com.njue.mis.model.Sales;
import com.njue.mis.model.SalesIn;

public class SalesStatisticFrame extends StatisticFrame{
	private List<Sales> salesList;
	protected String[] salesColumns={"编号","客户","商品","金额","操作人","时间"};
	protected String[] salesFields={"id","customerId","goodsId","totalPrice","operatePerson","time"};
	
	protected SalesControllerInterface salesService;
	
	public SalesStatisticFrame()
	{
		super("收据统计");
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		initIn();
	}
	public void initIn(){
		try {
			salesService = (SalesControllerInterface) Naming.lookup(Configure.SalesController);
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
				salesList = salesService.getAllSalesInByTime(begin, end);
				if(salesList != null){
					CommonUtil.updateJTable(table, salesColumns, salesList.toArray(), salesFields);
					totalMoney.setText(""+totalMoney(salesList));
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
		
	}
	
	private double totalMoney(List<Sales> salesList){
		double total = 0; 
		for(Sales sales:salesList){
			total += sales.getTotalPrice();
		}
		return total;
	}
}
