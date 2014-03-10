package com.njue.mis.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
public class MyExcel {
	private WritableWorkbook book; 
	private WritableSheet sheet;
	public MyExcel(String fileNames){
		try{
		book = Workbook.createWorkbook( new File( fileNames ));
		sheet = book.createSheet( " 商品 " , 0 );
		} catch(Exception e){
			System.out.println(e);
		}
	}
	private void addColumnNames(String[] names){
		int index=0;
		for(String name:names){
			Label label=new Label(index,0,name);
			try {
				sheet.addCell(label);
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			index++;
		}
	}
	private void addData(String[] Datas,int rowIndex){
		int index=0;
		for(String data:Datas){
			Label label=new Label(index,rowIndex,data);
			try {
				sheet.addCell(label);
			} catch (RowsExceededException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (WriteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			index++;
		}
		
	}
	public void generateFile(String[] columnNames, List dataList){
		addColumnNames(columnNames);
		int number = 1;

		for(Object object:dataList){
			Class class1 = object.getClass();
			Field[] fields = class1.getDeclaredFields();
			for(int i=0; i<columnNames.length; ++i){
				try {
					fields[i].setAccessible(true);
					String toShow = fields[i].get(object).toString();
					Label label=new Label(i,number,toShow);
					try {
						sheet.addCell(label);
					} catch (RowsExceededException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (WriteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			number++;
		}
		close();
	}
	private void close(){

		try {
			book.write();
			book.close();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

public static void main(String args[]) {

}
}