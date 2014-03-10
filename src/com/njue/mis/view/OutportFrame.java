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

import com.lowagie.text.pdf.BidiOrder;
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
import com.njue.mis.model.GoodsItem;
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
	private JTextField commentField;
	private Vector<Goods> goodsVec;
	private List<StoreHouse> storeHouseList;
	private PortControllerInterface portInService;

    protected String[] goodsColumns={"��Ʒ���","��Ʒ����","��Ʒ����"};
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
		super("�˻���",true,true,true,true);
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
			CommonUtil.showError("�������");
			return null;
		}
		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(screenSize.width * 3 / 5,
				screenSize.height * 3 / 5);
		JPanel northPanel = new JPanel(new BorderLayout());
		JPanel baseInfoPanel = new JPanel();
		JLabel ID_importlable = new JLabel("����Ʊ��:");
		ID_importtextField = new JTextField(10);
		JLabel customerLabel = new JLabel("��Ӧ��:");
		customerButton = new CustomerButton("...");
		customerButton.setSize(5, 5);
		baseInfoPanel.add(ID_importlable);
		baseInfoPanel.add(ID_importtextField);
		baseInfoPanel.add(customerLabel);
		baseInfoPanel.add(customerButton);
		
		JLabel paytypeLabel = new JLabel("�ֿ�ѡ��:");
		storeHouseComboBox = new JComboBox();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel(storeHouseList.toArray()));
		JLabel importtimeLabel = new JLabel("����ʱ��:");
		importtimeField = new JTextField(10);	
		JLabel opreaterLabel = new JLabel("����Ա:");
		operaterField = new JTextField(10);
		baseInfoPanel.add(paytypeLabel);
		baseInfoPanel.add(storeHouseComboBox);
		baseInfoPanel.add(importtimeLabel);
		baseInfoPanel.add(importtimeField);
		baseInfoPanel.add(opreaterLabel);
		baseInfoPanel.add(operaterField);
		JPanel commentPanel = new JPanel();
		JLabel commentLabel = new JLabel("��ע");
		commentPanel.add(commentLabel);
		commentField = new JTextField(20);
		commentPanel.add(commentField);
		northPanel.add(baseInfoPanel, BorderLayout.NORTH);
		northPanel.add(commentPanel, BorderLayout.SOUTH);
		initBase();
		panelGoods = new GoodsItemPanel(ID_importtextField.getText());
		JPanel panel3 = new JPanel();
		goodScrollPane = new JScrollPane();

		
		goodsTable = CommonUtil.createTable(goodsColumns,goodsVec.toArray(),goodsFields);
		//����иı�ʱ�������¼�
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
		JLabel goodsNameLabel = new JLabel("��Ʒ����:");
		goodsNameField = new JTextField(10);
		
		JButton searchButton = new JButton("��ѯ");
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
					CommonUtil.showError("�������");
				}
			}
		});
		GoodsButton goodsButton = new GoodsButton("������");
		panelSearch.add(goodsButton);
		panelSearch.add(goodsNameLabel);
		panelSearch.add(goodsNameField);
		panelSearch.add(searchButton);
		JPanel panel4 = new JPanel();
			
		JButton outButton = new JButton("�˻�");
		outButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				List<GoodsItem> list = panelGoods.getGoodsItemList();
				if(panelGoods.getGoodsItemList() == null || panelGoods.getGoodsItemList().size() <= 0){
					CommonUtil.showError("����ѡ��һ����Ʒ");
					return;
				}
				String inportID=ID_importtextField.getText();
				int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
				String inportTime=importtimeField.getText();
				String operator=operaterField.getText();
				if(customerButton.getCustomer() == null){
					CommonUtil.showError("��ѡ��һ���ͻ�");
					return;
				}
				int number = 0;
				for(GoodsItem goodsItem : list){
					number += goodsItem.getNumber();
				}
				String customerId = customerButton.getCustomerId();				
				PortBack portOut=new PortBack(inportID,"",shId,number,
						                  panelGoods.getMoney(),inportTime,operator,commentField.getText(),customerId,panelGoods.getGoodsItemList());
				
				try {
					if (portInService.addPortBack(portOut)!=null)
					{
						JOptionPane.showMessageDialog(null,"�˻�����ӳɹ�","����",JOptionPane.WARNING_MESSAGE);
						setEnableFalse();
						initBase();
						panelGoods.clearData(ID_importtextField.getText());
					}
					else
					{
						JOptionPane.showMessageDialog(null,"�˻������ʧ�ܣ��밴Ҫ����������","����",JOptionPane.WARNING_MESSAGE);	
					}
				} catch (HeadlessException | RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("�������");
				}
				
			}
			
		});
		
		setEnableFalse();  
		panel4.add(outButton);
		panel.add(northPanel,BorderLayout.NORTH);
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
	//���ò��ֿؼ�Ϊ������״̬
	private void setEnableFalse()
	{
		ID_importtextField.setEnabled(false);
		importtimeField.setEnabled(false);
		operaterField.setEnabled(false);
	}
	//���ò��ֿؼ�Ϊ����״̬
	private void setEnableTrue()
	{
		storeHouseComboBox.setEnabled(true);
	}
	
}
