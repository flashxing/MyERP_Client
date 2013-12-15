package com.njue.mis.common;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.njue.mis.client.Configure;
import com.njue.mis.interfaces.MoneyItemControllerInterface;
import com.njue.mis.model.MoneyItem;

public class MoneyItemButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4444753428626123730L;
	private MoneyItem moneyItem;
	private MoneyItemChooser moneyItemChooser;
	public MoneyItemButton(String text){
		super(text);
		this.setSize(5, 20);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        super.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (moneyItemChooser == null)
                    moneyItemChooser = new MoneyItemChooser();
                Point p = getLocationOnScreen();
                p.y = p.y + 30;
                moneyItemChooser.showCustomerChooser(p);
            }
        });
	}
	
	public	MoneyItem getMoneyItem(){
		return moneyItem;
	}
	public void setMoneyItem(MoneyItem moneyItem2){
		this.moneyItem = moneyItem2;
		setText(moneyItem2.getName());
	}
	
	private class MoneyItemChooser extends JPanel implements ActionListener,
    ChangeListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = 8446320077101070001L;
		private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		private int width = screenSize.width *1/3;
		private int height = screenSize.height *5/9;
		private JDialog dialog;
		
		private List<MoneyItem> moneytemList;
		
		private MoneyItemControllerInterface moneyItemService;
		
		private JTable table;
		private JScrollPane scrollPane;
		private String[] columns = {"条目"};
		private String[] fields = {"name"};
	    
	    private JTextField itemField;
	    private JButton addButton;
	    public MoneyItemChooser(){
			super();
			init();
		}
		public void init(){
			try {
				moneyItemService = (MoneyItemControllerInterface) Naming.lookup(Configure.MoneyItemController);
				moneytemList = moneyItemService.getAll();
				if(moneytemList == null){
					CommonUtil.showError("没有条目");
					return;
				}
			} catch (MalformedURLException | RemoteException
					| NotBoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				CommonUtil.showError("网络错误");
				return;
			}
			initPanel();
		}
		
		public void initPanel(){
            setLayout(new BorderLayout());
	        table = CommonUtil.createTable(columns,moneytemList.toArray(),fields);
	        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent e)
				{
					ListSelectionModel model = (ListSelectionModel)e.getSource();
					int index = model.getMaxSelectionIndex();
					if(index>=0 && moneytemList!=null &&index<moneytemList.size()){
						MoneyItem moneyItem = moneytemList.get(index);
						setMoneyItem(moneyItem);
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
	        itemField = new JTextField(10);
	        addButton = new JButton("添加");
	        addButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					String item = itemField.getText();
					if(item.length()<=0){
						CommonUtil.showError("请输入条目");
						return;
					}
					MoneyItem moneyItem = new MoneyItem();
					moneyItem.setName(item);
					try {
						if(moneyItemService.addMoneyItem(moneyItem) == null){
							CommonUtil.showError("添加失败,请查看是否重复");
							return;
						}
						moneytemList.add(moneyItem);
						CommonUtil.updateJTable(table, columns, moneytemList.toArray(), fields);
						itemField.setText("");
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						CommonUtil.showError("网络错误");
					}
				}
			});
	        panel_input.add(itemField);
	        panel_input.add(addButton);
	        
	        add(panel_input, BorderLayout.SOUTH);
		}
		@Override
		public void stateChanged(ChangeEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
        private JDialog createDialog(Frame owner) {
            JDialog result = new JDialog(owner, "费用条目选择", true);
            result.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            result.getContentPane().add(this, BorderLayout.CENTER);
            result.pack();
            result.setSize(width, height);
            return result;
        }
		@SuppressWarnings("deprecation")
		public void showCustomerChooser(Point position){
			Frame owner = (Frame) SwingUtilities
                    .getWindowAncestor(MoneyItemButton.this);
            if (dialog == null || dialog.getOwner() != owner)
                dialog = createDialog(owner);
            dialog.setLocation((screenSize.width - width) / 2,
                    (screenSize.height - height) / 2);
            dialog.show();
		}
	}

}
