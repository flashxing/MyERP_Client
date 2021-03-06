package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import com.njue.mis.common.CommonFactory;
import com.njue.mis.common.ValidationManager;
import com.njue.mis.interfaces.PortInControllerService;
import com.njue.mis.model.PortIn;

public class InputInforSearchFrame extends JInternalFrame
{
	public InputInforSearchFrame()
	{
		super("入库查询", true, true, true, true);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height  *2/ 3);
		this.setContentPane(new InputInforSearchPanel());
	}
}

class InputInforSearchPanel extends JPanel
{
	JTable table;	
	MyTableModel tableModel;
	JComboBox comboBox;
	JTextField textField;
	JCheckBox checkBox;
	JTextField textField_starttime;
	JTextField textField_endtime;
	
	public InputInforSearchPanel()
	{
		super(new BorderLayout());
		JPanel pane = search();
		this.add(pane, BorderLayout.NORTH);
		
		tableModel=new MyTableModel();
		table = new JTable(tableModel);
		
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		table.setAutoCreateRowSorter(true);

		JScrollPane scrollPane = new JScrollPane(table);

		add(scrollPane, BorderLayout.CENTER);
	}

	public JPanel search()
	{
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(2,1));
		JPanel panel2=new JPanel(new FlowLayout());
		JPanel panel3=new JPanel(new FlowLayout());
		
		JLabel lable = new JLabel("请选择查询条件：");
		panel2.add(lable);

		comboBox = new JComboBox();
		comboBox.addItem("入库单号");
		comboBox.addItem("操作员");
		comboBox.addItem("供应商编号");
		comboBox.addItem("商品编号");
		comboBox.setSelectedIndex(0);
		panel2.add(comboBox);

		textField = new JTextField();
		textField.setColumns(13);
		panel2.add(textField);
		
		JButton button = new JButton();
		button.setText("查询");
		button.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e)
		{
			String field=comboBox.getSelectedItem().toString();
			String value=textField.getText();
			if(checkBox.isSelected())
			{
				String beginTime=textField_starttime.getText();
				String endTime=textField_endtime.getText();
				if(beginTime==null||beginTime.trim().length()==0)
				{
					JOptionPane.showMessageDialog(null,"请输入开始时间","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (endTime==null||endTime.trim().length()==0)
				{
					JOptionPane.showMessageDialog(null,"请输入结束时间","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (!ValidationManager.validateDate(beginTime))
				{
					JOptionPane.showMessageDialog(null,"时间格式不正确!正确格式: yyyy-mm-dd","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
				if (!ValidationManager.validateDate(endTime))
				{
					JOptionPane.showMessageDialog(null,"时间格式不正确!正确格式: yyyy-mm-dd","警告",JOptionPane.WARNING_MESSAGE);
					return;
				}
				PortInControllerService handle = CommonFactory.getPortInServices();
				Vector<PortIn> portinVector=handle.searchPortInByTime(beginTime, endTime);
				if(portinVector.size()==0)
				{
					JOptionPane.showMessageDialog(null,"没有满足你条件的入库单","警告",JOptionPane.WARNING_MESSAGE);
				}
				tableModel.updateData(portinVector);
			}
			else
				{
					if (value == null || value.trim().length() == 0)
					{
						JOptionPane.showMessageDialog(null, "请输入搜索的值", "警告",
								JOptionPane.WARNING_MESSAGE);
					}
					else
					{
						PortInControllerService handle = CommonFactory
								.getPortInServices();
						Vector<PortIn> portinVector = handle.searchPortIn(
								getValue(field), value);
						if (portinVector.size() == 0)
						{
							JOptionPane.showMessageDialog(null, "没有满足你条件的入库单",
									"警告", JOptionPane.WARNING_MESSAGE);
						}
						tableModel.updateData(portinVector);
					}
				}
			
		}});
		panel2.add(button);

		checkBox = new JCheckBox("按指定日期查询");
		checkBox.addItemListener(new ItemListener()
		{
			
			public void itemStateChanged(ItemEvent e)
			{
				if (!checkBox.isSelected())
				{
					textField_starttime.setText("");
					textField_endtime.setText("");
					textField_starttime.setEnabled(false);
					textField_endtime.setEnabled(false);
				}
				else
				{
					textField_starttime.setEnabled(true);
					textField_endtime.setEnabled(true);
				}
			}
		});
		panel3.add(checkBox);

		JLabel lable1 = new JLabel("从");
		panel3.add(lable1);
		textField_starttime = new JTextField();
		textField_starttime.setColumns(13);
		textField_starttime.setEnabled(false);
		panel3.add(textField_starttime);
		JLabel lable2 = new JLabel("到");
		panel3.add(lable2);
		textField_endtime = new JTextField();
		textField_endtime.setColumns(13);
		textField_endtime.setEnabled(false);
		panel3.add(textField_endtime);
		JButton button1 = new JButton();
		button1.setText("显示全部信息");
		button1.addActionListener(new ActionListener()
		{
			
			public void actionPerformed(ActionEvent e)
			{
				PortInControllerService handle = CommonFactory.getPortInServices();
				Vector<PortIn> portinVector = handle.getAllPortIn();
				if(portinVector.size()==0)
				{
					JOptionPane.showMessageDialog(null,"当前不存在任何入库单","警告",JOptionPane.WARNING_MESSAGE);
				}
				else
				{
					tableModel.updateData(portinVector);
				}
			}
		});
		panel2.add(button1);

		panel.add(panel2);
		panel.add(panel3);
		
		return panel;
	}
	private String getValue(String field)
	{
		if (field.equals("退货单号"))
		{
			return "id";
		}
		else if (field.equals("操作员"))
		{
			return "operateperson";
		}
		else if (field.equals("供应商编号"))
		{
			return "providerid";
		}
		else 
		{
			return "goodsid";
		}
	}
	
	class MyTableModel extends AbstractTableModel
	{
		private String[] columnNames =
		{
				"入库票号", "商品编号", "商品名称", "单价", "数量", "金额", "供应商编号", "供应商名称",
				"入库时间", "操作员"
		};
		
		Vector<PortIn> portinVector=new Vector<PortIn>();
		public int getColumnCount()
		{
			return columnNames.length;
		}

		public int getRowCount()
		{
			return portinVector.size();
		}

		public String getColumnName(int col)
		{
			return columnNames[col];
		}

		public Object getValueAt(int row, int col)
		{
			PortIn portIn=portinVector.get(row);
			return portIn.getPortValue(col);
		}

		@SuppressWarnings("unchecked")
		public Class getColumnClass(int c)
		{
			return getValueAt(0, c).getClass();
		}

		public boolean isCellEditable(int row, int col)
		{
			return false;
		}
		//更新数据
		public void updateData(Vector<PortIn> portinVector)
		{
			this.portinVector=portinVector;
			if(portinVector.size()==0)
			{
				portinVector=new Vector<PortIn>();
			}
			else
			{
				fireTableRowsInserted(0, portinVector.size()-1);
			}
		}

	}
}
