package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;
import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerButton;
import com.njue.mis.common.CustomerUtil;
import com.njue.mis.common.DateChooserJButton;
import com.njue.mis.common.GoodsButton;
import com.njue.mis.common.SalesManButton;
import com.njue.mis.interfaces.PortControllerInterface;
import com.njue.mis.interfaces.ReceiptControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Customer;
import com.njue.mis.model.Port;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.Receipt;
import com.njue.mis.model.ReceiptIn;
import com.njue.mis.model.ReceiptOut;
import com.njue.mis.model.Sales;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;
import com.njue.mis.model.ShowObject;
import com.njue.mis.model.StoreHouse;

public class BusinessProcessFrame extends JInternalFrame
{
	/**
	 * 
	 */
	protected String[] columns={"时间","类型","客户","数量","金额","操作人"};
	protected String[] fields={"time","type","customerName","number","totalPrice","operator"};
	private List<ShowObject> showList = new ArrayList<>();
	private static final long serialVersionUID = 7608328935392470145L;
	protected List<Category> list = new ArrayList<Category>();
	protected JScrollPane scrollPane;
    protected JTree simpleTree;
    protected DefaultMutableTreeNode clickNode;
    protected DefaultMutableTreeNode root;
    protected JPanel panel;
    protected Container pane = this.getContentPane();
	private JScrollPane goodScrollPane;

    protected JTable table;
    protected String[] objects;
    protected String[] fieldsToShow;
    protected Object[] initList = {};
    protected ImageIcon image;
    
    protected DateChooserJButton beginButton;
    protected DateChooserJButton endButton;
    protected GoodsButton goodsButton;
    protected CustomerButton customerButton;
    protected SalesManButton salesManButton;
    protected JComboBox<StoreHouse> storeHouseComboBox;
    protected JTextField operator;
    protected JTextField comment;
    protected JButton searchButton;
    protected JButton showButton;
    
    protected StoreHouseControllerInterface storeHouseService;
    protected List<StoreHouse> storeHouseList;
    
    protected SalesControllerInterface salesService;
    protected PortControllerInterface portService;
    protected ReceiptControllerInterface receiptService;
    private List<Sales> salesList;
    private List<PortIn> portInList;
    private List<ReceiptIn> receiptInList;
    private List<ReceiptOut> receiptOutList;
	public BusinessProcessFrame()
	{
		super("经营历程",true,true,true,true);
		init();
    }
	
	protected void enableButton(){
	
	}
	protected void enableAllButAddButton(){
	
	}
	
	protected void disableButton(){
	}
	public void init()
	{
		salesService = RemoteService.salesService;
		portService = RemoteService.portService;
		receiptService = RemoteService.receiptService;
		JLabel beginLabel = new JLabel("开始时间");
		beginButton = new DateChooserJButton();
		JLabel endLabel = new JLabel("截止时间");
		endButton = new DateChooserJButton();
		JLabel goodsLabel = new JLabel("商品");
		goodsButton = new GoodsButton("。。。");
		JLabel customerLabel = new JLabel("往来单位");
		customerButton = new CustomerButton("。。。");
		JLabel salesManLabel = new JLabel("业务员");
		salesManButton = new SalesManButton("。。。");
		JLabel storeHouseLabel = new JLabel("库房");
		storeHouseService = RemoteService.storeHouseService;
		storeHouseList = new ArrayList<>();
		try {
			storeHouseList = storeHouseService.getAll();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("网络错误");
		}
		storeHouseComboBox = new JComboBox<StoreHouse>();
		storeHouseComboBox.setModel(new DefaultComboBoxModel<StoreHouse>());
		for(StoreHouse storeHouse: storeHouseList){
			storeHouseComboBox.addItem(storeHouse);
		}
		JLabel operatorLabel = new JLabel("经手人");
		operator = new JTextField(10);
		JLabel commentLabel = new JLabel("备注");
		comment = new JTextField(10);
		searchButton = new JButton("查询");
		searchButton.addActionListener(new QueryAction());
		JPanel northPanel = new JPanel(new BorderLayout());
		JPanel northPanel1 = new JPanel();
		JPanel northPanel2 = new JPanel();
		northPanel.add(northPanel1, BorderLayout.NORTH);
		northPanel.add(northPanel2, BorderLayout.SOUTH);
		northPanel1.add(beginLabel);
		northPanel1.add(beginButton);
		northPanel1.add(endLabel);
		northPanel1.add(endButton);
		northPanel1.add(goodsLabel);
		northPanel1.add(goodsButton);
		northPanel1.add(customerLabel);
		northPanel1.add(customerButton);
		northPanel1.add(salesManLabel);
		northPanel1.add(salesManButton);
		northPanel2.add(storeHouseLabel);
		northPanel2.add(storeHouseComboBox);
		northPanel2.add(operatorLabel);
		northPanel2.add(operator);
		northPanel2.add(commentLabel);
		northPanel2.add(comment);
		northPanel2.add(searchButton);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension(screenSize.width * 2 / 3,
				screenSize.height * 4/7));
		root = buildTree();
        simpleTree = new JTree(root);
        simpleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        simpleTree.setRootVisible(true);
        simpleTree.setShowsRootHandles(true);
        simpleTree.putClientProperty("JTree.lineStyle", "Angled");
        simpleTree.setEditable( false );
        simpleTree.setMaximumSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height  / 80));

        scrollPane = new JScrollPane( simpleTree );
        scrollPane.setPreferredSize(new Dimension(screenSize.width * 1 / 8,
				screenSize.height *1/80));
        panel = new JPanel ();
        
        disableButton();

        table = new JTable();
        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 4,
				screenSize.height  / 8));
        goodScrollPane = new JScrollPane();
        goodScrollPane.setViewportView(table);
        goodScrollPane.setPreferredSize(new Dimension(screenSize.width * 2 / 3,
				screenSize.height * 1/8));
        
        JPanel southPanel = new JPanel();
        showButton = new JButton("查看");
        showButton.addActionListener(new ShowAction());
        southPanel.add(showButton);
        this.getContentPane().add(northPanel, BorderLayout.NORTH);
        this.getContentPane().add(goodScrollPane, BorderLayout.CENTER);
        
        this.getContentPane().add(scrollPane,BorderLayout.WEST);
        this.getContentPane().add(southPanel, BorderLayout.SOUTH);
	}
	private void createNode(DefaultMutableTreeNode node, String[] list){
		for(String str: list){
			node.add(new DefaultMutableTreeNode(str));
		}
	}
	private DefaultMutableTreeNode buildTree(){
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("所有单据");
        DefaultMutableTreeNode sales = new DefaultMutableTreeNode("销售类单据");
        String[] salesList = {"销售单","销售退货单"};
        DefaultMutableTreeNode port = new DefaultMutableTreeNode("进货类单据");
        String[] portList = {"进货单","进货退货单"};
        DefaultMutableTreeNode receipt = new DefaultMutableTreeNode("财务类单据");
        String[] receiptList = {"付款单","收款单","现金费用单"};
        DefaultMutableTreeNode stock = new DefaultMutableTreeNode("库存类单据");
        String[] stockList ={"报损单","报溢单","赠送单","受赠单"};
        root.add(sales);
        createNode(sales,salesList);
        root.add(port);
        createNode(port, portList);
        root.add(receipt);
        createNode(receipt, receiptList);
        root.add(stock);
        createNode(stock, stockList);
        return root;
    }
	private class QueryAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String begin = beginButton.getDateString();
			String end = endButton.getDateString();
			String customerId = null;
			if(customerButton.getCustomer() != null){
				customerId = customerButton.getCustomer().getId();
			}
			String goodsId = null;
			if(goodsButton.getGoods() != null){
				goodsId = goodsButton.getGoods().getId();
			}
			String salesMan = null;
			if(salesManButton.getSalesMan() != null){
				salesMan = salesManButton.getSalesMan().getName();
			}
			int shId = 0;
			shId = ((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
			showList.clear();
			String operatorName = null;
			if(operator.getText()!=null && !operator.getText().equals("")){
				operatorName = operator.getText();
			}
			String commentString = null;
			if(comment.getText() != null && !comment.getText().equals("")){
				commentString = comment.getText();
			}
			try {
				String nodeName = null;
				if(simpleTree.getLastSelectedPathComponent() != null){
					nodeName = simpleTree.getLastSelectedPathComponent().toString();
				}
				System.out.println(nodeName);
				//销售单
				if(nodeName == null || nodeName.endsWith("所有单据")||nodeName.endsWith("销售类单据")||nodeName.endsWith("销售单")){
					salesList = salesService.searchSales(begin, end, customerId, goodsId, salesMan, shId, operatorName, commentString, 1);
					if(salesList != null){
						List<String> customerIds = new ArrayList<>();
						for(Sales sales : salesList){
							customerIds.add(sales.getCustomerId());
						}
						Map<String, Customer> customerMap = CustomerUtil.getCustomers(customerIds);
						for(Sales sales:salesList){
							if(null != goodsId){
								boolean hasGoods=false;
								for(SalesGoodsItem item:sales.getGoodsItemsList()){
									if(item.getGoodsId().equals(goodsId)){
										hasGoods = true;
										break;
									}
								}
								if(!hasGoods){
									continue;
								}
							}
							if(customerMap.containsKey(sales.getCustomerId())){
								showList.add(new ShowObject(sales.id, sales.getTime(),"销售单", customerMap.get(sales.getCustomerId()).getName(), sales.getNumber(), sales.getTotalPrice(), sales.getOperatePerson()));
							}
						}
					}
				}
				//进货单
				if(null==nodeName||nodeName.equals("进货类单据")||nodeName.equals("所有单据")||nodeName.equals("进货单")){
					portInList = portService.searchPortInByTime(begin, end);
					if(null != portInList){
						List<String> customerIds = new ArrayList<>();
						for(PortIn portIn: portInList){
							customerIds.add(portIn.getCustomerId());
						}
						Map<String, Customer> customerMap = CustomerUtil.getCustomers(customerIds);
						for(PortIn portIn:portInList){
							if(customerMap.containsKey(portIn.getCustomerId())){
								showList.add(new ShowObject(portIn.getId(), portIn.getTime(),"进货单", customerMap.get(portIn.getCustomerId()).getName(), portIn.getNumber(), portIn.getPrice(), portIn.getOperatePerson()));
							}
						}
					}
				}
				//收款单
				if(isNode(nodeName, "所有单据","财务类单据","收款单")){
					receiptInList = receiptService.getAllReceiptInByTime(begin, end);
					if(null != receiptInList){
						List<String> customerIds = new ArrayList<>();
						for(ReceiptIn receiptIn : receiptInList){
							customerIds.add(receiptIn.getCustomerId());
						}
						Map<String, Customer> customerMap = CustomerUtil.getCustomers(customerIds);
						for(ReceiptIn receiptIn:receiptInList){
							if(customerMap.containsKey(receiptIn.getCustomerId())){
								showList.add(new ShowObject(receiptIn.getId(), receiptIn.getTime(), "收款单", customerMap.get(receiptIn.getCustomerId()).getName(), 0, receiptIn.getMoney(), receiptIn.getOperator()));
							}
						}
					}
				}
				//付款单
				if(isNode(nodeName, "所有单据","财务类单据","付款单")){
					receiptOutList = receiptService.getAllReceiptOutByTime(begin, end);
					if(null != receiptOutList){
						List<String> customerIds = new ArrayList<>();
						for(ReceiptOut receiptOut : receiptOutList){
							customerIds.add(receiptOut.getCustomerId());
						}
						Map<String, Customer> customerMap = CustomerUtil.getCustomers(customerIds);
						for(ReceiptOut receiptOut:receiptOutList){
							if(customerMap.containsKey(receiptOut.getCustomerId())){
								showList.add(new ShowObject(receiptOut.getId(), receiptOut.getTime(), "付款单", customerMap.get(receiptOut.getCustomerId()).getName(), 0, receiptOut.getMoney(), receiptOut.getOperator()));
							}
						}
					}
				}
				//现金费用单
				if(isNode(nodeName, "所有单据","财务类单据","现金费用单")){
					
				}
				CommonUtil.updateJTable(table, columns, showList.toArray(), fields);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
	}
	private boolean isNode(String nodeName, String... strings){
		if(null == nodeName){
			return true;
		}
		for(String str:strings){
			if(nodeName.equals(str)){
				return true;
			}
		}
		return false;
	}
	private class ShowAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			ShowObject showObject = getSelectedItem();
			if(showObject==null){
				CommonUtil.showError("请选择要查看的单子");
				return ;
			}
			deal(showObject.getType(), showObject.getId());

		}
		public void deal(String type, String id){
			switch (type) {
			case "销售单":
				Sales sales = null;
				Iterator<Sales> iterator = salesList.iterator();
				while(iterator.hasNext()){
					Sales tmpSales = iterator.next();
					if(tmpSales.getId().equals(id)){
						sales = tmpSales;
					}
				}
				if(sales != null){
					SalesDraftEditFrame salesDraftEditFrame = new SalesDraftEditFrame((SalesIn) sales);
					MainFrame.getMainFrame().getContentPane().add(
							salesDraftEditFrame);
					salesDraftEditFrame.setVisible(true);
					salesDraftEditFrame.setUnable();
				}
				break;
			case "进货单":
				PortIn port = null;
				Iterator<PortIn> portInIterator = portInList.iterator();
				while(portInIterator.hasNext()){
					PortIn tmpPortIn = portInIterator.next();
					if(tmpPortIn.getId().equals(id)){
						port = tmpPortIn;
					}
				}
				if(port != null){
					InportViewFrame inportViewFrame = new InportViewFrame(port);
//					SalesDraftEditFrame salesDraftEditFrame = new SalesDraftEditFrame((SalesIn) sales);
					MainFrame.getMainFrame().getContentPane().add(
							inportViewFrame);
					inportViewFrame.setVisible(true);
				}
				break;
			case "收款单":
				Receipt receiptIn = null;
				Iterator<ReceiptIn> receiptIterator = receiptInList.iterator();
				while(receiptIterator.hasNext()){
					ReceiptIn tmpReceiptIn = receiptIterator.next();
					if(tmpReceiptIn.getId().equals(id)){
						receiptIn = tmpReceiptIn;
					}
				}
				if(receiptIn != null){
					ReceiptFrame receiptFrame = new ReceiptFrame("收款单");
					MainFrame.getMainFrame().getContentPane().add(receiptFrame);
					receiptFrame.initWithReceipt(receiptIn);
					receiptFrame.setVisible(true);
				}
				break;
			case "付款单":
				Receipt receiptOut = null;
				Iterator<ReceiptOut> receiptOutIterator = receiptOutList.iterator();
				while(receiptOutIterator.hasNext()){
					ReceiptOut tmpReceiptOut = receiptOutIterator.next();
					if(tmpReceiptOut.getId().equals(id)){
						receiptOut = tmpReceiptOut;
					}
				}
				if(receiptOut != null){
					ReceiptFrame receiptFrame = new ReceiptFrame("收款单");
					MainFrame.getMainFrame().getContentPane().add(receiptFrame);
					receiptFrame.initWithReceipt(receiptOut);
					receiptFrame.setVisible(true);
				}
				break;
			case "现金单据":
				
			
			default:
				break;
			}
		}
	}
	
	private ShowObject getSelectedItem(){
		ShowObject selected = null;
		int index = table.getSelectedRow();
		if(showList==null||index <0 || index>=showList.size()){
			return null;
		}else{
			selected = showList.get(index);
		}
		return selected;
	}
}
