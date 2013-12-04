package com.njue.mis.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.njue.mis.common.CommonUtil;
import com.njue.mis.model.Receipt;

public class ReceiptOutFrame extends ReceiptFrame {
	public ReceiptOutFrame(){
		super("³ö¾Ýµ¥");
		initReceiptOut();
		addListener();
	}
	public void initReceiptOut(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		idField.setText("RO"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}
	public void addListener(){
		addButton.addActionListener(new AddAction());
	}
	private class AddAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Receipt receipt = createReceip(1);
			if (receipt != null){
				try {
					if(receiptService.addReceipt(receipt)!=null){
						reset();
					}else{
						CommonUtil.showError("Ìí¼ÓÊ§°Ü");
						return;
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonUtil.showError("ÍøÂç´íÎó");
				}
			}
		}
		
	}
}
