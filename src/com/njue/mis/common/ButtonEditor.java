package com.njue.mis.common;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;

import com.njue.mis.model.ReceiptItem;

/**
 * �Զ���һ�����������Ӱ�ť�ĵ�Ԫ��༭������ü̳�DefaultCellEditor����ȻҪʵ�ֵķ�����̫���ˡ�
 * 
 */
public class ButtonEditor<T extends JButton> extends DefaultCellEditor
{

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = -6546334664166791132L;

    private JPanel panel;

    private T button;
    private Class tClass;
    public ButtonEditor(Class tClass)
    {
        // DefautlCellEditor�д˹���������Ҫ����һ�������������ʹ�õ���ֱ��newһ�����ɡ�
        super(new JTextField());
        this.tClass = tClass;
        // ���õ�����μ���༭��
        this.setClickCountToStart(1);

        this.initButton();

        this.initPanel();

        // ��Ӱ�ť��
        this.panel.add(this.button);
    }

    private void initButton()
    {	
        try {
			this.button = (T) tClass.getConstructor(String.class).newInstance("...");
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        // ���ð�ť�Ĵ�С��λ�á�
        this.button.setBounds(0, 0, 80, 20);

        // Ϊ��ť����¼�������ֻ�����ActionListner�¼���Mouse�¼���Ч��
//        this.button.addActionListener(new ActionListener()
//        {
//            public void actionPerformed(ActionEvent e)
//            {
//                // ����ȡ���༭���¼����������tableModel��setValue������
//                MyButtonEditor.this.fireEditingCanceled();
//
//                // �������������������
//                // ���Խ�table���룬ͨ��getSelectedRow,getSelectColumn������ȡ����ǰѡ����к��м����������ȡ�
//            }
//        });

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
    		this.button.setText(((T) value).toString()); 
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
        return this.button;
    }
    
    public T getButton(){
    	return this.button;
    }

}
