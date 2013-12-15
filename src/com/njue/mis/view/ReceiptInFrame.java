package com.njue.mis.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.njue.mis.common.CommonUtil;
import com.njue.mis.model.Receipt;

public class ReceiptInFrame extends ReceiptFrame {
	public ReceiptInFrame(){
		super(" ’æ›µ•");
		initIn();
		addListener();
	}
	public void initIn(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		idField.setText("RI"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}
	
	public void resetReceiptIn(){
		super.reset();
		initIn();
	}
	
	public void addListener(){
		addButton.addActionListener(new AddAction());
	}
	private class AddAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Receipt receipt = createReceip(0);
			if (receipt != null){
				try {
					if(receiptService.addReceipt(receipt)!=null){
						resetReceiptIn();
					}else{
						CommonUtil.showError("ÃÌº” ß∞‹");
						return;
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonUtil.showError("Õ¯¬Á¥ÌŒÛ");
				}
			}
		}
		
	}
}
