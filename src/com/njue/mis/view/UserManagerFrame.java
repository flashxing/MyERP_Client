package com.njue.mis.view;

import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.OperatorControllerInterface;
import com.njue.mis.interfaces.UserControllerInterface;
import com.njue.mis.interfaces.UserRoleControllerInterface;
import com.njue.mis.model.Module;
import com.njue.mis.model.Operator;
import com.njue.mis.model.Role;
import com.njue.mis.model.User;
import com.njue.mis.interfaces.RoleControllerInterface;

public class UserManagerFrame extends JInternalFrame
{
	private List<Role> roleList;
	private List<User> userList;
	private RoleControllerInterface roleHandler;
	private UserControllerInterface userHandler;
	private List<JCheckBox> addCheckBoxList;
	private Map<JCheckBox, Role> checkBoxRoleMap = new HashMap<JCheckBox, Role>();
	private Map<JCheckBox, Role> updateCheckBoxRoleMap = new HashMap<JCheckBox, Role>();
	private List<JCheckBox> updateCheckBoxList;
	
	private JPanel addPanel;
	private JPanel deletePanel;
	private JPanel updatePanel;
	private JTabbedPane userManagerPane;
	
	private JButton addButton;
	private JButton deleteButton;
	private JButton updateButton;
	
	private JTextField userNameField;
	private JPasswordField userPWField;
	private JPasswordField userPW2Field;
	private JTextField updateUserNameField;
	private JPasswordField updateUserPWField;
	private JPasswordField updateUserPW2Field;
	
	private JComboBox<User> deleteUserComboBox;
	private JComboBox<User> updateUserComboBox;
	
	public void init(){
		userManagerPane = new JTabbedPane();
		addPanel = new JPanel();
		deletePanel = new JPanel();
		updatePanel = new JPanel();
		userManagerPane.add("����û�", addPanel);
		userManagerPane.add("ɾ���û�",deletePanel);
		userManagerPane.add("�鿴�޸��û�",updatePanel);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 8, screenSize.height / 12,
				screenSize.width *2/5, screenSize.height *2/5);
		try {
			roleHandler = (RoleControllerInterface) Naming.lookup(Configure.RoleController);
			userHandler = (UserControllerInterface) Naming.lookup(Configure.UserController);
//			userRoleHandler = (UserRoleControllerInterface) Naming.lookup(Configure.UserRoleController);
			roleList = roleHandler.getAllRole();
			userList = userHandler.getAllUser();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("�������");
		}
		
		addCheckBoxList = new ArrayList<JCheckBox>();
		for(Role role:roleList){
			JCheckBox checkBox = new JCheckBox(role.getRoleName());
			checkBox.setSelected(false);
			addCheckBoxList.add(checkBox);
			checkBoxRoleMap.put(checkBox, role);
			addPanel.add(checkBox);
		}
		JLabel userNameLabel = new JLabel("�û����ƣ�");
		userNameField = new JTextField(10);
		JLabel userPWLabel = new JLabel("����(����6-12):");
		userPWField = new JPasswordField(12);
		JLabel userPW2Label = new JLabel("�û�����ȷ��:");
		userPW2Field = new JPasswordField(12);
		addButton = new JButton("���");
		addButton.addActionListener(new AddAction());
		addPanel.add(userNameLabel);
		addPanel.add(userNameField);
		addPanel.add(userPWLabel);
		addPanel.add(userPWField);
		addPanel.add(userPW2Label);
		addPanel.add(userPW2Field);
		addPanel.add(addButton);
		
		deleteUserComboBox = new JComboBox<User>();
		for(User user:userList){
			deleteUserComboBox.addItem(user);
		}
		deleteButton = new JButton("ɾ��");
		deleteButton.addActionListener(new DeleteAction());
		deletePanel.add(deleteUserComboBox);
		deletePanel.add(deleteButton);
		
		updateUserComboBox = new JComboBox<User>();
		for(User user:userList){
			updateUserComboBox.addItem(user);
		}
		updateUserComboBox.addItemListener(new SelectedChangeAction());
		User selectedUser = null;
		if(userList.size()>0){
			selectedUser = userList.get(0);
		}
		updateCheckBoxList = new ArrayList<JCheckBox>();
		for(Role role:roleList){
			JCheckBox checkBox = new JCheckBox(role.getRoleName());
			checkBox.setSelected(false);
			if(selectedUser !=null &&CommonUtil.stringInRolesName(role.getRoleName(), selectedUser.getRoleSet())){
				checkBox.setSelected(true);
			}
			updateCheckBoxList.add(checkBox);
			updateCheckBoxRoleMap.put(checkBox, role);
			updatePanel.add(checkBox);
		}
		updateButton = new JButton("����");
		updateButton.addActionListener(new UpdateAction());
		updatePanel.add(updateUserComboBox);
		JLabel updatePWLabel = new JLabel("������(6-12):");
		updateUserPWField = new JPasswordField(12);
		JLabel updatePW2Label = new JLabel("ȷ��������:");
		updateUserPW2Field = new JPasswordField(12);
		updatePanel.add(updatePWLabel);
		updatePanel.add(updateUserPWField);
		updatePanel.add(updatePW2Label);
		updatePanel.add(updateUserPW2Field);
		updatePanel.add(updateButton);
		
		this.getContentPane().add(userManagerPane);
	}
	public UserManagerFrame()
	{
		super("�û�����", true, true, false, true);
		init();
	}
	public void Reset(){
		userNameField.setText("");
		userPW2Field.setText("");
		userPWField.setText("");
		for(JCheckBox checkBox:addCheckBoxList){
			checkBox.setSelected(false);
		}
	}
	
	public void updateUpdateCheckList(){
		User user = (User) updateUserComboBox.getSelectedItem();
		for(JCheckBox checkBox:updateCheckBoxList){
			checkBox.setSelected(false);
			if(user !=null&&CommonUtil.stringInRolesName(checkBox.getText(), user.getRoleSet())){
				checkBox.setSelected(true);
			}
		}
	}
	private class AddAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String userName = userNameField.getText();
			if(userName.length()<0){
				CommonUtil.showError("�������ɫ����");
				return;
			}
			String userPW = userPWField.getText();
			if(userPW.length()<6||userPW.length()>12){
				CommonUtil.showError("���볤��Ϊ6-12λ");
				return;
			}
			if(!userPW.equals(userPW2Field.getText())){
				CommonUtil.showError("ȷ���������");
				return;
			}
			User user = new User();
			user.setUserName(userName);
			user.setUserPW(userPW);
			for(Entry<JCheckBox, Role> entry:checkBoxRoleMap.entrySet()){
				if(entry.getKey().isSelected()){
					user.getRoleSet().add(entry.getValue());
				}
			}
			try {
				int id;
				if((id = userHandler.addUser(user))>0){
					CommonUtil.showError("��ӳɹ�");
					Reset();
					user.setUserId(id);
					userList.add(user);
					updateUserComboBox.addItem(user);
					deleteUserComboBox.addItem(user);
				}else{
					CommonUtil.showError("��ɫ�����Ѵ���");
					Reset();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("����������ʧ��");
				Reset();
			}
		}
	}
	private class UpdateAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			User user = (User) updateUserComboBox.getSelectedItem();
			user.getRoleSet().clear();
			String pw = updateUserPWField.getText();
			if(pw.length()>0&&pw.length()<6||pw.length()>12){
				CommonUtil.showError("�����������6��12λ");
				return;
			}else if(pw.length()>=6&&pw.length()<=12&&pw.equals(updateUserPW2Field.getText())){
				user.setUserPW(pw);	
			}
			for(Entry<JCheckBox, Role> entry:updateCheckBoxRoleMap.entrySet()){
				if(entry.getKey().isSelected()){
					user.getRoleSet().add(entry.getValue());
				}
			}
			try {
				if(userHandler.updateUser(user)){
					CommonUtil.showError("���³ɹ�");
				}else{
					CommonUtil.showError("����ʧ��");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("������󣬸���ʧ��");
			}
		}
		
	}
	
	private class SelectedChangeAction implements ItemListener{

		@Override
		public void itemStateChanged(ItemEvent arg0) {
			// TODO Auto-generated method stub
			if(arg0.getStateChange()==ItemEvent.SELECTED){
				updateUpdateCheckList();
				updatePanel.repaint();
			}
		}
		
	}
	private class DeleteAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			User user = (User) deleteUserComboBox.getSelectedItem();
			try {
				if(userHandler.deleteUser(user)){
					CommonUtil.showError("ɾ���ɹ�");
					userList.remove(user);
					updateUserComboBox.removeItem(user);
					deleteUserComboBox.removeItem(user);
				}else{
					CommonUtil.showError("ɾ��ʧ��");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("�������ɾ��ʧ��");
			}
		}
	}
	
}
