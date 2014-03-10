package com.njue.mis.common;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.njue.mis.client.Configure;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.model.Category;

public class GoodsCategoryPanel extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7477928447136182115L;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private List<Category> categoryList;
	protected DefaultMutableTreeNode root;
    protected JTree simpleTree;
    protected JScrollPane scrollPane;
    protected DefaultMutableTreeNode clickNode;

	private CategoryControllerInterface categoryService;
	public GoodsCategoryPanel(){
		super();
		init();
	}
	public void init(){
		try {
			categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
			categoryList = categoryService.getAllGoodsCategory();
			if(categoryList == null){
				CommonUtil.showError("没有分类");
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
		root = CommonUtil.buildTree(categoryList);
		simpleTree = new JTree(root);
        simpleTree.putClientProperty("JTree.lineStyle", "Angled");
        simpleTree.setEditable( false );
        simpleTree.setRootVisible(true);
        simpleTree.setShowsRootHandles(true);

        scrollPane = new JScrollPane( simpleTree );
        scrollPane.setPreferredSize(new Dimension(screenSize.width * 1 / 8,
				screenSize.height *1/2));
        add(scrollPane);
	}
	
	public JTree getCategoryTree(){
		return simpleTree;
	}
	
	public List<Integer> getAllSubCateId(int cateId){
		return CommonUtil.getAllSubCategory(categoryList, cateId);
	}
	public List<Integer> getSubCateId(int cateId){
		return CommonUtil.getSubCategory(categoryList, cateId);
	}
	public Category getCategoryByCateId(int cateId){
		return CommonUtil.getCategoryById(cateId, categoryList);
	}
	public boolean isChildren(Category category, int cateId){
		return CommonUtil.isChildren(categoryList, category, cateId);
	}
}
