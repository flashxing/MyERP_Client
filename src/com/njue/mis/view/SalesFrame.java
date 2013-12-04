package com.njue.mis.view;


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
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.DiscountControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Discount;
import com.njue.mis.model.Goods;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.StoreHouse;


public class SalesFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4373144987548153299L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private JTextField customerNameField;
	private JTextField goodsField;
	private JTextField goodsNameField;
	private JTextField salesIdField;
	private JTextField numberField;
	private JTextField totalPriceField;
	private JTextField priceField;
	private JTextField operatorField;
	private JButton saleButton;
	private JButton customerSearchButton;
	private String[] customerColumns={"用户编号","用户名称"};
	private String[] customerFields={"id","name"};
	private List<Customer> customerList = new ArrayList<Customer>();
	private JTable customerTable;
	private JScrollPane customerScrollPane;
	private JComboBox<StoreHouse> storeHouseComboBox;
	
	private Vector<Goods> goodsVec;

    protected String[] goodsColumns={"商品编号","商品名称","商品描述"};
    protected String[] goodsFields={"productCode","goodsName","description"};
    protected Object[] goodsList = {};
    private JTable goodsTable;
	private JScrollPane goodScrollPane;
	private JTextField goodsDiscountField;
	
	private Customer selectedCustomer;
	private Goods selectedGoods;
	private List<StoreHouse> storeHouseList;
	
	private GoodsControllerInterface goodsHandler;
	private CustomerControllerInterface customerHandler;
	private DiscountControllerInterface discountHandler;
	private StoreHouseControllerInterface storeHouseHandler;
	private SalesControllerInterface salesHandler;
	private Discount discount;
	private JTextField timeField;
	private Date date;
	
	public SalesFrame()
	{
		super("销售单",true,true,true,true);
		date = new Date();
		init();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 2 / 3);
		this.getContentPane().add(importgoods());
	}
	public void init(){
		try {
			goodsHandler=(GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
			customerHandler=(CustomerControllerInterface) Naming.lookup(Configure.CustomerController);
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
//		priceField.setText("");
		numberField.setText("");
	}

	public JPanel importgoods()
	{

		JPanel panel = new JPanel();
		
		JPanel panel5 = new JPanel();
		JLabel salesIdlable = new JLabel("销售票号:");
		salesIdField = new JTextField(12);
		salesIdField.setEditable(false);
		JLabel priceLabel = new JLabel("销售总价:");
		totalPriceField = new JTextField(10); 
		JLabel numberLabel = new JLabel("数量:");
		numberField = new JTextField(10);
		saleButton = new JButton("销售");
		saleButton.addActionListener(new SalesAddListener());
		JLabel operatorLabel = new JLabel("操作员:");
		operatorField = new JTextField(10);
		operatorField.setText(MainFrame.username);
		operatorField.setEditable(false);
		JLabel priceLabel1 = new JLabel("单价：");
		priceField = new JTextField(10);
		JLabel shLabel = new JLabel("仓库");
		storeHouseComboBox = new JComboBox<StoreHouse>();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel<StoreHouse>());
		for(StoreHouse sh:storeHouseList){
			storeHouseComboBox.addItem(sh);
		}
		panel5.add(salesIdlable);
		panel5.add(salesIdField);
		panel5.add(priceLabel);
		panel5.add(totalPriceField);
		panel5.add(priceLabel1);
		panel5.add(priceField);
		panel5.add(numberLabel);
		panel5.add(numberField);
		panel5.add(shLabel);
		panel5.add(storeHouseComboBox);
		panel5.setSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height/40));
		panel5.setSize(screenSize.width, screenSize.height/20);
		
		
		JLabel label = new JLabel("时间:");
		timeField = new JTextField();
		timeField.setColumns(12);
		timeField.setEditable(false);
		
		SimpleDateFormat formate =new SimpleDateFormat("yyyyMMddHHmmss");
		salesIdField.setText("PS"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	
		JPanel panel6 = new JPanel();
		panel6.add(label);
		panel6.add(timeField);
		panel6.add(operatorLabel);
		panel6.add(operatorField);
		panel6.add(saleButton);
		
		JPanel panel1 = new JPanel();
		JLabel customerNamelable = new JLabel("客户姓名:");
		customerNameField = new JTextField(10);
		
		customerSearchButton = new JButton("查询");
		customerSearchButton.addActionListener(new CustomerSearchAction());
		

		customerTable = CommonUtil.createTable(customerColumns, customerList.toArray(), customerFields);
		customerTable.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height  / 8));
		customerTable.addMouseListener(new CustomerTableMouseListener());
		customerScrollPane = new JScrollPane();
		customerScrollPane.setViewportView(customerTable);
		
		panel1.add(customerNamelable);
		panel1.add(customerNameField);
		panel1.add(customerSearchButton);
		
		JPanel panel2 = new JPanel();
		panel2.add(customerScrollPane);
	
		JPanel panel3 = new JPanel();
		goodScrollPane = new JScrollPane();

		
		goodsTable = CommonUtil.createTable(goodsColumns,goodsList,goodsFields);
		//表格行改变时发生的事件
		goodsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				if(discount == null){
					CommonUtil.showError("请先选择一个客户");
					return;
				}
				ListSelectionModel model = (ListSelectionModel)e.getSource();
				int index = model.getMaxSelectionIndex();
				double value = discount.getDiscount();
				if(index>=0 && goodsVec!=null &&index<goodsVec.size() && discount!=null){
					selectedGoods = goodsVec.get(index);
					if(discount.getGoodsDiscount().containsKey(selectedGoods.getId())){
						value = discount.getGoodsDiscount().get(selectedGoods.getId());
					}
					goodsDiscountField.setText(value+"");
				}
				priceField.setText(value*selectedGoods.getPrice()+"");
			}
		});
		
		
		goodsTable.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height  / 8));
		goodScrollPane.setViewportView(goodsTable);
		panel3.add(goodScrollPane);
		
		JPanel panel4 = new JPanel();
		JLabel goodsLabel = new JLabel("商品编号:");
		goodsField = new JTextField(10);
		JLabel goodsNameLabel = new JLabel("商品名称:");
		goodsNameField = new JTextField(10);
		
		JButton addButton = new JButton("查询");
		addButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(selectedCustomer == null){
					CommonUtil.showError("请选中一个客户");
					return;
				}
				//get the goods data and storeHouse data
				try {
					String goodsName = goodsNameField.getText();
					if(goodsName.length()==0){
						JOptionPane.showMessageDialog(null,"请输入查询商品的名称","警告",JOptionPane.WARNING_MESSAGE);
						return;
					}	
					goodsVec = goodsHandler.getAllGoodsByGoodsName(goodsName);
					CommonUtil.updateJTable(goodsTable, goodsColumns, goodsVec.toArray(), goodsFields);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络错误");
				}
//				Date date=new Date();
//				SimpleDateFormat formate=new SimpleDateFormat("yyyyMMddHHmmss");
//				ID_importtextField.setText("PI"+formate.format(date));
//				formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				importtimeField.setText(formate.format(date));
//				operaterField.setText(MainFrame.username);
//				numberField.setText("");
//				setEnableTrue();
			}
		});
		JLabel goodsDiscountLabel = new JLabel("商品折扣");
		goodsDiscountField = new JTextField(10);
		goodsDiscountField.setEditable(false);
		
		setEnableFalse();
		panel4.add(goodsLabel);
		panel4.add(goodsField);
		panel4.add(goodsNameLabel);
		panel4.add(goodsNameField);
		panel4.add(addButton);
		panel4.add(goodsDiscountLabel);
		panel4.add(goodsDiscountField);
		panel.add(panel5);
		panel.add(panel6);
		panel.add(panel1);
		panel.add(panel2);
		panel.add(panel4);
		panel.add(panel3);
	
		return panel;
	}
	//设置部分控件为不可用状态
	private void setEnableFalse()
	{
//		customerNameField.setEnabled(false);
//		discountField.setEnabled(false);
//		goodsField.setEnabled(false);
	}

	private void clearTextField()
	{
		goodsDiscountField.setText("");
	}
	private class CustomerSearchAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			try {
				String customerName = customerNameField.getText();
				if(customerName.length()==0){
					JOptionPane.showMessageDialog(null,"请输入查询用户的名称","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
				customerList= customerHandler.searchCustomerByName(customerName);
				CommonUtil.updateJTable(customerTable, customerColumns, customerList.toArray(), customerFields);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
	}
	
	private class CustomerTableMouseListener implements MouseListener{

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			clearTextField();
			System.out.print(customerTable.getSelectedRow());
			if((customerTable.getSelectedRow()>=0)&&(customerTable.getSelectedRow()<customerList.size())){
				selectedCustomer = customerList.get(customerTable.getSelectedRow());
				try {
					discount = discountHandler.getDiscount(selectedCustomer.getId());
					if(discount!=null){
						goodsDiscountField.setText(discount.getDiscount()+"");
					}else{
						discount = new Discount();
						discount.setCustomerId(selectedCustomer.getId());
						discount.setDiscount(1);
						discountHandler.addDiscount(discount);
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonUtil.showError("网络错误");
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	private class SalesAddListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			if(selectedCustomer == null || discount == null){
				CommonUtil.showError("请选择一个客户");
				return;
			}
			if(selectedGoods == null){
				CommonUtil.showError("请选中一个商品");
				return;
			}
			String id = salesIdField.getText();
			String goodsId = selectedGoods.getId();
			String customerId = selectedCustomer.getId();
			String time = timeField.getText();
			String operator = operatorField.getText();
			double dis = discount.getDiscount();
			double price = selectedGoods.getPrice()*dis;
			NumberFormat nf = NumberFormat.getNumberInstance();
	        nf.setMaximumFractionDigits(2);
	        price = Double.parseDouble(nf.format(price));
			if(discount.getGoodsDiscount().containsKey(selectedGoods.getId())){
				dis = discount.getGoodsDiscount().get(selectedGoods.getId());
			}
			int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
			int number;
			try{
				number = Integer.parseInt(numberField.getText());
			}catch(Exception ex){
				CommonUtil.showError("数量必须为数字");
				return;
			}
			if(number <=0){
				CommonUtil.showError("数量必须大于0");
				return;
			}
			double money = price*number;
			String comment="";
			SalesIn sales = new SalesIn(id,customerId,goodsId,number,price,time,operator,comment,dis,money,shId);
			try {
				String result = salesHandler.addSalesIn(sales);
				if(result == null){
					CommonUtil.showError("添加销售单失败,可能库存不足");
					return;
				}
				CommonUtil.showError("销售单添加成功");
				resetData();
				System.out.println();
				System.out.println(result);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
	}
}
