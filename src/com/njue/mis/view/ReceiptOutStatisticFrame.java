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
import com.njue.mis.model.ReceiptOut;

public class ReceiptOutStatisticFrame extends StatisticFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5296622451282410379L;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private List<ReceiptOut> receiptList = new ArrayList<ReceiptOut>();
	protected String[] receiptColumns={"编号","客户","金额","操作人","时间"};
	protected String[] receiptFields={"id","customerId","money","operator","time"};

	protected ReceiptControllerInterface receiptService;
	public ReceiptOutStatisticFrame()
	{
		super("付款统计");
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		initOut();
	}
	public void initOut(){
		try {
			receiptService = (ReceiptControllerInterface) Naming.lookup(Configure.ReceiptController);
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
				receiptList = receiptService.getAllReceiptOutByTime(begin, end);
				if(receiptList != null){
					CommonUtil.updateJTable(table, receiptColumns, receiptList.toArray(), receiptFields);
					totalMoney.setText(""+totalMoney(receiptList));
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
		
	}
	private double totalMoney(List<ReceiptOut> receiptList){
		double total = 0; 
		for(ReceiptOut receipt:receiptList){
			total += receipt.getMoney();
		}
		return total;
	}
}
