package com.njue.mis.view;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventObject;
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
import javax.swing.event.CellEditorListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CardItemButton;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.ButtonEditor;
import com.njue.mis.common.ButtonRender;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.MoneyItemPanel;
import com.njue.mis.common.MyButtonEditor;
import com.njue.mis.common.MyButtonRender;
import com.njue.mis.common.ReceiptItemButton;
import com.njue.mis.common.MoneyItemButton;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.MoneyControllerInterface;
import com.njue.mis.interfaces.ReceiptControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StockObjectControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.CardItem;
import com.njue.mis.model.GiftIn;
import com.njue.mis.model.Goods;
import com.njue.mis.model.Money;
import com.njue.mis.model.MoneyItem;
import com.njue.mis.model.MoneyItemDetail;
import com.njue.mis.model.Receipt;
import com.njue.mis.model.ReceiptIn;
import com.njue.mis.model.ReceiptItem;
import com.njue.mis.model.ReceiptItemDetail;
import com.njue.mis.model.ReceiptOut;
import com.njue.mis.model.Stock;
import com.njue.mis.model.StoreHouse;
import com.sun.media.sound.ModelAbstractChannelMixer;

import javax.swing.JTextArea;
import java.awt.Font;


public class MoneyFrame extends JInternalFrame
{
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	protected JTextField idField;
	protected JTextField moneyField;
	protected JTextField timeField;
	protected JTextField operatorField;
	protected CardItemButton cardItemButton;
	protected JButton addButton;
	protected JButton addItemButton;
	protected JButton deleteItemButton;
	protected JTable table;
	protected DefaultTableModel model;
	protected JScrollPane scrollPane;
	protected JTextArea commentArea;
	protected Date date;
	protected MoneyControllerInterface moneyService;
	protected String[] columns = {"编号","条目","金额","备注"};
	protected List<MoneyItemDetail> moneyItemDetailList;
	protected MoneyItemPanel moneyItemPanel;
	public MoneyFrame()
	{
		super("钱流管理",true,true,true,true);
		init();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.getContentPane().add(importgoods());
		
	}
	public void init(){
		try {
			moneyService = (MoneyControllerInterface) Naming.lookup(Configure.MoneyController);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("网络错误");
		}
	}
	
	public void initBaseInfo(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		idField.setText("MR"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}
	public void reset(){
		moneyItemPanel.reset();
		initBaseInfo();
	}
	/*
	 * type = 0 then return an receiptIn
	 * type =1 then return an ReceiptOut
	 */
	public Money createMoney(){
		String id = idField.getText();

		String operator = operatorField.getText();
		String time = timeField.getText();
		CardItem cardItem = cardItemButton.getCardItem();
		if(cardItem == null){
			CommonUtil.showError("请选择一个账户");
			return null;
		}
		return moneyItemPanel.getMoney(id, operator, time, cardItem);
	}
	public JPanel importgoods()
	{

		JPanel panel = new JPanel(new BorderLayout());
		JPanel panelBaseInfo = new JPanel();
		JLabel idlable = new JLabel("编号:");
		idField = new JTextField(12);
		idField.setEditable(false);
		JLabel timeLabel = new JLabel("时间");
		timeField = new JTextField(12);
		timeField.setEditable(false);
		JLabel cardLabel = new JLabel("账户");
		cardItemButton = new CardItemButton("。。。");
		JLabel operatorLabel = new JLabel("操作员:");
		operatorField = new JTextField(10);
		operatorField.setText(MainFrame.username);
		operatorField.setEditable(false);
		panelBaseInfo.add(idlable);
		panelBaseInfo.add(idField);
		panelBaseInfo.add(timeLabel);
		panelBaseInfo.add(timeField);
		panelBaseInfo.add(cardLabel);
		panelBaseInfo.add(cardItemButton);
		panelBaseInfo.add(operatorLabel);
		panelBaseInfo.add(operatorField);
		panelBaseInfo.setSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height/40));
		panelBaseInfo.setSize(screenSize.width, screenSize.height/20);
		initBaseInfo();
		
		
		panel.add(panelBaseInfo, BorderLayout.NORTH);
		
//		JPanel panelItem = new JPanel();
//		addItemButton = new JButton("添加条目");
//		addItemButton.addActionListener(new AddItemAction());
//		deleteItemButton = new JButton("删除条目");
//		deleteItemButton.addActionListener(new DeleteItemAction());
//		JLabel moneyLabel = new JLabel("总金额:");
//		moneyField = new JTextField(10);
////		panelItem.add(moneyLabel);
////		panelItem.add(moneyField);
//		panelItem.add(addItemButton);
//		panelItem.add(deleteItemButton);
//		model = new DefaultTableModel(columns, 0){
//			@Override
//			public boolean isCellEditable(int r,int c){
//				if(c == 0){
//					return false;
//				}
//				return true;
//			}
//		};
//		table = new JTable(model);
//		CommonUtil.setDuiqi(table);
//		table.setModel(model);
//		table.setRowSelectionAllowed(false);
//		table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 5,
//				screenSize.height  / 5));
//        scrollPane = new JScrollPane();
//        scrollPane.setViewportView(table);
//        scrollPane.setPreferredSize(new Dimension(screenSize.width * 3 / 5,
//				screenSize.height * 1/5));
//        panelItem.add(scrollPane, BorderLayout.CENTER);
//			
//		panel.add(panelItem, BorderLayout.CENTER);
		
		moneyItemPanel = new MoneyItemPanel();
		panel.add(moneyItemPanel, BorderLayout.CENTER);
		JPanel panelButton = new JPanel();
		addButton = new JButton("提交");
		addButton.addActionListener(new AddAction());
		JButton cacelButton = new JButton("取消");
		cacelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				try {
					setClosed(true);
				} catch (PropertyVetoException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
		});
		panelButton.add(addButton);
		panelButton.add(cacelButton);
		
		panel.add(panelButton, BorderLayout.SOUTH);
		return panel;
	}
	
	private class AddAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			Money money = createMoney();
			if (money!= null){
				try {
					if(moneyService.addMoney(money)!=null){
						reset();
					}else{
						CommonUtil.showError("添加失败");
						return;
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonUtil.showError("网络错误");
				}
			}
		}
		
	}
}
