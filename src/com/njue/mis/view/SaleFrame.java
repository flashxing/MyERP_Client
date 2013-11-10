package com.njue.mis.view;


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

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.PortInControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Goods;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.StoreHouse;


public class SaleFrame extends JInternalFrame
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	JTextField ID_importtextField;
	JTextField priceField;
	JTextField numberField;
	JComboBox storeHouseComboBox;
	JTextField importtimeField;
	JTextField operaterField;
	JTextField goodsField;
	JTextField goodsNameField;
	private Vector<Goods> goodsVec;
	private List<StoreHouse> storeHouseList = new ArrayList<StoreHouse>();
	private double goodsPrices=0;  //记录商品的单价
	private PortInControllerInterface portInService;
    protected String[] objects={"商品编号","商品名称","商品描述"};
    protected String[] fieldsToShow={"productCode","goodsName","description"};
    protected Object[] initList = {};
	
	public SaleFrame()
	{
		super("出货单",true,true,true,true);
		
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 2 / 3);
		this.getContentPane().add(importgoods());
	}

	public JPanel importgoods()
	{

		JPanel panel = new JPanel();
		
		JPanel panel5 = new JPanel();
		JLabel ID_importlable = new JLabel("出货票号:");
		ID_importtextField = new JTextField(10);
		JLabel priceLabel = new JLabel("进货单价:");
		priceField = new JTextField(10); 
		JLabel numberLabel = new JLabel("数量:");
		numberField = new JTextField(10);
		panel5.add(ID_importlable);
		panel5.add(ID_importtextField);
		panel5.add(priceLabel);
		panel5.add(priceField);
		panel5.add(numberLabel);
		panel5.add(numberField);
		
		
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
		
		JPanel panel3 = new JPanel();
		JScrollPane goodScrollPane = new JScrollPane();

		final JTable goodsTable;
		goodsTable = CommonUtil.createTable(objects,initList,fieldsToShow);
//		//表格行改变时发生的事件
//		goodsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener()
//		{
//			public void valueChanged(ListSelectionEvent e)
//			{
//				ListSelectionModel model = (ListSelectionModel)e.getSource();
//				int index = model.getMaxSelectionIndex();
//				goodsField.setText(goodsTable.getValueAt(index, 0).toString());
//				goodsPrices=Double.valueOf(goodsTable.getValueAt(index, 8).toString());
//			}
//		});
		
		
		goodsTable.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height  / 3));
		goodScrollPane.setViewportView(goodsTable);
		panel3.add(goodScrollPane);
		
		JPanel panel4 = new JPanel();
		JLabel goodsLabel = new JLabel("商品编号:");
		goodsField = new JTextField(10);
		JLabel goodsNameLabel = new JLabel("商品名称:");
		goodsNameField = new JTextField(20);
		
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
					GoodsControllerInterface handler=(GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
					goodsVec = handler.getAllGoodsByGoodsName(goodsName);
					CommonUtil.updateJTable(goodsTable, objects, goodsVec.toArray(), fieldsToShow);
				} catch (MalformedURLException | RemoteException | NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络错误");
				}
//				Date date=new Date();
//				SimpleDateFormat formate=new SimpleDateFormat("yyyyMMddHHmmss");
//				ID_importtextField.setText("PS"+formate.format(date));
//				formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				importtimeField.setText(formate.format(date));
//				operaterField.setText(MainFrame.username);
//				numberField.setText("");
//				setEnableTrue();
			}
		});
		JButton inButton = new JButton("入库");
		inButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				String inportID=ID_importtextField.getText();
				String numberStr=numberField.getText();
				int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
				String inportTime=importtimeField.getText();
				String operator=operaterField.getText();
				String goodsID=goodsField.getText();
				String comment=goodsNameField.getText();
				double goodsPrice = 0;
				if(numberStr==null||numberStr.trim().length()==0)
				{
					JOptionPane.showMessageDialog(null,"请输入商品数量","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
				int number=0;
				try
				{
					number=Integer.valueOf(numberStr);
					goodsPrice = Double.valueOf(priceField.getText());
				}
				catch (Exception ex)
				{
					JOptionPane.showMessageDialog(null,"商品的数量和价格必须为数字","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
				PortIn portIn=new PortIn(inportID,goodsID,shId,number,
						                  goodsPrice,inportTime,operator,comment,0);
				
				try {
					if (portInService.addPortIn(portIn)!=null)
					{
						JOptionPane.showMessageDialog(null,"入货单添加成功","警告",JOptionPane.WARNING_MESSAGE);
						numberField.setText("");
						setEnableFalse();
					}
					else
					{
						JOptionPane.showMessageDialog(null,"入货单添加失败，请按要求输入数据","警告",JOptionPane.WARNING_MESSAGE);	
					}
				} catch (HeadlessException | RemoteException e1) {
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
		panel4.add(inButton);
		panel.add(panel5);
		panel.add(panel2);
		panel.add(panel4);
		panel.add(panel3);
	
		return panel;
	}
	//设置部分控件为不可用状态
	private void setEnableFalse()
	{
		ID_importtextField.setEnabled(false);
		priceField.setEnabled(false);
		numberField.setEnabled(false);
		storeHouseComboBox.setEnabled(false);
		importtimeField.setEnabled(false);
		operaterField.setEnabled(false);
		goodsField.setEnabled(false);
	}
	//设置部分控件为可用状态
	private void setEnableTrue()
	{
		numberField.setEnabled(true);
		storeHouseComboBox.setEnabled(true);
		goodsNameField.setEnabled(true);
		priceField.setEnabled(true);
	}
}
