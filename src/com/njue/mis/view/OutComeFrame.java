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
import com.njue.mis.model.OutCome;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.Sales;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.Stock;
import com.njue.mis.model.StoreHouse;


public class OutComeFrame extends JInternalFrame
{
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private JTextField goodsField;
	private JTextField goodsNameField;
	private JTextField outComeIdField;
	private JTextField numberField;
	private JTextField existNumberField;
	private JTextField operatorField;
	private JButton outComeButton;
	private JComboBox storeHouseComboBox;
	
	private Vector<Goods> goodsVec;

    protected String[] goodsColumns={"商品编号","商品名称","商品描述"};
    protected String[] goodsFields={"productCode","goodsName","description"};
    protected Object[] goodsList = {};
    private JTable goodsTable;
	private JScrollPane goodScrollPane;
	
//	private Customer selectedCustomer;
	private Goods selectedGoods;
	private StoreHouse selectedSH;
	private List<StoreHouse> storeHouseList;
	
	private GoodsControllerInterface goodsHandler;
	private StoreHouseControllerInterface storeHouseHandler;
	private StockObjectControllerInterface stockObjectHandler;
	private StockControllerInterface stockHandler;
	
	private JTextField timeField;
	private Date date;
	
	private int maxNumber;
	
	public OutComeFrame()
	{
		super("库存报损",true,true,true,true);
		init();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 2 / 3);
		this.getContentPane().add(importgoods());
	}
	public void init(){
		try {
			goodsHandler=(GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
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
		outComeIdField.setText("OC"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeField.setText(formate.format(date));
	}

	public JPanel importgoods()
	{

		JPanel panel = new JPanel();
		
		JPanel panel5 = new JPanel();
		JLabel salesIdlable = new JLabel("报损票号:");
		outComeIdField = new JTextField(12);
		outComeIdField.setEditable(false);
		JLabel numberLabel = new JLabel("数量:");
		numberField = new JTextField(10);
		outComeButton = new JButton("报损");
		outComeButton.addActionListener(new OutComeListener());
		JLabel operatorLabel = new JLabel("操作员:");
		operatorField = new JTextField(10);
		operatorField.setText(MainFrame.username);
		operatorField.setEditable(false);
		JLabel shLabel = new JLabel("仓库");
		storeHouseComboBox = new JComboBox<StoreHouse>();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel(storeHouseList.toArray()));
		storeHouseComboBox.addItemListener(new ItemChangeAction());
		JLabel existNumberLabel = new JLabel("库存");
		existNumberField = new JTextField(10);
		existNumberField.setEditable(false);
		panel5.add(salesIdlable);
		panel5.add(outComeIdField);
		panel5.add(numberLabel);
		panel5.add(numberField);
		panel5.add(shLabel);
		panel5.add(storeHouseComboBox);
		panel5.add(existNumberLabel);
		panel5.add(existNumberField);
		panel5.setSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height/40));
		panel5.setSize(screenSize.width, screenSize.height/20);
		
		
		JLabel label = new JLabel("时间:");
		timeField = new JTextField();
		timeField.setColumns(12);
		timeField.setEditable(false);
		initBaseInfo();
		
		JPanel panel6 = new JPanel();
		panel6.add(label);
		panel6.add(timeField);
		panel6.add(operatorLabel);
		panel6.add(operatorField);
		panel6.add(outComeButton);
		
		JPanel panel2 = new JPanel();	
		JPanel panel3 = new JPanel();
		goodScrollPane = new JScrollPane();

		
		goodsTable = CommonUtil.createTable(goodsColumns,goodsList,goodsFields);
		//表格行改变时发生的事件
		goodsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				ListSelectionModel model = (ListSelectionModel)e.getSource();
				int index = model.getMaxSelectionIndex();
				if(index>=0 && goodsVec!=null &&index<goodsVec.size()){
					selectedGoods = goodsVec.get(index);
					selectedSH = (StoreHouse) storeHouseComboBox.getSelectedItem();
					if(selectedSH != null){
						maxNumber = getMaxNumber(selectedSH.getId(),selectedGoods.getId());
						existNumberField.setText(maxNumber+"");
					}
				}
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
			}
		});

		
		setEnableFalse();
		panel4.add(goodsLabel);
		panel4.add(goodsField);
		panel4.add(goodsNameLabel);
		panel4.add(goodsNameField);
		panel4.add(addButton);
		panel.add(panel5);
		panel.add(panel6);
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
	
	private int getMaxNumber(int shId, String goodsId){
		int result = 0;
		Stock stock = null;
		try {
			stock = stockHandler.getStock(shId, goodsId);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (stock != null){
			result = stock.getNumber();
		}
		return result;
	}


	private class OutComeListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			if(selectedGoods == null||selectedSH == null){
				CommonUtil.showError("请先选择一个商品和仓库");
				return;
			}
			
			String id = outComeIdField.getText();
			String receiver = "";
			String goodsId = selectedGoods.getId();
			int storeHouseId = selectedSH.getId();
			int number = 0;
			try{
				number = Integer.parseInt(numberField.getText());
			}catch (Exception ex){
				CommonUtil.showError("数量必须为数字");
				return;
			}
			if(number > maxNumber||number<=0){
				CommonUtil.showError("数量不能大于库存数量或者小于0");
				return;
			}
			double price = 0.0;
			String time = timeField.getText();
			String operatePerson = operatorField.getText();
			String comment = "";
			OutCome outCome = new OutCome(id, receiver, goodsId, storeHouseId, number, price, time, operatePerson, comment);
			try {
				if(!id.equals(stockObjectHandler.addStockOut(outCome))){
					CommonUtil.showError("添加失败");
					return;
				}else{
					CommonUtil.showError("添加成功");
					initBaseInfo();
					maxNumber = maxNumber-number;
					existNumberField.setText(maxNumber+"");
					numberField.setText("");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				CommonUtil.showError("网络错误");
				return;
			}
		}
	}
	
	private class ItemChangeAction implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getStateChange()==ItemEvent.SELECTED){
				selectedSH = (StoreHouse) storeHouseComboBox.getSelectedItem();
				if(selectedGoods != null){
					maxNumber = getMaxNumber(selectedSH.getId(), selectedGoods.getId());
					existNumberField.setText(maxNumber+"");
				}
			}
		}
		
	}
}
