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

import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsItem;
import com.njue.mis.model.StockItem;
import com.sun.org.apache.bcel.internal.generic.NEW;

public class StockGoodsItemPanel extends JPanel{
	private String portId;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private List<StockItem> stockItemList;
	private JButton addButton;
	private JButton deleteButton;
	protected JTable table;
	protected DefaultTableModel model;
	protected JScrollPane scrollPane;
	protected String[] columns = {"编号","商品","数量","备注"};
	protected double money;
	public StockGoodsItemPanel(String portId){
		super();
		this.portId = portId;
		stockItemList = new ArrayList<>();
		init();
	}
	public void init(){
		setLayout(new BorderLayout());
		setSize(screenSize.width * 3/5, screenSize.height*2/5);
		JPanel north = new JPanel();
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
	public List<StockItem> getGoodsItemList(){
		money = 0;
		stockItemList.clear();
		for(int i = 0; i < model.getRowCount(); i++){
			int itemId = (Integer) model.getValueAt(i, 0);
			Goods goods = (Goods) model.getValueAt(i, 1);
			if(goods == null){
				continue;
			}
			int number;
			try{
				number = Integer.parseInt((String)model.getValueAt(i, 2));
				if(number<=0){
					CommonUtil.showError("数量必须为正");
				}
			}catch(Exception ex){
				continue;
			}
			String item_comment = (String) model.getValueAt(i, 3);
			System.out.println("GoodsId: "+goods.getId());
			stockItemList.add(new StockItem(itemId, portId, goods.getId(), number, item_comment));
		}
		return this.stockItemList;
	}
	public double getMoney(){
		return money;
	}
	private class AddItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int id = model.getRowCount();
			table.getColumnModel().getColumn(1).setCellRenderer(new GoodsButtonRender());
			table.getColumnModel().getColumn(1).setCellEditor(new GoodsButtonEditor());
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
	public void clearData(){
		while(model.getRowCount()>0){
			model.removeRow(0);
		}
	}
}
