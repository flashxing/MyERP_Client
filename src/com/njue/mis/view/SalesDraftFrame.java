package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.DateChooserJButton;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.model.SalesIn;

public class SalesDraftFrame extends JInternalFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7957137597276118098L;
	protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected DateChooserJButton beginButton;
	protected DateChooserJButton endButton;
	protected JButton queryButton;
	protected JButton submitButton;
	protected JButton updateButton;
	protected JTable table;
	
	private List<SalesIn> salesList;
	protected String[] salesColumns={"编号","客户","商品","金额","操作人","时间"};
	protected String[] salesFields={"id","customerId","goodsId","totalPrice","operatePerson","time"};
	
	protected SalesControllerInterface salesService;
	
	public SalesDraftFrame()
	{
		super("业务单稿",true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		this.init();
		initIn();
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
		
		JPanel outputPanel = new JPanel();
		table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 2 / 3-60,
				screenSize.height * 2/ 5));
		outputPanel.add(new JScrollPane(table));
		
		JPanel commitPanel = new JPanel();
		updateButton = new JButton("查看单稿");
		submitButton = new JButton("入帐");
		updateButton.addActionListener(new UpdateAction());
		submitButton.addActionListener(new SubmitAction());
		commitPanel.add(updateButton);
		commitPanel.add(submitButton);
		
		panel.add(panel_input, BorderLayout.NORTH);
		panel.add(outputPanel, BorderLayout.CENTER);
		panel.add(commitPanel, BorderLayout.SOUTH);
	}
	public void initIn(){
		salesService = RemoteService.salesService;
		queryButton.addActionListener(new QueryAction());
	}
	private class QueryAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String begin = beginButton.getDateString();
			String end = endButton.getDateString();
			try {
				salesList = salesService.getAllSalesDraft(begin, end);
				if(salesList != null){
					CommonUtil.updateJTable(table, salesColumns, salesList.toArray(), salesFields);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
		
	}
	
	private class SubmitAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			SalesIn salesIn = getSelectedSalesIn();
			if(salesIn==null){
				CommonUtil.showError("请选择一个业务单稿");
				return ;
			}
			try {
				if(!salesService.setSalesToPublished(salesIn)){
					CommonUtil.showError("入账失败");
					return;
				}else{
					CommonUtil.showError("入账成功");
					table.remove(table.getSelectedRow());
					return;
				}
			} catch (Exception e1) {
				e1.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
		
	}
	
	private class UpdateAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			SalesIn salesIn = getSelectedSalesIn();
			if(salesIn==null){
				CommonUtil.showError("请选择一个业务单稿");
				return ;
			}
			SalesDraftEditFrame salesDraftEditFrame = new SalesDraftEditFrame(salesIn);
			MainFrame.getMainFrame().getContentPane().add(
					salesDraftEditFrame);
			salesDraftEditFrame.setVisible(true);
		}
		
	}
	
	private SalesIn getSelectedSalesIn(){
		SalesIn selected = null;
		int index = table.getSelectedRow();
		if(salesList==null||index <0 || index>=salesList.size()){
			return null;
		}else{
			selected = salesList.get(index);
		}
		return selected;
	}
}
