package com.njue.mis.common;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
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

import com.njue.mis.client.RemoteService;
import com.njue.mis.interfaces.SalesManControllerInterface;
import com.njue.mis.model.SalesMan;

public class SalesManButton extends JButton{
	/**
	 * 
	 */
	private static final long serialVersionUID = 591316920017790725L;
	private SalesMan salesMan;
	private SalesManChooser salesManChooser;
	public SalesManButton(String text){
		super(text);
		this.setSize(8, 20);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        super.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (salesManChooser == null)
                    salesManChooser = new SalesManChooser();
                Point p = getLocationOnScreen();
                p.y = p.y + 30;
                salesManChooser.showCustomerChooser(p);
            }
        });
	}
	
	public	SalesMan getSalesMan(){
		return salesMan;
	}
	public void setSalesMan(SalesMan salesMan){
		this.salesMan = salesMan;
		setText(salesMan.getName());
	}
	
	private class SalesManChooser extends JPanel implements ActionListener,
    ChangeListener {
		/**
		 * 
		 */
		private static final long serialVersionUID = -2630743773767035918L;
		private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		private int width = screenSize.width *2/5;
		private int height = screenSize.height *5/9;
		private JDialog dialog;
		
		private List<SalesMan> salesManList;
		
		private SalesManControllerInterface salesManService;
		
		private JTable table;
		private JScrollPane scrollPane;
		private String[] columns = {"姓名","联系电话"};
		private String[] fields = {"name","phoneNumber"};
	    
	    private JTextField itemField;
	    private JTextField numberField;
	    private JButton addButton;
	    public SalesManChooser(){
			super();
			init();
		}
		public void init(){
			try {
				salesManService = RemoteService.salesManService;
				salesManList = salesManService.getAllSalesMans();
				if(salesManList == null){
					CommonUtil.showError("没有业务员");
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
	        table = CommonUtil.createTable(columns,salesManList.toArray(),fields);
	        table.getSelectionModel().addListSelectionListener(new ListSelectionListener()
			{
				@Override
				public void valueChanged(ListSelectionEvent e)
				{
					ListSelectionModel model = (ListSelectionModel)e.getSource();
					int index = model.getMaxSelectionIndex();
					if(index>=0 && salesManList!=null &&index<salesManList.size()){
						SalesMan salesMan = salesManList.get(index);
						setSalesMan(salesMan);
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
	        JLabel itemLabel = new JLabel("业务员");
	        itemField = new JTextField(10);
	        JLabel numberLabel = new JLabel("联系电话");
	        numberField = new JTextField(20);
	        addButton = new JButton("添加");
	        addButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					String item = itemField.getText();
					String number = numberField.getText();
					if(item.length()<=0||(number.length()>0&&!ValidationManager.validatePhone(number))){
						CommonUtil.showError("请输入业务员名称和正确的联系电话");
						return;
					}
					SalesMan salesMan = new SalesMan();
					salesMan.setName(item);
					salesMan.setPhoneNumber(number);
					try {
						if(salesManService.addSalesMan(salesMan) <= 0){
							CommonUtil.showError("添加失败,请查看是否重复");
							return;
						}
						salesManList.add(salesMan);
						CommonUtil.updateJTable(table, columns, salesManList.toArray(), fields);
						itemField.setText("");
						numberField.setText("");
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
            JDialog result = new JDialog(owner, "业务员选择", true);
            result.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
            result.getContentPane().add(this, BorderLayout.CENTER);
            result.pack();
            result.setSize(width, height);
            return result;
        }
		@SuppressWarnings("deprecation")
		public void showCustomerChooser(Point position){
			Frame owner = (Frame) SwingUtilities
                    .getWindowAncestor(SalesManButton.this);
            if (dialog == null || dialog.getOwner() != owner)
                dialog = createDialog(owner);
            dialog.setLocation((screenSize.width - width) / 2,
                    (screenSize.height - height) / 2);
            dialog.show();
		}
	}
	@Override
	public String toString(){
		if(this.salesMan!=null){
			return this.salesMan.getName();
		}else{
			return "";
		}
	}
}
