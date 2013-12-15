package com.njue.mis.common;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MyButtonRender implements TableCellRenderer
{
    private JPanel panel;

    private ReceiptItemButton button;

    public MyButtonRender()
    {
        this.initButton();
        this.initPanel();
        // 添加按钮。
        this.panel.add(this.button);
    }

    private void initButton()
    {
        this.button = new ReceiptItemButton("...");
    }

    private void initPanel()
    {
        this.panel = new JPanel();
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row,
            int column)
    {
        // 只为按钮赋值即可。也可以作其它操作，如绘背景等。
        this.button.setText(value == null ? "" : String.valueOf(value));

        return this.button;
    }

}
