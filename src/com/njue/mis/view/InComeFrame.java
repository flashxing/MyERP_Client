package com.njue.mis.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.njue.mis.common.CommonUtil;
import com.njue.mis.model.InCome;


public class InComeFrame extends GiftFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7398536202556794935L;
	public InComeFrame()
	{
		super("¿â´æ±¨Òç");
		panel5.remove(customerButton);
		panel5.remove(receiverLabel);
		commitButton.addActionListener(new InComeListener());
		initInCome();
	}
	public void initInCome(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		idField.setText("IC"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}
	private class InComeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			InCome inCome = (InCome) createStockObject(2);
			try {
				if(stockObjectHandler.addStockIn(inCome)==null){
					CommonUtil.showError("Ìí¼ÓÊ§°Ü");
					return;
				}else{
					CommonUtil.showError("Ìí¼Ó³É¹¦");
					initInCome();
					goodsPanel.clearData();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				CommonUtil.showError("ÍøÂç´íÎó");
				return;
			}
		}
	}
}
