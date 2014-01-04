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
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.MyButtonEditor;
import com.njue.mis.common.MyButtonRender;
import com.njue.mis.common.ReceiptItemButton;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.ReceiptControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StockObjectControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.CardItem;
import com.njue.mis.model.GiftIn;
import com.njue.mis.model.Goods;
import com.njue.mis.model.Receipt;
import com.njue.mis.model.ReceiptIn;
import com.njue.mis.model.ReceiptItem;
import com.njue.mis.model.ReceiptItemDetail;
import com.njue.mis.model.ReceiptOut;
import com.njue.mis.model.Stock;
import com.njue.mis.model.StoreHouse;
import javax.swing.JTextArea;
import java.awt.Font;


public class ReceiptFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -885141522366198922L;
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
	protected CustomerButton customerButton;
	protected JTextArea commentArea;
	protected Date date;
	protected ReceiptControllerInterface receiptService;
	protected String[] columns = {"编号","条目","金额","备注"};
	protected List<ReceiptItemDetail> receiptItemDetailList;
	public ReceiptFrame(String frameName)
	{
		super(frameName,true,true,true,true);
		init();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.getContentPane().add(importgoods());
		
	}
	public void init(){
		try {
			receiptService = (ReceiptControllerInterface) Naming.lookup(Configure.ReceiptController);
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("网络错误");
		}
	}
	
	public void initBaseInfo(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		idField.setText("RI"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}
	public void reset(){
		moneyField.setText("");
		while(model.getRowCount()>0){
			model.removeRow(0);
		}
	}
	/*
	 * type = 0 then return an receiptIn
	 * type =1 then return an ReceiptOut
	 */
	public Receipt createReceip(int type){
		String id = idField.getText();
		if(customerButton.getCustomer() == null){
			CommonUtil.showError("客户不能为空");
			return null;
		}
		String customerId = customerButton.getCustomerId();
		if(customerId.length()<0){
			CommonUtil.showError("客户不能为空");
			return null;
		}
		CardItem cardItem = cardItemButton.getCardItem();
		if(cardItem == null){
			CommonUtil.showError("账户不能为空");
			return null;
		}
		String moneyString = moneyField.getText();
		Double money = 0.0;

		String operator = operatorField.getText();
		String time = timeField.getText();

		receiptItemDetailList = new ArrayList<ReceiptItemDetail>();
		for(int i = 0; i < model.getRowCount(); i++){
			int itemId = (Integer) model.getValueAt(i, 0);
			System.out.println("itemId "+itemId);
			ReceiptItem item = (ReceiptItem) model.getValueAt(i, 1);
			if(item == null){
				continue;
			}
			double item_money;
			try{
				item_money = Double.parseDouble((String) model.getValueAt(i, 2));
				money+=item_money;
				if(money<0){
					CommonUtil.showError("金额必须为正");
				}
			}catch(Exception ex){
				CommonUtil.showError("第几"+i+"行金额未输入,此行抛弃");
				continue;
			}
			String item_comment = (String) model.getValueAt(i, 3);
			receiptItemDetailList.add(new ReceiptItemDetail(id, itemId, item.getItem(), item_money, item_comment));
		}
		if(type == 0){
			return new ReceiptIn(id,customerId,money,time,operator, cardItem.getName(), "",receiptItemDetailList);
		}else if(type == 1){
			return new ReceiptOut(id, customerId, money, time, operator, cardItem.getName(), "",receiptItemDetailList);
		}else{
			return null;
		}
	}
	public JPanel importgoods()
	{

		JPanel panel = new JPanel(new BorderLayout());
		JPanel panelBaseInfo = new JPanel();
		JLabel idlable = new JLabel("编号:");
		idField = new JTextField(12);
		idField.setEditable(false);
		JLabel customerLabel = new JLabel("客户:");
		customerButton = new CustomerButton("......");
		JLabel timeLabel = new JLabel("时间");
		timeField = new JTextField(12);
		timeField.setEditable(false);
		JLabel cardLabel = new JLabel("账号");
		cardItemButton = new CardItemButton("。。。");
		JLabel operatorLabel = new JLabel("操作员:");
		operatorField = new JTextField(10);
		operatorField.setText(MainFrame.username);
		operatorField.setEditable(false);
		panelBaseInfo.add(idlable);
		panelBaseInfo.add(idField);
		panelBaseInfo.add(customerLabel);
		panelBaseInfo.add(customerButton);
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
		
		JPanel panelItem = new JPanel();
		addItemButton = new JButton("添加条目");
		addItemButton.addActionListener(new AddItemAction());
		deleteItemButton = new JButton("删除条目");
		deleteItemButton.addActionListener(new DeleteItemAction());
		JLabel moneyLabel = new JLabel("总金额:");
		moneyField = new JTextField(10);
//		panelItem.add(moneyLabel);
//		panelItem.add(moneyField);
		panelItem.add(addItemButton);
		panelItem.add(deleteItemButton);
		model = new DefaultTableModel(columns, 0){
			@Override
			public boolean isCellEditable(int r,int c){
				if(c == 0){
					return false;
				}
				return true;
			}
		};
		table = new JTable(model);
		CommonUtil.setDuiqi(table);
		table.setModel(model);
		table.setRowSelectionAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 5,
				screenSize.height  / 5));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(new Dimension(screenSize.width * 3 / 5,
				screenSize.height * 1/5));
        panelItem.add(scrollPane, BorderLayout.CENTER);
			
		panel.add(panelItem, BorderLayout.CENTER);
				
		JPanel panelButton = new JPanel();
		addButton = new JButton("添加");
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
	
	//设置JTable表格单元格对齐的效果     
    public void setDuiqi(JTable table)     
    {     
        //对其方式设置     
        DefaultTableCellRenderer d = new DefaultTableCellRenderer();     
                  
        //设置表格单元格的对齐方式为居中对齐方式     
        d.setHorizontalAlignment(JLabel.RIGHT);     
        for(int i = 0; i< table.getColumnCount();i++)     
        {     
            TableColumn col = table.getColumn(table.getColumnName(i));     
            col.setCellRenderer(d);     
        }     
    }     
	
	private class AddItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int id = model.getRowCount();
			Vector rows = new Vector();
			rows.add(id);
			model.addRow(rows);
			table.getColumnModel().getColumn(1).setCellRenderer(new MyButtonRender());
			table.getColumnModel().getColumn(1).setCellEditor(new MyButtonEditor());
		}
		
	}
	
	private class DeleteItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(table.getSelectedColumn()!=0&&table.getSelectedColumn()!=2&&table.getSelectedColumn()!=3){
				CommonUtil.showError("请选中一个条目");
				return;
			}
			int index = table.getSelectedRow();
			System.out.println(index);
			if(!(index<table.getRowCount()&&index>=0)){
				CommonUtil.showError("请先选择一个条目");
				return;
			}
			model.removeRow(index);
		}
		
	}
}
