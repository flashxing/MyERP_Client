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
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.StoreHouse;


public class SalesFrame extends JInternalFrame
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
	private JTextField goodsDiscountField;
	private List<StoreHouse> storeHouseList;
	

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
//		priceField.setText("");
//		numberField.setText("");
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
		
		JPanel panelCommit = new JPanel();
		JButton commitButton = new JButton("提交");
		commitButton.addActionListener(new SalesAddListener());
		panelCommit.add(commitButton);
		
		panel.add(panel5, BorderLayout.NORTH);
		panel.add(goodsPanel, BorderLayout.CENTER);
		panel.add(panelCommit, BorderLayout.SOUTH);
	
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
//	private class CustomerSearchAction implements ActionListener{
//
//		@Override
//		public void actionPerformed(ActionEvent arg0) {
//			// TODO Auto-generated method stub
//			try {
//				String customerName = customerNameField.getText();
//				if(customerName.length()==0){
//					JOptionPane.showMessageDialog(null,"请输入查询用户的名称","警告",JOptionPane.WARNING_MESSAGE);
//					return;
//				}
//				customerList= customerHandler.searchCustomerByName(customerName);
//				CommonUtil.updateJTable(customerTable, customerColumns, customerList.toArray(), customerFields);
//			} catch (RemoteException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//				CommonUtil.showError("网络错误");
//			}
//		}
//	}
//	
//	private class CustomerTableMouseListener implements MouseListener{
//
//		@Override
//		public void mouseClicked(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//			clearTextField();
//			System.out.print(customerTable.getSelectedRow());
//			if((customerTable.getSelectedRow()>=0)&&(customerTable.getSelectedRow()<customerList.size())){
//				selectedCustomer = customerList.get(customerTable.getSelectedRow());
//				try {
//					discount = discountHandler.getDiscount(selectedCustomer.getId());
//					if(discount!=null){
//						goodsDiscountField.setText(discount.getDiscount()+"");
//					}else{
//						discount = new Discount();
//						discount.setCustomerId(selectedCustomer.getId());
//						discount.setDiscount(1);
//						discountHandler.addDiscount(discount);
//					}
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					CommonUtil.showError("网络错误");
//				}
//			}
//		}
//
//		@Override
//		public void mouseEntered(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void mouseExited(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void mousePressed(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//
//		@Override
//		public void mouseReleased(MouseEvent arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//	}
	
	private class SalesAddListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			Customer customer;
			if((customer = customerButton.getCustomer())== null){
				CommonUtil.showError("请选择一个客户");
				return;
			}
			List<SalesGoodsItem> list = goodsPanel.getGoodsItemList();
			if(list == null|| list.size() == 0){
				CommonUtil.showError("请选中一个商品");
				return;
			}
			String id = salesIdField.getText();
			String time = timeField.getText();
			String operator = operatorField.getText();
//			double dis = discount.getDiscount();
//			double price = selectedGoods.getPrice()*dis;
//			NumberFormat nf = NumberFormat.getNumberInstance();
//	        nf.setMaximumFractionDigits(2);
//	        price = Double.parseDouble(nf.format(price));
//			if(discount.getGoodsDiscount().containsKey(selectedGoods.getId())){
//				dis = discount.getGoodsDiscount().get(selectedGoods.getId());
//			}
			int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
//			int number;
//			try{
//				number = Integer.parseInt(numberField.getText());
//			}catch(Exception ex){
//				CommonUtil.showError("数量必须为数字");
//				return;
//			}
//			if(number <=0){
//				CommonUtil.showError("数量必须大于0");
//				return;
//			}
//			double money = price*number;
//			String comment="";
			SalesIn sales = new SalesIn(id,customer.getId(),"",0,goodsPanel.getMoney(),time,operator,"",0,0,shId,list);
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
	
}
