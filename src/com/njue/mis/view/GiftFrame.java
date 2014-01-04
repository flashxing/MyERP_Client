package com.njue.mis.view;


import java.awt.BorderLayout;
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
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.SalesGoodsItemPanel;
import com.njue.mis.common.StockGoodsItemPanel;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StockObjectControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.GiftIn;
import com.njue.mis.model.GiftOut;
import com.njue.mis.model.Goods;
import com.njue.mis.model.InCome;
import com.njue.mis.model.OutCome;
import com.njue.mis.model.Stock;
import com.njue.mis.model.StockItem;
import com.njue.mis.model.StockObject;
import com.njue.mis.model.StoreHouse;


public class GiftFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1996990177020227534L;

	protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	protected JTextField goodsField;
	protected JTextField goodsNameField;
	protected JTextField idField;
	protected JTextField operatorField;
	protected JLabel receiverLabel;
	protected CustomerButton customerButton;
	protected JButton commitButton;
	protected JComboBox<StoreHouse> storeHouseComboBox;
	protected JPanel panel5;
	
	protected StockGoodsItemPanel goodsPanel;
//	private Customer selectedCustomer;
	protected Goods selectedGoods;
	protected StoreHouse selectedSH;
	protected List<StoreHouse> storeHouseList;
	
	protected StoreHouseControllerInterface storeHouseHandler;
	protected StockObjectControllerInterface stockObjectHandler;
	protected StockControllerInterface stockHandler;
	
	protected JTextField timeField;
	protected Date date;
	public GiftFrame(String frameName)
	{
		super(frameName,true,true,true,true);
		init();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.getContentPane().add(importgoods());
	}
	public void init(){
		try {
			stockObjectHandler = (StockObjectControllerInterface) Naming.lookup(Configure.StockObjectController);
			stockHandler = (StockControllerInterface) Naming.lookup(Configure.StockController);
			storeHouseHandler = (StoreHouseControllerInterface) Naming.lookup(Configure.StoreHouseController);
			storeHouseList = storeHouseHandler.getAll();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("网络错误");
		}
	}
	
	public void initBaseInfo(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		idField.setText("GI"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}

	public JPanel importgoods()
	{

		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(screenSize.width * 3 / 5,
				screenSize.height * 3 / 5);
		panel5 = new JPanel();
		JLabel salesIdlable = new JLabel("受赠票号:");
		idField = new JTextField(12);
		idField.setEditable(false);
		receiverLabel = new JLabel("赠送者:");
		customerButton = new CustomerButton("。。。");

		JLabel operatorLabel = new JLabel("操作员:");
		operatorField = new JTextField(10);
		operatorField.setText(MainFrame.username);
		operatorField.setEditable(false);
		JLabel shLabel = new JLabel("仓库");
		storeHouseComboBox = new JComboBox<StoreHouse>();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel<StoreHouse>());
		for(StoreHouse sh:storeHouseList){
			storeHouseComboBox.addItem(sh);
		}
		panel5.add(salesIdlable);
		panel5.add(idField);
		panel5.add(receiverLabel);
		panel5.add(customerButton);
		panel5.add(shLabel);
		panel5.add(storeHouseComboBox);
		panel5.setSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height/40));
		panel5.setSize(screenSize.width, screenSize.height/20);
		
		
		JLabel label = new JLabel("时间:");
		timeField = new JTextField();
		timeField.setColumns(12);
		timeField.setEditable(false);
		initBaseInfo();
		panel5.add(label);
		panel5.add(timeField);
		panel5.add(operatorLabel);
		panel5.add(operatorField);
		
		goodsPanel = new StockGoodsItemPanel(idField.getText());
		JPanel commitPanel = new JPanel();
		commitButton = new JButton("提交");
		commitPanel.add(commitButton);
		
		panel.add(panel5,BorderLayout.NORTH);
		panel.add(goodsPanel, BorderLayout.CENTER);
		panel.add(commitPanel, BorderLayout.SOUTH);
		return panel;
	}
	protected StockObject createStockObject(int type){
		String id = idField.getText();
		int storeHouseId = ((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
		double price = 0.0;
		String time = timeField.getText();
		String operatePerson = operatorField.getText();
		String comment = "";
		List<StockItem> stockItems = goodsPanel.getGoodsItemList();
		Customer customer;
		String receiver;
		switch (type) {
		case 0:
			customer = customerButton.getCustomer();
			if(customer == null){
				CommonUtil.showError("赠送者不能为空");
				return null;
			}
			receiver = customer.getName();
			return new GiftIn(id, receiver, "", storeHouseId, 0, price, time, operatePerson, comment, stockItems);
		case 1:
			customer = customerButton.getCustomer();
			if(customer == null){
				CommonUtil.showError("赠送者不能为空");
				return null;
			}
			receiver = customer.getName();
			return new GiftOut(id, receiver, "", storeHouseId, 0, price, time, operatePerson, comment, stockItems);
		case 2:
			return new InCome(id, "", "", storeHouseId, 0, price, time, operatePerson, comment, stockItems);
		case 3:
			return new OutCome(id, "", "", storeHouseId, 0, price, time, operatePerson, comment, stockItems);
		default:
			return null;
		}	
	}
}
