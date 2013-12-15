package com.njue.mis.view;
import javax.swing.table.*;
import javax.swing.JTable.*;
import javax.swing.*;
import java.awt.Component;
import java.awt.event.*;
/**
*在Jtable中添加一列按钮
* @author xuan
*/
public class ExtensionCellRenderer extends AbstractCellEditor 
        implements TableCellRenderer, TableCellEditor,ActionListener{
        JTable table;
        JButton renderButton;
        JButton editButton;
        String text="测试按钮";

        public ExtensionCellRenderer(JTable table, int column)
        {
            super();
            this.table = table;
            renderButton = new JButton("按钮1");

            editButton = new JButton("按钮2");
            editButton.setFocusPainted( false );
            editButton.addActionListener( this );

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(column).setCellRenderer( this );
            columnModel.getColumn(column).setCellEditor( this );
            renderButton.addActionListener(this);
        }

        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (hasFocus)
            {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }
            else if (isSelected)
            {
                renderButton.setForeground(table.getSelectionForeground());
                renderButton.setBackground(table.getSelectionBackground());
            }
            else
            {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }

            renderButton.setText( (value == null) ? "" : "我来了" );
            return renderButton;
        }

        public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column)
        {
            text = (value == null) ? "" : value.toString();
            editButton.setText( text );
            return editButton;
        }

        public Object getCellEditorValue()
        {
            return text;
        }

        public void actionPerformed(ActionEvent e)
        {
            fireEditingStopped();
            System.out.println( e.getActionCommand() + " : " + table.getSelectedRow());
        }
        public static void main(String[] args){
        	JTable table =new JTable();
        	ExtensionCellRenderer render = new ExtensionCellRenderer(table,1);
        }
}