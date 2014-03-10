package com.njue.mis.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.table.AbstractTableModel;

import com.lowagie.text.pdf.BidiOrder;
import com.njue.mis.client.Configure;
import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.GoodsButton;
import com.njue.mis.common.GoodsItemPanel;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.PortControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsItem;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.StoreHouse;


public class InportViewFrame extends JInternalFrame
{
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	
	private JTextField ID_importtextField;
	private JComboBox storeHouseComboBox;
	private JTextField importtimeField;
	private JTextField operaterField;
	private JTextField goodsField;
	private JTextField goodsNameField;
	private JTextField commentField;
	private Vector<Goods> goodsVec;
	private List<StoreHouse> storeHouseList;
	private PortControllerInterface portInService;
	private CustomerControllerInterface customerService;

    protected String[] goodsColumns={"商品编号","商品名称","商品描述"};
    protected String[] goodsFields={"productCode","goodsName","description"};
    protected Object[] goodsList = {};
    private JTable goodsTable;
	private JScrollPane goodScrollPane;

	private CustomerButton customerButton;
	private GoodsControllerInterface handler;
	
	private Goods selectedGoods;
	private PortIn portIn;
	private GoodsItemPanel panelGoods;
	private Customer customer;
	private JButton deleteButton;
	public InportViewFrame(PortIn portIn)
	{
		super("进货单",true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.portIn = portIn;
		this.getContentPane().add(importgoods());
		initWithPortIn();
	}

	public void initWithPortIn(){
		if(null != portIn){
			ID_importtextField.setText(portIn.getId());
			importtimeField.setText(portIn.getTime());
			operaterField.setText(portIn.getOperatePerson());
			commentField.setText(portIn.getComment());
			customerButton.setText(portIn.getCustomerId());
			try {
				customer = customerService.getCustomerInfo(portIn.getCustomerId());
				customerButton.setText(customer.getName());
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			panelGoods.initWithGoodsList(portIn);
		}
		
	}
	public void setButtonVisible(boolean visible){
		if(!visible){
			deleteButton.setVisible(true);
		}
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
			customerService = RemoteService.customerService;
		} catch (MalformedURLException | RemoteException | NotBoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			CommonUtil.showError("网络错误");
			return null;
		}
		JPanel panel = new JPanel(new BorderLayout());
		JPanel northPanel = new JPanel(new BorderLayout());
		panel.setSize(screenSize.width * 3 / 5,
				screenSize.height * 3 / 5);
		JPanel baseInfoPanel = new JPanel();
		JLabel ID_importlable = new JLabel("进货票号:");
		ID_importtextField = new JTextField(10);
		JLabel customerLabel = new JLabel("供应商:");
		customerButton = new CustomerButton("...");
		customerButton.setSize(5, 5);
		baseInfoPanel.add(ID_importlable);
		baseInfoPanel.add(ID_importtextField);
		baseInfoPanel.add(customerLabel);
		baseInfoPanel.add(customerButton);
		
		JLabel paytypeLabel = new JLabel("仓库选择:");
		storeHouseComboBox = new JComboBox();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel(storeHouseList.toArray()));
		JLabel importtimeLabel = new JLabel("进货时间:");
		importtimeField = new JTextField(10);	
		JLabel opreaterLabel = new JLabel("操作员:");
		operaterField = new JTextField(10);
		baseInfoPanel.add(paytypeLabel);
		baseInfoPanel.add(storeHouseComboBox);
		baseInfoPanel.add(importtimeLabel);
		baseInfoPanel.add(importtimeField);
		baseInfoPanel.add(opreaterLabel);
		baseInfoPanel.add(operaterField);
		JPanel commentPanel = new JPanel();
		JLabel commentLabel = new JLabel("备注");
		commentField = new JTextField(20);
		commentPanel.add(commentLabel);
		commentPanel.add(commentField);
		northPanel.add(baseInfoPanel, BorderLayout.NORTH);
		northPanel.add(commentPanel, BorderLayout.SOUTH);
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
//					if(goodsName.length()==0){
//						JOptionPane.showMessageDialog(null,"请输入查询商品的名称","警告",JOptionPane.WARNING_MESSAGE);
//						return;
//					}	
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
		deleteButton = new JButton("红冲");
		deleteButton.addActionListener(new DeleteAction());
		panel4.add(deleteButton);
		setEnableFalse();  
//		panel4.add(inButton);
		panel.add(northPanel,BorderLayout.NORTH);
//		panel.add(panelSearch);
		panel.add(panelGoods, BorderLayout.CENTER);
		panel.add(panel4,BorderLayout.SOUTH);
		return panel;
	}
	
	private void initBase(){
		Date date=new Date();
		SimpleDateFormat formate=new SimpleDateFormat("yyyyMMddHHmmss");
		ID_importtextField.setText("PI"+formate.format(date));
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
	private class DeleteAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int confirm = JOptionPane.showConfirmDialog(null, "确认删除？");
			if(JOptionPane.YES_OPTION != confirm){
				return;
			}
			try {
				if(!portInService.deletePort(portIn)){
					CommonUtil.showError("删除失败");
				}else{
					CommonUtil.showError("删除成功");
					setClosed(true);
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (PropertyVetoException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	
}
