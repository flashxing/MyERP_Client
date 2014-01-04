package com.njue.mis.view;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.njue.mis.client.Configure;
import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.DateChooserJButton;
import com.njue.mis.common.GoodsCategoryPanel;
import com.njue.mis.common.GoodsItemPanel;
import com.njue.mis.common.SalesGoodsItemPanel;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.DiscountControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.GoodsItemControllerInterface;
import com.njue.mis.interfaces.PortControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.StockControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Discount;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsCategory;
import com.njue.mis.model.GoodsItem;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.Stock;
import com.njue.mis.model.StockManagerObject;
import com.njue.mis.model.StoreHouse;


public class StockManagerFrame extends JInternalFrame
{

	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = screenSize.width *2/3;
	private int height = screenSize.height *5/9;
	
	private GoodsCategoryPanel goodsCategoryPanel;
	protected DefaultMutableTreeNode root;
    protected JTree simpleTree;
    protected JScrollPane scrollPane;
    protected DefaultMutableTreeNode clickNode;
    private GoodsCategory selected;
    
    private JTable table;
    private String[] columns = {"商品名称","销售总数","销售均价","销售总价","进货总数","进货均价","进货总价","库存数量","库存均价","库存总额"};
    private String[] fields = {"goodsName","salesNumber","salesPrice",
    		"totalSalesMoney","portNumber","portPrice","totalPortMoney",
    		"stockNumber", "stockPrice", "stockMoney"};
    private JScrollPane goodScrollPane;
    

    
    private DateChooserJButton beginButton;
    private DateChooserJButton endButton;
    private JButton queryButton;
    
    private SalesControllerInterface salesService;
    private PortControllerInterface portService;
    private GoodsControllerInterface goodsService;
    private GoodsItemControllerInterface goodsItemService;
    private StockControllerInterface stockService;
    private List<Goods> goodsList;
    private List<SalesIn> salesInList;
    private List<PortIn> portInList;
	public StockManagerFrame()
	{
		super("库存管理",true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		init();
	}
	public void init(){
		stockService = RemoteService.stockService;
		salesService = RemoteService.salesService;
		portService = RemoteService.portService;
		goodsService = RemoteService.goodsService;
		goodsItemService = RemoteService.goodsItemService;
		initPanel();
	}
	
	public void initPanel(){
		JPanel panel = new JPanel(new BorderLayout());
		
        goodsCategoryPanel = new GoodsCategoryPanel();
        JPanel goodsPanel = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel();
        JPanel resultPanel = new JPanel();
        goodsPanel.add(searchPanel, BorderLayout.NORTH);
        goodsPanel.add(resultPanel, BorderLayout.CENTER);
        JLabel beginJLabel = new JLabel("起始时间:");
        JLabel endJLabel = new JLabel("截止时间:");
        beginButton = new DateChooserJButton();
        endButton = new DateChooserJButton();
        queryButton = new JButton("查询");
        queryButton.addActionListener(new QueryActionListener());
        searchPanel.add(beginJLabel);
        searchPanel.add(beginButton);
        searchPanel.add(endJLabel);
        searchPanel.add(endButton);
        searchPanel.add(queryButton);
        
        simpleTree = goodsCategoryPanel.getCategoryTree();
        simpleTree.addTreeSelectionListener(new TreeSelectionListener() {
			
			@Override
			public void valueChanged(TreeSelectionEvent arg0) {
				// TODO Auto-generated method stub
				root = ((DefaultMutableTreeNode) simpleTree.getLastSelectedPathComponent());
				selected = (GoodsCategory) root.getUserObject();
				if(selected == null){
					goodsList = null;
					return;
				}
				int cateId = selected.getCate_id();
				List<Integer> cateIdList = goodsCategoryPanel.getAllSubCateId(cateId);
				try{
					goodsList = goodsService.getAllGoodsByCateId(cateIdList);
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络连接错误");
				}
			}
		});
        table = new JTable();
        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 1 / 2,
				screenSize.height  *4/ 9));
        goodScrollPane = new JScrollPane();
        goodScrollPane.setViewportView(table);
        goodScrollPane.setPreferredSize(new Dimension(screenSize.width * 1 / 2,
				screenSize.height * 4/9));
        resultPanel.add(goodScrollPane);
        
        panel.add(goodsCategoryPanel, BorderLayout.WEST);
        panel.add(goodsPanel, BorderLayout.CENTER);
        
        getContentPane().add(panel);
	}
	
	private class QueryActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			String beginTime = beginButton.getDateString();
			String endTime = endButton.getDateString();
			if(goodsList == null){
				try {
					goodsList = goodsService.getAllGoods();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					CommonUtil.showError("网络错误");
					return;
				}
			}
			String[] goodsIds = new String[goodsList.size()];
			for(int i = 0; i < goodsList.size(); ++i){
				goodsIds[i] = goodsList.get(i).getId();
			}
			try {
				List<List<SalesGoodsItem>> salesInList = goodsItemService.getSalesGoodsItemByTimeAndGoods(beginTime, endTime, goodsIds);
				List<List<GoodsItem>> portList = goodsItemService.getPortGoodsItemByTimeAndGoods(beginTime, endTime, goodsIds);
				List<Stock> stockList = stockService.searchStocksByTime(goodsIds, endTime);
				List<StockManagerObject> stockManagerObjects = new ArrayList<>();
				for(int i = 0 ;i < goodsList.size(); ++i){
					String goodsName = goodsList.get(i).getGoodsName();
					int salesNumber =0;
					double salesPrice=0.0;
					double totalSalesMoney=0.0;
					for(SalesGoodsItem item:salesInList.get(i)){
						salesNumber += item.getNumber();
						totalSalesMoney += item.getUnitPrice()*item.getNumber();
					}
					salesPrice = totalSalesMoney/salesNumber;
					
					int portNumber =0;
					double portPrice=0.0;
					double totalPortMoney=0.0;
					for(GoodsItem item:portList.get(i)){
						portNumber += item.getNumber();
						totalPortMoney += item.getUnitPrice()*item.getNumber();
					}
					portPrice = totalPortMoney/portNumber;
					
					int stockNumber = stockList.get(i).getNumber();
					double stockPrice = stockList.get(i).getPrice();
					double stockMoney = stockNumber*stockPrice;
					System.out.println(stockNumber+" "+stockMoney);
					StockManagerObject object = new StockManagerObject(goodsName, salesNumber, salesPrice, totalSalesMoney,
							portNumber, portPrice, totalPortMoney, stockNumber, stockPrice, stockMoney);					
					stockManagerObjects.add(object);
				}
				CommonUtil.updateJTable(table, columns, stockManagerObjects.toArray(), fields);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
}
