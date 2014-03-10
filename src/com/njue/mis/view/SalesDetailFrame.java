package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.DateChooserJButton;
import com.njue.mis.common.GoodsCategoryPanel;
import com.njue.mis.common.MyExcel;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Goods;
import com.njue.mis.model.Sales;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.ShowSalesDetailObject;

public class SalesDetailFrame extends JInternalFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2304402452323171534L;
	Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private GoodsCategoryPanel goodsCategoryPanel;
	private DateChooserJButton beginButton;
	private DateChooserJButton endButton;
	private JButton queryButton;
	private CustomerButton customerButton;
	private JButton exportButton;
	private JTable table;
	private JScrollPane goodScrollPane;
	private List<Sales> salesInList;
	private SalesControllerInterface salesService;
	private GoodsControllerInterface goodsService;
	private TreeSet<ShowSalesDetailObject> treeSet;
	private String[] columns = {"时间","商品","型号","数量","单价","总价"};
	private String[] fields = {"time","name","description","number","price","money"};
	
	private Customer customer;
	public SalesDetailFrame(){
		super("销售明细",true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		init();
	}
	public void init(){
		salesService = RemoteService.salesService;
		goodsService = RemoteService.goodsService;
		treeSet = new TreeSet<>();
		JPanel panel = new JPanel(new BorderLayout());
		this.getContentPane().add(panel);
		JPanel westPanel = new JPanel();
		JPanel northPanel = new JPanel();
		JPanel centerPanel = new JPanel();
		JPanel southPanel = new JPanel();
		panel.add(westPanel, BorderLayout.WEST);
		panel.add(southPanel, BorderLayout.SOUTH);
		panel.add(northPanel, BorderLayout.NORTH);
		panel.add(centerPanel, BorderLayout.CENTER);
		goodsCategoryPanel = new GoodsCategoryPanel();
		westPanel.add(goodsCategoryPanel);
		beginButton = new DateChooserJButton();
		endButton = new DateChooserJButton();
		queryButton = new JButton("查询");
		queryButton.addActionListener(new QueryAction());
		customerButton = new CustomerButton("。。。");
		northPanel.add(customerButton);
		northPanel.add(beginButton);
		northPanel.add(endButton);
		northPanel.add(queryButton);
		exportButton = new JButton("导出数据");
		exportButton.addActionListener(new ShowAction());
		southPanel.add(exportButton);
		table = new JTable();
        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 4,
				screenSize.height *3/ 8));
        goodScrollPane = new JScrollPane();
        goodScrollPane.setViewportView(table);
        goodScrollPane.setPreferredSize(new Dimension(screenSize.width * 2 / 4,
				screenSize.height * 3/8));
        centerPanel.add(goodScrollPane);
		
	}
	private class QueryAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String begin = beginButton.getDateString();
			String end = endButton.getDateString();
			customer = customerButton.getCustomer();
			try {
				treeSet.clear();
				salesInList = salesService.getAllSalesInByTime(begin, end);
				if(null != salesInList){
					Set<String> goodIds = new HashSet<>();
					for(Sales sales: salesInList){
						for(SalesGoodsItem item:sales.getGoodsItemsList()){
							goodIds.add(item.getGoodsId());
						}
					}
					List<String> goodsIdList = new ArrayList<>(goodIds);
					List<Goods> goodsList = goodsService.getGoods(goodsIdList);
					Map<String, Goods> map = new HashMap<>();
					for(Goods goods: goodsList){
						map.put(goods.getId(), goods);
					}
					for(Sales sales: salesInList){
						for(SalesGoodsItem item: sales.getGoodsItemsList()){
							if(map.containsKey(item.getGoodsId())){
								Goods goods = map.get(item.getGoodsId());
								treeSet.add(new ShowSalesDetailObject(sales.getTime(), goods.getGoodsName(), goods.getDescription(), 
																		item.getNumber(), item.getUnitPrice(), CommonUtil.formateDouble(item.getTotalPrice())));
							}
						}
					}
					CommonUtil.updateJTable(table, columns, treeSet.toArray(), fields);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}
		
	}
	private class ShowAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String path = CommonUtil.chooseExportPath();
			if(null == path){
				return;
			}
			String[] ColunmNames={"time","name","description","number","price","money"};
			MyExcel myexcel=new MyExcel("test.xls");
			myexcel.generateFile(ColunmNames, new ArrayList<>(treeSet));
		}
		
	}
}
