package com.njue.mis.common;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import com.njue.mis.client.RemoteService;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsItem;
import com.njue.mis.model.Port;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;

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
	protected String[] columns = {"编号","商品名称","商品型号","数量","单价","总价","备注"};
	private GoodsControllerInterface goodsService;
	protected Map<String, Integer> map = new HashMap<String, Integer>(){{
		put("id", 0);
		put("goods", 1);
		put("description", 2);
		put("number", 3);
		put("unitPrice", 4);
		put("totalPrice", 5);
		put("comment", 6);
	}};
	protected double money;
	private DecimalFormat df=new DecimalFormat(".##");
	public GoodsItemPanel(String portId){
		super();
		this.portId = portId;
		goodsItemList = new ArrayList<GoodsItem>();
		init();
	}
	public void init(){
		goodsService = RemoteService.goodsService;
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
				if(c == map.get("id")|| c == map.get("description")){
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
				int row = arg0.getFirstRow();
				if(row<0 || row>= model.getRowCount()){
					return;
				}
				Goods goods = (Goods) model.getValueAt(row, map.get("goods"));
				if(goods == null){
					return;
				}
				Object tmpDescription = model.getValueAt(row, map.get("description"));
				if(tmpDescription == null || tmpDescription.toString().equals("") || !tmpDescription.toString().equals(goods.getDescription())){
					model.setValueAt(goods.getDescription(), row, map.get("description"));
				}
				Object priceObject = model.getValueAt(row, map.get("unitPrice"));
				double price = goods.getPrice();
//				if(priceObject != null&&!priceObject.equals("")){
//					double tmpPrice = CommonUtil.formateDouble(Double.parseDouble(priceObject.toString()));
//					if(tmpPrice == price){
//						continue;	
//					}
//				}
				if(arg0.getColumn() == map.get("goods")){
					String st=df.format(price);
					model.setValueAt(st, row, map.get("unitPrice"));
				}
				if(arg0.getColumn() == map.get("unitPrice")){
					money = calTotalMoney();
				}
				money = calTotalMoney();
				totalMoneyField.setText(""+money);
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
			int itemId = (Integer) model.getValueAt(i, map.get("id"));
			Goods goods = (Goods) model.getValueAt(i, map.get("goods"));
			if(goods == null){
				continue;
			}
			int number;
			double item_money;
			try{
				number = Integer.parseInt((String)model.getValueAt(i, map.get("number")));
				item_money = Double.parseDouble(df.format(Double.parseDouble((String) model.getValueAt(i, map.get("unitPrice")))));
				money+=number*item_money;
				money=Double.parseDouble(df.format((money)));
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
				}
			}catch(Exception ex){
				continue;
			}
			double totalPrice = number*item_money;
			String item_comment = (String) model.getValueAt(i, map.get("comment"));
			goodsItemList.add(new GoodsItem(i, portId, goods.getId(), number, item_money, totalPrice, item_comment));
		}
		return this.goodsItemList;
	}
	public double getMoney(){
		return CommonUtil.formateDouble(money);
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
			if(table.getSelectedColumn()!=0&&table.getSelectedColumn()!=3&&table.getSelectedColumn()!=4&&table.getSelectedColumn()!=5){
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
		money = 0.00;
		for(int i = 0; i < model.getRowCount(); i++){
			int number;
			double item_money;
			try{
				number = Integer.parseInt(model.getValueAt(i, map.get("number")).toString());
				item_money = Double.parseDouble(model.getValueAt(i, map.get("unitPrice")).toString());
				money+=number*item_money;
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
				}
				double total_price = 0.00;
				total_price = CommonUtil.formateDouble(number*item_money);
				Object object = model.getValueAt(i, map.get("totalPrice"));
				if(object!=null && total_price == Double.parseDouble((String) object)){
					System.out.println(total_price+"***"+model.getValueAt(i, map.get("totalPrice")));
					continue;
				}	
				model.setValueAt(total_price+"", i, map.get("totalPrice"));
			}catch(Exception ex){
				continue;
			}
		}
		money = CommonUtil.formateDouble(money);
		return money;
	}
	
	public void clearData(String portId){
		while(model.getRowCount()>0){
			model.removeRow(0);
		}
		calTotalMoney();
		totalMoneyField.setText(""+money);
		this.portId = portId;
	}
	public boolean initWithGoodsList(Port port){
		this.goodsItemList = port.getGoodsItemList();
		List<String> goodsIdList = new ArrayList<>();
		for(GoodsItem goodsItem:goodsItemList){
			goodsIdList.add(goodsItem.getGoodsId());
		}
		List<Goods> goodsList = null;
		try {
			goodsList = goodsService.getGoods(goodsIdList);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(goodsList == null){
			return false;
		}

		for(int i = 0; i< goodsItemList.size(); i++){
			int id = addRow();
			GoodsItem goodsItem = goodsItemList.get(i);
			model.setValueAt(goodsList.get(i), id, map.get("goods"));
			model.setValueAt(goodsList.get(i).getDescription(), id, map.get("description"));
			model.setValueAt(goodsItem.getNumber()+"", id, map.get("number"));
			model.setValueAt(goodsItem.getUnitPrice()+"", id, map.get("unitPrice"));
			model.setValueAt(goodsItem.getTotalPrice()+"", id, map.get("totalPrice"));
			model.setValueAt(goodsItem.getComment()+"", id, map.get("comment"));
		}
		money = port.getPrice();
		totalMoneyField.setText(CommonUtil.formateDouble(money)+"");
//		calTotalMoney();
//		totalMoneyField.setText(""+salesIn.getTotalPrice());
//		decreasePriceField.setText(""+salesIn.getDecreasePrice());
//		actualPriceField.setText(""+salesIn.getPrice());
		return true;
	}
	private int addRow(){
		int id = model.getRowCount();
		Vector rows = new Vector();
		rows.add(id);
		model.addRow(rows);
		table.getColumnModel().getColumn(1).setCellRenderer(new GoodsButtonRender());
		table.getColumnModel().getColumn(1).setCellEditor(new GoodsButtonEditor());
		return id;
	}
}
