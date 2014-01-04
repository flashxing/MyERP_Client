package com.njue.mis.view;


import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StockObjectControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.GiftOut;
import com.njue.mis.model.Goods;
import com.njue.mis.model.Stock;
import com.njue.mis.model.StoreHouse;


public class GiftOutFrame extends GiftFrame
{
	public GiftOutFrame()
	{
		super("ø‚¥Ê‘˘ÀÕ");
		commitButton.addActionListener(new GiftOutListener());
		initGiftOut();
	}
	public void initGiftOut(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		idField.setText("GO"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}
	private class GiftOutListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			System.out.println("commint");
			GiftOut giftOut = (GiftOut) createStockObject(1);
			try {
				if(stockObjectHandler.addStockOut(giftOut)==null){
					CommonUtil.showError("ÃÌº” ß∞‹");
					return;
				}else{
					CommonUtil.showError("ÃÌº”≥…π¶");
					initGiftOut();
					goodsPanel.clearData();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				CommonUtil.showError("Õ¯¬Á¥ÌŒÛ");
				return;
			}
		}
	}
}
