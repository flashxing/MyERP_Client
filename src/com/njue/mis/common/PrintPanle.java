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
		JLabel label = new JLabel("打印样式");
		printStyleComboBox = new JComboBox<>(new DefaultComboBoxModel<String>());
		printStyleComboBox.addItem("打印样式一");
		printStyleComboBox.addItem("打印样式二");
		printStyleComboBox.setSelectedIndex(0);
		printButton = new JButton("打印");
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
