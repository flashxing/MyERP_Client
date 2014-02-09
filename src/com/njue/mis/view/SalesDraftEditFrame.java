package com.njue.mis.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.PrintPanle;
import com.njue.mis.common.Printer;
import com.njue.mis.common.SalesGoodsItemPanel;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.DiscountControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Discount;
import com.njue.mis.model.SalesIn;
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
	
	private PrintPanle printPanle;
	
	private SalesIn salesIn;
	
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
		
		JLabel commentLabel = new JLabel("��ע");
		panel5.add(commentLabel);
		commentField = new JTextField(20);
		commentField.setText(salesIn.getComment());
		panel5.add(commentField);
		
		goodsPanel = new SalesGoodsItemPanel(salesIn.getId());
		goodsPanel.initWithGoodsList(salesIn);
		initDiscount();
		JPanel panelCommit = new JPanel();
		JButton commitButton = new JButton("����");
		JButton commitAsDraftButton = new JButton("�޸�");
		commitAsDraftButton.addActionListener(new AddAsDraftAction());
		commitButton.addActionListener(new SalesAddListener());
		panelCommit.add(commitButton);
		panelCommit.add(commitAsDraftButton);
		printPanle = new PrintPanle();
		panelCommit.add(printPanle);
		printPanle.getPrintButton().addActionListener(new PrintAction());
		
		panel.add(panel5, BorderLayout.NORTH);
		panel.add(goodsPanel, BorderLayout.CENTER);
		panel.add(panelCommit, BorderLayout.SOUTH);
	
		return panel;
	}
	private void initDiscount(){ 
		goodsPanel.setDiscount(discount);
	}
	private boolean updateSalesIn(){
		int shId=((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
		if(goodsPanel.getActualPrice()+customer.getCustomerMoney().getReceive()>customer.getMaxMoney()){
			CommonUtil.showError("�����ͻ������޶�");
			return false;
		}
		salesIn.setTotalPrice(goodsPanel.getMoney());
		salesIn.setDecreasePrice(goodsPanel.getDecreaseMoney());
		salesIn.setPrice(goodsPanel.getActualPrice());
		if(goodsPanel.getGoodsItemList(shId) == null){
			CommonUtil.showError("��治��");
			return false;
		}
		salesIn.setComment(commentField.getText());
		salesIn.setGoodsItemsList(goodsPanel.getGoodsItemList(shId));
		return true;
	}
	private class SalesAddListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0){
			if (!updateSalesIn()){
				return ;
			}
			try {
				if(!salesService.setSalesToPublished(salesIn)){
					CommonUtil.showError("������۵�ʧ��,���ܿ�治��");
					return;
				}
				goodsPanel.clearData();
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
