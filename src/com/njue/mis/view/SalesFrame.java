package com.njue.mis.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.PrintPanle;
import com.njue.mis.common.Printer;
import com.njue.mis.common.SalesGoodsItemPanel;
import com.njue.mis.common.SalesManButton;
import com.njue.mis.interfaces.DiscountControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Discount;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.SalesMan;
import com.njue.mis.model.StoreHouse;


public class SalesFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4373144987548153299L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private JTextField salesIdField;
	private JTextField operatorField;
	private CustomerButton customerButton;
	private SalesManButton salesManButton;
	private SalesGoodsItemPanel goodsPanel;
	private JComboBox<StoreHouse> storeHouseComboBox;
	private JComboBox<String> printStyleComboBox;
	
    protected String[] goodsColumns={"商品编号","商品名称","商品描述"};
    protected String[] goodsFields={"productCode","goodsName","description"};
    protected Object[] goodsList = {};
	private JTextField goodsDiscountField;
	private List<StoreHouse> storeHouseList;
	private PrintPanle printPanle;

	private DiscountControllerInterface discountHandler;
	private StoreHouseControllerInterface storeHouseHandler;
	private SalesControllerInterface salesHandler;
	private Discount discount;
	private JTextField timeField;
	private JTextField commentField;
	private Date date;
	
	private Customer customer;
	public SalesFrame()
	{
		super("销售出货单",true,true,true,true);
		date = new Date();
		init();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.getContentPane().add(importgoods());
	}
	public void init(){
		try {
			discountHandler=(DiscountControllerInterface) Naming.lookup(Configure.DiscountController);
			salesHandler = (SalesControllerInterface) Naming.lookup(Configure.SalesController);
			storeHouseHandler = (StoreHouseControllerInterface) Naming.lookup(Configure.StoreHouseController);
			storeHouseList = storeHouseHandler.getAll();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("网络错误");
		}
	}
	
	private void resetData(){
		date = new Date();
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		salesIdField.setText("PS"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
		commentField.setText("");
//		priceField.setText("");
//		numberField.setText("");
	}

	public JPanel importgoods()
	{

		JPanel panel = new JPanel(new BorderLayout());
		JPanel northPanel = new JPanel(new BorderLayout());
		JPanel commentPanel = new JPanel();
		panel.setSize(screenSize.width * 3 / 5,
				screenSize.height * 3 / 5);
		JPanel baseInfoPanel = new JPanel();
		JLabel salesIdlable = new JLabel("销售票号:");
		salesIdField = new JTextField(12);
		salesIdField.setEditable(false);
		baseInfoPanel.add(salesIdlable);
		baseInfoPanel.add(salesIdField);
		JLabel customerLabel = new JLabel("客户");
		customerButton = new CustomerButton("。。。");
		baseInfoPanel.add(customerLabel);
		baseInfoPanel.add(customerButton);
		JLabel salesManLabel = new JLabel("业务员:");
		salesManButton = new SalesManButton("。。。");
		baseInfoPanel.add(salesManLabel);
		baseInfoPanel.add(salesManButton);
		JLabel operatorLabel = new JLabel("操作员:");
		operatorField = new JTextField(10);
		operatorField.setText(MainFrame.username);
		operatorField.setEditable(false);
		baseInfoPanel.add(operatorLabel);
		baseInfoPanel.add(operatorField);
		JLabel shLabel = new JLabel("仓库");
		storeHouseComboBox = new JComboBox<StoreHouse>();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel<StoreHouse>());
		for(StoreHouse sh:storeHouseList){
			storeHouseComboBox.addItem(sh);
		}
		baseInfoPanel.add(shLabel);
		baseInfoPanel.add(storeHouseComboBox);
		JLabel timeLabel = new JLabel("时间");
		timeField = new JTextField(10);
		timeField.setEditable(false);
		baseInfoPanel.add(timeLabel);
		baseInfoPanel.add(timeField);
		
		JLabel commentLabel = new JLabel("备注");
		commentPanel.add(commentLabel);
		commentField = new JTextField(20);
		commentPanel.add(commentField);
		
		resetData();
		goodsPanel = new SalesGoodsItemPanel(salesIdField.getText());
		initDiscount();
		JPanel panelCommit = new JPanel();
		JButton commitButton = new JButton("提交");
		JButton commitAsDraftButton = new JButton("存为草稿");
		printPanle = new PrintPanle();
		printPanle.getPrintButton().addActionListener(new PrintAction());
		
		
		commitAsDraftButton.addActionListener(new AddAsDraftAction());
		commitButton.addActionListener(new SalesAddListener());
		panelCommit.add(commitButton);
		panelCommit.add(commitAsDraftButton);
		panelCommit.add(printPanle);
		
		northPanel.add(baseInfoPanel, BorderLayout.NORTH);
		northPanel.add(commentPanel, BorderLayout.SOUTH);
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(goodsPanel, BorderLayout.CENTER);
		panel.add(panelCommit, BorderLayout.SOUTH);
	
		return panel;
	}
	private void initDiscount(){
		discount = new Discount();
		customerButton.setDiscount(discount);
		goodsPanel.setDiscount(discount);
	}
	
	public SalesIn createSalesIn(){
		if((customer = customerButton.getCustomer())== null){
			CommonUtil.showError("请选择一个客户");
			return null;
		}
		if(storeHouseComboBox.getSelectedItem() == null){
			CommonUtil.showError("请先选择一个仓库");
			return null;
		}
		int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
		List<SalesGoodsItem> list = goodsPanel.getGoodsItemList(shId);
		if(list == null|| list.size() == 0){
			CommonUtil.showError("请选中一个商品");
			return null;
		}
		String id = salesIdField.getText();
		String time = timeField.getText();
		String operator = operatorField.getText();
		if(goodsPanel.getActualPrice()+customer.getCustomerMoney().getReceive()>customer.getMaxMoney()){
			CommonUtil.showError("超过客户销售限额");
			return null;
		}
		SalesMan salesMan = salesManButton.getSalesMan();
		String salesManName = "";
		if(salesMan != null){
			salesManName = salesMan.getName();
		}
		SalesIn sales = new SalesIn(id,customer.getId(),"",0,goodsPanel.getActualPrice(),time,operator,commentField.getText(),0,
				goodsPanel.getMoney(),goodsPanel.getDecreaseMoney(),shId, 1, salesManName, list);
		return sales;
	}
	
	private class SalesAddListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			SalesIn sales = createSalesIn();
			if(sales == null){
				return;
			}
			try {
				String result = salesHandler.addSalesIn(sales);
				if(result == null){
					CommonUtil.showError("添加销售单失败,可能库存不足");
					return;
				}
				resetData();
				goodsPanel.clearData();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
	}
	
	private class AddAsDraftAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if((customer = customerButton.getCustomer())== null){
				CommonUtil.showError("请选择一个客户");
				return;
			}
			int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
			List<SalesGoodsItem> list = goodsPanel.getGoodsItemList(shId);
			if(list == null|| list.size() == 0){
				CommonUtil.showError("请选中一个商品");
				return;
			}
			String id = salesIdField.getText();
			String time = timeField.getText();
			String operator = operatorField.getText();
			if(goodsPanel.getActualPrice()+customer.getCustomerMoney().getReceive()>customer.getMaxMoney()){
				CommonUtil.showError("超过客户销售限额");
				return;
			}			
			SalesMan salesMan = salesManButton.getSalesMan();
			String salesManName = "";
			if(salesMan != null){
				salesManName = salesMan.getName();
			}
			SalesIn sales = new SalesIn(id,customer.getId(),"",0,goodsPanel.getActualPrice(),time,operator,"",0,
					goodsPanel.getMoney(),goodsPanel.getDecreaseMoney(),shId, 0, salesManName, list);
			try {
				String result = salesHandler.addSalesIn(sales);
				if(result == null){
					CommonUtil.showError("添加销售单失败,可能库存不足");
					return;
				}
				resetData();
				goodsPanel.clearData();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
		
	}
	private class PrintAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			SalesIn salesIn = createSalesIn();
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
