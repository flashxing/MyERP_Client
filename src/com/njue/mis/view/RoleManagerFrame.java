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
import java.util.List;
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
import com.njue.mis.model.Module;
import com.njue.mis.model.Operator;
import com.njue.mis.model.Role;
import com.njue.mis.interfaces.RoleControllerInterface;

public class RoleManagerFrame extends JInternalFrame
{
	private List<Module> moduleList;
	private List<Role> roleList;
	private CheckboxGroup group;
	private RoleControllerInterface roleHandler;
	private List<JCheckBox> checkBoxList;
	private List<JCheckBox> updateCheckBoxList;
	
	private JPanel addPanel;
	private JPanel deletePanel;
	private JPanel updatePanel;
	private JTabbedPane roleManagerPane;
	
	private JButton addButton;
	private JButton deleteButton;
	private JButton updateButton;
	
	private JTextField roleNameField;
	
	private JComboBox<Role> deleteRoleComboBox;
	private JComboBox<Role> updateRoleComboBox;
	
	public void init(){
		roleManagerPane = new JTabbedPane();
		addPanel = new JPanel();
		deletePanel = new JPanel();
		updatePanel = new JPanel();
		roleManagerPane.add("��ӽ�ɫ", addPanel);
		roleManagerPane.add("ɾ����ɫ",deletePanel);
		roleManagerPane.add("�鿴�޸Ľ�ɫ",updatePanel);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 8, screenSize.height / 12,
				screenSize.width *2/5, screenSize.height *2/5);
		try {
			roleHandler = (RoleControllerInterface) Naming.lookup(Configure.RoleController);
			moduleList = roleHandler.getAllModule();
			roleList = roleHandler.getAllRole();
		} catch (MalformedURLException | RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("�������");
		}
		
		checkBoxList = new ArrayList<JCheckBox>();
		for(Module module:moduleList){
			JCheckBox checkBox = new JCheckBox(module.getName());
			checkBox.setSelected(false);
			checkBoxList.add(checkBox);
			addPanel.add(checkBox);
		}
		JLabel roleNameLabel = new JLabel("��ɫ����");
		roleNameField = new JTextField(10);
		addButton = new JButton("���");
		addButton.addActionListener(new AddAction());
		addPanel.add(roleNameLabel);
		addPanel.add(roleNameField);
		addPanel.add(addButton);
		
		deleteRoleComboBox = new JComboBox<Role>();
		for(Role role:roleList){
			deleteRoleComboBox.addItem(role);
		}
		deleteButton = new JButton("ɾ��");
		deleteButton.addActionListener(new DeleteAction());
		deletePanel.add(deleteRoleComboBox);
		deletePanel.add(deleteButton);
		
		updateRoleComboBox = new JComboBox<Role>();
		for(Role role:roleList){
			updateRoleComboBox.addItem(role);
		}
		updateRoleComboBox.addItemListener(new SelectedChangeAction());
		Role selectedRole = null;
		if(roleList.size()>0){
			selectedRole = roleList.get(0);
		}
		updateCheckBoxList = new ArrayList<JCheckBox>();
		for(Module module:moduleList){
			JCheckBox checkBox = new JCheckBox(module.getName());
			checkBox.setSelected(false);
			if(selectedRole !=null && selectedRole.getModuleSet().contains(module.getName())){
				checkBox.setSelected(true);
			}
			updateCheckBoxList.add(checkBox);
			updatePanel.add(checkBox);
		}
		updateButton = new JButton("����");
		updateButton.addActionListener(new UpdateAction());
		updatePanel.add(updateRoleComboBox);
		updatePanel.add(updateButton);
		
		this.getContentPane().add(roleManagerPane);
	}
	public RoleManagerFrame()
	{
		super("����Ա����", true, true, false, true);
		init();
	}
	public void Reset(){
		roleNameField.setText("");
		for(JCheckBox checkBox:checkBoxList){
			checkBox.setSelected(false);
		}
	}
	
	public void updateUpdateCheckList(){
		Role role = (Role) updateRoleComboBox.getSelectedItem();
		for(JCheckBox checkBox:updateCheckBoxList){
			checkBox.setSelected(false);
			if(role !=null&&role.getModuleSet().contains(checkBox.getText())){
				checkBox.setSelected(true);
			}
		}
	}
	private class AddAction implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String roleName = roleNameField.getText();
			if(roleName.length()<=0){
				CommonUtil.showError("�������ɫ����");
				return;
			}
			Role role = new Role();
			role.setRoleName(roleName);
			for(JCheckBox checkBox:checkBoxList){
				if(checkBox.isSelected()){
					role.getModuleSet().add(checkBox.getText());
				}
			}
			try {
				int id;
				if((id = roleHandler.addRole(role))>0){
					CommonUtil.showError("��ӳɹ�");
					Reset();
					role.setRoleId(id);
					roleList.add(role);
					updateRoleComboBox.addItem(role);
					deleteRoleComboBox.addItem(role);
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
			Role role = (Role) updateRoleComboBox.getSelectedItem();
			role.getModuleSet().clear();
			for(JCheckBox checkBox:updateCheckBoxList){
				if(checkBox.isSelected()){
					role.getModuleSet().add(checkBox.getText());
				}
			}
			try {
				if(roleHandler.updateRole(role)){
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
			Role role = (Role) deleteRoleComboBox.getSelectedItem();
			try {
				if(roleHandler.deleteRole(role)){
					CommonUtil.showError("ɾ���ɹ�");
					roleList.remove(role);
					updateRoleComboBox.removeItem(role);
					deleteRoleComboBox.removeItem(role);
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
