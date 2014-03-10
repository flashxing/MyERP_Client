package com.njue.mis.common;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.List;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.jfree.base.BaseBoot;
import org.jfree.report.ElementAlignment;
import org.jfree.report.JFreeReport;
import org.jfree.report.JFreeReportBoot;
import org.jfree.report.PageDefinition;
import org.jfree.report.PageFooter;
import org.jfree.report.PageHeader;
import org.jfree.report.ReportFooter;
import org.jfree.report.ReportHeader;
import org.jfree.report.ReportProcessingException;
import org.jfree.report.elementfactory.LabelElementFactory;
import org.jfree.report.elementfactory.StaticShapeElementFactory;
import org.jfree.report.elementfactory.TextFieldElementFactory;
import org.jfree.report.function.PageOfPagesFunction;
import org.jfree.report.modules.gui.base.PreviewDialog;
import org.jfree.ui.FloatDimension;

import com.njue.mis.model.Customer;
import com.njue.mis.model.Goods;
import com.njue.mis.model.GoodsItem;
import com.njue.mis.model.Sales;
import com.njue.mis.model.SalesGoodsItem;
import com.njue.mis.model.SalesIn;
import com.sun.istack.internal.FinalArrayList;


import bsh.This;
/**
 * 使用JFreeReport生成报表的简单例子，用于演示使用JFreeReport生成报表的一些基本步骤
 * 
 * 本例子中，为了简化操作，报表定义是使用java直接编码
 *
 * @ 作者 ： bookman
 */
public class Printer implements ActionListener{
	public static final int ROW_HEIGHT = 15;    //报告：行高度
    public static final int HEADER_HEIGHT = 20;
    public int footer_height;
    public int pageWidth = 400;
    private TableModel model;
    private String time = "";
    private String operator = "";
    private final Sales salesIn = null;
    private String id = "";
    private String salesMan = "";
    private String customer = "";
    private String comment = "";
    private String sender = "";
    private String distributor = "";
    private String totalNumber = "";
    private String totalMoney = "";
    private String[] columns;
    private boolean hasUnitPrice;
    
    /**
     * 处理窗口关闭事件
     */
    protected static class CloseHandler extends WindowAdapter {
        public void windowClosing(final WindowEvent event) {
        }
    }
    
    public void setData(String time, String operator){
    	this.time = time;
    	this.operator = operator;
    }
    /**
     * 创建生成报表需要用到的数据
     *
     * @返回一个TableModel实例
     */
    private TableModel createData() {

        final Object[] columnNames = new String[] { "编号", "商品名称" };
        final DefaultTableModel result = new DefaultTableModel(columnNames, 100);
        int rownum = 0;
        for (; rownum < 100; rownum++) {
            result.setValueAt("say Hello " + rownum + "次", rownum, 0);
            result.setValueAt("say World " + rownum + "次", rownum, 1);
        }

        return result;

    }
    public void init(final Sales salesIn,final List<Goods> goodsList,final Customer customer, boolean hasUnitPrice){
    	System.out.println(goodsList.size()+" "+salesIn.getGoodsItemsList().size());
    	this.time = salesIn.getTime();
    	this.id = salesIn.getId();
    	this.salesMan = salesIn.getSalesMan();
    	this.customer = customer.getName();
    	this.operator = salesIn.getOperatePerson();
    	this.comment = salesIn.getComment();
    	this.hasUnitPrice = hasUnitPrice;
    	int number = 0;
    	for(SalesGoodsItem goodsItem: salesIn.getGoodsItemsList()){
    		number += goodsItem.getNumber();
    	}
    	this.totalNumber = number+"";
    	this.totalMoney = salesIn.getPrice()+"";
    	if (hasUnitPrice){
    		columns= new String[] {"编号","商品名称","商品型号","数量","单价","金额","备注"};
    	}else{
    		columns = new String[]{"编号","商品名称","商品型号","数量","备注"};
    	}
    	final DefaultTableModel resul = new DefaultTableModel(columns, salesIn.getGoodsItemsList().size());
    	for(int row = 0; row<salesIn.getGoodsItemsList().size(); row++){
    		resul.setValueAt(salesIn.getGoodsItemsList().get(row).getId(), row, 0);
    		resul.setValueAt(goodsList.get(row).getGoodsName(), row, 1);
    		resul.setValueAt(goodsList.get(row).getDescription(), row, 2);
       		resul.setValueAt(salesIn.getGoodsItemsList().get(row).getNumber(), row, 3);
       		if (hasUnitPrice) {
           		resul.setValueAt(salesIn.getGoodsItemsList().get(row).getUnitPrice(), row, 4);
           		resul.setValueAt(salesIn.getGoodsItemsList().get(row).getTotalPrice(), row, 5);
           		resul.setValueAt(salesIn.getGoodsItemsList().get(row).getComment(), row, 6);	
       		}else{	
       			resul.setValueAt(salesIn.getGoodsItemsList().get(row).getComment(), row, 4);	       			
       		}
       		
    	}
    	footer_height = HEADER_HEIGHT+4*ROW_HEIGHT+(salesIn.getGoodsItemsList().size()+1)*(ROW_HEIGHT+5)+10;
    	this.model = resul;
    }
    
    public void setTableModel(TableModel model){
    	this.model = model;
    }

    /**
     * 创建一个报表定义
     *
     * @返回一个报表定义实例
     */
    private JFreeReport createReportDefinition() {
        final JFreeReport report = new JFreeReport();
        report.setName("南京明川电器有限公司出货单");
        PageDefinition pd = report.getPageDefinition();    //取得报告页面定义
        float pageWidth = pd.getWidth();    //取得打印材质的页宽
        float pageHeight = pd.getHeight();
        //定义页头
        PageHeader header = new PageHeader();
        LabelElementFactory title = new LabelElementFactory();    //标题元素
        title.setText("南京明川电器有限公司出货单");    //设置文本内容
        title.setColor(Color.BLACK);    //设置颜色
        title.setAbsolutePosition(new Point2D.Float((pageWidth-100)/2, HEADER_HEIGHT));    //设置显示位置
        title.setMinimumSize(new FloatDimension(pageWidth, ROW_HEIGHT));    //设置尺寸
        title.setHorizontalAlignment(ElementAlignment.LEFT);
        title.setVerticalAlignment(ElementAlignment.MIDDLE);
        title.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(title.createElement());
        LabelElementFactory timeLabel = new LabelElementFactory();
        timeLabel.setText("单据时间："+time);    //设置文本内容
        timeLabel.setColor(Color.BLACK);    //设置颜色
        timeLabel.setAbsolutePosition(new Point2D.Float(0, HEADER_HEIGHT+ROW_HEIGHT));    //设置显示位置
        timeLabel.setMinimumSize(new FloatDimension(pageWidth/3, ROW_HEIGHT));    //设置尺寸
        timeLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        timeLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        timeLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(timeLabel.createElement());
        LabelElementFactory idLabel = new LabelElementFactory();
        idLabel.setText("单据编号："+id);    //设置文本内容
        idLabel.setColor(Color.BLACK);    //设置颜色
        idLabel.setAbsolutePosition(new Point2D.Float(pageWidth/3, HEADER_HEIGHT+ROW_HEIGHT));    //设置显示位置
        idLabel.setMinimumSize(new FloatDimension(pageWidth/3, ROW_HEIGHT));    //设置尺寸
        idLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        idLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        idLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(idLabel.createElement());
        LabelElementFactory salesManLabel = new LabelElementFactory();
        salesManLabel.setText("业务员："+salesMan);    //设置文本内容
        salesManLabel.setColor(Color.BLACK);    //设置颜色
        salesManLabel.setAbsolutePosition(new Point2D.Float(pageWidth*2/3, HEADER_HEIGHT+ROW_HEIGHT));    //设置显示位置
        salesManLabel.setMinimumSize(new FloatDimension(pageWidth/3, ROW_HEIGHT));    //设置尺寸
        salesManLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        salesManLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        salesManLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(salesManLabel.createElement());
        LabelElementFactory customerLabel = new LabelElementFactory();
        customerLabel.setText("往来单位："+customer);    //设置文本内容
        customerLabel.setColor(Color.BLACK);    //设置颜色
        customerLabel.setAbsolutePosition(new Point2D.Float(pageWidth*0/2, HEADER_HEIGHT+2*ROW_HEIGHT));    //设置显示位置
        customerLabel.setMinimumSize(new FloatDimension(pageWidth/2, ROW_HEIGHT));    //设置尺寸
        customerLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        customerLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        customerLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(customerLabel.createElement());
        
        LabelElementFactory operatorLabel = new LabelElementFactory();
        operatorLabel.setText("制单人："+operator);    //设置文本内容
        operatorLabel.setColor(Color.BLACK);    //设置颜色
        operatorLabel.setAbsolutePosition(new Point2D.Float(pageWidth*1/2, HEADER_HEIGHT+2*ROW_HEIGHT));    //设置显示位置
        operatorLabel.setMinimumSize(new FloatDimension(pageWidth/2, ROW_HEIGHT));    //设置尺寸
        operatorLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        operatorLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        operatorLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(operatorLabel.createElement());
        
        LabelElementFactory commentLabel = new LabelElementFactory();
        commentLabel.setText("备注："+comment);    //设置文本内容
        commentLabel.setColor(Color.BLACK);    //设置颜色
        commentLabel.setAbsolutePosition(new Point2D.Float(pageWidth*0/4, HEADER_HEIGHT+3*ROW_HEIGHT));    //设置显示位置
        commentLabel.setMinimumSize(new FloatDimension(pageWidth/2, ROW_HEIGHT));    //设置尺寸
        commentLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        commentLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        commentLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(commentLabel.createElement());
        
        LabelElementFactory senderLabel = new LabelElementFactory();
        senderLabel.setText("送货人："+sender);    //设置文本内容
        senderLabel.setColor(Color.BLACK);    //设置颜色
        senderLabel.setAbsolutePosition(new Point2D.Float(pageWidth*2/4, HEADER_HEIGHT+3*ROW_HEIGHT));    //设置显示位置
        senderLabel.setMinimumSize(new FloatDimension(pageWidth*1/4, ROW_HEIGHT));    //设置尺寸
        senderLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        senderLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        senderLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(senderLabel.createElement());
        
        LabelElementFactory distributorLabel = new LabelElementFactory();
        distributorLabel.setText("配货人："+distributor);    //设置文本内容
        distributorLabel.setColor(Color.BLACK);    //设置颜色
        distributorLabel.setAbsolutePosition(new Point2D.Float(pageWidth*3/4, HEADER_HEIGHT+3*ROW_HEIGHT));    //设置显示位置
        distributorLabel.setMinimumSize(new FloatDimension(pageWidth/4, ROW_HEIGHT));    //设置尺寸
        distributorLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        distributorLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        distributorLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        header.addElement(distributorLabel.createElement());
        
        report.setPageHeader(header);
        float tmpWidth = pageWidth/(columns.length+1);
        float[] dataWidth = {tmpWidth,tmpWidth,tmpWidth*2,tmpWidth,tmpWidth,tmpWidth,tmpWidth,tmpWidth};
        float positionx = 0;
        if (columns != null && columns.length > 0) {
              report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));    //绘制表格的横线
              //定义报表头
              ReportHeader reportHeader = new ReportHeader();

              for (int i = 0; i < columns.length; i++) {
                  //字段名元素
            	  if(i > 0) positionx += dataWidth[i-1];
                  LabelElementFactory col = new LabelElementFactory();
                  col.setName(columns[i]);
                  col.setColor(Color.BLACK);
                  col.setHorizontalAlignment(ElementAlignment.CENTER);
                  col.setVerticalAlignment(ElementAlignment.MIDDLE);
                  col.setDynamicHeight(true);
                  col.setAbsolutePosition(new Point2D.Float(positionx, 0));
               	  col.setMinimumSize(new FloatDimension(dataWidth[i], ROW_HEIGHT));    //设置最小尺寸 
                  col.setBold(true);    //设置是否粗体显示
                  col.setText(columns[i]);
                  reportHeader.addElement(col.createElement());
                  reportHeader.addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), col.getAbsolutePosition().getX()));    //元素左侧竖线
                  reportHeader.addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));    //元素上方横线
                  reportHeader.addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), pageWidth));    //元素右侧竖线
                  report.setReportHeader(reportHeader);
                  
                  //字段内容元素
                  TextFieldElementFactory data = new TextFieldElementFactory();
                  data.setName(columns[i]);
     
                  data.setColor(Color.BLACK);
                  data.setAbsolutePosition(new Point2D.Float(positionx, 0));
                  data.setMinimumSize(new FloatDimension(dataWidth[i], ROW_HEIGHT)); 
                  data.setHorizontalAlignment(ElementAlignment.CENTER);
     
                  data.setVerticalAlignment(ElementAlignment.MIDDLE);
                  data.setDynamicHeight(true);
                  data.setWrapText(new Boolean(true));
                  data.setNullString("-");    //如果字段内容为空，显示的文本
                  data.setFieldname(columns[i]);
                  report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));
                  report.getItemBand().addElement(data.createElement());
                  report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), -100));
                  report.getItemBand().addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), data.getAbsolutePosition().getX()));
                  report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));
               }// end for(int i=0;i<columnNames.length;i++)
               
               //最后的竖线
               report.getItemBand().addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), pageWidth));
        }
        PageFooter footer = new PageFooter();
        
        //设置页号
        PageOfPagesFunction pageFunction = new PageOfPagesFunction("PAGE_NUMBER");    //构造一个页号函数对象
        pageFunction.setFormat("{0} / {1}页");    //设置页号显示格式（此处显示的格式为“1/5页”）
        report.addExpression(pageFunction);
        
        LabelElementFactory totalNumberLabel = new LabelElementFactory();
        totalNumberLabel.setText("合计数量："+totalNumber);    //设置文本内容
        totalNumberLabel.setColor(Color.BLACK);    //设置颜色
        totalNumberLabel.setAbsolutePosition(new Point2D.Float(0, 0));    //设置显示位置
        totalNumberLabel.setMinimumSize(new FloatDimension(pageWidth*1/2, ROW_HEIGHT));    //设置尺寸
        totalNumberLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        totalNumberLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        totalNumberLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        footer.addElement(totalNumberLabel.createElement());
        
        if(hasUnitPrice){
        	LabelElementFactory totalMoneyLabel = new LabelElementFactory();
            totalMoneyLabel.setText("实收金额："+totalMoney);    //设置文本内容
            totalMoneyLabel.setColor(Color.BLACK);    //设置颜色
            totalMoneyLabel.setAbsolutePosition(new Point2D.Float(pageWidth*1/2, 0));    //设置显示位置
            totalMoneyLabel.setMinimumSize(new FloatDimension(pageWidth*1/2, ROW_HEIGHT));    //设置尺寸
            totalMoneyLabel.setHorizontalAlignment(ElementAlignment.LEFT);
            totalMoneyLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
            totalMoneyLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
            footer.addElement(totalMoneyLabel.createElement());
        }
        
        LabelElementFactory confirmLabel = new LabelElementFactory();
        confirmLabel.setText("数量确认签字：");    //设置文本内容
        confirmLabel.setColor(Color.BLACK);    //设置颜色
        confirmLabel.setAbsolutePosition(new Point2D.Float(0, 20));    //设置显示位置
        confirmLabel.setMinimumSize(new FloatDimension(pageWidth*1/2, 20));    //设置尺寸
        confirmLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        confirmLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        confirmLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        footer.addElement(confirmLabel.createElement());
        
        LabelElementFactory phoneLabel = new LabelElementFactory();
        phoneLabel.setText("电话：025-86612181");    //设置文本内容
        phoneLabel.setColor(Color.BLACK);    //设置颜色
        phoneLabel.setAbsolutePosition(new Point2D.Float(pageWidth*1/2, 20));    //设置显示位置
        phoneLabel.setMinimumSize(new FloatDimension(pageWidth*1/2, 20));    //设置尺寸
        phoneLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        phoneLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        phoneLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        footer.addElement(phoneLabel.createElement());
        
        LabelElementFactory addressLabel = new LabelElementFactory();
        addressLabel.setText("南京市雨花区板桥红太阳装饰城B7区2幢2楼231");    //设置文本内容
        addressLabel.setColor(Color.BLACK);    //设置颜色
        addressLabel.setAbsolutePosition(new Point2D.Float(0, 60));    //设置显示位置
        addressLabel.setMinimumSize(new FloatDimension(pageWidth, ROW_HEIGHT));    //设置尺寸
        addressLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        addressLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        addressLabel.setDynamicHeight(true);    //设置是否动态调整高度（如果为true，当文本内容超出显示范围时高度自动加长）
        footer.addElement(addressLabel.createElement());
        
        
        
        TextFieldElementFactory pageCount = new TextFieldElementFactory();
        pageCount.setFieldname("PAGE_NUMBER");
        pageCount.setColor(Color.black);
        pageCount.setAbsolutePosition(new Point2D.Float(0, pageHeight-footer_height));
        pageCount.setMinimumSize(new FloatDimension(pageWidth, 0));
        pageCount.setHorizontalAlignment(ElementAlignment.RIGHT);
        pageCount.setVerticalAlignment(ElementAlignment.MIDDLE);
        pageCount.setDynamicHeight(true);
        footer.addElement(pageCount.createElement());
        report.setPageFooter(footer);
        
        ReportFooter reportFooter = new ReportFooter();
        reportFooter.addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));
        report.setReportFooter(reportFooter);
        return report;
    }

    public void print(){
    	// 获得创建报表需要用到的数据
        final TableModel data = createData();
        //获得报表要用到的报表定义内容
        final JFreeReport report = createReportDefinition();
        //将报表定义和数据结合
        report.setData(model);
        try {
            //将生成的报表放到预览窗口中
            final PreviewDialog preview = new PreviewDialog(report);
            preview.addWindowListener(new CloseHandler());
            preview.pack();
            //显示报表预览窗口
            preview.setVisible(true);
        } catch (ReportProcessingException e) {
            System.out.println(e);
        }
    }
//    public static void main(String[] args){
//    	JFreeReportBoot.getInstance().start();
//    	new Printer().print();
//    }
	@Override
	public void actionPerformed(ActionEvent arg0) {
    	this.print();
	}
}
