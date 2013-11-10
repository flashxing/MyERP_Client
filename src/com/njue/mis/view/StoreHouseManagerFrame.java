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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
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
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.OperatorControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Goods;
import com.njue.mis.model.StoreHouse;

public class StoreHouseManagerFrame extends JInternalFrame
{
	private List<StoreHouse> list = new ArrayList<StoreHouse>();
    private JButton updateButton;
    private JButton addButton;
    private JPanel panel;
    private JButton removeButton;
    private JButton refreshButton;

    private StoreHouse selectedSH;
    private JTable table;
    private Object[] objects = {"仓库编号","仓库名字","仓库类型","助记码"};
    private String[] fieldsToShow = {"id","name","type","mnemonics"};
	public StoreHouseManagerFrame(List<StoreHouse> list)
	{
		super("仓库管理",true,true,false,false);
		init(list);
    }
	public void init(final List<StoreHouse> list)
	{
		addButton = new JButton("添加仓库");
		addButton.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				StoreHouseInfoManagerFrame storeHouseFrame = new StoreHouseInfoManagerFrame(list);
				MainFrame.getMainFrame().getContentPane().add(storeHouseFrame);
				storeHouseFrame.setVisible(true);
			}
		});
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 2 / 3);
		this.list = list;
        updateButton = new JButton("更新仓库");
        updateButton.addActionListener(new UpdateActionListener());
        refreshButton = new JButton("更新页面");
        refreshButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				CommonUtil.updateJTable(table, objects, list.toArray(), fieldsToShow);
				table.repaint();
			}
		});
        removeButton = new JButton ("删除仓库");
        removeButton.addActionListener(new DeleteActionListener());
        panel = new JPanel ( new GridLayout(30,30) );
        panel.add( addButton );
        panel.add(updateButton);
        panel.add(refreshButton);
        
        this.getContentPane().add( panel,BorderLayout.LINE_START );
        
        table = CommonUtil.createTable(objects,this.list.toArray(),fieldsToShow);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				int row = table.getSelectedRow();
				if(row > 0){
					selectedSH = list.get(row);
				}
			}
		});
        this.getContentPane().add(new JScrollPane(table), BorderLayout.LINE_END);
	}
  
    class DeleteActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(selectedSH ==null){
				CommonUtil.showError("请选择要删除的仓库");
				return;
			}
			list.remove(selectedSH);
			CommonUtil.updateJTable(table, objects, list.toArray(), fieldsToShow);
			table.repaint();
		}
    }
    
    class UpdateActionListener implements ActionListener{
    	
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			if(selectedSH==null){
				CommonUtil.showError("请选择要更新的仓库");
				return;
			}
			StoreHouseInfoManagerFrame storeHouseFrame = new StoreHouseInfoManagerFrame(selectedSH,list);
			MainFrame.getMainFrame().getContentPane().add(storeHouseFrame);
			storeHouseFrame.setVisible(true);
		}
    }
}
