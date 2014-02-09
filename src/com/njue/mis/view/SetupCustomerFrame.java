package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.CustomerCategoryPanel;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.SetupCustomerControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Customer;
import com.njue.mis.model.CustomerCategory;
import com.njue.mis.model.SetupCustomer;

public class SetupCustomerFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1446389238639775488L;
	private CustomerCategoryPanel customerCategoryPanel;
	private CustomerControllerInterface customerService;
	private List<Customer> customerList;
	private JTree simpleTree;
    private DefaultMutableTreeNode clickNode;
    private Category selected;
	private JTable table;
	public String[] titles = {"商品名称","联系方式","应付","应收"};
	private String[] fields = {"name","telephone","toGive","toReceive"};
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JScrollPane customerScrollPane;
	private JButton commitButton;
	private SetupCustomerControllerInterface setupCustomerService;
	private List<SetupCustomer> setupCustomerList;
	public SetupCustomerFrame()
	{
		super("客户期初建账",true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		initService();
		this.init();
    }
	
	public void initService(){
		customerService = RemoteService.customerService;
		setupCustomerService = RemoteService.setupCustomerService;
	}
	
	public void init()
	{
		customerCategoryPanel = new CustomerCategoryPanel();
		simpleTree = customerCategoryPanel.getCategoryTree();
		simpleTree.addMouseListener(new ClickNodeActionListener());
		table = new JTable();
        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 4,
				screenSize.height  / 8));
        customerScrollPane = new JScrollPane();
        customerScrollPane.setViewportView(table);
        customerScrollPane.setPreferredSize(new Dimension(screenSize.width * 2 / 3,
				screenSize.height * 1/8));
        JPanel goodsPanel = new JPanel(new BorderLayout());
        commitButton = new JButton("修改");
        commitButton.addActionListener(new AddAction());
        goodsPanel.add(customerScrollPane, BorderLayout.CENTER);
        goodsPanel.add(commitButton, BorderLayout.SOUTH);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(customerCategoryPanel, BorderLayout.WEST);
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
			selected = (CustomerCategory) clickNode.getUserObject();
			if(selected != null){
			}
			if (arg0.getClickCount() != 2){
				return;
			}
			if(clickNode.isLeaf()){
				int cateId = selected.getCate_id();
				try{
					customerList = customerService.getAllCustomerByCateId(cateId);
					if(customerList != null){
						List<String> customerIdList = new ArrayList<>();
						for(Customer customer: customerList){
							customerIdList.add(customer.getId());
						}
						setupCustomerList = setupCustomerService.getAllSetupCustomer(customerIdList);
						CommonUtil.updateJTable(table, titles, fields, customerList.toArray(), setupCustomerList.toArray());
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
				double toGive = CommonUtil.formateDouble(table.getValueAt(i, table.getColumnCount()-2).toString());
				double toRecieve = CommonUtil.formateDouble(table.getValueAt(i, table.getColumnCount()-1).toString());
				if(toGive<0 || toRecieve < 0){
					CommonUtil.showError("第"+i+"行的应收应付必须为数字");
					return;
				}
				System.out.println("To give is :"+toGive+" and to receive is "+toRecieve);
				setupCustomerList.get(i).setToGive(toGive);
				setupCustomerList.get(i).setToReceive(toRecieve);
			}
			try {
				if(!setupCustomerService.saveOrUpdateSetupCustomerList(setupCustomerList)){
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
