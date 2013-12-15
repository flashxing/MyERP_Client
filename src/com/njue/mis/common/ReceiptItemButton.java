package com.njue.mis.common;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
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
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import com.njue.mis.client.Configure;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.interfaces.CustomerControllerInterface;
import com.njue.mis.interfaces.ReceiptItemControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Customer;
import com.njue.mis.model.CustomerCategory;
import com.njue.mis.model.ReceiptItem;

public class ReceiptItemButton extends JButton{
	private ReceiptItem receiptItem;
	private ReceiptItemChooser receiptItemChooser;
	public ReceiptItemButton(String text){
		super(text);
		this.setSize(5, 20);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        super.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	System.out.println("ReceiptItemButton clikc");
                if (receiptItemChooser == null)
                    receiptItemChooser = new ReceiptItemChooser();
                Point p = getLocationOnScreen();
                p.y = p.y + 30;
                receiptItemChooser.showCustomerChooser(p);
            }
        });
	}
	
	public ReceiptItem getReceiptItem(){
		return receiptItem;
	}
	public void setReceiptItem(ReceiptItem receiptItem){
		this.receiptItem = receiptItem;
		setText(receiptItem.getItem());
	}
	
	private class ReceiptItemChooser extends JPanel implements ActionListener,
    ChangeListener {
		private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		private int width = screenSize.width *1/3;
		private int height = screenSize.height *5/9;
		private JDialog dialog;
		
		private List<ReceiptItem> receiptItemList;
		
		private ReceiptItemControllerInterface receiptItemService;
		
		private JTable table;
		private JScrollPane scrollPane;
		private String[] columns = {"条目"};
		private String[] fields = {"item"};
	    private ReceiptItem selected;
	    
	    private JTextField itemField;
	    private JButton addButton;
	    public ReceiptItemChooser(){
			super();
			init();
		}
		public void init(){
			try {
				receiptItemService = (ReceiptItemControllerInterface) Naming.lookup(Configure.ReceiptItemController);
				receiptItemList = receiptItemService.getAll();
				if(receiptItemList == null){
					CommonUtil.showError("没有条目");
					return;
				}
			} catch (MalformedURLException | RemoteException
					| NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
				return;
			}
			initPanel();
		}
		
		public void initPanel(){
            setLayout(new BorderLayout());
	        table = CommonUtil.createTable(columns,receiptItemList.toArray(),fields);
	        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent e)
				{
					ListSelectionModel model = (ListSelectionModel)e.getSource();
					int index = model.getMaxSelectionIndex();
					if(index>=0 && receiptItemList!=null &&index<receiptItemList.size()){
						ReceiptItem receiptItem = receiptItemList.get(index);
						setReceiptItem(receiptItem);
					}
				}
			});
	        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 4,
					screenSize.height  / 8));
	        scrollPane = new JScrollPane();
	        scrollPane.setViewportView(table);
	        scrollPane.setPreferredSize(new Dimension(screenSize.width * 1 / 3,
					screenSize.height * 1/80));
	        add(scrollPane, BorderLayout.CENTER);
	        
	        JPanel panel_input = new JPanel();
	        itemField = new JTextField(10);
	        addButton = new JButton("添加");
	        addButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					String item = itemField.getText();
					if(item.length()<=0){
						CommonUtil.showError("请输入条目");
						return;
					}
					ReceiptItem receiptItem = new ReceiptItem();
					receiptItem.setItem(item);
					try {
						if(receiptItemService.addReceiptItem(receiptItem) == null){
							CommonUtil.showError("添加失败,请查看是否重复");
							return;
						}
						receiptItemList.add(receiptItem);
						CommonUtil.updateJTable(table, columns, receiptItemList.toArray(), fields);
						itemField.setText("");
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CommonUtil.showError("网络错误");
					}
				}
			});
	        panel_input.add(itemField);
	        panel_input.add(addButton);
	        
	        add(panel_input, BorderLayout.SOUTH);
		}
		@Override
		public void stateChanged(ChangeEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
        private JDialog createDialog(Frame owner) {
            JDialog result = new JDialog(owner, "客户选择", true);
            result.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            result.getContentPane().add(this, BorderLayout.CENTER);
            result.pack();
            result.setSize(width, height);
            return result;
        }
		public void showCustomerChooser(Point position){
			Frame owner = (Frame) SwingUtilities
                    .getWindowAncestor(ReceiptItemButton.this);
            if (dialog == null || dialog.getOwner() != owner)
                dialog = createDialog(owner);
            dialog.setLocation((screenSize.width - width) / 2,
                    (screenSize.height - height) / 2);
            dialog.show();
		}
	}

}
