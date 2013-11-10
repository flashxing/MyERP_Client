package com.njue.mis.common;

import java.awt.Component;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Field;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.njue.mis.model.Goods;
import com.njue.mis.model.Role;
import com.njue.mis.model.User;

public class CommonUtil {
	/*
	 * @params object the table to show
	 * @params string[] fieldsTOShow the fields of the object to show
	 * use the reflection to get the member of the object to be the table column.
	 */
	public static JTable createTable(Object[] column, Object[] object, String[] fieldsToShow){
		if (object == null || object.length < 0){
			showError("传入对象错误");
		}
		final Object columns[] = new Object[column.length+1];
		for(int i = 0; i < column.length; ++i){
			columns[i] = column[i];
		}
		columns[column.length] = "修改";
		JTable table = new JTable(){
			@Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == columns.length-1 && row >= 0) {
                    return new TableCellRenderer() {
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                            return new JButton();
                        }
                    };
                }
                return super.getCellRenderer(row, column);
            }
		};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		for(Object obj:object){
			Vector vdata = new Vector();
			for(String str : fieldsToShow){
				Field field = getField(obj, str);
				if (field == null){
					CommonUtil.showError("数据转换错误");
				}
				field.setAccessible(true);
				try {  
	                vdata.add(field.get(obj)); 
                } catch (IllegalArgumentException e) {  
                    // TODO Auto-generated catch block
                	CommonUtil.showError("数据转换错误");
                    e.printStackTrace();  
                } catch (IllegalAccessException e) {  
                    // TODO Auto-generated catch block 
                	CommonUtil.showError("数据转换错误");
                    e.printStackTrace();  
                } catch (SecurityException e) {
					// TODO Auto-generated catch block
					CommonUtil.showError("数据转换错误");
					e.printStackTrace();
				} 
			}
			model.addRow(vdata);
		}
		table.setModel(model);
		return table;
	}
	
    public static Field getField(Object obj,String str){
    	Field field = null;
    	Class myClass;
    	for(myClass = obj.getClass();myClass!= Object.class;){
    		try {
				field = myClass.getDeclaredField(str);
				break;
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
				myClass=myClass.getSuperclass();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return field;
    }
	/*
	 * to update the JTable
	 */
	public static void updateJTable(JTable table, Object[] column, Object[] object, String[] fieldsToShow){
		if (object == null || object.length < 0){
			showError("传入对象错误");
		}
		final Object columns[] = new Object[column.length+1];
		for(int i = 0; i < column.length; ++i){
			columns[i] = column[i];
		}
		columns[column.length] = "添加";
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		for(Object obj:object){
			Vector vdata = new Vector();
			for(String str : fieldsToShow){
				Field field = getField(obj, str);
				if (field == null){
					CommonUtil.showError("数据转换错误");
					return;
				}
				field.setAccessible(true);
				try {  
	                vdata.add(field.get(obj)); 
                } catch (IllegalArgumentException e) {  
                    // TODO Auto-generated catch block
                	CommonUtil.showError("数据转换错误");
                    e.printStackTrace();  
                } catch (IllegalAccessException e) {  
                    // TODO Auto-generated catch block 
                	CommonUtil.showError("数据转换错误");
                    e.printStackTrace();  
                } catch (SecurityException e) {
					// TODO Auto-generated catch block
					CommonUtil.showError("数据转换错误");
					e.printStackTrace();
				} 
			}
			model.addRow(vdata);
		}
		table.setModel(model);
	}
	
	public static void showError(String string){
		JOptionPane.showMessageDialog(null, string,"警告",JOptionPane.WARNING_MESSAGE);
	}
	
	public static boolean hasPermisson(User user, String string){
		if(user == null){
			return false;
		}
		for(Role role:user.getRoleSet()){
			for(String str:role.getModuleSet()){
				if(str.equals(string)){
					return true;
				}
			}
		}
		return false;
	}
	public static boolean stringInRolesName(String string, Set<Role> roleSet){
		if(roleSet == null){
			return false;
		}
		for(Role role:roleSet){
			if(role.getRoleName().equals(string)){
				return true;
			}
		}
		return false;
	}
	
//	public static void main(String[] args){
//		Goods good1 = new Goods();
//        good1.set_package("package");
//        good1.setAvailable(1);
//        good1.setDescription("description");
//        good1.setGoodsName("goodsName");
//        good1.setId("id");
//        good1.setPrice(12.44);
//        good1.setProducePlace("producePlace");
//        good1.setProductCode("productCode");
//        good1.setPromitCode("promitCode");
//        good1.setProviderId("providerId");
//        good1.setSize("size");
//        Goods good = new Goods();
//        good.set_package("package");
//        good.setAvailable(1);
//        good.setDescription("description");
//        good.setGoodsName("goodsName");
//        good.setId("id");
//        good.setPrice(12.44);
//        good.setProducePlace("producePlace");
//        good.setProductCode("productCode");
//        good.setPromitCode("promitCode");
//        good.setProviderId("providerId");
//        good.setSize("size");
//        
//        Goods[] goodList = {good1,good};
//        Object[] objects = {"test1","test2"};
//        JTable table = CommonUtil.createTable(objects,goodList);
//	}
}
