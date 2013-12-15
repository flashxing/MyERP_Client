package com.njue.mis.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
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

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.GoodsButton;
import com.njue.mis.common.GoodsItemPanel;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.PortControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Goods;
import com.njue.mis.model.PortBack;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.StoreHouse;


public class OutportFrame extends JInternalFrame
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private JTextField ID_importtextField;
	private JComboBox storeHouseComboBox;
	private JTextField importtimeField;
	private JTextField operaterField;
	private JTextField goodsField;
	private JTextField goodsNameField;
	private Vector<Goods> goodsVec;
	private List<StoreHouse> storeHouseList;
	private PortControllerInterface portInService;

    protected String[] goodsColumns={"商品编号","商品名称","商品描述"};
    protected String[] goodsFields={"productCode","goodsName","description"};
    protected Object[] goodsList = {};
    private JTable goodsTable;
	private JScrollPane goodScrollPane;

	private CustomerButton customerButton;
	private GoodsControllerInterface handler;
	
	private Goods selectedGoods;
	
	private GoodsItemPanel panelGoods;
	public OutportFrame()
	{
		super("进货单",true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.getContentPane().add(importgoods());
	}

	
	public JPanel importgoods()
	{
		//get the goods data and storeHouse data
		try {
			handler =(GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
			goodsVec = handler.getAllGoods();
			StoreHouseControllerInterface storeHouseService = (StoreHouseControllerInterface) Naming.lookup(Configure.StoreHouseController);
			storeHouseList = storeHouseService.getAll();
			portInService = (PortControllerInterface) Naming.lookup(Configure.PortInController);
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			CommonUtil.showError("网络错误");
			return null;
		}
		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(screenSize.width * 3 / 5,
				screenSize.height * 3 / 5);
		JPanel panel1 = new JPanel();
		JLabel ID_importlable = new JLabel("进货票号:");
		ID_importtextField = new JTextField(10);
		JLabel customerLabel = new JLabel("供应商:");
		customerButton = new CustomerButton("...");
		customerButton.setSize(5, 5);
		panel1.add(ID_importlable);
		panel1.add(ID_importtextField);
		panel1.add(customerLabel);
		panel1.add(customerButton);
		
		JPanel panel2 = new JPanel();
		JLabel paytypeLabel = new JLabel("仓库选择:");
		storeHouseComboBox = new JComboBox();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel(storeHouseList.toArray()));
		JLabel importtimeLabel = new JLabel("进货时间:");
		importtimeField = new JTextField(10);	
		JLabel opreaterLabel = new JLabel("操作员:");
		operaterField = new JTextField(10);
		panel2.add(paytypeLabel);
		panel2.add(storeHouseComboBox);
		panel2.add(importtimeLabel);
		panel2.add(importtimeField);
		panel2.add(opreaterLabel);
		panel2.add(operaterField);
		
		initBase();
		panelGoods = new GoodsItemPanel(ID_importtextField.getText());
		JPanel panel3 = new JPanel();
		goodScrollPane = new JScrollPane();

		
		goodsTable = CommonUtil.createTable(goodsColumns,goodsVec.toArray(),goodsFields);
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
					goodsField.setText(selectedGoods.getProductCode());
				}
			}
		});
		
		
		goodsTable.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height  / 8));
		goodScrollPane.setViewportView(goodsTable);
		panel3.add(goodScrollPane);
		
		JPanel panelSearch = new JPanel();
		JLabel goodsNameLabel = new JLabel("商品名称:");
		goodsNameField = new JTextField(10);
		
		JButton searchButton = new JButton("查询");
		searchButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				try {
					String goodsName = goodsNameField.getText();
					goodsVec = handler.getAllGoodsByGoodsName(goodsName);
					CommonUtil.updateJTable(goodsTable, goodsColumns, goodsVec.toArray(), goodsFields);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络错误");
				}
			}
		});
		GoodsButton goodsButton = new GoodsButton("。。。");
		panelSearch.add(goodsButton);
		panelSearch.add(goodsNameLabel);
		panelSearch.add(goodsNameField);
		panelSearch.add(searchButton);
		JPanel panel4 = new JPanel();
			
		JButton outButton = new JButton("退货");
		outButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if(panelGoods.getGoodsItemList() == null || panelGoods.getGoodsItemList().size() <= 0){
					CommonUtil.showError("请先选择一个商品");
					return;
				}
				String inportID=ID_importtextField.getText();
				int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
				String inportTime=importtimeField.getText();
				String operator=operaterField.getText();
				if(customerButton.getCustomer() == null){
					CommonUtil.showError("请选择一个客户");
					return;
				}
				String customerId = customerButton.getCustomerId();				
				PortBack portOut=new PortBack(inportID,"",shId,0,
						                  panelGoods.getMoney(),inportTime,operator,"",customerId,panelGoods.getGoodsItemList());
				
				try {
					if (portInService.addPortBack(portOut)!=null)
					{
						JOptionPane.showMessageDialog(null,"退货单添加成功","警告",JOptionPane.WARNING_MESSAGE);
						setEnableFalse();
						initBase();
						panelGoods.clearData();
					}
					else
					{
						JOptionPane.showMessageDialog(null,"退货单添加失败，请按要求输入数据","警告",JOptionPane.WARNING_MESSAGE);	
					}
				} catch (HeadlessException | RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络错误");
				}
				
			}
			
		});
		
		setEnableFalse();  
		panel4.add(outButton);
		panel1.add(panel2);
		panel.add(panel1,BorderLayout.NORTH);
//		panel.add(panelSearch);
		panel.add(panelGoods, BorderLayout.CENTER);
		panel.add(panel4,BorderLayout.SOUTH);
		return panel;
	}
	
	private void initBase(){
		Date date=new Date();
		SimpleDateFormat formate=new SimpleDateFormat("yyyyMMddHHmmss");
		ID_importtextField.setText("IB"+formate.format(date));
		formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		importtimeField.setText(formate.format(date));
		operaterField.setText(MainFrame.username);
		setEnableTrue();
	}
	//设置部分控件为不可用状态
	private void setEnableFalse()
	{
		ID_importtextField.setEnabled(false);
		importtimeField.setEnabled(false);
		operaterField.setEnabled(false);
	}
	//设置部分控件为可用状态
	private void setEnableTrue()
	{
		storeHouseComboBox.setEnabled(true);
	}
	
}
