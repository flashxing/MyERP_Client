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
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.GoodsCategoryPanel;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.SetupGoodsControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsCategory;
import com.njue.mis.model.SetupGoods;
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
	public String[] titles = {"商品编码","商品名称","型号","规格","数量"};
	private String[] fields = {"productCode","goodsName","description","size","number"};
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JScrollPane goodScrollPane;
	private JButton commitButton;
	private JComboBox<StoreHouse> storeHouseComboBox;
	private StoreHouseControllerInterface storeHouseService;
	private List<StoreHouse> storeHouseList;
	private SetupGoodsControllerInterface setupGoodsService;
	private List<SetupGoods> setupGoodsList;
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
        goodsPanel.add(commitButton, BorderLayout.SOUTH);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(goodsCategoryPanel, BorderLayout.WEST);
        panel.add(goodsPanel, BorderLayout.CENTER);
        this.getContentPane().add(panel);
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
			if (arg0.getClickCount() != 2){
				return;
			}
			if(clickNode.isLeaf()){
				int cateId = selected.getCate_id();
				try{
					goodsList = goodsService.getAllGoodsByCateId(cateId);
					if(goodsList != null){
						List<String> goodsIdList = new ArrayList<>();
						for(Goods goods: goodsList){
							goodsIdList.add(goods.getId());
						}
						if(storeHouseComboBox.getSelectedItem() == null){
							CommonUtil.showError("请先选择一个仓库");
						}
						int shId = ((StoreHouse) storeHouseComboBox.getSelectedItem()).getId();
						setupGoodsList = setupGoodsService.getAllSetupGoods(goodsIdList, shId);
						CommonUtil.updateJTable(table, titles, fields, goodsList.toArray(), setupGoodsList.toArray());
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
					System.out.println("content is "+table.getValueAt(i, table.getColumnCount()-1));
					number = Integer.parseInt(table.getValueAt(i, table.getColumnCount()-1).toString());
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
