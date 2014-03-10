package com.njue.mis.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
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

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.GoodsItemPanel;
import com.njue.mis.common.PrintPanle;
import com.njue.mis.common.Printer;
import com.njue.mis.common.SalesGoodsItemPanel;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.DiscountControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Discount;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsItem;
import com.njue.mis.model.SalesBack;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.StoreHouse;

public class SalesBackFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4373144987548153299L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private JTextField salesIdField;
	private JTextField numberField;
	private JTextField operatorField;
	private CustomerButton customerButton;
	private SalesGoodsItemPanel goodsPanel;
	private JComboBox<StoreHouse> storeHouseComboBox;
	
    protected String[] goodsColumns={"商品编号","商品名称","商品描述"};
    protected String[] goodsFields={"productCode","goodsName","description"};
    protected Object[] goodsList = {};
	private List<StoreHouse> storeHouseList;

	private StoreHouseControllerInterface storeHouseHandler;
	private SalesControllerInterface salesHandler;
	private Discount discount;
	private JTextField timeField;
	private Date date;
	
	private PrintPanle printPanle;
	
	public SalesBackFrame()
	{
		super("销售退货单",true,true,true,true);
		date = new Date();
		init();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.getContentPane().add(importgoods());
	}
	public void init(){
		try {
			salesHandler = (SalesControllerInterface) Naming.lookup(Configure.SalesController);
			storeHouseHandler = (StoreHouseControllerInterface) Naming.lookup(Configure.StoreHouseController);
			storeHouseList = storeHouseHandler.getAll();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("网络错误");
		}
	}
	private void initDiscount(){
		discount = new Discount();
		customerButton.setDiscount(discount);
		goodsPanel.setDiscount(discount);
	}
	private void resetData(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		salesIdField.setText("SB"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}

	public JPanel importgoods()
	{

		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(screenSize.width * 3 / 5,
				screenSize.height * 3 / 5);
		JPanel panel5 = new JPanel();
		JLabel salesIdlable = new JLabel("销售票号:");
		salesIdField = new JTextField(12);
		salesIdField.setEditable(false);
		panel5.add(salesIdlable);
		panel5.add(salesIdField);
		JLabel customerLabel = new JLabel("客户");
		customerButton = new CustomerButton("。。。");
		panel5.add(customerLabel);
		panel5.add(customerButton);
		JLabel operatorLabel = new JLabel("操作员:");
		operatorField = new JTextField(10);
		operatorField.setText(MainFrame.username);
		operatorField.setEditable(false);
		panel5.add(operatorLabel);
		panel5.add(operatorField);
		JLabel shLabel = new JLabel("仓库");
		storeHouseComboBox = new JComboBox<StoreHouse>();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel<StoreHouse>());
		for(StoreHouse sh:storeHouseList){
			storeHouseComboBox.addItem(sh);
		}
		panel5.add(shLabel);
		panel5.add(storeHouseComboBox);
		JLabel timeLabel = new JLabel("时间");
		timeField = new JTextField(10);
		timeField.setEditable(false);
		panel5.add(timeLabel);
		panel5.add(timeField);
		
		resetData();
		goodsPanel = new SalesGoodsItemPanel(salesIdField.getText());
		initDiscount();
		
		printPanle = new PrintPanle();
		printPanle.getPrintButton().addActionListener(new PrintAction());
		
		JPanel panelCommit = new JPanel();
		JButton commitButton = new JButton("提交");
		commitButton.addActionListener(new SalesAddListener());
		panelCommit.add(commitButton);
		panelCommit.add(printPanle);
		
		panel.add(panel5, BorderLayout.NORTH);
		panel.add(goodsPanel, BorderLayout.CENTER);
		panel.add(panelCommit, BorderLayout.SOUTH);
	
		return panel;
	}

	private SalesBack createSalesBack(){
		Customer customer;
		if((customer = customerButton.getCustomer())== null){
			CommonUtil.showError("请选择一个客户");
			return null;
		}
		List<SalesGoodsItem> list = goodsPanel.getGoodsItemList(-1);
		if(list == null|| list.size() == 0){
			CommonUtil.showError("请选中一个商品");
			return null;
		}
		String id = salesIdField.getText();
		String time = timeField.getText();
		String operator = operatorField.getText();
		int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
		SalesBack sales = new SalesBack(id,customer.getId(),"",0,goodsPanel.getActualPrice(),time,operator,"",0,
				goodsPanel.getMoney(), goodsPanel.getDecreaseMoney(),shId, 1, "", list);
		return sales;
	}
	
	private class SalesAddListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			SalesBack sales = createSalesBack();
			if(sales == null){
				return;
			}
			try {
				String result = salesHandler.addSalesBack(sales);
				if(result == null){
					CommonUtil.showError("添加销售单失败,可能库存不足");
					return;
				}
				resetData();
				goodsPanel.clearData(salesIdField.getText());
			} catch (Exception e) {
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
	}
	private class PrintAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			Customer customer;
			customer = customerButton.getCustomer();
			SalesBack salesIn = createSalesBack();
			if(salesIn == null || customer == null){
				return;
			}else{
				Printer printer = new Printer();
				if(printPanle.getPrintStyleComboBox().getSelectedIndex() == 0){
					printer.init(salesIn, goodsPanel.getGoodsList(), customer, true);
				}else{
					printer.init(salesIn, goodsPanel.getGoodsList(), customer, false);
				}
				printer.print();
			}
		}
		
	}
	
}
