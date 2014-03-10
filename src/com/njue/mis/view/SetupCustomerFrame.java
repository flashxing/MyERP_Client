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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
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
import com.njue.mis.model.CustomerTotal;
import com.njue.mis.model.SetupCustomer;
import com.njue.mis.model.SetupGoodsModel;

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
	private String[] totalTitles = {"分类名称","合计应付","合计应收"};
	private String[] totalFields = {"categoryName","totalGive","totalReceive"};
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private JScrollPane customerScrollPane;
	private JButton commitButton;
	private SetupCustomerControllerInterface setupCustomerService;
	private List<SetupCustomer> setupCustomerList;
	private JTextField totalGiveField;
	private JTextField totalReceiveField;
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
        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel totalPanel = new JPanel();
        JLabel totalGiveLabel = new JLabel("应付总额");
        totalGiveField = new JTextField(10);
        totalGiveField.setEditable(false);
        JLabel totalReceiveLabel = new JLabel("应收总额");
        totalReceiveField = new JTextField(10);
        totalReceiveField.setEditable(false);
        totalPanel.add(totalGiveLabel);
        totalPanel.add(totalGiveField);
        totalPanel.add(totalReceiveLabel);
        totalPanel.add(totalReceiveField);
        southPanel.add(totalPanel, BorderLayout.NORTH);
        southPanel.add(commitButton, BorderLayout.SOUTH);
        goodsPanel.add(southPanel, BorderLayout.SOUTH);
        goodsPanel.add(customerScrollPane, BorderLayout.CENTER);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(customerCategoryPanel, BorderLayout.WEST);
        panel.add(goodsPanel, BorderLayout.CENTER);
        this.getContentPane().add(panel);
	}
	private void updateTotalPanel(){
		double totalGive = 0;
		double totalReceive = 0;
		if(setupCustomerList != null){
			for(SetupCustomer setupCustomer: setupCustomerList){
				totalGive += setupCustomer.getToGive();
				totalReceive += setupCustomer.getToReceive();
			}
		}
		totalGiveField.setText(CommonUtil.formateDouble(totalGive)+"");
		totalReceiveField.setText(CommonUtil.formateDouble(totalReceive)+"");
	}
	private void resetTotalPanel(){
		totalGiveField.setText("");
		totalReceiveField.setText("");
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
			if(arg0.getClickCount() == 1 && !clickNode.isLeaf()){
				int cateId = 0;
				if(selected != null){
					cateId = selected.getCate_id();
				}
				List<Integer> allCateIdList = customerCategoryPanel.getAllSubCateId(cateId);
				List<Integer> cateIdList = customerCategoryPanel.getSubCateId(cateId);
				try{
					if(cateIdList.size() == 0){
						return;
					}
					customerList = customerService.getAllCustomersByCateId(allCateIdList);
					if(customerList != null){
						List<String> customerIdList = new ArrayList<>();
						for(Customer customer: customerList){
							customerIdList.add(customer.getId());
						}
						setupCustomerList = setupCustomerService.getAllSetupCustomer(customerIdList);
						List<CustomerTotal> customerTotals = new ArrayList<>();
						double totalGive = 0;
						double totalReceive = 0;
						for(Integer tmpCateId: cateIdList){
							Category category = customerCategoryPanel.getCategoryByCateId(tmpCateId);
							double toGive = 0;
							double toReceieve = 0;
							for(SetupCustomer setupCustomer: setupCustomerList){
								Customer myCustomer = null;
								for(Customer customer : customerList){
									if(customer.getId().equals(setupCustomer.getCustomerId())){
										myCustomer = customer;
										break;
									}
								}
								if(myCustomer == null){
									continue;
								}
								if(customerCategoryPanel.isChildren(category, myCustomer.getCateId())){
									toGive += setupCustomer.getToGive();
									toReceieve += setupCustomer.getToReceive();
								}
							}
							totalGive += toGive;
							totalReceive += toReceieve;
							customerTotals.add(new CustomerTotal(category.getCate_name(), CommonUtil.formateDouble(toGive), CommonUtil.formateDouble(toReceieve)));
						}
						totalGiveField.setText(CommonUtil.formateDouble(totalGive)+"");
						totalReceiveField.setText(CommonUtil.formateDouble(totalReceive)+"");
						CommonUtil.updateJTable(table, totalTitles, totalFields, customerTotals.toArray());
//						table.getModel().addTableModelListener(new TableModelListener() {
//							
//							@Override
//							public void tableChanged(TableModelEvent arg0) {
//								TableModel model = table.getModel();
//								for(int i = 0; i < model.getRowCount(); ++i){
//									SetupCustomer setupCustomer = setupCustomerList.get(i);
//									double give = (Double) model.getValueAt(i, 2);
//									double receive = (Double) model.getValueAt(i, 3);
//									System.out.println(i+" "+receive+" "+give);
//									setupCustomer.setToGive(give);
//									setupCustomer.setToReceive(receive);
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
						table.getModel().addTableModelListener(new TableModelListener() {
							
							@Override
							public void tableChanged(TableModelEvent arg0) {
								TableModel model = table.getModel();
								for(int i = 0; i < model.getRowCount(); ++i){
									SetupCustomer setupCustomer = setupCustomerList.get(i);
									double give = CommonUtil.formateDouble(Double.parseDouble(model.getValueAt(i, 2).toString()));
									double receive = CommonUtil.formateDouble(Double.parseDouble(model.getValueAt(i, 3).toString()));
									System.out.println(i+" "+receive+" "+give);
									setupCustomer.setToGive(give);
									setupCustomer.setToReceive(receive);
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
