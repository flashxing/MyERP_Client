package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.common.DateChooserJButton;
import com.njue.mis.interfaces.MoneyControllerInterface;
import com.njue.mis.interfaces.PortControllerInterface;
import com.njue.mis.interfaces.SalesControllerInterface;
import com.njue.mis.model.Money;
import com.njue.mis.model.MoneyItemDetail;
import com.njue.mis.model.Port;
import com.njue.mis.model.PortBack;
import com.njue.mis.model.PortIn;
import com.njue.mis.model.Sales;
import com.njue.mis.model.StatisticObject;

public class AllStatisticFrame extends JInternalFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7433011447851925736L;
	protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected DateChooserJButton beginButton;
	protected DateChooserJButton endButton;
	protected JButton queryButton;
	protected JButton submitButton;
	protected JButton updateButton;
	protected JTable table;
	
	private List<Sales> salesIns;
	private List<Sales> salesBacks;
	private List<PortIn> portIns;
	private List<PortBack> portBacks;
	private List<Money> moneys;
	protected SalesControllerInterface salesService;
	protected PortControllerInterface portService;
	protected MoneyControllerInterface moneyService;
	
	public String[] columns = {"类别","项目","金额"};
	public String[] fields = {"itemName","subItemName","number"};
	
	public AllStatisticFrame()
	{
		super("经营历程表",true,true,true,true);
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
//		updateButton.addActionListener(new UpdateAction());
//		submitButton.addActionListener(new SubmitAction());
		commitPanel.add(updateButton);
		commitPanel.add(submitButton);
		
		panel.add(panel_input, BorderLayout.NORTH);
		panel.add(outputPanel, BorderLayout.CENTER);
//		panel.add(commitPanel, BorderLayout.SOUTH);
	}
	public void initIn(){
		salesService = RemoteService.salesService;
		portService = RemoteService.portService;
		moneyService = RemoteService.moneyService;
		queryButton.addActionListener(new QueryAction());
	}
	private class QueryAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			String begin = beginButton.getDateString();
			String end = endButton.getDateString();
			try {
				salesIns = salesService.getAllSalesInByTime(begin, end);
				salesBacks = salesService.getAllSalesBackByTime(begin, end);
				moneys = moneyService.getMoneysByTime(begin, end);
				portIns = portService.searchPortInByTime(begin, end);
				portBacks = portService.searchPortBackByTime(begin, end);
				if(salesIns == null||salesBacks==null||moneys==null||portBacks==null||portIns==null){
					CommonUtil.showError("获取数据失败");
					return;
				}
				Map<String, Double> moneyMap = calMoney(moneys);
				double moneyTotal = 0;
				for(Entry<String, Double> mapEntry:moneyMap.entrySet()){
					moneyTotal += mapEntry.getValue();
				}
				
				List<StatisticObject> statisticObjects = new ArrayList<>();
				double salesInMoney = calSalesTotal(salesIns);
				double portBackMoney = calPortBackTotal(portBacks);
				statisticObjects.add(new StatisticObject("收入类","",salesInMoney+portBackMoney));
				statisticObjects.add(new StatisticObject("","销售额",salesInMoney));
				statisticObjects.add(new StatisticObject("","进货退货",portBackMoney));
				double salesBackMoney = calSalesTotal(salesBacks);
				double portInMoney = calPortInTotal(portIns);
				statisticObjects.add(new StatisticObject("支出类","",salesBackMoney+portInMoney+moneyTotal));
				statisticObjects.add(new StatisticObject("","销售退货",salesBackMoney));
				statisticObjects.add(new StatisticObject("","进货",portInMoney));

				for(Entry<String, Double> mapEntry:moneyMap.entrySet()){
					statisticObjects.add(new StatisticObject("",mapEntry.getKey(),mapEntry.getValue()));
				}
				CommonUtil.updateJTable(table, columns, statisticObjects.toArray(), fields);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
			}
		}
		
	}
	private double calSalesTotal(List<Sales> salesList){
		double result = 0.0;
		for(Sales sales: salesList){
			result+=sales.getPrice();
		}
		return result;
	}
	
	private double calPortBackTotal(List<PortBack> portList){
		double result = 0.0;
		for(Port port : portList){
			result += port.getPrice();
		}
		return result;
	}
	
	private double calPortInTotal(List<PortIn> portList){
		double result = 0.0;
		for(Port port : portList){
			result += port.getPrice();
		}
		return result;
	}
	
	private Map<String, Double> calMoney(List<Money> moneys){
		Map<String, Double> moneyMap = new HashMap<String, Double>();
		for(Money money : moneys){
			for(MoneyItemDetail moneyItemDetail : money.getMoneyItemDetailList()){
				if(!moneyMap.containsKey(moneyItemDetail.getMoneyItem())){
					moneyMap.put(moneyItemDetail.getMoneyItem(), moneyItemDetail.getMoney());
				}else{
					moneyMap.put(moneyItemDetail.getMoneyItem(), moneyItemDetail.getMoney()+moneyMap.get(moneyItemDetail.getMoneyItem()));
				}
			}
		}
		return moneyMap;
	}
}
