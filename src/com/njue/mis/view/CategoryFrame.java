package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

import com.njue.mis.common.CommonUtil;
import com.njue.mis.model.Category;

public class CategoryFrame extends JInternalFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7608328935392470145L;
	protected List<Category> list = new ArrayList<Category>();
	protected JScrollPane scrollPane;
    protected JTree simpleTree;
    protected JButton addButton;
    protected JButton removeButton;
    protected JButton updateButton;
    protected JButton addGoodsButton;
    protected JButton updateEntityButton;
    protected JButton deleteEntityButton;
    protected DefaultMutableTreeNode clickNode;
    protected DefaultMutableTreeNode root;
    protected JTextField insertField;
    protected JTextField updateField;
    protected JPanel panel;
    protected Container pane = this.getContentPane();
	private JScrollPane goodScrollPane;
	
    protected Category toAdd;
    protected Category toDelete;
    public Category selected;
    protected Category toUpdate;
    protected JTable table;
    protected String[] objects;
    protected String[] fieldsToShow;
    protected Object[] initList = {};
    protected ImageIcon image;
	public CategoryFrame(String frameName,List<Category> list)
	{
		super(frameName,true,true,true,true);
    }
	
	protected void enableButton(){
		updateButton.setEnabled(true);
		addButton.setEnabled(true);
		removeButton.setEnabled(true);
		addGoodsButton.setEnabled(true);
		deleteEntityButton.setEnabled(true);
	}
	protected void enableAllButAddButton(){
		updateButton.setEnabled(true);
		removeButton.setEnabled(true);
		addGoodsButton.setEnabled(true);
		deleteEntityButton.setEnabled(true);
	}
	
	protected void disableButton(){
		updateButton.setEnabled(false);
		removeButton.setEnabled(false);	
		addGoodsButton.setEnabled(false);
		deleteEntityButton.setEnabled(false);
	}
	public void init(final List<Category> list)
	{
		addGoodsButton = new JButton("添加商品");
		updateEntityButton = new JButton();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(new Dimension(screenSize.width * 2 / 3,
				screenSize.height * 4/7));
		this.list = list;
		root = buildTree(list);
        simpleTree = new JTree(root);
        simpleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        simpleTree.setRootVisible(false);
        simpleTree.setShowsRootHandles(true);
        simpleTree.putClientProperty("JTree.lineStyle", "Angled");
        simpleTree.setEditable( false );
        simpleTree.setCellRenderer(new MyRenderer());
        simpleTree.setMaximumSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height  / 80));

        scrollPane = new JScrollPane( simpleTree );
        scrollPane.setPreferredSize(new Dimension(screenSize.width * 1 / 8,
				screenSize.height *1/80));
        addButton = new JButton ("添加分类");        
        updateButton = new JButton("更新分类");

        removeButton = new JButton ("删除分类");
        deleteEntityButton = new JButton();
        insertField = new JTextField(10);
        updateField = new JTextField(10);
        panel = new JPanel ();
        disableButton();
        panel.add(insertField);
        panel.add( addButton );
        panel.add( removeButton );
        panel.add(updateField);
        panel.add(updateButton);
        panel.add(addGoodsButton);
        panel.add(updateEntityButton);
        panel.add(deleteEntityButton);

       
        table = CommonUtil.createTable(objects,initList,fieldsToShow);
        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 4,
				screenSize.height  / 8));
        goodScrollPane = new JScrollPane();
        goodScrollPane.setViewportView(table);
        goodScrollPane.setPreferredSize(new Dimension(screenSize.width * 2 / 3,
				screenSize.height * 1/8));
        this.getContentPane().add(goodScrollPane, BorderLayout.CENTER);
        
        this.getContentPane().add( scrollPane,BorderLayout.WEST);
        
        this.getContentPane().add( panel,BorderLayout.SOUTH);
	}
	private Category getCategoryById(int cate_id){
		for(Category category:list){
			if(category.getCate_id() == cate_id){
				return category;
			}
		}
		return null;
	}
	/*
	 * @params HashMap map the container save the KV(Integer, DefaultMutableTreeNode);
	 * 			int father_id the cate_id of the category to create the node
	 * 			node to node to add to the father
	 * @returns the father's cate_id.if exists father
	 * 			else return -1
	 */
	private int createNode(HashMap<Integer, DefaultMutableTreeNode> map, int father_id, DefaultMutableTreeNode node){
		Category category = getCategoryById(father_id);
		if (category == null){
			return -1;
		}
		DefaultMutableTreeNode father = new DefaultMutableTreeNode(category);
		map.put(father_id, father);
		father.add(node);
		return category.getPrefer_id();
	}
	
    private DefaultMutableTreeNode buildTree(List<Category> list){
        HashMap<Integer, DefaultMutableTreeNode> map = new HashMap<Integer, DefaultMutableTreeNode>();
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    	map.put(0, root);
        for(Category category:list){
        	if(map.containsKey(category.getCate_id())){
        		continue;
        	}
        	DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
        	map.put(category.getCate_id(), node);
        	DefaultMutableTreeNode father;
        	int father_id = category.getPrefer_id();
        	while ((father = map.get(father_id)) == null){
        		int tmp_id = createNode(map, father_id, node);
        		if (tmp_id < 0){
        			break;
        		}
        		node = map.get(father_id);
        		father_id = tmp_id;
        	}
        	if(father != null){
        		if(father != node){
        			father.add(node);
        		}
        	}
        }
        return root;
    }
    private class MyRenderer extends DefaultTreeCellRenderer{
    	/**
		 * 
		 */
		private static final long serialVersionUID = -2953626780476944067L;

		public Component getTreeCellRendererComponent(JTree tree, Object value,boolean sel, boolean expanded, boolean leaf, int row,boolean hasFocus) 
    	{
    		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,row, hasFocus);
    		setLeafIcon(image);//叶子结点图片
    		setClosedIcon(image);//关闭树后显示的图片
    		setOpenIcon(image);//打开树时显示的图片
    		return this;
    	}
    }
}
