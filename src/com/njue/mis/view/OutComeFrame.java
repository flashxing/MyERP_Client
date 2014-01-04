package com.njue.mis.view;


import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.rmi.ConnectException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.table.AbstractTableModel;

import sun.security.krb5.Config;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.DiscountControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StockObjectControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Discount;
import com.njue.mis.model.Goods;
import com.njue.mis.model.InCome;
import com.njue.mis.model.OutCome;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.Sales;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.Stock;
import com.njue.mis.model.StoreHouse;


public class OutComeFrame extends GiftFrame
{
	public OutComeFrame()
	{
		super("¿â´æ±¨Ëð");
		commitButton.addActionListener(new OutComeListener());
		initOutCome();
	}
	public void initOutCome(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		idField.setText("OC"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}
	private class OutComeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			OutCome outCome = (OutCome) createStockObject(3);
			try {
				if(stockObjectHandler.addStockOut(outCome)==null){
					CommonUtil.showError("Ìí¼ÓÊ§°Ü");
					return;
				}else{
					CommonUtil.showError("Ìí¼Ó³É¹¦");
					initOutCome();
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
