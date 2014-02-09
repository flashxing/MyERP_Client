package com.njue.mis.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
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

import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsItem;

public class GoodsItemPanel extends JPanel{
	private String portId;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private List<GoodsItem> goodsItemList;
	private JButton addButton;
	private JButton deleteButton;
	private JTextField totalMoneyField;
	protected JTable table;
	protected DefaultTableModel model;
	protected JScrollPane scrollPane;
	protected String[] columns = {"编号","商品","数量","单价","总价","备注"};
	protected double money;
	private DecimalFormat df=new DecimalFormat(".##");
	public GoodsItemPanel(String portId){
		super();
		this.portId = portId;
		goodsItemList = new ArrayList<GoodsItem>();
		init();
	}
	public void init(){
		setLayout(new BorderLayout());
		setSize(screenSize.width * 3/5, screenSize.height*2/5);
		JPanel north = new JPanel();
		JLabel label = new JLabel("总额:");
		totalMoneyField = new JTextField(10);
		totalMoneyField.setEditable(false);
		north.add(label);
		north.add(totalMoneyField);
		addButton = new JButton("添加商品");
		deleteButton = new JButton("删除商品");
		addButton.addActionListener(new AddItemAction());
		deleteButton.addActionListener(new DeleteItemAction());
		north.add(addButton);
		north.add(deleteButton);
		model = new DefaultTableModel(columns, 0){
			@Override
			public boolean isCellEditable(int r,int c){
				if(c == 0|| c == 4){
					return false;
				}
				return true;
			}
		};
		table = new JTable(model);
		table.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent arg0) {
				// TODO Auto-generated method stub
				money = calTotalMoney();
				totalMoneyField.setText(""+money);
			}
		});
		CommonUtil.setDuiqi(table);
		table.setModel(model);
		table.getModel().addTableModelListener(new TableModelListener() {
			
			@Override
			public void tableChanged(TableModelEvent arg0) {
				money = calTotalMoney();
				totalMoneyField.setText(""+money);
				for(int i = 0; i < model.getRowCount(); ++i){
					Goods goods = (Goods) model.getValueAt(i, 1);
					if(goods == null){
						continue;
					}
					Object priceObject = model.getValueAt(i, 3);
					if(priceObject != null&&!priceObject.equals("")){
						continue;
					}
					double price = goods.getPrice();
					String st=df.format(price);
					model.setValueAt(st, i, 3);
				}
			}
		});
		table.setRowSelectionAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 5,
				screenSize.height  / 5));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(new Dimension(screenSize.width * 3 / 5,
				screenSize.height * 1/5));
        add(north, BorderLayout.SOUTH);
        add(scrollPane, BorderLayout.CENTER);
	}
	public List<GoodsItem> getGoodsItemList(){
		money = 0;
		goodsItemList.clear();
		System.out.println(model.getRowCount());
		for(int i = 0; i < model.getRowCount(); i++){
			int itemId = (Integer) model.getValueAt(i, 0);
			Goods goods = (Goods) model.getValueAt(i, 1);
			if(goods == null){
				continue;
			}
			int number;
			double item_money;
			try{
				number = Integer.parseInt((String)model.getValueAt(i, 2));
				item_money = Double.parseDouble(df.format(Double.parseDouble((String) model.getValueAt(i, 3))));
				money+=number*item_money;
				money=Double.parseDouble(df.format((money)));
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
				}
			}catch(Exception ex){
				continue;
			}
			double totalPrice = number*item_money;
			String item_comment = (String) model.getValueAt(i, 5);
			goodsItemList.add(new GoodsItem(itemId, portId, goods.getId(), number, item_money, totalPrice, item_comment));
		}
		return this.goodsItemList;
	}
	public double getMoney(){
		return money;
	}
	private class AddItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int id = model.getRowCount();
			Vector rows = new Vector();
			rows.add(id);
			model.addRow(rows);
			table.getColumnModel().getColumn(1).setCellRenderer(new GoodsButtonRender());
			table.getColumnModel().getColumn(1).setCellEditor(new GoodsButtonEditor());
		}
		
	}
	
	private class DeleteItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			/*
			 * 不允许选中第二列去删除，会出错
			 */
			if(table.getSelectedColumn()!=0&&table.getSelectedColumn()!=2&&table.getSelectedColumn()!=3&&table.getSelectedColumn()!=4){
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
	
	private double calTotalMoney(){
		money = 0;
		for(int i = 0; i < model.getRowCount(); i++){
			int number;
			double item_money;
			try{
				number = Integer.parseInt((String)model.getValueAt(i, 2));
				item_money = Double.parseDouble(df.format(Double.parseDouble((String) model.getValueAt(i, 3))));
				money+=number*item_money;
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
				}
				double total_price = 0;
				total_price = number*item_money;
				Object object = model.getValueAt(i, 4);
				if(object!=null && total_price == Double.parseDouble((String) object)){
					System.out.println(total_price+"***"+model.getValueAt(i, 4));
					continue;
				}	
				model.setValueAt(total_price, i, 4);
			}catch(Exception ex){
				continue;
			}
		}
		return money;
	}
	
	public void clearData(){
		while(model.getRowCount()>0){
			model.removeRow(0);
		}
		calTotalMoney();
		totalMoneyField.setText(""+money);
	}
}
