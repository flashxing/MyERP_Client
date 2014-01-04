package com.njue.mis.common;

import java.awt.Component;
import java.io.ObjectInputStream.GetField;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;

import com.njue.mis.model.Category;
import com.njue.mis.model.Goods;
import com.njue.mis.model.Role;
import com.njue.mis.model.User;

public class CommonUtil {
	private static DecimalFormat df=new DecimalFormat(".##");
	/*
	 * to update the JTable
	 */
	public static void updateJTable(JTable table, Object[] column, String[] fieldsToShow, Object[] ...objects){
		if (objects.length <= 0){
			showError("传入对象错误");
		}

		Object[] object = objects[0];
		final Object columns[] = new Object[column.length];
		for(int i = 0; i < column.length; ++i){
			columns[i] = column[i];
		}
//		columns[column.length] = "添加";
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		List<Class<?>> list = null;
		if(object.length > 0){
			list = new ArrayList<Class<?>>();
			for(int i = 0; i < objects.length; ++i){
				list.add(objects[i][0].getClass());
			}
		}
		for(Class<?> class1:list){
			System.out.print(class1);
		}
		System.out.println();
		for(int i = 0; i <object.length; ++i){
			Vector vdata = new Vector();
			for(String str : fieldsToShow){
				System.out.println("deal with "+str);
				Field field = getFieldByClasses(list, str);
				if (field == null){
					System.out.println(str+" not found");
					CommonUtil.showError("数据转换错误");
				}
				field.setAccessible(true);
				try {  
					List<Object> list2 = new ArrayList<>();
					for(int j = 0; j < objects.length; ++j){
						list2.add(objects[j][i]);
					}
	                vdata.add(getValue(list2.toArray(), field)); 
                } catch (IllegalArgumentException e) {  
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
	public static JTable createTable(Object[] column, String[] fieldsToShow, Object[] ...objects){
		if (objects.length <= 0){
			showError("传入对象错误");
		}

		Object[] object = objects[0];
		final Object columns[] = new Object[column.length];
		for(int i = 0; i < column.length; ++i){
			columns[i] = column[i];
		}
//		columns[column.length] = "修改";
		JTable table = new JTable(){
			@Override
            public TableCellRenderer getCellRenderer(int row, int column) {
//                if (column == columns.length-1 && row >= 0) {
//                    return new TableCellRenderer() {
//                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                            return new JButton();
//                        }
//                    };
//                }
                return super.getCellRenderer(row, column);
            }
		};
		DefaultTableModel model = new DefaultTableModel(columns, 0);
		List<Class<?>> list = null;
		if(object.length > 0){
			list = new ArrayList<>();
			for(int i = 0; i < objects.length; ++i){
				list.add(objects[i][0].getClass());
			}
		}
		for(int i = 0; i <object.length; ++i){
			Vector vdata = new Vector();
			for(String str : fieldsToShow){
				Field field = getFieldByClasses(list, str);
				if (field == null){
					CommonUtil.showError("数据转换错误");
				}
				field.setAccessible(true);
				try {  
					List<Object> list2 = new ArrayList<>();
					for(int j = 0; j < objects.length; ++j){
						list2.add(objects[i][j]);
					}
	                vdata.add(getValue(list2.toArray(), field)); 
                } catch (IllegalArgumentException e) {  
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
	/*
	 * @params object the table to show
	 * @params string[] fieldsTOShow the fields of the object to show
	 * use the reflection to get the member of the object to be the table column.
	 */
	public static JTable createTable(Object[] column, Object[] object, String[] fieldsToShow){
		if (object == null || object.length < 0){
			showError("传入对象错误");
		}
		final Object columns[] = new Object[column.length];
		for(int i = 0; i < column.length; ++i){
			columns[i] = column[i];
		}
//		columns[column.length] = "修改";
		JTable table = new JTable(){
			@Override
            public TableCellRenderer getCellRenderer(int row, int column) {
//                if (column == columns.length-1 && row >= 0) {
//                    return new TableCellRenderer() {
//                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//                            return new JButton();
//                        }
//                    };
//                }
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
	public static Object getValue(Object[] objects, Field field){
		for(Object object:objects){
			try {
				Object object2 = field.get(object);
				return object2;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				continue;
			}
		}
		return null;
	}
    public static Field getFieldByClasses(List<Class<?>> classes, String str){
    	System.out.println("Find "+str+" in "+classes+" "+classes.size());
    	Field field = null;
    	for(Class<?> class1:classes){
    		if((field = getFieldByClass(class1, str)) != null){
    			return field;
    		}
    	}
    	return null;
    }
    
    public static Field getFieldByClass(Class<?> class1, String str){
    	System.out.println("Find "+str+" in "+class1);
    	Field field = null;
    	Class<?> myClass;
    	for(myClass = class1;myClass!= Object.class;){
    		try {
				field = myClass.getDeclaredField(str);
				break;
			} catch (NoSuchFieldException e) {
				myClass=myClass.getSuperclass();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	return field;
    }
	public static Field getField(Object obj,String str){
		System.out.println("call here");
    	Field field = null;
    	Class<?> myClass;
    	for(myClass = obj.getClass();myClass!= Object.class;){
    		try {
				field = myClass.getDeclaredField(str);
				break;
			} catch (NoSuchFieldException e) {
				// TODO Auto-generated catch block
//				field = getFieldInDeclaredClass(myClass, str);
//				if(field == null){
				myClass=myClass.getSuperclass();
//				}else{
//					return field;
//				}
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
		final Object columns[] = new Object[column.length];
		for(int i = 0; i < column.length; ++i){
			columns[i] = column[i];
		}
//		columns[column.length] = "添加";
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
	public static List<Integer> getSubCategory(List<Category> list, int cateId){
		List<Integer> subCateList = new ArrayList<>();
		Set<Integer> toAddSet = new HashSet<>();
		Set<Integer> tmpSet = new HashSet<>();
		toAddSet.add(cateId);
		while(toAddSet.size() > 0){
			subCateList.addAll(toAddSet);
			tmpSet.clear();
			for(Integer id: toAddSet){
				for(Category category:list){
					if(category.getPrefer_id() == id){
						tmpSet.add(category.getCate_id());
					}
				}
			}
			toAddSet.clear();
			toAddSet.addAll(tmpSet);
		}
		return subCateList;
	}
	private static Category getCategoryById(int cate_id, List<Category> list){
		for(Category category:list){
			if(category.getCate_id() == cate_id){
				return category;
			}
		}
		return null;
	}
	
	private static int createNode(HashMap<Integer, DefaultMutableTreeNode> map, int father_id, DefaultMutableTreeNode node, List<Category> list){
		Category category = getCategoryById(father_id, list);
		if (category == null){
			return -1;
		}
		DefaultMutableTreeNode father = new DefaultMutableTreeNode(category);
		map.put(father_id, father);
		father.add(node);
		return category.getPrefer_id();
	}
	
	public static DefaultMutableTreeNode buildTree(List<Category> list){
        HashMap<Integer, DefaultMutableTreeNode> map = new HashMap<Integer, DefaultMutableTreeNode>();
    	DefaultMutableTreeNode root = new DefaultMutableTreeNode();
    	map.put(0, root);
        for(Category category:list){
        	if(map.containsKey(category.getCate_id())){
        		continue;
        	}
        	DefaultMutableTreeNode node = new DefaultMutableTreeNode(category);
        	map.put(category.getCate_id(), node);
        	DefaultMutableTreeNode father;
        	int father_id = category.getPrefer_id();
        	while ((father = map.get(father_id)) == null){
        		int tmp_id = createNode(map, father_id, node, list);
        		if (tmp_id < 0){
        			break;
        		}
        		node = map.get(father_id);
        		father_id = tmp_id;
        	}
        	if(father != null){
        		if(father != node){
        			father.add(node);
        		}
        	}
        }
        return root;
    }
    public static void setDuiqi(JTable table)     
    {     
        //对其方式设置     
        DefaultTableCellRenderer d = new DefaultTableCellRenderer();     
                  
        //设置表格单元格的对齐方式为居中对齐方式     
        d.setHorizontalAlignment(JLabel.RIGHT);     
        for(int i = 0; i< table.getColumnCount();i++)     
        {     
            TableColumn col = table.getColumn(table.getColumnName(i));     
            col.setCellRenderer(d);     
        }     
    }
    
    public static double getDoubleFromTextField(JTextField textField){
    	double result = 0.0;
    	try{
    		System.out.println(textField.getText());
    		result = Double.parseDouble(df.format(Double.parseDouble(textField.getText())));
    	}catch (Exception ex){
    		ex.printStackTrace();
    		return -1;
    	}
    	return result;
    }
    
    public static double formateDouble(double number){
    	return Double.parseDouble(df.format(number));
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
