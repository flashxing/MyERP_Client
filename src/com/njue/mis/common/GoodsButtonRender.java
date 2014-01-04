package com.njue.mis.common;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import com.njue.mis.model.Goods;

public class GoodsButtonRender implements TableCellRenderer
{
    private JPanel panel;

    private GoodsButton button;
    private static int number=0;
    public GoodsButtonRender()
    {
    	number++;
    	System.out.println("Goods Button Render number is:"+number);
        this.initButton();

        this.initPanel();

        // Ìí¼Ó°´Å¥¡£
        this.panel.add(this.button);
    }

    private void initButton()
    {
        this.button = new GoodsButton("...");
    }

    private void initPanel()
    {
        this.panel = new JPanel();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column)
    {
    	if(value != null){
    		this.button.setText(((Goods) value).getGoodsName()); 
    	}else{
    		this.button.setText("");
    	}
        return this.button;
    }

	public GoodsButton getButton() {
		// TODO Auto-generated method stub
		return this.button;
	}

}
