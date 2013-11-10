package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jws.soap.SOAPBinding.Use;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.interfaces.OperatorControllerInterface;
import com.njue.mis.interfaces.UserControllerInterface;
import com.njue.mis.model.User;

public class LoginFrame extends JFrame implements ActionListener
{

	JTextField username;
	JPasswordField password;
	JButton submit;
	JButton cancel;

	public LoginFrame()
	{
		super("���������ϵͳ");
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 3, screenSize.height / 3, 330, 230);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		submit = new JButton("ȷ��");
		submit.addActionListener(this);

		cancel = new JButton("ȡ��");
		cancel.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				System.exit(0);
			}
		});

		loginLayout();

		this.setVisible(true);

	}

	public void actionPerformed(ActionEvent e)
	{
		String usernameString = username.getText();
		String passwordString = String.valueOf(password.getPassword());

 		if (usernameString.trim().length() == 0)
		{
			JOptionPane.showMessageDialog(null, "�û�������Ϊ�գ�","����",JOptionPane.WARNING_MESSAGE);
		}
		else
			if (passwordString.trim().length() == 0)
			{
				JOptionPane.showMessageDialog(null, "���벻��Ϊ�գ�","����",JOptionPane.WARNING_MESSAGE);
			}
			else
			{
				try {
					UserControllerInterface userHandler = (UserControllerInterface) Naming.lookup(Configure.UserController);
					User user = userHandler.validUser(usernameString, passwordString);
					if (user!=null)
					{
						MainFrame.username=usernameString;
						MainFrame.user = user;
						MainFrame.getMainFrame().setVisible(true);
						this.setVisible(false);
					}
					else
					{
						JOptionPane.showMessageDialog(null, "��½ʧ��,�û������������","����",JOptionPane.WARNING_MESSAGE);
					}
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					CommonUtil.showError("�������Ӵ���");
					e1.printStackTrace();
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					CommonUtil.showError("�������Ӵ���");
					e1.printStackTrace();
				} catch (NotBoundException e1) {
					// TODO Auto-generated catch block
					CommonUtil.showError("�������Ӵ���");
					e1.printStackTrace();
				}

			}
	}

	/**
	 * ���岼��
	 */
	private void loginLayout()
	{
		JLabel name;
		JLabel passwordLabel;
		JPanel panel_center, panel_south;
		JLabel label;
		name = new JLabel("�û���:  ", JLabel.RIGHT);
		name.setForeground(new Color(0, 128, 255));
		passwordLabel = new JLabel("����:  ", JLabel.RIGHT);
		passwordLabel.setForeground(new Color(0, 128, 255));
		username = new JTextField();
		username.setColumns(10);
		password = new JPasswordField();
		password.setColumns(10);
		password.setEchoChar('*');
		panel_center = new JPanel();
		panel_center.setLayout(new GridLayout(3, 1));
		panel_south = new JPanel();
		this.setLayout(new BorderLayout());
		this.setContentPane(new JPanel()
		{
			public void paintComponent(Graphics g)
			{
				setDoubleBuffered(true);
				g.drawImage(new ImageIcon(LoginFrame.class.getResource("images/login.jpg"))
						.getImage(), 0, 0, null);
			}
		});
		for (int i = 0; i < 13; i++)
		{
			label = new JLabel();
			label.setPreferredSize(new Dimension(600, 1));
			this.getContentPane().add(label, BorderLayout.NORTH);
		}
		panel_center.add(name);
		panel_center.add(username);
		panel_center.add(passwordLabel);
		panel_center.add(password);
		label = new JLabel();
		label.setPreferredSize(new Dimension(70, 1));
		panel_south.add(label);
		panel_south.add(submit);
		panel_south.add(cancel);
		panel_center.setOpaque(false);
		panel_south.setOpaque(false);
		this.getContentPane().add(panel_center, BorderLayout.EAST);
		this.getContentPane().add(panel_south, BorderLayout.SOUTH);
	}
}
