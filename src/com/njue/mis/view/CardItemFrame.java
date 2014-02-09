package com.njue.mis.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.njue.mis.client.RemoteService;
import com.njue.mis.common.CommonUtil;
import com.njue.mis.interfaces.CardItemControllerInterface;
import com.njue.mis.model.CardItem;

public class CardItemFrame  extends JInternalFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8154707297417711740L;
	private List<CardItem> cardItemList;
	private CardItemControllerInterface cardItemService;
	private JTable table;
	private JScrollPane scrollPane;
	private String[] columns = {"条目","账号","余额"};
	private String[] fields = {"name","number","money"};
    private JTextField itemField;
    private JTextField numberField;
    private JTextField moneyField;
    private JButton addButton;
    private JButton deleteButton;
    private JButton updateButton;
	protected Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private CardItem cardItem;
	public CardItemFrame(){
		super("账户期初建账",true,true,true,true);
		this.setBounds(0, 0, screenSize.width * 2 / 3,
				screenSize.height * 4 / 7);
		init();
	}
	public void init(){
		try {
			cardItemService = RemoteService.cardItemService;
			cardItemList = cardItemService.getAllCardItems();
			if(cardItemList == null){
				CommonUtil.showError("没有条目");
				return;
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			CommonUtil.showError("网络错误");
			return;
		}
		initPanel();
	}
	public void initPanel(){
        setLayout(new BorderLayout());
        table = CommonUtil.createTable(columns,cardItemList.toArray(),fields);
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
		{
			@Override
			public void valueChanged(ListSelectionEvent e)
			{
				ListSelectionModel model = (ListSelectionModel)e.getSource();
				int index = model.getMaxSelectionIndex();
				if(index>=0 && cardItemList!=null &&index<cardItemList.size()){
					CardItem cardItem = cardItemList.get(index);
					setCardItem(cardItem);
				}
			}
		});
        table.setPreferredScrollableViewportSize(new Dimension(screenSize.width * 3 / 4,
				screenSize.height  / 8));
        scrollPane = new JScrollPane();
        scrollPane.setViewportView(table);
        scrollPane.setPreferredSize(new Dimension(screenSize.width * 1 / 3,
				screenSize.height * 1/80));
        add(scrollPane, BorderLayout.CENTER);
        
        JPanel panel_input = new JPanel();
        JLabel itemLabel = new JLabel("银行");
        itemField = new JTextField(10);
        JLabel numberLabel = new JLabel("账号");
        numberField = new JTextField(20);
        JLabel moneyLabel = new JLabel("余额");
        moneyField = new JTextField(10);
        addButton = new JButton("添加");
        deleteButton = new JButton("删除");
        deleteButton.addActionListener(new DeleteAction());
        updateButton = new JButton("更新");
        updateButton.addActionListener(new UpdateAction());
        addButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				String item = itemField.getText();
				String number = numberField.getText();
				if(item.length()<=0 || number.length()<=0){
					CommonUtil.showError("请输入银行和账号");
					return;
				}
				double money = CommonUtil.getDoubleFromTextField(moneyField);
				if(money < 0){
					CommonUtil.showError("余额必须为数字");
					return ;
				}
				CardItem cardItem = new CardItem(item, number, money);
				try {
					if(cardItemService.addCardItem(cardItem) == null){
						CommonUtil.showError("添加失败,请查看是否重复");
						return;
					}
					cardItemList.add(cardItem);
					CommonUtil.updateJTable(table, columns, cardItemList.toArray(), fields);
					itemField.setText("");
					numberField.setText("");
					moneyField.setText("");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					CommonUtil.showError("网络错误");
				}
			}
		});
        panel_input.add(itemLabel);
        panel_input.add(itemField);
        panel_input.add(numberLabel);
        panel_input.add(numberField);
        panel_input.add(moneyLabel);
        panel_input.add(moneyField);
        panel_input.add(addButton);
        panel_input.add(deleteButton);
        panel_input.add(updateButton);
        
        add(panel_input, BorderLayout.SOUTH);
	}
	
	public	CardItem getCardItem(){
		return cardItem;
	}
	public void setCardItem(CardItem cardItem){
		this.cardItem = cardItem;
	}
	
	private class DeleteAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			int index = table.getSelectionModel().getMaxSelectionIndex();
			if(index>=0 && cardItemList!=null &&index<cardItemList.size()){
				CardItem cardItem = cardItemList.get(index);
				try {
					if(cardItemService.deleteCardItem(cardItem)){
						CommonUtil.showError("删除成功!");
						cardItemList.remove(cardItem);
						CommonUtil.updateJTable(table, columns, cardItemList.toArray(), fields);
						return;
					}else {
						CommonUtil.showError("删除失败");
						return;
					}
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					CommonUtil.showError("网络错误");
					return;
				}
			}else{
				CommonUtil.showError("请选择一个账号删除");
				return;
			}
		}
		
	}
	private class UpdateAction implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			for(int i = 0; i<table.getRowCount(); i++){
				double money = CommonUtil.formateDouble(table.getValueAt(i, table.getColumnCount()-1).toString());
				if(money <0){
					CommonUtil.showError("第"+i+"行的金额必为数字");
					return;
				}
				cardItemList.get(i).setMoney(money);
			}
			try {
				if(!cardItemService.saveOrUpdateCardItems(cardItemList)){
						CommonUtil.showError("更新失败");
				}else{
					CommonUtil.showError("更新成功");
				}
			} catch (RemoteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				CommonUtil.showError("更新失败，网络错误");
			}
			
		}
		
	}
}
