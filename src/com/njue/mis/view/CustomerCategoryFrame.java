package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.OperatorControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Customer;
import com.njue.mis.model.CustomerCategory;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsCategory;
import com.njue.mis.view.GoodsCategoryFrame.ClickNodeActionListener;
import com.njue.mis.view.GoodsCategoryFrame.DeleteActionListener;
import com.njue.mis.view.GoodsCategoryFrame.UpdateActionListener;

public class CustomerCategoryFrame extends CategoryFrame
{
	public CustomerCategoryFrame(List<Category> list)
	{
		super("客户管理",list);
		init(list);
    }
	public void init(final List<Category> list)
	{
		objects = new String[]{
				"客户编号","客户姓名"
		};
		fieldsToShow = new String[]{
				"id","name"
		};
		super.init(list);
		addGoodsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if((clickNode != null)&&(!clickNode.isLeaf())){
					CommonUtil.showError("请选择一个分类再添加商品");
					return;
				}
				CustomerFrame customerFrame = new CustomerFrame(selected.getCate_id());
				MainFrame.getMainFrame().getContentPane().add(customerFrame);
				customerFrame.setVisible(true);
			}
		});
        addButton.addActionListener( new ActionListener(){
            public void actionPerformed( ActionEvent e){
                TreePath parentPath = simpleTree.getSelectionPath();
                if(insertField.getText().length() <1){
                	CommonUtil.showError("请输入分类名称");
                	return;
                }
                toAdd = new CustomerCategory();
                toAdd.setCate_name(insertField.getText());
                DefaultMutableTreeNode parentNode = null;
                if( parentPath != null ){
                    parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
                    selected = (CustomerCategory) parentNode.getUserObject();
                    toAdd.setPrefer_id(selected.getCate_id());
                }
                try {
    				CategoryControllerInterface categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
    				if (categoryService.categoryHasGoods(toAdd.getCate_id())){
    					CommonUtil.showError("不能在该分类下添加子类，该分类下存在客户");
    					return;
    				}
    				toAdd.setCate_id(categoryService.addCategory(toAdd));
    			} catch (MalformedURLException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("网络连接错误");
    			} catch (RemoteException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("网络连接错误");
    			} catch (NotBoundException e1) {
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
	}
	class DeleteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if((clickNode == null)|| (!clickNode.isLeaf())){
				CommonUtil.showError("请选择一个没有下一级的分类");
				return;
			}
			toDelete = (CustomerCategory) clickNode.getUserObject();
            try {
				CategoryControllerInterface categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
				if (categoryService.categoryHasGoods(toDelete.getCate_id())){
					CommonUtil.showError("不能删除该分类,该分类还有商品存在");
					return;
				}
				categoryService.delCategory(toDelete);
				clickNode.removeFromParent();
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CommonUtil.showError("网络连接错误");
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CommonUtil.showError("网络连接错误");
			} catch (NotBoundException e1) {
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
            if((currentPath == null) || (insertField.getText().length() <1)){
            	CommonUtil.showError("请输入分类名称并选择更新的分类");
            	return;
            }
            if( currentPath != null ){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)currentPath.getLastPathComponent();
                toUpdate = (CustomerCategory) node.getUserObject();
                String tmpCateName = toUpdate.getCate_name();
                toUpdate.setCate_name(insertField.getText());
                try {
    				CategoryControllerInterface categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
    				if(!categoryService.updateCategory(toUpdate)){
    					toUpdate.setCate_name(tmpCateName);
    					CommonUtil.showError("更新失败");
    				}
    			} catch (MalformedURLException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("网络连接错误");
    			} catch (RemoteException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("网络连接错误");
    			} catch (NotBoundException e1) {
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
			selected = (CustomerCategory) clickNode.getUserObject();
			if (arg0.getClickCount() != 2){
				return;
			}
			if(clickNode.isLeaf()){
				int cateId = selected.getCate_id();
				try{
					CustomerControllerInterface customerService = (CustomerControllerInterface) Naming.lookup(Configure.CustomerController);
					Vector<Customer> customerList = customerService.getAllCustomerByCateId(cateId);
					if(customerList != null){
						System.out.println(customerList.toArray()[0].getClass());
						CommonUtil.updateJTable(table, objects, customerList.toArray(), fieldsToShow);
					}
					table.repaint();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络连接错误");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络连接错误");
				} catch (NotBoundException e1) {
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

}
