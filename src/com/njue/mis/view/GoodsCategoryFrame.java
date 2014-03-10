package com.njue.mis.view;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsCategory;

public class GoodsCategoryFrame extends CategoryFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1446389238639775488L;
	private CategoryControllerInterface categoryService;
	private GoodsControllerInterface goodsService;
	private List<Goods> goodsList;
	public GoodsCategoryFrame(List<Category> list)
	{
		super("商品管理",list);
		image = new ImageIcon("images/goods.png");
		image.setImage(image.getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT));
		initService();
		System.out.println("Finish the init service");
		this.init(list);
    }
	public void initService(){
		try {
			categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
			goodsService = (GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	public void init(List<Category> list)
	{
		objects = new String[]{
				"商品编码","商品名称","型号","规格","数量","进价","零售价","最近销售价","最近进价"
		};
		fieldsToShow = new String[]{
				"productCode","goodsName","description","size","goodsNum","price","salesPrice","lastSalePrice","lastStockPrice"
		};
		super.init(list);
		addGoodsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if((clickNode != null)&&(!clickNode.isLeaf())||(selected == null)){
					CommonUtil.showError("请选择一个分类再添加商品");
					return;
				}
				GoodsFrame goodsFrame = new GoodsFrame(selected.getCate_id());
				MainFrame.getMainFrame().getContentPane().add(goodsFrame);
				goodsFrame.setVisible(true);
				int index = table.getSelectedRow();
				if(index<0||goodsList==null||index>=goodsList.size()||goodsList.get(index)==null){
				}else{
					goodsFrame.initWithGoods(goodsList.get(index));
				}
			}
		});		
		updateEntityButton.setText("更新商品");
		updateEntityButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int index = table.getSelectedRow();
				if(index<0||goodsList==null||index>=goodsList.size()||goodsList.get(index)==null){
					CommonUtil.showError("请选择一个商品!");
					return;
				}else{
					GoodsFrame goodsFrame = new GoodsFrame(goodsList.get(index));
					MainFrame.getMainFrame().getContentPane().add(goodsFrame);
					goodsFrame.setVisible(true);
				}
			}
		});
		deleteEntityButton.setText("删除商品");
		deleteEntityButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				int confirm = JOptionPane.showConfirmDialog(null, "确认删除？");
				if(JOptionPane.YES_OPTION != confirm){
					return;
				}
				int index = table.getSelectedRow();
				if(index<0||goodsList==null||index>=goodsList.size()||goodsList.get(index)==null){
					CommonUtil.showError("请选择一个商品!");
					return;
				}else{
					try {
						if(!goodsService.deleteGoods(goodsList.get(index))){
							CommonUtil.showError("商品不能被删除");
							return;
						}else{
							goodsList.remove(index);
							CommonUtil.showError("删除成功");
							rePaintTable();
						}
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		});
        addButton.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent e){
                TreePath parentPath = simpleTree.getSelectionPath();
                if(insertField.getText().length() <1){
                	CommonUtil.showError("请输入分类名称");
                	return;
                }
                toAdd = new GoodsCategory();
                toAdd.setCate_name(insertField.getText());
                DefaultMutableTreeNode parentNode = null;
                if( parentPath != null ){
                    parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
                    selected = (Category) parentNode.getUserObject();
                }else{
                	selected = new Category();
                	selected.setCate_id(0);
                }
                toAdd.setPrefer_id(selected.getCate_id());
                try {
    				if (categoryService.categoryHasGoods(selected.getCate_id())){
    					CommonUtil.showError("不能在该分类下添加子分类，该分类下有商品存在");
    					return;
    				}
    				toAdd.setCate_id(categoryService.addCategory(toAdd));
    				insertField.setText("");
    			} catch (RemoteException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("网络连接错误");
    			}
                DefaultTreeModel model = (DefaultTreeModel)simpleTree.getModel();
                DefaultMutableTreeNode child = new DefaultMutableTreeNode( toAdd );
                if (parentNode != null){
                	model.insertNodeInto( child , parentNode,0 );
                }else{
                	model.insertNodeInto(child, root, 0);
                }
                simpleTree.scrollPathToVisible( new TreePath( child.getPath() ) ); 
            }
        });
        updateButton.addActionListener(new UpdateActionListener());
        removeButton.addActionListener(new DeleteActionListener());
        simpleTree.addMouseListener(new ClickNodeActionListener());
        searchButton.addActionListener(new SearchAction());
	}
	class SearchAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			String goodsName = searchField.getText();
			if(goodsName.length()<=0){
				CommonUtil.showError("搜索关键词不能为空");
				return;
			}
			try {
				goodsList = goodsService.getAllGoodsByGoodsName(goodsName);
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			CommonUtil.updateJTable(table, objects, goodsList.toArray(), fieldsToShow);
		}
		
	}
	class DeleteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			int confirm = JOptionPane.showConfirmDialog(null, "确认删除？");
			if(JOptionPane.YES_OPTION != confirm){
				return;
			}
			if((clickNode == null)|| (!clickNode.isLeaf())){
				CommonUtil.showError("请选择一个没有下一级的分类");
				return;
			}
			toDelete = (GoodsCategory) clickNode.getUserObject();
            try {
				if (categoryService.categoryHasGoods(toDelete.getCate_id())){
					CommonUtil.showError("不能删除该分类,该分类还有商品存在");
					return;
				}
				categoryService.delCategory(toDelete);
				clickNode.removeFromParent();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CommonUtil.showError("网络连接错误");
			}
            DefaultTreeModel model = (DefaultTreeModel)simpleTree.getModel();
            model.reload();
		}
    }
    
    class UpdateActionListener implements ActionListener{
    	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			TreePath currentPath = simpleTree.getSelectionPath();
            if((currentPath == null) || (updateField.getText().length() <1)){
            	CommonUtil.showError("请输入分类名称并选择更新的分类");
            	return;
            }
            if( currentPath != null ){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)currentPath.getLastPathComponent();
                toUpdate = (GoodsCategory) node.getUserObject();
                String tmpCateName = toUpdate.getCate_name();
                toUpdate.setCate_name(updateField.getText());
                try {
    				if(!categoryService.updateCategory(toUpdate)){
    					toUpdate.setCate_name(tmpCateName);
    					CommonUtil.showError("更新失败");
    				}
    			} catch (RemoteException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("网络连接错误");
    			}
                DefaultTreeModel model = (DefaultTreeModel)simpleTree.getModel();
                model.reload();
                simpleTree.scrollPathToVisible(currentPath);
            }
		}
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
				updateField.setText(selected.getCate_name());
				enableButton();
			}
			if (arg0.getClickCount() != 2){
				return;
			}
			if(clickNode.isLeaf()){
				int cateId = selected.getCate_id();
				try{
					goodsList = goodsService.getAllGoodsByCateId(cateId);
					if(goodsList != null){
						CommonUtil.updateJTable(table, objects, goodsList.toArray(), fieldsToShow);
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
    
    public void rePaintTable(){
		if(goodsList != null){
			CommonUtil.updateJTable(table, objects, goodsList.toArray(), fieldsToShow);
		};
    }
}
