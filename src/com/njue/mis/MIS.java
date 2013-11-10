package com.njue.mis;
import javax.swing.JFrame;

import com.njue.mis.view.CategoryFrame;
import com.njue.mis.view.LoginFrame;

public class MIS
{
	public static void main(String[] args)
	{
		JFrame.setDefaultLookAndFeelDecorated(true);
		@SuppressWarnings("unused")
//		CategoryFrame cate = new CategoryFrame();
		LoginFrame loginFrame=new LoginFrame();
		JFrame.setDefaultLookAndFeelDecorated(true);
	}
}