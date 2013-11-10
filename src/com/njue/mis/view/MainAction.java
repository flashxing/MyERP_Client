package com.njue.mis.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import com.njue.mis.client.Configure;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.interfaces.OperatorControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Category;
import com.njue.mis.model.StoreHouse;


public class MainAction
{
	public static ActionListener clickCustomerInfoManager()
	{

		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try {
					CategoryControllerInterface categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
					List<Category> list = categoryService.getAllCustomerCategory();
					CustomerCategoryFrame customerFrame = new CustomerCategoryFrame(list);
					MainFrame.getMainFrame().getContentPane().add(customerFrame);
					customerFrame.setVisible(true);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};
	}

	public static ActionListener clickGoodsInfoManager()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				try {
					CategoryControllerInterface categoryService = (CategoryControllerInterface) Naming.lookup(Configure.CategoryController);
					List<Category> list = categoryService.getAllGoodsCategory();
					GoodsCategoryFrame categoryFrame = new GoodsCategoryFrame(list);
					MainFrame.getMainFrame().getContentPane().add(categoryFrame);
					categoryFrame.setVisible(true);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//				GoodsFrame goodsFrame = new GoodsFrame();
//				MainFrame.getMainFrame().getContentPane().add(goodsFrame);
//				goodsFrame.setVisible(true);
			}
		};
	}
	public static ActionListener storeHouseManager(){
		return new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				try {
					StoreHouseControllerInterface storeHouseService = (StoreHouseControllerInterface) Naming.lookup(Configure.StoreHouseController);
					List<StoreHouse> list = storeHouseService.getAll();
					System.out.println(list.size());
					StoreHouseManagerFrame storeHouseManager = new StoreHouseManagerFrame(list);
					MainFrame.getMainFrame().getContentPane().add(storeHouseManager);
					storeHouseManager.setVisible(true);
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		};
	}
	public static ActionListener clickDiscountManager()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				DiscountFrame discountFrame = new DiscountFrame();
				MainFrame.getMainFrame().getContentPane().add(discountFrame);
				discountFrame.setVisible(true);
			}
		};
	}

	public static ActionListener clickProviderInfoManager()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ProviderFrame privoderFrame = new ProviderFrame();
				MainFrame.getMainFrame().getContentPane().add(privoderFrame);
				privoderFrame.setVisible(true);
			}
		};
	}

	public static ActionListener clickCustomerInforserch()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				CustomerInforSearchFrame customerInforSearchFrame = new CustomerInforSearchFrame();
				MainFrame.getMainFrame().getContentPane().add(
						customerInforSearchFrame);
				customerInforSearchFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener clickGoodsInforserch()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				GoodsInforSearchFrame goodsInforSearchFrame = new GoodsInforSearchFrame();
				MainFrame.getMainFrame().getContentPane().add(
						goodsInforSearchFrame);
				goodsInforSearchFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener clickInputInforserch()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				InputInforSearchFrame inputInforSearchFrame = new InputInforSearchFrame();
				MainFrame.getMainFrame().getContentPane().add(
						inputInforSearchFrame);
				inputInforSearchFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener clickOutputInforserch()
	{
		return new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				OutputInforSearchFrame outputInforSearchFrame = new OutputInforSearchFrame();
				MainFrame.getMainFrame().getContentPane().add(
						outputInforSearchFrame);
				outputInforSearchFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener clickPrivoderInforSearch()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				ProviderInforSearchFrame privoderInforSearchFrame = new ProviderInforSearchFrame();
				MainFrame.getMainFrame().getContentPane().add(
						privoderInforSearchFrame);
				privoderInforSearchFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener clickSaleBackInforSearch()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				SaleBackInforSearchFrame saleBackInforSearchFrame = new SaleBackInforSearchFrame();
				MainFrame.getMainFrame().getContentPane().add(
						saleBackInforSearchFrame);
				saleBackInforSearchFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener clickSaleInforSearch()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				SaleInforSearchFrame saleInforSearchFrame = new SaleInforSearchFrame();
				MainFrame.getMainFrame().getContentPane().add(
						saleInforSearchFrame);
				saleInforSearchFrame.setVisible(true);
			}
		};
	}
	

	public static ActionListener operaterManager()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				OperaterManagerFrame operaterManagerFrame = new OperaterManagerFrame();
				MainFrame.getMainFrame().getContentPane().add(
						operaterManagerFrame);
				operaterManagerFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener saleGoods()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				SalesFrame salesFrame = new SalesFrame();
				MainFrame.getMainFrame().getContentPane().add(
						salesFrame);
				salesFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener salesBack()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				SalesBackFrame salesBackFrame = new SalesBackFrame();
				MainFrame.getMainFrame().getContentPane().add(
						salesBackFrame);
				salesBackFrame.setVisible(true);
			}
		}; 
	}
	
	public static ActionListener priceChange()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				PriceChangeFrame priceChangeFrame = new PriceChangeFrame();
				MainFrame.getMainFrame().getContentPane().add(
						priceChangeFrame);
				priceChangeFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener importGoods()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				InportFrame importFrame = new InportFrame();
				MainFrame.getMainFrame().getContentPane().add(
						importFrame);
				importFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener outportGoods()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				OutportFrame outportFrame = new OutportFrame();
				MainFrame.getMainFrame().getContentPane().add(
						outportFrame);
				outportFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener storeHouseInfor()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				StorehouseInfoFrame storehouseInfoFrame = new StorehouseInfoFrame();
				MainFrame.getMainFrame().getContentPane().add(
						storehouseInfoFrame);
				storehouseInfoFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener roleManager(){
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				RoleManagerFrame roleManagerFrame = new RoleManagerFrame();
				MainFrame.getMainFrame().getContentPane().add(roleManagerFrame);
				roleManagerFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener userManager(){
		return new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				UserManagerFrame userManagerFrame = new UserManagerFrame();
				MainFrame.getMainFrame().getContentPane().add(userManagerFrame);
				userManagerFrame.setVisible(true);
			}
		};
	}
	
	public static ActionListener changePassword()
	{
		return new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				ChangePasswordFrame changePasswordFrame = new ChangePasswordFrame();
				MainFrame.getMainFrame().getContentPane().add(
						changePasswordFrame);
				changePasswordFrame.setVisible(true);
			}
		};
	}
}
