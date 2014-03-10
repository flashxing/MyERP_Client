package com.njue.mis.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.PrintPanle;
import com.njue.mis.common.Printer;
import com.njue.mis.common.SalesGoodsItemPanel;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.DiscountControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Discount;
import com.njue.mis.model.Goods;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.Stock;
import com.njue.mis.model.StoreHouse;


public class SalesDraftEditFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4373144987548153299L;

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

	private JTextField salesIdField;
	private JTextField operatorField;
	private SalesGoodsItemPanel goodsPanel;
	private JComboBox<StoreHouse> storeHouseComboBox;
	
    protected String[] goodsColumns={"��Ʒ���","��Ʒ����","��Ʒ����"};
    protected String[] goodsFields={"productCode","goodsName","description"};
    protected Object[] goodsList = {};
	private List<StoreHouse> storeHouseList;
	

	private DiscountControllerInterface discountService;
	private StoreHouseControllerInterface storeHouseService;
	private SalesControllerInterface salesService;
	private CustomerControllerInterface customerService;
	private Discount discount;
	private Customer customer;
	private JTextField timeField;
	private JTextField commentField;
	private CustomerButton customerButton;
	private JButton commitButton;
	private JButton commitAsDraftButton;
	private JButton deleteButton;
	private PrintPanle printPanle;
	
	private SalesIn salesIn;
	private Date date;
	private SimpleDateFormat formate;
	
	
	public SalesDraftEditFrame(SalesIn salesIn)
	{
		super("���۵���༭",true,true,true,true);
		this.salesIn =salesIn;
		init();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.getContentPane().add(importgoods());
	}
	public void init(){
		try {
			date = new Date();
			formate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			customerService = RemoteService.customerService;
			discountService=RemoteService.discountService;
			salesService = RemoteService.salesService;
			storeHouseService = RemoteService.storeHouseService;
			storeHouseList = storeHouseService.getAll();
			customer = customerService.getCustomerInfo(salesIn.getCustomerId());
			discount = discountService.getDiscount(salesIn.getCustomerId());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("�������");
		}
	}

	public JPanel importgoods()
	{

		JPanel panel = new JPanel(new BorderLayout());
		panel.setSize(screenSize.width * 3 / 5,
				screenSize.height * 3 / 5);
		JPanel panel5 = new JPanel();
		JLabel salesIdlable = new JLabel("����Ʊ��:");
		salesIdField = new JTextField(12);
		salesIdField.setEditable(false);
		salesIdField.setText(salesIn.getId());
		panel5.add(salesIdlable);
		panel5.add(salesIdField);
		JLabel customerLabel = new JLabel("������λ");
		customerButton = new CustomerButton(customer.getName());
		panel5.add(customerLabel);
		panel5.add(customerButton);
		JLabel operatorLabel = new JLabel("����Ա:");
		operatorField = new JTextField(10);
		operatorField.setText(salesIn.getOperatePerson());
		operatorField.setEditable(false);
		panel5.add(operatorLabel);
		panel5.add(operatorField);
		JLabel shLabel = new JLabel("�ֿ�");
		storeHouseComboBox = new JComboBox<StoreHouse>();
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel<StoreHouse>());
		for(StoreHouse sh:storeHouseList){
			storeHouseComboBox.addItem(sh);
		}
		panel5.add(shLabel);
		panel5.add(storeHouseComboBox);
		JLabel timeLabel = new JLabel("ʱ��");
		timeField = new JTextField(10);
		timeField.setEditable(false);
		timeField.setText(salesIn.getTime());
		panel5.add(timeLabel);
		panel5.add(timeField);
		
		JPanel commentPanel = new JPanel();
		JLabel commentLabel = new JLabel("��ע");
		commentPanel.add(commentLabel);
		commentField = new JTextField(20);
		commentField.setText(salesIn.getComment());
		commentPanel.add(commentField);
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(panel5,BorderLayout.NORTH);
		northPanel.add(commentPanel, BorderLayout.SOUTH);
		
		goodsPanel = new SalesGoodsItemPanel(salesIn.getId());
		goodsPanel.initWithGoodsList(salesIn);
		initDiscount();
		JPanel panelCommit = new JPanel();
		commitButton = new JButton("����");
		commitAsDraftButton = new JButton("�޸�");
		commitAsDraftButton.addActionListener(new AddAsDraftAction());
		commitButton.addActionListener(new SalesAddListener());
		deleteButton = new JButton("���");
		deleteButton.setVisible(false);
		deleteButton.addActionListener(new DeleteAction());
		panelCommit.add(commitButton);
		panelCommit.add(commitAsDraftButton);
		panelCommit.add(deleteButton);
		printPanle = new PrintPanle();
		panelCommit.add(printPanle);
		printPanle.getPrintButton().addActionListener(new PrintAction());
		
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(goodsPanel, BorderLayout.CENTER);
		panel.add(panelCommit, BorderLayout.SOUTH);
	
		return panel;
	}
	private void initDiscount(){ 
		goodsPanel.setDiscount(discount);
	}
	public void setUnable(){
		commitButton.setVisible(false);
		commitAsDraftButton.setVisible(false);
		commitButton.setEnabled(false);
		commitAsDraftButton.setEnabled(false);
		deleteButton.setVisible(true);
	}
	private boolean updateSalesIn(){
		Customer tmpCustomer = customerButton.getCustomer();
		if(null != tmpCustomer){
			customer = tmpCustomer;
			salesIn.setCustomerId(customer.getId());
		}
		int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
		salesIn.setTotalPrice(goodsPanel.getMoney());
		salesIn.setDecreasePrice(goodsPanel.getDecreaseMoney());
		salesIn.setPrice(goodsPanel.getActualPrice());
		List<SalesGoodsItem> list = goodsPanel.getDraftGoodsItemList(shId);
		if(list == null || list.size() == 0){
			CommonUtil.showError("����������һ����Ʒ");
			return false;
		}
		int number = 0;
		for(SalesGoodsItem salesGoodsItem : list){
			number += salesGoodsItem.getNumber();
		}
		salesIn.setNumber(number);
		salesIn.setComment(commentField.getText());
		salesIn.setGoodsItemsList(list);
		return true;
	}
	private class SalesAddListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			if (!updateSalesIn()){
				return ;
			}
			if(goodsPanel.getActualPrice()+customer.getCustomerMoney().getReceive()>customer.getMaxMoney()){
				CommonUtil.showError("�����ͻ������޶�");
				return;
			}
			try {
				Customer customer = RemoteService.customerService.getCustomerInfo(salesIn.getCustomerId());
				if(salesIn.getPrice()+customer.getCustomerMoney().getReceive()>customer.getMaxMoney()){
					CommonUtil.showError("�����ͻ������޶�");
					return;
				}
				int shId = salesIn.getShId();
				StockControllerInterface stockService = RemoteService.stockService;
				GoodsControllerInterface goodsService = RemoteService.goodsService;
				for(SalesGoodsItem salesGoodsItem : salesIn.getGoodsItemsList()){
					Goods goods = goodsService.getGoods(salesGoodsItem.getGoodsId());
					Stock stock = null;
					if(shId > 0){ 
						stock = stockService.getStock(shId, goods.getId());
					}
					if(stock != null && salesGoodsItem.getNumber() > stock.getNumber()){
						CommonUtil.showError("��治��"+goods.getGoodsName());
						return;
					}
				}
				if(!salesService.setSalesToPublished(salesIn)){
					CommonUtil.showError("������۵�ʧ��,���ܿ�治��");
					return;
				}
				goodsPanel.clearData(salesIdField.getText());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("�������");
			}
		}
	}
	
	private class AddAsDraftAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(!updateSalesIn()){
				return;
			}
			try {
				System.out.println(salesIn);
				if(!salesService.updateSales(salesIn)){
					CommonUtil.showError("����ҵ�񵥸�ʧ��,���ܿ�治��");
					return;
				}else{
					CommonUtil.showError("�޸�ҵ�񵥸�ɹ�");
					
				}
//				goodsPanel.clearData();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CommonUtil.showError("�������");
			}
		}
	}
	private class DeleteAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			int confirm = JOptionPane.showConfirmDialog(null, "ȷ��ɾ����");
			if(JOptionPane.YES_OPTION != confirm){
				return;
			}
			try {
				if(!salesService.deleteSales(salesIn)){
					CommonUtil.showError("ɾ��ʧ��");
					return;
				}else{
					CommonUtil.showError("ɾ���ɹ�");
					setClosed(true);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
	private class PrintAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(!updateSalesIn()){
				return;
			}
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
