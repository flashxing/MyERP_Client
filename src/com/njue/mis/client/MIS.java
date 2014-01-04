package com.njue.mis.client;
import javax.swing.JFrame;

import com.njue.mis.common.CommonUtil;
import com.njue.mis.view.CategoryFrame;
import com.njue.mis.view.LoginFrame;
import com.sun.xml.internal.ws.wsdl.writer.document.Service;

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
	}
}