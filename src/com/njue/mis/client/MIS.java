package com.njue.mis.client;
import javax.swing.JFrame;

import org.jfree.report.JFreeReportBoot;

import com.njue.mis.common.CommonUtil;
import com.njue.mis.view.LoginFrame;

public class MIS
{
	public static void main(String[] args)
	{
		Configure.init();
		if(!(RemoteService.load()>0)){
			CommonUtil.showError("找不到服务器");
			return ;
		}
		JFrame.setDefaultLookAndFeelDecorated(true);
		@SuppressWarnings("unused")
//		CategoryFrame cate = new CategoryFrame();
		LoginFrame loginFrame=new LoginFrame();
		JFrame.setDefaultLookAndFeelDecorated(true);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
		    	JFreeReportBoot.getInstance().start();
			}
		}).run();
	}
}