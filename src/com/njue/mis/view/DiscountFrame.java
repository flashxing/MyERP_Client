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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
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
import com.njue.mis.model.Customer;
import com.njue.mis.model.Discount;
import com.njue.mis.model.Goods;


public class DiscountFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4783529949458927656L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private JTextField customerNameField;
	private JTextField discountField;
	private JTextField goodsField;
	private JTextField goodsNameField;
	private JButton customerSearchButton;
	private JButton discountUpdateButton;
	private String[] customerColumns={"用户编号","用户名称"};
	private String[] customerFields={"id","name"};
	private List<Customer> customerList = new ArrayList<Customer>();
	private JTable customerTable;
	private JScrollPane customerScrollPane;
	
	private Vector<Goods> goodsVec;

    protected String[] goodsColumns={"商品编号","商品名称","商品描述"};
    protected String[] goodsFields={"productCode","goodsName","description"};
    protected Object[] goodsList = {};
    private JTable goodsTable;
	private JScrollPane goodScrollPane;
	private JTextField goodsDiscountField;
	
	private Customer selectedCustomer;
	private Goods selectedGoods;
	
	private GoodsControllerInterface goodsHandler;
	private CustomerControllerInterface customerHandler;
	private DiscountControllerInterface discountHandler;
	private Discount discount;
	
	public DiscountFrame()
	{
		super("折扣管理",true,true,true,true);
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
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("网络错误");
		}
	}

	public JPanel importgoods()
	{

		JPanel panel = new JPanel();
		
		JPanel panel1 = new JPanel();
		JLabel customerNamelable = new JLabel("客户编号:");
		customerNameField = new JTextField(10);
		JLabel discountLabel = new JLabel("销售折扣:");
		discountField = new JTextField(10);
		
		customerSearchButton = new JButton("查询");
		customerSearchButton.addActionListener(new CustomerSearchAction());
		
		discountUpdateButton = new JButton("更新折扣");
		discountUpdateButton.addActionListener(new DiscountUpdateAction());
		customerTable = CommonUtil.createTable(customerColumns, customerList.toArray(), customerFields);
		customerTable.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height  / 5));
		customerTable.addMouseListener(new CustomerTableMouseListener());
		customerScrollPane = new JScrollPane();
		customerScrollPane.setViewportView(customerTable);
		
		panel1.add(customerNamelable);
		panel1.add(customerNameField);
		panel1.add(customerSearchButton);
		panel1.add(discountLabel);
		panel1.add(discountField);
		panel1.add(discountUpdateButton);
		
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
			}
		});
		
		
		goodsTable.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height  / 5));
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
		
		JButton inButton = new JButton("更新商品折扣");
		inButton.addActionListener(new GoodsDiscountUpdateListener());
		
		setEnableFalse();  
		panel4.add(goodsLabel);
		panel4.add(goodsField);
		panel4.add(goodsNameLabel);
		panel4.add(goodsNameField);
		panel4.add(addButton);
		panel4.add(goodsDiscountLabel);
		panel4.add(goodsDiscountField);
		panel4.add(inButton);
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
	private class DiscountUpdateAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(selectedCustomer == null||discount == null){
				CommonUtil.showError("请选择要更新折扣的用户");
				return;
			}
			double dis;
			try{
				dis = Double.parseDouble(discountField.getText());
			}catch(Exception ex){
				CommonUtil.showError("请输入数字");
				return;
			}
			if(dis>1||dis<0){
				CommonUtil.showError("折扣应在0到1之间");
				return;
			}
			discount.setDiscount(dis);
			try {
				discountHandler.updateDiscount(discount);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				CommonUtil.showError("网络错误，更新失败");
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
						discountField.setText(discount.getDiscount()+"");
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
	private class GoodsDiscountUpdateListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(discount == null || selectedGoods == null){
				CommonUtil.showError("请选选择要更新折扣的用户和商品");
				return;
			}
			double value=1;
			try{
				value = Double.parseDouble(goodsDiscountField.getText());
			}catch(Exception ex){
				CommonUtil.showError("折扣必须为数字");
				return;
			}
			if(value>1||value<0){
				CommonUtil.showError("折扣应在0到1之间");
				return;
			}
			discount.getGoodsDiscount().put(selectedGoods.getId(), value);
			try {
				discountHandler.updateDiscount(discount);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				CommonUtil.showError("网络错误，更新失败");
			}
		}
	}
}
