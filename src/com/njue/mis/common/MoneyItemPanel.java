package com.njue.mis.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

import com.njue.mis.model.CardItem;
import com.njue.mis.model.Money;
import com.njue.mis.model.MoneyItem;
import com.njue.mis.model.MoneyItemDetail;

public class MoneyItemPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected String[] columns = {"编号","条目","金额","备注"};
	protected List<MoneyItemDetail> moneyItemDetailList;
	protected JTextField moneyField;

	protected JButton addItemButton;
	protected JButton deleteItemButton;

	protected JTable table;
	protected DefaultTableModel model;
	protected JScrollPane scrollPane;
	public MoneyItemPanel(){
		super();
		this.moneyItemDetailList = new ArrayList<>();
		init();
	}
	public void init(){
		setLayout(new BorderLayout());
		setSize(screenSize.width * 3/5, screenSize.height*2/7);
		JPanel north = new JPanel();
		JLabel label = new JLabel("总额:");
		moneyField = new JTextField(8);
		addItemButton = new JButton("添加条目");
		addItemButton.addActionListener(new AddItemAction());
		deleteItemButton = new JButton("删除条目");
		deleteItemButton.addActionListener(new DeleteItemAction());
		north.add(label);
		north.add(moneyField);
		north.add(addItemButton);
		north.add(deleteItemButton);
		model = new DefaultTableModel(columns, 0){
			/**
			 * 
			 */
			private static final long serialVersionUID = -7791606034562723563L;

			@Override
			public boolean isCellEditable(int r,int c){
				if(c == 0){
					return false;
				}
				return true;
			}
		};
		table = new JTable(model);
		CommonUtil.setDuiqi(table);
		table.setModel(model);
		table.setRowSelectionAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 5,
				screenSize.height  / 5));
		table.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent arg0) {
				// TODO Auto-generated method stub
				if(arg0.getColumn() == 2){
					double money = 0;
					for(int i = 0; i < model.getRowCount(); ++i){
						double item_money;
						try{
							item_money = Double.parseDouble((String) model.getValueAt(i, 2));
							if(item_money<0){
								CommonUtil.showError("金额必须为正");
							}
							money+=item_money;
							
						}catch(Exception ex){
							continue;
						}
					}
					moneyField.setText(CommonUtil.formateDouble(money)+"");
				}
			}
			
		});
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(new Dimension(screenSize.width * 3 / 5,
				screenSize.height * 1/5));
        add(scrollPane,BorderLayout.CENTER);
        add(north, BorderLayout.SOUTH);
	}
	/*
	 * type = 0 then return an receiptIn
	 * type =1 then return an ReceiptOut
	 */
	public Money getMoney(String id, String operator, String time, CardItem cardItem){
		Double money = 0.0;
		moneyItemDetailList = new ArrayList<MoneyItemDetail>();
		for(int i = 0; i < model.getRowCount(); i++){
			int itemId = (Integer) model.getValueAt(i, 0);
			System.out.println("itemId "+itemId);
			MoneyItemButton itemButton;
			itemButton = (MoneyItemButton) model.getValueAt(i, 1);
			if(itemButton.getMoneyItem() == null){
				continue;
			}
			MoneyItem item = itemButton.getMoneyItem();
			if(item == null){
				continue;
			}
			double item_money;
			try{
				item_money = Double.parseDouble((String) model.getValueAt(i, 2));
				money+=item_money;
				if(money<0){
					CommonUtil.showError("金额必须为正");
				}
			}catch(Exception ex){
				CommonUtil.showError("第几"+i+"行金额未输入,此行抛弃");
				continue;
			}
			String item_comment = (String) model.getValueAt(i, 3);
			moneyItemDetailList.add(new MoneyItemDetail(id, itemId, item.getName(), item_money, item_comment));
		}
		return new Money(id,money,time,operator,cardItem.getName(), moneyItemDetailList);
	}
	private class AddItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int id = model.getRowCount();
			Vector rows = new Vector();
			rows.add(id);
			model.addRow(rows);
			table.getColumnModel().getColumn(1).setCellRenderer(new ButtonRender<MoneyItemButton>(MoneyItemButton.class));
			table.getColumnModel().getColumn(1).setCellEditor(new ButtonEditor<MoneyItemButton>(MoneyItemButton.class));
		}
		
	}
	public void reset(){
		moneyField.setText("");
		while(model.getRowCount()>0){
			model.removeRow(0);
		}
	}
	private class DeleteItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(table.getSelectedColumn()!=0&&table.getSelectedColumn()!=2&&table.getSelectedColumn()!=3){
				CommonUtil.showError("请选中一个条目");
				return;
			}
			int index = table.getSelectedRow();
			if(!(index<table.getRowCount()&&index>=0)){
				CommonUtil.showError("请先选择一个条目");
				return;
			}
			model.removeRow(index);
		}
	}
}
