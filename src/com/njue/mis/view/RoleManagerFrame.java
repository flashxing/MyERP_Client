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
		roleManagerPane.add("添加角色", addPanel);
		roleManagerPane.add("删除角色",deletePanel);
		roleManagerPane.add("查看修改角色",updatePanel);
		
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
			CommonUtil.showError("网络错误");
		}
		
		checkBoxList = new ArrayList<JCheckBox>();
		for(Module module:moduleList){
			JCheckBox checkBox = new JCheckBox(module.getName());
			checkBox.setSelected(false);
			checkBoxList.add(checkBox);
			addPanel.add(checkBox);
		}
		JLabel roleNameLabel = new JLabel("角色名称");
		roleNameField = new JTextField(10);
		addButton = new JButton("添加");
		addButton.addActionListener(new AddAction());
		addPanel.add(roleNameLabel);
		addPanel.add(roleNameField);
		addPanel.add(addButton);
		
		deleteRoleComboBox = new JComboBox<Role>();
		for(Role role:roleList){
			deleteRoleComboBox.addItem(role);
		}
		deleteButton = new JButton("删除");
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
		updateButton = new JButton("更新");
		updateButton.addActionListener(new UpdateAction());
		updatePanel.add(updateRoleComboBox);
		updatePanel.add(updateButton);
		
		this.getContentPane().add(roleManagerPane);
	}
	public RoleManagerFrame()
	{
		super("操作员管理", true, true, false, true);
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
				CommonUtil.showError("请输入角色名字");
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
					CommonUtil.showError("添加成功");
					Reset();
					role.setRoleId(id);
					roleList.add(role);
					updateRoleComboBox.addItem(role);
					deleteRoleComboBox.addItem(role);
				}else{
					CommonUtil.showError("角色名称已存在");
					Reset();
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误，添加失败");
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
					CommonUtil.showError("更新成功");
				}else{
					CommonUtil.showError("更新失败");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误，更新失败");
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
					CommonUtil.showError("删除成功");
					roleList.remove(role);
					updateRoleComboBox.removeItem(role);
					deleteRoleComboBox.removeItem(role);
				}else{
					CommonUtil.showError("删除失败");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误，删除失败");
			}
		}
	}
}
