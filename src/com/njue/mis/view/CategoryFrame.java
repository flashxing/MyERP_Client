package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import com.njue.mis.common.CommonUtil;
import com.njue.mis.model.Category;

public class CategoryFrame extends JInternalFrame
{
	protected List<Category> list = new ArrayList<Category>();
	protected JScrollPane scrollPane;
    protected JTree simpleTree;
    protected JButton addButton;
    protected JButton removeButton;
    protected JButton updateButton;
    protected JButton addGoodsButton;
    protected DefaultMutableTreeNode clickNode;
    protected DefaultMutableTreeNode root;
    protected JTextField insertField;
    protected JPanel panel;
    protected Container pane = this.getContentPane();
    
    protected Category toAdd;
    protected Category toDelete;
    public Category selected;
    protected Category toUpdate;
    protected JTable table;
    protected String[] objects;
    protected String[] fieldsToShow;
    protected Object[] initList = {};
	public CategoryFrame(String frameName,List<Category> list)
	{
		super(frameName,true,true,false,false);
    }
	public void init(final List<Category> list)
	{
		addGoodsButton = new JButton("添加商品");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 2 / 3);
		this.list = list;
		root = buildTree(list);
        simpleTree = new JTree(root);
        simpleTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        simpleTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener(){
            public void valueChanged( TreeSelectionEvent e ){
                System.out.println( "selection changed" );
            }
        });
        simpleTree.setRootVisible(false);
        simpleTree.setShowsRootHandles(true);
//        simpleTree.putClientProperty("JTree.lineStyle","Horizontal");
        simpleTree.putClientProperty("JTree.lineStyle", "Angled");
//        simpleTree.putClientProperty("JTree.lineStyle","None");
//        simpleTree.setCellRenderer( new CustomTreeCellRenderer() );
        simpleTree.setEditable( false );
//        simpleTree.getModel().addTreeModelListener(new TreeModelListener(){
//                public void treeNodesChanged(TreeModelEvent e) {
//                    System.out.println("node changed");
//                }
//                public void treeNodesInserted(TreeModelEvent e) {
//                    System.out.println( "node inserted" );
//                }
//                public void treeNodesRemoved(TreeModelEvent e) {
//                    System.out.println("node removed");
//                }
//                public void treeStructureChanged(TreeModelEvent e) {
//                    System.out.println( "strutrued changed" );
//                }
//            });
        scrollPane = new JScrollPane( simpleTree );

        addButton = new JButton ("添加分类");        
        updateButton = new JButton("更新分类");
        
        removeButton = new JButton ("删除分类");
        insertField = new JTextField(20);
        panel = new JPanel ( new GridLayout(30,30) );
        panel.add(insertField);
        panel.add( addButton );
        panel.add( removeButton );
        panel.add(updateButton);
        panel.add(addGoodsButton);
        
        this.getContentPane().add( panel,BorderLayout.LINE_START );
       
        table = CommonUtil.createTable(objects,initList,fieldsToShow);
        this.getContentPane().add(new JScrollPane(table), BorderLayout.LINE_END);
        
        this.getContentPane().add( scrollPane,BorderLayout.CENTER  );
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
}
