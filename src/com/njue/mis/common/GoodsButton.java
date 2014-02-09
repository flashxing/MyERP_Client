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
import javax.swing.JLabel;
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
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.Customer;
import com.njue.mis.model.CustomerCategory;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsCategory;

public class GoodsButton extends JButton{
	private String goodsId;
	private Goods goods;
	private GoodsChooser goodsChooser;
	public GoodsButton(String text){
		super(text);
		this.setSize(5, 20);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        super.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (goodsChooser == null)
                    goodsChooser = new GoodsChooser();
                Point p = getLocationOnScreen();
                p.y = p.y + 30;
                goodsChooser.showCustomerChooser(p);
            }
        });
	}
	
	public String getGoodsId(){
		return goods.getId();
	}
	public Goods getGoods(){
		return goods;
	}
	public void setGoods(Goods goods){
		this.goods = goods;
		setText(goods.getGoodsName());
	}
	
	private class GoodsChooser extends JPanel implements ActionListener,
    ChangeListener {
		private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		private int width = screenSize.width *2/3;
		private int height = screenSize.height *5/9;
		private JDialog dialog;
		
		private List<Category> categoryList;
		private List<Goods> goodsList;
		
		private CategoryControllerInterface categoryService;
		private GoodsControllerInterface goodsService;
		
	    protected DefaultMutableTreeNode root;
	    protected JTree simpleTree;
	    protected JScrollPane scrollPane;
	    
	    private JTable table;
	    private String[] columns = {"商品名称","型号","数量","销售价","最近销售价"};
	    private String[] fields = {"goodsName","description","goodsNum","salesPrice","lastSalePrice"};
	    private JScrollPane goodScrollPane;
	    
	    protected DefaultMutableTreeNode clickNode;
	    private GoodsCategory selected;
	    private JTextField nameField;
	    private JButton searchButton;
	    public GoodsChooser(){
			super();
			init();
		}
		public void init(){
			try {
				categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
				goodsService = (GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
				categoryList = categoryService.getAllGoodsCategory();
				goodsList = new ArrayList<Goods>();
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
            setLayout(new BorderLayout());
			root = CommonUtil.buildTree(categoryList);
			simpleTree = new JTree(root);
	        simpleTree.putClientProperty("JTree.lineStyle", "Angled");
	        simpleTree.setEditable( false );
	        simpleTree.setRootVisible(false);
	        simpleTree.setShowsRootHandles(true);
	        simpleTree.setMaximumSize(new Dimension(screenSize.width * 2 / 3-60,
					screenSize.height  / 8));
	        simpleTree.addMouseListener(new ClickNodeActionListener());

	        scrollPane = new JScrollPane( simpleTree );
	        scrollPane.setPreferredSize(new Dimension(screenSize.width * 1 / 8,
					screenSize.height *1/8));
	        table = CommonUtil.createTable(columns,goodsList.toArray(),fields);
	        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent e)
				{
					ListSelectionModel model = (ListSelectionModel)e.getSource();
					int index = model.getMaxSelectionIndex();
					if(index>=0 && goodsList!=null &&index<goodsList.size()){
						Goods goods = goodsList.get(index);
						setGoods(goods);
					}
				}
			});
	        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 4,
					screenSize.height  / 8));
	        goodScrollPane = new JScrollPane();
	        goodScrollPane.setViewportView(table);
	        goodScrollPane.setPreferredSize(new Dimension(screenSize.width * 2 / 3,
					screenSize.height * 1/8));
	        JPanel searchPanel = new JPanel();
	        JLabel nameLabel = new JLabel("");
	        nameField = new JTextField(10);
	        searchButton = new JButton("查询");
	        searchButton.addActionListener(new SearchAction());
	        searchPanel.add(nameLabel);
	        searchPanel.add(nameField);
	        searchPanel.add(searchButton);
	        JPanel rightPanel = new JPanel(new BorderLayout());
	        rightPanel.add(searchPanel, BorderLayout.NORTH);
	        rightPanel.add(goodScrollPane, BorderLayout.CENTER);
	        add(rightPanel, BorderLayout.CENTER);
	        add(scrollPane,BorderLayout.WEST);
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
            JDialog result = new JDialog(owner, "商品选择", true);
            result.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            result.getContentPane().add(this, BorderLayout.CENTER);
            result.pack();
            result.setSize(width, height);
            return result;
        }
		public void showCustomerChooser(Point position){
			Frame owner = (Frame) SwingUtilities
                    .getWindowAncestor(GoodsButton.this);
            if (dialog == null || dialog.getOwner() != owner)
                dialog = createDialog(owner);
            dialog.setLocation((screenSize.width - width) / 2,
                    (screenSize.height - height) / 2);
            dialog.show();
		}

	    private class ClickNodeActionListener implements MouseListener{
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				TreePath currentPath = simpleTree.getPathForLocation(arg0.getX(), arg0.getY());
				if (currentPath == null){
					return;
				}
				clickNode = (DefaultMutableTreeNode) currentPath.getLastPathComponent();
				selected = (GoodsCategory) clickNode.getUserObject();
				if (arg0.getClickCount() != 2){
					return;
				}
				if(clickNode.isLeaf()){
					int cateId = selected.getCate_id();
					try{
						goodsList = goodsService.getAllGoodsByCateId(cateId);
						if(goodsList != null){
							CommonUtil.updateJTable(table, columns, goodsList.toArray(), fields);
						}
						table.repaint();
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
		private class SearchAction implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String goodsName = nameField.getText();
				if(goodsName.length()<=0){
					CommonUtil.showError("搜索关键词不能为空");
					return;
				}
				try {
					goodsList = goodsService.getAllGoodsByGoodsName(goodsName);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				CommonUtil.updateJTable(table, columns, goodsList.toArray(), fields);
			}
		}

	}


}
