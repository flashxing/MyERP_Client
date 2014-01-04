package com.njue.mis.common;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.njue.mis.model.Goods;

/**
 * �Զ���һ�����������Ӱ�ť�ĵ�Ԫ��༭������ü̳�DefaultCellEditor����ȻҪʵ�ֵķ�����̫���ˡ�
 * 
 */
public class GoodsButtonEditor extends DefaultCellEditor
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6546334664166791132L;

    private JPanel panel;

    private GoodsButton button;
    private static int number;
    public GoodsButtonEditor()
    {
        // DefautlCellEditor�д˹���������Ҫ����һ�������������ʹ�õ���ֱ��newһ�����ɡ�
        super(new JTextField());
        number++;
        System.out.println("Editor number is:"+number);
        // ���õ�����μ���༭��
        this.setClickCountToStart(1);

        this.initButton();

        this.initPanel();

        // ��Ӱ�ť��
        this.panel.add(this.button);
    }

    private void initButton()
    {
        this.button = new GoodsButton("������");
        this.button.setBounds(0, 0, 80, 20);
    }

    private void initPanel()
    {
        this.panel = new JPanel();

        // panelʹ�þ��Զ�λ������button�Ͳ������������Ԫ��
        this.panel.setLayout(null);
    }


    /**
     * ������д����ı༭����������һ��JPanel���󼴿ɣ�Ҳ����ֱ�ӷ���һ��Button���󣬵��������������������Ԫ��
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
    {
    	if(value != null){
    		this.button.setText(((Goods) value).getGoodsName()); 
    	}else{
    		this.button.setText("");
    	}
        return this.button;
    }

    /**
     * ��д�༭��Ԫ��ʱ��ȡ��ֵ���������д��������ܻ�Ϊ��ť���ô����ֵ��
     */
    @Override
    public Object getCellEditorValue()
    {
        return this.button.getGoods();
    }
    
    public GoodsButton getButton(){
    	return this.button;
    }
}
