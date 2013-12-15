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
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Customer;
import com.njue.mis.model.CustomerCategory;
import com.njue.mis.model.CustomerMoney;

public class CustomerCategoryFrame extends CategoryFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6208433914029145693L;
	private CategoryControllerInterface categoryService;
	private CustomerControllerInterface customerService;
	public CustomerCategoryFrame(List<Category> list)
	{
		super("�ͻ�����",list);
		image = new ImageIcon("images/customer.png");
		image.setImage(image.getImage().getScaledInstance(20,20,Image.SCALE_DEFAULT));
		init(list);
    }
	public void init(final List<Category> list)
	{
		try {
			customerService = (CustomerControllerInterface) Naming.lookup(Configure.CustomerController);
			categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
		} catch (MalformedURLException | RemoteException | NotBoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		objects = new String[]{
				"�ͻ����","�ͻ�����","����","�绰","��ַ","Ӧ��","Ӧ��"
		};
		fieldsToShow = new String[]{
				"id","name","email","telephone","address","give","receive"
		};
		super.init(list);
		addGoodsButton.setText("��ӿͻ�");
		addGoodsButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				if((clickNode != null)&&(!clickNode.isLeaf())||(selected==null)){
					CommonUtil.showError("��ѡ��һ����������ӿͻ�");
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
                	CommonUtil.showError("�������������");
                	return;
                }
                toAdd = new CustomerCategory();
                toAdd.setCate_name(insertField.getText());
                DefaultMutableTreeNode parentNode = null;
                if( parentPath != null ){
                    parentNode = (DefaultMutableTreeNode)parentPath.getLastPathComponent();
                    selected = (CustomerCategory) parentNode.getUserObject();
                }else{
                	selected = new Category();
                	selected.setCate_id(0);
                }
                toAdd.setPrefer_id(selected.getCate_id());
                try {
    				if (categoryService.categoryHasCustomer(selected.getCate_id())){
    					CommonUtil.showError("�����ڸ÷�����������࣬�÷����´��ڿͻ�");
    					return;
    				}
    				toAdd.setCate_id(categoryService.addCategory(toAdd));
    				insertField.setText("");
    			} catch (RemoteException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("�������Ӵ���");
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
				CommonUtil.showError("��ѡ��һ��û����һ���ķ���");
				return;
			}
			toDelete = (CustomerCategory) clickNode.getUserObject();
            try {
				if (categoryService.categoryHasGoods(toDelete.getCate_id())){
					CommonUtil.showError("����ɾ���÷���,�÷��໹�пͻ�����");
					return;
				}
				categoryService.delCategory(toDelete);
				clickNode.removeFromParent();
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CommonUtil.showError("�������Ӵ���");
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
            	CommonUtil.showError("������������Ʋ�ѡ����µķ���");
            	return;
            }
            if( currentPath != null ){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode)currentPath.getLastPathComponent();
                toUpdate = (CustomerCategory) node.getUserObject();
                String tmpCateName = toUpdate.getCate_name();
                toUpdate.setCate_name(updateField.getText());
                try {
    				if(!categoryService.updateCategory(toUpdate)){
    					toUpdate.setCate_name(tmpCateName);
    					CommonUtil.showError("����ʧ��");
    				}
    			} catch (RemoteException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("�������Ӵ���");
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
					Vector<Customer> customerList = customerService.getAllCustomerByCateId(cateId);
					Vector<CustomerMoney> customerMoneyList = new Vector<>();
					for(Customer customer:customerList){
						if(customer.getCustomerMoney() == null){
							customerMoneyList.add(new CustomerMoney(customer.getId(), 0 , 0));
						}else{
							customerMoneyList.add(customer.getCustomerMoney());
						}
					}
					if(customerList != null){
						CommonUtil.updateJTable(table, objects, fieldsToShow, customerList.toArray(), customerMoneyList.toArray());
					}
					table.repaint();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("�������Ӵ���");
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
