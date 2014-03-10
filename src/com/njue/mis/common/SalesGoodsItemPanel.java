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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.report.JFreeReportBoot;

import com.njue.mis.client.RemoteService;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.model.Discount;
import com.njue.mis.model.Goods;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.Stock;
import com.sun.org.apache.xml.internal.security.c14n.implementations.Canonicalizer11_OmitComments;

public class SalesGoodsItemPanel extends JPanel{
	private String portId;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private List<SalesGoodsItem> goodsItemList;
	private List<Goods> goodsList = new ArrayList<>();
	private JButton addButton;
	private JButton deleteButton;
	private JTextField totalMoneyField;
	private JTextField decreasePriceField;
	private JTextField actualPriceField;
	protected JTable table;
	protected DefaultTableModel model;
	protected JScrollPane scrollPane;
	protected String[] columns = {"编号","商品名称","商品型号","数量","单价","总价","备注"};
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
	protected Discount discount;
	private DecimalFormat df=new DecimalFormat(".##");
	private StockControllerInterface stockService;
	private GoodsControllerInterface goodsService;
	private int shId;
	private SalesIn salesIn;
	public SalesGoodsItemPanel(String portId){
		super();
		this.portId = portId;
		goodsItemList = new ArrayList<SalesGoodsItem>();
		init();
	}
	public void init(){
		stockService = RemoteService.stockService;
		goodsService = RemoteService.goodsService;
		setLayout(new BorderLayout());
		setSize(screenSize.width * 3/5, screenSize.height*2/7);
		JPanel north = new JPanel();
		JLabel label = new JLabel("总额:");
		totalMoneyField = new JTextField(8);
		totalMoneyField.setEditable(false);
		north.add(label);
		north.add(totalMoneyField);
		JLabel decreaseLabel = new JLabel("折让:");
		decreasePriceField = new JTextField(8);
		decreasePriceField.setText("0");
		decreasePriceField.getDocument().addDocumentListener(new PriceChangeAction());;
		JLabel actuaLabel = new JLabel("实际金额:");
		actualPriceField = new JTextField(8);
		actualPriceField.setEditable(false);
		totalMoneyField.getDocument().addDocumentListener(new PriceChangeAction());
		north.add(decreaseLabel);
		north.add(decreasePriceField);
		north.add(actuaLabel);
		north.add(actualPriceField);
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
				System.out.println("table changed");
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
				double price = goods.getSalesPrice();
//				if(priceObject != null&&!priceObject.equals("")){
//					double tmpPrice = CommonUtil.formateDouble(Double.parseDouble(priceObject.toString()));
//					if(tmpPrice == price){
//						continue;	
//					}
//				}
				if(discount != null){
					price = price*discount.getDiscount(goods.getId());
				}
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
		CommonUtil.setDuiqi(table);
		table.setModel(model);
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
	public List<SalesGoodsItem> getGoodsItemList(int shId){
		money = 0;
		this.shId = shId;
		goodsItemList.clear();
		goodsList.clear();
		for(int i = 0; i < model.getRowCount(); i++){
			int itemId = (Integer) model.getValueAt(i, map.get("id"));
			Goods goods = (Goods) model.getValueAt(i, map.get("goods"));
			if(goods == null){
				continue;
			}
			goodsList.add(goods);
			int number = 0;
			double item_money = 0;
			try{
				number = Integer.parseInt((String)model.getValueAt(i, map.get("number")));
				Stock stock = null;
				if(shId > 0){ 
					stock = stockService.getStock(shId, goods.getId());
				}else{
					CommonUtil.showError("请选择一个仓库");
					return null;
				}
				if(stock != null && number > stock.getNumber()){
					CommonUtil.showError("库存不足"+goods.getGoodsName()+" 库存"+stock.getNumber());
					return null;
				}
				item_money = Double.parseDouble(df.format(Double.parseDouble((String) model.getValueAt(i, map.get("unitPrice")))));
				money+=number*item_money;
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
					continue;
				}
			}catch(Exception ex){
				continue;
			}
			double total_price = CommonUtil.formateDouble(number*item_money);
			String item_comment = (String) model.getValueAt(i, map.get("comment"));
			goodsItemList.add(new SalesGoodsItem(i, portId, goods.getId(), number, item_money, total_price, item_comment));
		}
		return this.goodsItemList;
	}
	public List<SalesGoodsItem> getDraftGoodsItemList(int shId){
		money = 0;
		this.shId = shId;
		goodsItemList.clear();
		goodsList.clear();
		for(int i = 0; i < model.getRowCount(); i++){
			int itemId = (Integer) model.getValueAt(i, map.get("id"));
			Goods goods = (Goods) model.getValueAt(i, map.get("goods"));
			if(goods == null){
				continue;
			}
			goodsList.add(goods);
			int number = 0;
			double item_money = 0;
			try{
				number = Integer.parseInt((String)model.getValueAt(i, map.get("number")));
				item_money = Double.parseDouble(df.format(Double.parseDouble((String) model.getValueAt(i, map.get("unitPrice")))));
				money+=number*item_money;
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
					continue;
				}
			}catch(Exception ex){
				continue;
			}
			double total_price = CommonUtil.formateDouble(number*item_money);
			String item_comment = (String) model.getValueAt(i, map.get("comment"));
			goodsItemList.add(new SalesGoodsItem(i, portId, goods.getId(), number, item_money, total_price, item_comment));
		}
		return this.goodsItemList;
	}
	public double getMoney(){
		return money;
	}
	
	@SuppressWarnings("finally")
	public double getDecreaseMoney(){
		String decreasePriceString = decreasePriceField.getText();
		double decreasePrice = 0;
		try{
			decreasePrice = Double.parseDouble(df.format(Double.parseDouble(decreasePriceString)));
		}catch(Exception exception){
			decreasePrice = 0;
		}finally{
			return decreasePrice;
		}
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
	private class AddItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			addRow();
		}
		
	}
	
	private class DeleteItemAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			/*
			 * 不允许选中第二列去删除，会出错
			 */
			if(table.getSelectedColumn()!=0&&table.getSelectedColumn()!=3
					&&table.getSelectedColumn()!=4&&table.getSelectedColumn()!=5&&table.getSelectedColumn()!=6){
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
			double total_price;
			try{
				number = Integer.parseInt(model.getValueAt(i, map.get("number")).toString());
				item_money = Double.parseDouble(df.format(Double.parseDouble((String) model.getValueAt(i, map.get("unitPrice")))));
				total_price = CommonUtil.formateDouble(number*item_money);
				money+=total_price;
				money=Double.parseDouble(df.format((money)));
				System.out.println(money);
				if(money<0||number<=0){
					CommonUtil.showError("数量和单价必须为正");
					continue;
				}
				Object object = model.getValueAt(i, map.get("totalPrice"));
				if(object!=null && total_price == Double.parseDouble((String) object)){
					System.out.println(total_price+"***"+model.getValueAt(i, map.get("totalPrice")));
					continue;
				}	
				model.setValueAt(total_price+"", i, map.get("totalPrice"));
			}catch(Exception ex){
				ex.printStackTrace();
				continue;
			}
//			String st=df.format(total_price);
//			model.setValueAt(st, i, 4);
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
		decreasePriceField.setText("0");
		this.portId = portId;
	}
	
	public void setDiscount(Discount discount){
		this.discount = discount;
	}
	
	@SuppressWarnings("finally")
	public double getActualPrice(){
		String actualPriceString = actualPriceField.getText();
		double actualPrice = 0;
		try{
			actualPrice = Double.parseDouble(df.format(Double.parseDouble(actualPriceString)));
		}catch(Exception exception){
			actualPrice = 0;
		}finally{
			return CommonUtil.formateDouble(actualPrice);
		}
	}
	
	private class PriceChangeAction implements DocumentListener{
		@Override
		public void changedUpdate(DocumentEvent arg0) {
			double actualPrice = getMoney()-getDecreaseMoney();
			actualPriceField.setText(df.format(actualPrice));	
		}

		@Override
		public void insertUpdate(DocumentEvent arg0) {		
			double actualPrice = getMoney()-getDecreaseMoney();
			actualPriceField.setText(df.format(actualPrice));
		}

		@Override
		public void removeUpdate(DocumentEvent arg0) {
			double actualPrice = getMoney()-getDecreaseMoney();
			actualPriceField.setText(df.format(actualPrice));
		}
		
	}
	
	public boolean initWithGoodsList(SalesIn salesIn){
		System.out.println("void initWithGoodsList begin goodsItemList"+salesIn.getGoodsItemsList().size());
		this.goodsItemList = salesIn.getGoodsItemsList();
		List<String> goodsIdList = new ArrayList<>();
		for(SalesGoodsItem goodsItem:goodsItemList){
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
		System.out.println("void initWithGoodsList goodsList "+goodsList.size());

		for(int i = 0; i< goodsItemList.size(); i++){
			int id = addRow();
			SalesGoodsItem goodsItem = goodsItemList.get(i);
			model.setValueAt(goodsList.get(i), id, map.get("goods"));
			model.setValueAt(goodsList.get(i).getDescription(), id, map.get("description"));
			model.setValueAt(goodsItem.getNumber()+"", id, map.get("number"));
			model.setValueAt(goodsItem.getUnitPrice()+"", id, map.get("unitPrice"));
			model.setValueAt(goodsItem.getTotalPrice()+"", id, map.get("totalPrice"));
			model.setValueAt(goodsItem.getComment()+"", id, map.get("comment"));
		}
		money = salesIn.getTotalPrice();
		totalMoneyField.setText(CommonUtil.formateDouble(money)+"");
		decreasePriceField.setText(CommonUtil.formateDouble(salesIn.getDecreasePrice())+"");
		actualPriceField.setText(CommonUtil.formateDouble(salesIn.getPrice())+"");
//		calTotalMoney();
//		totalMoneyField.setText(""+salesIn.getTotalPrice());
//		decreasePriceField.setText(""+salesIn.getDecreasePrice());
//		actualPriceField.setText(""+salesIn.getPrice());
		return true;
	}
	public TableModel getModel(){
		return table.getModel();
	}
	public List<Goods> getGoodsList(){
		return goodsList;
	}
}
