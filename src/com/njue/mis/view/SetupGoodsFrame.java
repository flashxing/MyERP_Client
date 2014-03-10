package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.GoodsCategoryPanel;
import com.njue.mis.common.GoodsTotal;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.SetupGoodsControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Customer;
import com.njue.mis.model.CustomerTotal;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsCategory;
import com.njue.mis.model.SetupCustomer;
import com.njue.mis.model.SetupGoods;
import com.njue.mis.model.SetupGoodsModel;
import com.njue.mis.model.StoreHouse;

public class SetupGoodsFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1446389238639775488L;
	private GoodsCategoryPanel goodsCategoryPanel;
	private GoodsControllerInterface goodsService;
	private List<Goods> goodsList;
	private JTree simpleTree;
    private DefaultMutableTreeNode clickNode;
    private Category selected;
	private JTable table;
	private String[] titles = {"商品编码","商品名称","型号","规格","数量","单价","总额"};
	private String[] fields = {"productCode","goodsName","description","size","number","price","totalPrice"};
	private String[] totalTitles = {"分类名称","合计数量","合计金额"};
	private String[] totalFields = {"categoryName","totalNumber","totalMoney"};
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JScrollPane goodScrollPane;
	private JButton commitButton;
	private JComboBox<StoreHouse> storeHouseComboBox;
	private StoreHouseControllerInterface storeHouseService;
	private List<StoreHouse> storeHouseList;
	private SetupGoodsControllerInterface setupGoodsService;
	private List<SetupGoods> setupGoodsList;
	private List<SetupGoodsModel> setupGoodsModels = new ArrayList<>();
	private JTextField totalNumberField;
	private JTextField totalMoneyField;
	public SetupGoodsFrame()
	{
		super("商品期初建账",true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		initService();
		this.init();
    }
	
	public void initService(){
		goodsService = RemoteService.goodsService;
		setupGoodsService = RemoteService.setupGoodsService;
		storeHouseService = RemoteService.storeHouseService;
		try {
			storeHouseList = storeHouseService.getAll();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void init()
	{
		goodsCategoryPanel = new GoodsCategoryPanel();
		simpleTree = goodsCategoryPanel.getCategoryTree();
		simpleTree.addMouseListener(new ClickNodeActionListener());
		JPanel storeHousePanel = new JPanel();
		JLabel storeHouseLabel = new JLabel("仓库选择:");
		storeHouseComboBox = new JComboBox<StoreHouse>();
		StoreHouse[] storeHouses = new StoreHouse[storeHouseList.size()];
		for(int i=0; i<storeHouseList.size(); i++){
			storeHouses[i] = storeHouseList.get(i);
		}
		storeHouseComboBox.setModel(new javax.swing.DefaultComboBoxModel<StoreHouse>(storeHouses));
		if(storeHouses.length > 0){
			storeHouseComboBox.setSelectedIndex(0);
		}
		storeHouseComboBox.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if(goodsList != null){
					List<String> goodsIdList = new ArrayList<>();
					for(Goods goods: goodsList){
						goodsIdList.add(goods.getId());
					}
					int shId = ((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
					try {
						setupGoodsList = setupGoodsService.getAllSetupGoods(goodsIdList, shId);
						CommonUtil.updateJTable(table, titles, fields, goodsList.toArray(), setupGoodsList.toArray());
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		storeHousePanel.add(storeHouseLabel);
		storeHousePanel.add(storeHouseComboBox);
		table = new JTable();
        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 4,
				screenSize.height  / 8));
        goodScrollPane = new JScrollPane();
        goodScrollPane.setViewportView(table);
        goodScrollPane.setPreferredSize(new Dimension(screenSize.width * 2 / 3,
				screenSize.height * 1/8));
        JPanel goodsPanel = new JPanel(new BorderLayout());
        commitButton = new JButton("修改");
        commitButton.addActionListener(new AddAction());
        goodsPanel.add(storeHousePanel, BorderLayout.NORTH);
        goodsPanel.add(goodScrollPane, BorderLayout.CENTER);
        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel totalPanel = new JPanel();
        JLabel totalNumberLabel = new JLabel("总数量");
        totalNumberField = new JTextField(10);
        totalNumberField.setEditable(false);
        JLabel totalMoneyLabel = new JLabel("总价值");
        totalMoneyField = new JTextField(10);
        totalMoneyField.setEditable(false);
        totalPanel.add(totalNumberLabel);
        totalPanel.add(totalNumberField);
        totalPanel.add(totalMoneyLabel);
        totalPanel.add(totalMoneyField);
        southPanel.add(totalPanel, BorderLayout.NORTH);
        southPanel.add(commitButton, BorderLayout.SOUTH);
        goodsPanel.add(southPanel, BorderLayout.SOUTH);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(goodsCategoryPanel, BorderLayout.WEST);
        panel.add(goodsPanel, BorderLayout.CENTER);
        this.getContentPane().add(panel);
	}
	private void updateTotalPanel(){
		int totalNumber = 0;
		double totalMoney = 0;
		if(setupGoodsModels != null){
			for(SetupGoodsModel setupGoodsModel : setupGoodsModels){
				totalNumber += setupGoodsModel.getNumber();
				totalMoney += setupGoodsModel.getTotalPrice();
			}
		}
		totalNumberField.setText(totalNumber+"");
		totalMoneyField.setText(CommonUtil.formateDouble(totalMoney)+"");
	}
	
	private void resetTotalPanel(){
		totalMoneyField.setText("");
		totalNumberField.setText("");
	}
	class ClickNodeActionListener implements MouseListener{
		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			TreePath currentPath = simpleTree.getPathForLocation(arg0.getX(), arg0.getY());
			if (currentPath == null){
				return;
			}
			clickNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
			selected = (GoodsCategory) clickNode.getUserObject();
			if(selected != null){
			}
			if(arg0.getClickCount() == 1&& !clickNode.isLeaf()){
				int cateId = 0;
				if(selected != null){
					cateId = selected.getCate_id();
				}
				List<Integer> allCateIdList = goodsCategoryPanel.getAllSubCateId(cateId);
				List<Integer> cateIdList = goodsCategoryPanel.getSubCateId(cateId);
				try{
					System.out.println(cateIdList);
					goodsList = goodsService.getAllGoodsByCateId(allCateIdList);
					if(goodsList != null){
						setupGoodsModels.clear();
						List<String> goodsIdList = new ArrayList<>();
						for(Goods goods: goodsList){
							goodsIdList.add(goods.getId());
						}
						if(storeHouseComboBox.getSelectedItem() == null){
							CommonUtil.showError("请先选择一个仓库");
						}
						int shId = ((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
						setupGoodsList = setupGoodsService.getAllSetupGoods(goodsIdList, shId);
						for(int i = 0; i< setupGoodsList.size(); i++){
							Goods goods = goodsList.get(i);
							SetupGoods setupGoods = setupGoodsList.get(i);
							SetupGoodsModel setupGoodsModel = new SetupGoodsModel(goods.getProductCode(), goods.getGoodsName(), goods.getSize(),
									goods.getDescription(), setupGoods.getNumber(), goods.getPrice());
							setupGoodsModels.add(setupGoodsModel);
						}
						List<GoodsTotal> goodsTotals = new ArrayList<>();
						int allNumber = 0;
						double allMoney = 0;
						for(Integer tmpCateId: cateIdList){
							Category category = goodsCategoryPanel.getCategoryByCateId(tmpCateId);
							int totalNumber = 0;
							double totalMoney = 0;
							for(SetupGoods setupgoods: setupGoodsList){
								Goods myGoods = null;
								for(Goods tmpGoods: goodsList){
									if(tmpGoods.getId().equals(setupgoods.getGoodsId())){
										myGoods = tmpGoods;
										break;
									}
								}
								if(myGoods == null){
									continue;
								}
								if(goodsCategoryPanel.isChildren(category, myGoods.getCateId())){
									totalNumber += setupgoods.getNumber();
									totalMoney += CommonUtil.formateDouble(setupgoods.getNumber()*myGoods.getPrice());
								}
							}
							allNumber += totalNumber;
							allMoney += totalMoney;
							goodsTotals.add(new GoodsTotal(category.getCate_name(), totalNumber, totalMoney));
						}
						totalNumberField.setText(allNumber+"");
						totalMoneyField.setText(CommonUtil.formateDouble(allMoney)+"");
						CommonUtil.updateJTable(table, totalTitles, totalFields, goodsTotals.toArray());
//						table.getModel().addTableModelListener(new TableModelListener() {
//							
//							@Override
//							public void tableChanged(TableModelEvent arg0) {
//								TableModel model = table.getModel();
//								for(int i = 0; i < model.getRowCount(); ++i){
//									SetupGoodsModel setupGoodsModel = setupGoodsModels.get(i);
//									int number = Integer.parseInt(model.getValueAt(i, 4).toString());
//									double price = (Double) model.getValueAt(i, 5);
//									double totalPrice = (Double) model.getValueAt(i, 6);
//									System.out.println(i+" "+number+" "+price);
//									if(CommonUtil.formateDouble(price*number) != totalPrice){
//										model.setValueAt(CommonUtil.formateDouble(price*number), i, 6);
//										setupGoodsModel.setNumber(number);
//										setupGoodsModel.setTotalPrice(CommonUtil.formateDouble(number*price));
//									}
//								}
//								updateTotalPanel();
//							}
//						});
//						updateTotalPanel();
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络连接错误");
				}
			}
			if (arg0.getClickCount() != 2){
				return;
			}
			if(clickNode.isLeaf()){
				resetTotalPanel();
				int cateId = selected.getCate_id();
				try{
					goodsList = goodsService.getAllGoodsByCateId(cateId);
					if(goodsList != null){
						setupGoodsModels.clear();
						List<String> goodsIdList = new ArrayList<>();
						for(Goods goods: goodsList){
							goodsIdList.add(goods.getId());
						}
						if(storeHouseComboBox.getSelectedItem() == null){
							CommonUtil.showError("请先选择一个仓库");
						}
						int shId = ((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
						setupGoodsList = setupGoodsService.getAllSetupGoods(goodsIdList, shId);
						for(int i = 0; i< setupGoodsList.size(); i++){
							Goods goods = goodsList.get(i);
							SetupGoods setupGoods = setupGoodsList.get(i);
							SetupGoodsModel setupGoodsModel = new SetupGoodsModel(goods.getProductCode(), goods.getGoodsName(), goods.getSize(),
									goods.getDescription(), setupGoods.getNumber(), goods.getPrice());
							setupGoodsModels.add(setupGoodsModel);
						}
						CommonUtil.updateJTable(table, titles, fields, setupGoodsModels.toArray());
						table.getModel().addTableModelListener(new TableModelListener() {
							
							@Override
							public void tableChanged(TableModelEvent arg0) {
								TableModel model = table.getModel();
								for(int i = 0; i < model.getRowCount(); ++i){
									SetupGoodsModel setupGoodsModel = setupGoodsModels.get(i);
									int number = Integer.parseInt(model.getValueAt(i, 4).toString());
									double price = (Double) model.getValueAt(i, 5);
									double totalPrice = (Double) model.getValueAt(i, 6);
									System.out.println(i+" "+number+" "+price);
									if(CommonUtil.formateDouble(price*number) != totalPrice){
										model.setValueAt(CommonUtil.formateDouble(price*number), i, 6);
										setupGoodsModel.setNumber(number);
										setupGoodsModel.setTotalPrice(CommonUtil.formateDouble(number*price));
									}
								}
								updateTotalPanel();
							}
						});
						updateTotalPanel();
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络连接错误");
				}
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
    }
	
	private class AddAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			for(int i=0; i<table.getRowCount(); i++){
				int number = 0;
				try{
					System.out.println("content is "+table.getValueAt(i, 4));
					number = Integer.parseInt(table.getValueAt(i, 4).toString());
				}catch(Exception ex){
					CommonUtil.showError("第"+i+"行的数量必须为数字");
					return;
				}
				System.out.println(number);
				setupGoodsList.get(i).setNumber(number);
			}
			try {
				if(!setupGoodsService.saveOrUpdateSetupGoodsList(setupGoodsList)){
					CommonUtil.showError("更新失败");
				}else{
					CommonUtil.showError("更新成功");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("更新失败，网络错误");
			}
		}
		
	}
}
