package com.njue.mis.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import com.njue.mis.common.CommonUtil;
import com.njue.mis.model.User;


public class MainFrame extends JFrame
{
	public static String power;
	public static String username;
	public static User user;
	private JDesktopPane desktopPane;
	private static MainFrame mainFrame;
	/**
	 * 创建主窗体
	 */
	private MainFrame()
	{
		super("进销存管理系统");

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width/6,screenSize.height/6, screenSize.width*2/3,
				screenSize.height*2/3);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		desktopPane=new JDesktopPane();
		
		desktopPane.setOpaque(true);

		this.setContentPane(desktopPane);
		this.setJMenuBar(createMenuBar());
		
		//Set up the backgound image
		desktopPane.setBackground(new Color(200,218,235));
		// Make dragging a little faster but perhaps uglier.
		desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
	}
	
	public static MainFrame getMainFrame()
	{
		if(mainFrame==null)
		{
			mainFrame=new MainFrame();
		}
		return mainFrame;
	}

	/**
	 * 创建主窗体的菜单栏
	 * 
	 * @return JMenuBar
	 */
	protected JMenuBar createMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		JMenu menu;
		JMenuItem menuItem;
		// Set up the info menu.
		if(CommonUtil.hasPermisson(user, "基础信息管理")){
			menu = new JMenu("基础信息管理");
			menuBar.add(menu);
			menuItem = new JMenuItem("客户信息管理");
			menuItem.addActionListener(MainAction.clickCustomerInfoManager());
			menu.add(menuItem);
			menuItem = new JMenuItem("商品信息管理");
			menuItem.addActionListener(MainAction.clickGoodsInfoManager());
			menu.add(menuItem);
			menuItem = new JMenuItem("折扣管理");
			menuItem.addActionListener(MainAction.clickDiscountManager());
			menu.add(menuItem);
		}
		
		if(CommonUtil.hasPermisson(user, "进货管理")){
			// Set up the inport menu.
			menu = new JMenu("进货管理");
			menuBar.add(menu);
			menuItem = new JMenuItem("进货单");
			menuItem.addActionListener(MainAction.importGoods());
			menu.add(menuItem);
			menuItem = new JMenuItem("退货单");
			menuItem.addActionListener(MainAction.outportGoods());
			menu.add(menuItem);
		}
		
		if(CommonUtil.hasPermisson(user, "销售管理")){
			// Set up the sales menu.
			menu = new JMenu("销售管理");
			menuBar.add(menu);
			menuItem = new JMenuItem("销售出货");
			menuItem.addActionListener(MainAction.saleGoods());
			menu.add(menuItem);
			menuItem = new JMenuItem("销售退货");
			menuItem.addActionListener(MainAction.salesBack());
			menu.add(menuItem);
			//todo add the actionListener
			menuItem = new JMenuItem("销售进货");
			menuItem.addActionListener(MainAction.salesBack());
			menu.add(menuItem);
			menuItem = new JMenuItem("业务单稿");
			menuItem.addActionListener(MainAction.salesBack());
			menu.add(menuItem);
		}
		if(CommonUtil.hasPermisson(user, "查询管理")){
			// Set up the select menu.
			menu = new JMenu("查询管理");
			menuBar.add(menu);
			menuItem = new JMenuItem("客户查询");
			menuItem.addActionListener(MainAction.clickCustomerInforserch());
			menu.add(menuItem);
			menuItem = new JMenuItem("商品查询");
			menuItem.addActionListener(MainAction.clickGoodsInforserch());
			menu.add(menuItem);
			menuItem = new JMenuItem("供应商查询");
			menuItem.addActionListener(MainAction.clickPrivoderInforSearch());
			menu.add(menuItem);
			menuItem = new JMenuItem("销售查询");
			menuItem.addActionListener(MainAction.clickSaleInforSearch());
			menu.add(menuItem);
			menuItem = new JMenuItem("销售退货查询");
			menuItem.addActionListener(MainAction.clickSaleBackInforSearch());
			menu.add(menuItem);
			menuItem = new JMenuItem("入库查询");
			menuItem.addActionListener(MainAction.clickInputInforserch());
			menu.add(menuItem);
			menuItem = new JMenuItem("入库退货查询");
			menuItem.addActionListener(MainAction.clickOutputInforserch());
			menu.add(menuItem);
		}
		if(CommonUtil.hasPermisson(user, "库存管理")){
			// Set up the save menu.
			menu = new JMenu("库存管理");
			menuBar.add(menu);
			menuItem = new JMenuItem("仓库管理");
			menuItem.addActionListener(MainAction.storeHouseManager());
			menu.add(menuItem);
			menuItem = new JMenuItem("库存盘点");
			menuItem.addActionListener(MainAction.storeHouseInfor());
			menu.add(menuItem);
//			menuItem = new JMenuItem("价格调整");
//			menuItem.addActionListener(MainAction.priceChange());
//			menu.add(menuItem);
			//todo  add the actionListener
			menuItem = new JMenuItem("库存获赠");
			menuItem.addActionListener(MainAction.giftIn());
			menu.add(menuItem);
			menuItem = new JMenuItem("库存赠送");
			menuItem.addActionListener(MainAction.giftOut());
			menu.add(menuItem);
			menuItem = new JMenuItem("库存报溢");
			menuItem.addActionListener(MainAction.inCome());
			menu.add(menuItem);
			menuItem = new JMenuItem("库存报损");
			menuItem.addActionListener(MainAction.outCome());
			menu.add(menuItem);
			menuItem = new JMenuItem("库存拆装");
			menuItem.addActionListener(MainAction.priceChange());
			menu.add(menuItem);
			menuItem = new JMenuItem("库存报警");
			menuItem.addActionListener(MainAction.priceChange());
			menu.add(menuItem);
		}
		if(CommonUtil.hasPermisson(user, "财务管理")){
			menu = new JMenu("财务管理");
			menuBar.add(menu);
			menuItem = new JMenuItem("收款单据");
			menuItem.addActionListener(MainAction.receiptIn());
			menu.add(menuItem);
			
			menuItem = new JMenuItem("付款单据");
			menuItem.addActionListener(MainAction.receiptOut());
			menu.add(menuItem);
			
//			menuItem = new JMenuItem("现金管理");
//			menu.add(menuItem);
			
			menuItem = new JMenuItem("钱流管理");
			menu.add(menuItem);
		}
		if(CommonUtil.hasPermisson(user, "统计报表")){
			menu = new JMenu("统计报表");
			menuBar.add(menu);
			menuItem = new JMenuItem("收据报表");
			menuItem.addActionListener(MainAction.receipInStatistics());
			menu.add(menuItem);
			
			menuItem = new JMenuItem("付款统计");
			menuItem.addActionListener(MainAction.receipOutStatistics());
			menu.add(menuItem);
			
			menuItem = new JMenuItem("销售统计");
			menuItem.addActionListener(MainAction.salesStatistics());
			menu.add(menuItem);
		}

//		 Set up the system menu.
		if(CommonUtil.hasPermisson(user, "系统管理")){
			menu = new JMenu("系统管理");
			menuBar.add(menu);
			menuItem = new JMenuItem("角色管理");
			menuItem.addActionListener(MainAction.roleManager());
			menu.add(menuItem);
			menuItem = new JMenuItem("用户管理");
			menuItem.addActionListener(MainAction.userManager());
			menu.add(menuItem);
			menuItem = new JMenuItem("更改密码");
			menuItem.addActionListener(MainAction.changePassword());
			menu.add(menuItem);
		}
//		if (power.equals("管理员"))
//		{
//			menuItem = new JMenuItem("操作员管理");
//			menuItem.addActionListener(MainAction.operaterManager());
//			menu.add(menuItem);
//		}
//		else if(power.equals("操作员"))
//		{
//		}
//		else
//		{
//			JOptionPane.showMessageDialog(null, "非法用户！！！","警告",JOptionPane.WARNING_MESSAGE);
//			System.exit(0);
//		}
		return menuBar;
	}
}
