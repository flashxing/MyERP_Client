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
 * 自定义一个往列里边添加按钮的单元格编辑器。最好继承DefaultCellEditor，不然要实现的方法就太多了。
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
        // DefautlCellEditor有此构造器，需要传入一个，但这个不会使用到，直接new一个即可。
        super(new JTextField());
        number++;
        System.out.println("Editor number is:"+number);
        // 设置点击几次激活编辑。
        this.setClickCountToStart(1);

        this.initButton();

        this.initPanel();

        // 添加按钮。
        this.panel.add(this.button);
    }

    private void initButton()
    {
        this.button = new GoodsButton("。。。");
        this.button.setBounds(0, 0, 80, 20);
    }

    private void initPanel()
    {
        this.panel = new JPanel();

        // panel使用绝对定位，这样button就不会充满整个单元格。
        this.panel.setLayout(null);
    }


    /**
     * 这里重写父类的编辑方法，返回一个JPanel对象即可（也可以直接返回一个Button对象，但是那样会填充满整个单元格）
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
     * 重写编辑单元格时获取的值。如果不重写，这里可能会为按钮设置错误的值。
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
