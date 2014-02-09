package com.njue.mis.common;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PrintPanle extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6611162801406029973L;
	private JComboBox<String> printStyleComboBox;
	private JButton printButton;
	public PrintPanle(){
		JLabel label = new JLabel("��ӡ��ʽ");
		printStyleComboBox = new JComboBox<>(new DefaultComboBoxModel<String>());
		printStyleComboBox.addItem("��ӡ��ʽһ");
		printStyleComboBox.addItem("��ӡ��ʽ��");
		printStyleComboBox.setSelectedIndex(0);
		printButton = new JButton("��ӡ");
		this.add(label);
		this.add(printStyleComboBox);
		this.add(printButton);
	}
	public JComboBox<String> getPrintStyleComboBox() {
		return printStyleComboBox;
	}
	public void setPrintStyleComboBox(JComboBox<String> printStyleComboBox) {
		this.printStyleComboBox = printStyleComboBox;
	}
	public JButton getPrintButton() {
		return printButton;
	}
	public void setPrintButton(JButton printButton) {
		this.printButton = printButton;
	}
	

}
