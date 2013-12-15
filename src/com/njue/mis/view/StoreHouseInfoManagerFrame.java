package com.njue.mis.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import com.njue.mis.client.Configure;
import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.ValidationManager;
import com.njue.mis.interfaces.CategoryControllerInterface;
import com.njue.mis.interfaces.GoodsControllerInterface;
import com.njue.mis.interfaces.StoreHouseControllerInterface;
import com.njue.mis.model.Goods;
import com.njue.mis.model.StoreHouse;

public class StoreHouseInfoManagerFrame extends JInternalFrame
{
	JTextField storeHouseIdField;
	JTextField storeHouseNameField;
	JTextField storeHouseTypeField;
	JTextField storeHouseMnemonicsField;
	List<StoreHouse> list;
	private StoreHouse storeHouse;
	public StoreHouseInfoManagerFrame(StoreHouse storeHouse, List<StoreHouse> list)
	{
		super("�ֿ����", true, true, false, true);
		this.storeHouse = storeHouse;
		this.list = list;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 12, screenSize.height / 20,
				screenSize.width / 2, screenSize.height / 2);
		this.getContentPane().add(createTabbedPane());
	}
	public StoreHouseInfoManagerFrame(List<StoreHouse> list)
	{
		super("�ֿ����", true, true, false, true);
		this.list = list;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 12, screenSize.height / 20,
				screenSize.width / 2, screenSize.height / 2);
		this.getContentPane().add(createTabbedPane());
	}

	public JTabbedPane createTabbedPane()
	{
		/*
		 * ����������
		 */
		JTabbedPane tabbedPane = new JTabbedPane();
		/*
		 * ������Ʒ�����Ϣҳ��
		 * */
		JPanel addPanel = new JPanel();

		storeHouseIdField = new JTextField(30);
		storeHouseNameField = new JTextField(13);
		storeHouseTypeField = new JTextField(13);
		storeHouseMnemonicsField = new JTextField(30);

		JPanel addpanel1 = new JPanel();
		JLabel storeHouseIdLabel = new JLabel("�ֿ���*:");
		addpanel1.add(storeHouseIdLabel);
		addpanel1.add(storeHouseIdField);

		JPanel addpanel2 = new JPanel();
		JLabel storeHouseNameLabel = new JLabel("�ֿ�����*:");
		addpanel2.add(storeHouseNameLabel);
		addpanel2.add(storeHouseNameField);
		JLabel storeHouseTypeLabel = new JLabel("�ֿ�����:");
		addpanel2.add(storeHouseTypeLabel);
		addpanel2.add(storeHouseTypeField);

		JPanel addpanel3 = new JPanel();
		JLabel storeHouseMnemonicLabel = new JLabel("������:         ");
		addpanel3.add(storeHouseMnemonicLabel);

		addpanel3.add(storeHouseMnemonicsField);
		JButton saveButton = new JButton("����");
		saveButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				if (storeHouseIdField.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "�ֿ��Ų���Ϊ�գ�", "����",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				int id = 0;
				try{
					id = Integer.parseInt(storeHouseIdField.getText().trim());
				}catch(Exception ex){
					JOptionPane.showMessageDialog(null, "�ֿ�������Ϊ����","����",JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					StoreHouse toAdd = new StoreHouse();
					toAdd.setId(id);
					toAdd.setName(storeHouseNameField.getText());
					toAdd.setType(storeHouseTypeField.getText());
					toAdd.setMnemonics(storeHouseMnemonicsField.getText());
					StoreHouseControllerInterface storeHouseService = (StoreHouseControllerInterface) Naming.lookup(Configure.StoreHouseController);
					int result = storeHouseService.saveStoreHouse(toAdd);
					if(result > 0){
						CommonUtil.showError("��Ӳֿ�ɹ�");
					}
					list.add(toAdd);
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
				
				if (storeHouseNameField.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "�ֿ����ֲ���Ϊ�գ�", "����",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});
		JButton updateButton = new JButton("����");
		updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					storeHouse.setName(storeHouseNameField.getText());
					storeHouse.setType(storeHouseTypeField.getText());
					storeHouse.setMnemonics(storeHouseMnemonicsField.getText());
					StoreHouseControllerInterface storeHouseService = (StoreHouseControllerInterface) Naming.lookup(Configure.StoreHouseController);
					boolean result = storeHouseService.updateStoreHouse(storeHouse);
					if(result){
						CommonUtil.showError("���²ֿ�ɹ�");
					}
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
				
				if (storeHouseNameField.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "�ֿ����ֲ���Ϊ�գ�", "����",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
		});
		JPanel addpanel8 = new JPanel();
		
		JButton reButton = new JButton("����");
		reButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				setNull();
			}
		});
		addpanel8.add(reButton);

		addPanel.add(addpanel1);
		addPanel.add(addpanel2);
		addPanel.add(addpanel3);
		addPanel.add(addpanel8);
		tabbedPane.addTab("��Ʒ�����Ϣ", addPanel);
		
		if(this.storeHouse != null){
			storeHouseIdField.setText(""+storeHouse.getId());
			storeHouseIdField.setEditable(false);
			storeHouseNameField.setText(storeHouse.getName());
			storeHouseTypeField.setText(storeHouse.getType());
			storeHouseMnemonicsField.setText(storeHouse.getMnemonics());
			addpanel8.add(updateButton);
		}else{
			addpanel8.add(saveButton);
		}
		return tabbedPane;
	}

	private void setNull()
	{
		storeHouseNameField.setText("");
		storeHouseIdField.setText("");
		storeHouseMnemonicsField.setText("");
		storeHouseTypeField.setText("");
	}
}
