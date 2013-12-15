package com.njue.mis.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.NumericShaper;
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
	protected String[] columns = {"编号","商品","数量","单价","备注"};
	protected double money;
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
				if(c == 0){
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
		table.getColumnModel().getColumn(1).setCellRenderer(new GoodsButtonRender());
		table.getColumnModel().getColumn(1).setCellEditor(new GoodsButtonEditor());
		CommonUtil.setDuiqi(table);
		table.setModel(model);
		table.setRowSelectionAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 5,
				screenSize.height  / 5));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(new Dimension(screenSize.width * 3 / 5,
				screenSize.height * 1/5));
        add(north, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
	}
	public List<GoodsItem> getGoodsItemList(){
		money = 0;
		goodsItemList.clear();
		System.out.println(model.getRowCount());
		for(int i = 0; i < model.getRowCount(); i++){
			int itemId = (Integer) model.getValueAt(i, 0);
			GoodsButtonEditor cellEditor = (GoodsButtonEditor) table.getCellEditor(i, 1);
			GoodsButton goodsButton;
			goodsButton = cellEditor.getButton();
			if(goodsButton == null){
				continue;
			}
			Goods goods = goodsButton.getGoods();
			if(goods == null){
				continue;
			}
			int number;
			double item_money;
			try{
				number = Integer.parseInt((String)model.getValueAt(i, 2));
				item_money = Double.parseDouble((String) model.getValueAt(i, 3));
				money+=number*item_money;
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
				}
			}catch(Exception ex){
				continue;
			}
			String item_comment = (String) model.getValueAt(i, 3);
			goodsItemList.add(new GoodsItem(itemId, portId, goods.getId(), number, item_money, item_comment));
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
				item_money = Double.parseDouble((String) model.getValueAt(i, 3));
				money+=number*item_money;
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
				}
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
