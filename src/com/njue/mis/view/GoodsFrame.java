package com.njue.mis.view;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

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
import com.njue.mis.interfaces.ProviderServicesHandler;
import com.njue.mis.model.Goods;

public class GoodsFrame extends JInternalFrame
{
	JTextField goodsField;
	JTextField ID_goodsField;
	JTextField priceField;
	JTextField goodsdressField;
	JTextField packageField;
	JTextField sizeField;
	JTextField promitField;
	JTextField decriptionField;
	JTextField ID_privoderField;

	JTextField goodsField1;
	JTextField ID_goodsField1;
	JTextField priceField1;
	JTextField goodsdressField1;
	JTextField packageField1;
	JTextField sizeField1;
	JTextField productField1;
	JTextField promitField1;
	JTextField decriptionField1;
	JTextField ID_privoderField1;
	
	private int cateId;

	public GoodsFrame(int cateId)
	{
		super("��Ʒ��Ϣ����", true, true, false, true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(screenSize.width / 12, screenSize.height / 20,
				screenSize.width / 2, screenSize.height / 2);
		this.getContentPane().add(createTabbedPane());
		this.cateId = cateId;
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

		goodsField = new JTextField(30);
		ID_goodsField = new JTextField(13);
		priceField = new JTextField(13);
		goodsdressField = new JTextField(30);
		ID_privoderField = new JTextField(13);
		packageField = new JTextField(13);
		sizeField = new JTextField(13);
		promitField = new JTextField(30);
		decriptionField = new JTextField(30);

		JPanel addpanel1 = new JPanel();
		JLabel goodsLabel = new JLabel("��Ʒ����*:");
		addpanel1.add(goodsLabel);
		addpanel1.add(goodsField);

		JPanel addpanel2 = new JPanel();
		JLabel ID_goodsLabel = new JLabel("��Ʒ����*:");
		addpanel2.add(ID_goodsLabel);
		addpanel2.add(ID_goodsField);
		JLabel priceLabel = new JLabel("����*:");
		addpanel2.add(priceLabel);
		addpanel2.add(priceField);

		JPanel addpanel3 = new JPanel();
		JLabel goodsdressLabel = new JLabel("����:         ");
		addpanel3.add(goodsdressLabel);

		addpanel3.add(goodsdressField);

		JPanel addpanel4 = new JPanel();
		JLabel ID_privoderLabel = new JLabel("��Ӧ�̺�:");
		addpanel4.add(ID_privoderLabel);

		addpanel4.add(ID_privoderField);
		JLabel sizeLabel = new JLabel("���:");
		addpanel4.add(sizeLabel);

		addpanel4.add(sizeField);

		JPanel addpanel5 = new JPanel();
		JLabel packageLabel = new JLabel("��װ:         ");
		addpanel5.add(packageLabel);

		addpanel5.add(packageField);
		JLabel productLabel = new JLabel("����:");
		addpanel5.add(productLabel);


		JPanel addpanel6 = new JPanel();
		JLabel promitLabel = new JLabel("��׼�ĺ�:");
		addpanel6.add(promitLabel);

		addpanel6.add(promitField);

		JPanel addpanel7 = new JPanel();
		JLabel descriptionLabel = new JLabel("�ͺ�*:         ");
		addpanel7.add(descriptionLabel);

		addpanel7.add(decriptionField);

		JPanel addpanel8 = new JPanel();
		JButton saveButton = new JButton("����");
		saveButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				if (goodsField.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "��Ʒȫ�Ʋ���Ϊ�գ�", "����",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (ID_goodsField.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "��Ʒ��Ų���Ϊ�գ�", "����",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (priceField.getText().trim().length() == 0)
				{
					JOptionPane.showMessageDialog(null,"��Ʒ�۸���Ϊ�գ�", "����",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (priceField.getText().trim().length() != 0)
				{
					if (!ValidationManager.validatePrice(priceField.getText().trim()))
					{
						JOptionPane.showMessageDialog(null,"��Ʒ�۸񲻺Ϸ���", "����",
								JOptionPane.WARNING_MESSAGE);
						return;
					}
				}
				
				try {
    				GoodsControllerInterface goodsService = (GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
    				Goods goods = new Goods(null,
							goodsField.getText(), goodsdressField.getText(),
							sizeField.getText(), packageField.getText(),
							ID_goodsField.getText(), promitField.getText(),
							decriptionField.getText(), Double
									.valueOf(priceField.getText()),
							ID_privoderField.getText());
    				goods.setCateId(cateId);
    				String goodsId = goodsService.addGoods(goods);
    				
    				if (goodsId != null)
					{
						JOptionPane.showMessageDialog(null, "��Ʒ��Ϣ��ӳɹ���", "��Ϣ",
								JOptionPane.INFORMATION_MESSAGE);
						setNull();
					}
					else
					{
						JOptionPane.showMessageDialog(null,
								"��Ʒ��Ϣ���ʧ�ܣ�������Ʒ�����Ƿ��Ѵ��ڣ�", "����",
								JOptionPane.WARNING_MESSAGE);
						setNull();
					}
    			} catch (MalformedURLException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("�������Ӵ���");
    			} catch (RemoteException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("�������Ӵ���");
    			} catch (NotBoundException e1) {
    				// TODO Auto-generated catch block
    				e1.printStackTrace();
    				CommonUtil.showError("�������Ӵ���");
    			}
			}
		});
		addpanel8.add(saveButton);
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
		addPanel.add(addpanel7);
		addPanel.add(addpanel3);
		addPanel.add(addpanel4);
		addPanel.add(addpanel5);
		addPanel.add(addpanel6);
		addPanel.add(addpanel8);
		tabbedPane.addTab("��Ʒ�����Ϣ", addPanel);

		/*
		 * ������Ʒɾ����Ϣҳ��
		 * */
		goodsField1 = new JTextField(11);
		goodsField1.setEditable(false);
		ID_goodsField1 = new JTextField(13);
		priceField1 = new JTextField(13);
		priceField1.setEditable(false);
		goodsdressField1 = new JTextField(30);
		goodsdressField1.setEditable(false);
		ID_privoderField1 = new JTextField(13);
		ID_privoderField1.setEditable(false);
		sizeField1 = new JTextField(13);
		sizeField1.setEditable(false);
		packageField1 = new JTextField(13);
		packageField1.setEditable(false);
		productField1 = new JTextField(13);
		productField1.setEditable(false);
		promitField1 = new JTextField(30);
		promitField1.setEditable(false);
		decriptionField1 = new JTextField(30);
		decriptionField1.setEditable(false);

		final JPanel deletePanel = new JPanel();

		JPanel deletepanel1 = new JPanel();
		JLabel goodsLabel1 = new JLabel("��ƷID:");
		deletepanel1.add(goodsLabel1);
		deletepanel1.add(ID_goodsField1);
		
		JButton selectButton = new JButton("��ѯ");
		selectButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{

				if (ID_goodsField1.getText().length() == 0)
				{
					JOptionPane.showMessageDialog(null, "��ƷID����Ϊ��", "����",
							JOptionPane.WARNING_MESSAGE);
				}

				else
				{
					try {
	    				GoodsControllerInterface goodsService = (GoodsControllerInterface) Naming.lookup(Configure.GoodsController);
	    			} catch (MalformedURLException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    				CommonUtil.showError("�������Ӵ���");
	    			} catch (RemoteException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    				CommonUtil.showError("�������Ӵ���");
	    			} catch (NotBoundException e1) {
	    				// TODO Auto-generated catch block
	    				e1.printStackTrace();
	    				CommonUtil.showError("�������Ӵ���");
	    			}
				}
			}
		});
		deletepanel1.add(selectButton);

		JPanel deletepanel2 = new JPanel();
		JLabel ID_goodsLabel1 = new JLabel("��Ʒȫ��:");
		deletepanel2.add(ID_goodsLabel1);

		deletepanel2.add(goodsField1);
		JLabel priceLabel1 = new JLabel("����:");
		deletepanel2.add(priceLabel1);

		deletepanel2.add(priceField1);

		JPanel deletepanel3 = new JPanel();
		JLabel goodsdressLabel1 = new JLabel("����:         ");
		deletepanel3.add(goodsdressLabel1);

		deletepanel3.add(goodsdressField1);

		JPanel deletepanel4 = new JPanel();
		JLabel ID_privoderLabel1 = new JLabel("��Ӧ�̺�:");
		deletepanel4.add(ID_privoderLabel1);

		deletepanel4.add(ID_privoderField1);
		JLabel sizeLabel1 = new JLabel("���:");
		deletepanel4.add(sizeLabel1);

		deletepanel4.add(sizeField1);

		JPanel deletepanel5 = new JPanel();
		JLabel packageLabel1 = new JLabel("��װ:         ");
		deletepanel5.add(packageLabel1);

		deletepanel5.add(packageField1);
		JLabel productLabel1 = new JLabel("����:");
		deletepanel5.add(productLabel1);

		deletepanel5.add(productField1);

		JPanel deletepanel6 = new JPanel();
		JLabel promitLabel1 = new JLabel("��׼�ĺ�:");
		deletepanel6.add(promitLabel1);

		deletepanel6.add(promitField1);

		JPanel deletepanel7 = new JPanel();
		JLabel descriptionLabel1 = new JLabel("����:         ");
		deletepanel7.add(descriptionLabel1);

		deletepanel7.add(decriptionField1);

		JPanel deletepanel8 = new JPanel();
		JButton deleteButton = new JButton("ɾ��");
		deleteButton.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				if(ID_goodsField1.getText().length()==0)
				{
					JOptionPane.showMessageDialog(null, "��ƷID����Ϊ��", "����",
							JOptionPane.WARNING_MESSAGE);
				}
				else
				{
//					GoodsServicesHandler goodsServicesHandler=CommonFactory.getGoodsServices();
//					if(goodsServicesHandler.deleteGoods(ID_goodsField1.getText())){
//						JOptionPane.showMessageDialog(null, "��ϲ�㣬ɾ���ɹ���", "��Ϣ",
//								JOptionPane.INFORMATION_MESSAGE);
//						ID_goodsField1.setEditable(true);
//					}
//					else{
//						JOptionPane.showMessageDialog(null, "�Բ���ɾ��ʧ�ܣ�", "����",
//								JOptionPane.WARNING_MESSAGE);
//					}
				}
				
			}
		});
		
		deletepanel8.add(deleteButton);
		JButton reButton1 = new JButton("ȡ��");
		reButton1.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		deletepanel8.add(reButton1);

		deletePanel.add(deletepanel1);
		deletePanel.add(deletepanel2);
		deletePanel.add(deletepanel3);
		deletePanel.add(deletepanel4);
		deletePanel.add(deletepanel5);
		deletePanel.add(deletepanel6);
		deletePanel.add(deletepanel7);
		deletePanel.add(deletepanel8);

		tabbedPane.addTab("��Ʒɾ����Ϣ", deletePanel);

		return tabbedPane;
	}

	private void setNull()
	{
		ID_goodsField.setText("");
		goodsField.setText("");
		goodsdressField.setText("");
		sizeField.setText("");
		packageField.setText("");
		promitField.setText("");
		decriptionField.setText("");
		priceField.setText("");
		ID_privoderField.setText("");
	}
}
