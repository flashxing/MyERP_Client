package com.njue.mis.view;


import java.awt.BorderLayout;
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
import com.njue.mis.interfaces.ReceiptControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StockObjectControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.GiftIn;
import com.njue.mis.model.Goods;
import com.njue.mis.model.Receipt;
import com.njue.mis.model.ReceiptIn;
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
	protected JTextField customerField;
	protected JTextField moneyField;
	protected JTextField timeField;
	protected JTextField operatorField;
	protected JButton addButton;
	protected JTextArea commentArea;
	protected Date date;
	protected ReceiptControllerInterface receiptService;
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
		customerField.setText("");
		moneyField.setText("");
		commentArea.setText("");
	}
	/*
	 * type = 0 then return an receiptIn
	 * type =1 then return an ReceiptOut
	 */
	public Receipt createReceip(int type){
		String id = idField.getText();
		String customerName = customerField.getText();
		if(customerName.length()<0){
			CommonUtil.showError("客户不能为空");
			return null;
		}
		String moneyString = moneyField.getText();
		Double money;
		try{
			money = Double.parseDouble(moneyString);
		}catch(Exception ex){
			CommonUtil.showError("金额必须为数字");
			return null;
		}
		if(money < 0){
			CommonUtil.showError("金额必须为正");
			return null;
		}
		String operator = operatorField.getText();
		String time = timeField.getText();
		String comment = commentArea.getText();
		if(comment.length()>100){
			CommonUtil.showError("备注不能多于100个字");
			return null;
		}
		if(type == 0){
			return new ReceiptIn(id,customerName,money,time,operator,comment);
		}else if(type == 1){
			return new ReceiptOut(id, customerName, money, time, operator, comment);
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
		customerField = new JTextField(10);
		JLabel moneyLabel = new JLabel("金额:");
		moneyField = new JTextField(10);
		JLabel timeLabel = new JLabel("时间");
		timeField = new JTextField(12);
		JLabel operatorLabel = new JLabel("操作员:");
		operatorField = new JTextField(10);
		operatorField.setText(MainFrame.username);
		operatorField.setEditable(false);
		panelBaseInfo.add(idlable);
		panelBaseInfo.add(idField);
		panelBaseInfo.add(customerLabel);
		panelBaseInfo.add(customerField);
		panelBaseInfo.add(moneyLabel);
		panelBaseInfo.add(moneyField);
		panelBaseInfo.add(timeLabel);
		panelBaseInfo.add(timeField);
		panelBaseInfo.add(operatorLabel);
		panelBaseInfo.add(operatorField);
		panelBaseInfo.setSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height/40));
		panelBaseInfo.setSize(screenSize.width, screenSize.height/20);
		initBaseInfo();
		
		
		panel.add(panelBaseInfo, BorderLayout.NORTH);
		
		JPanel panelComment = new JPanel();
		JLabel commentLabel = new JLabel("备注:");
		panelComment.add(commentLabel);
		
		panel.add(panelComment, BorderLayout.CENTER);
		
		commentArea = new JTextArea(5,20);
		commentArea.setBorder(getBorder());
		commentArea.setLineWrap(true);
		panelComment.add(new JScrollPane(commentArea));
		
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
}
