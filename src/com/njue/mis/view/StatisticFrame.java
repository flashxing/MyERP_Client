package com.njue.mis.view;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import com.njue.mis.common.DateChooserJButton;

public class StatisticFrame extends JInternalFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3022256299053826183L;
	protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected DateChooserJButton beginButton;
	protected DateChooserJButton endButton;
	protected JButton queryButton;
	protected JTextField totalMoney;
	
	protected JTable table;
	public StatisticFrame(String frameName)
	{
		super(frameName,true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.init();
	}
	public void init(){
		JPanel panel = new JPanel(new BorderLayout());
		getContentPane().add(panel);
		
		JPanel panel_input = new JPanel();
		
		JLabel labelBegin = new JLabel("起始时间:");
		panel_input.add(labelBegin);
		beginButton = new DateChooserJButton();
		panel_input.add(beginButton);
		
		JLabel labelEnd = new JLabel("截止时间:");
		endButton = new DateChooserJButton();
		panel_input.add(labelEnd);
		panel_input.add(endButton);
		queryButton = new JButton("查询");
		panel_input.add(queryButton);
		
		JPanel panel_output = new JPanel();
		JLabel totalTable = new JLabel("总额：");
		totalMoney = new JTextField(10);
		panel_output.add(totalTable);
		panel_output.add(totalMoney);
		table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height * 2/ 5));
		panel_output.add(new JScrollPane(table));
		
		panel.add(panel_input, BorderLayout.NORTH);
		panel.add(panel_output, BorderLayout.CENTER);
	}
}
