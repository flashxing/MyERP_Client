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
 * ʹ��JFreeReport���ɱ���ļ����ӣ�������ʾʹ��JFreeReport���ɱ����һЩ��������
 * 
 * �������У�Ϊ�˼򻯲�������������ʹ��javaֱ�ӱ���
 *
 * @ ���� �� bookman
 */
public class Printer implements ActionListener{
	public static final int ROW_HEIGHT = 15;    //���棺�и߶�
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
     * �����ڹر��¼�
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
     * �������ɱ�����Ҫ�õ�������
     *
     * @����һ��TableModelʵ��
     */
    private TableModel createData() {

        final Object[] columnNames = new String[] { "���", "��Ʒ����" };
        final DefaultTableModel result = new DefaultTableModel(columnNames, 100);
        int rownum = 0;
        for (; rownum < 100; rownum++) {
            result.setValueAt("say Hello " + rownum + "��", rownum, 0);
            result.setValueAt("say World " + rownum + "��", rownum, 1);
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
    		columns= new String[] {"���","��Ʒ����","��Ʒ�ͺ�","����","����","���","��ע"};
    	}else{
    		columns = new String[]{"���","��Ʒ����","��Ʒ�ͺ�","����","��ע"};
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
     * ����һ��������
     *
     * @����һ��������ʵ��
     */
    private JFreeReport createReportDefinition() {
        final JFreeReport report = new JFreeReport();
        report.setName("�Ͼ������������޹�˾������");
        PageDefinition pd = report.getPageDefinition();    //ȡ�ñ���ҳ�涨��
        float pageWidth = pd.getWidth();    //ȡ�ô�ӡ���ʵ�ҳ��
        float pageHeight = pd.getHeight();
        //����ҳͷ
        PageHeader header = new PageHeader();
        LabelElementFactory title = new LabelElementFactory();    //����Ԫ��
        title.setText("�Ͼ������������޹�˾������");    //�����ı�����
        title.setColor(Color.BLACK);    //������ɫ
        title.setAbsolutePosition(new Point2D.Float((pageWidth-100)/2, HEADER_HEIGHT));    //������ʾλ��
        title.setMinimumSize(new FloatDimension(pageWidth, ROW_HEIGHT));    //���óߴ�
        title.setHorizontalAlignment(ElementAlignment.LEFT);
        title.setVerticalAlignment(ElementAlignment.MIDDLE);
        title.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(title.createElement());
        LabelElementFactory timeLabel = new LabelElementFactory();
        timeLabel.setText("����ʱ�䣺"+time);    //�����ı�����
        timeLabel.setColor(Color.BLACK);    //������ɫ
        timeLabel.setAbsolutePosition(new Point2D.Float(0, HEADER_HEIGHT+ROW_HEIGHT));    //������ʾλ��
        timeLabel.setMinimumSize(new FloatDimension(pageWidth/3, ROW_HEIGHT));    //���óߴ�
        timeLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        timeLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        timeLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(timeLabel.createElement());
        LabelElementFactory idLabel = new LabelElementFactory();
        idLabel.setText("���ݱ�ţ�"+id);    //�����ı�����
        idLabel.setColor(Color.BLACK);    //������ɫ
        idLabel.setAbsolutePosition(new Point2D.Float(pageWidth/3, HEADER_HEIGHT+ROW_HEIGHT));    //������ʾλ��
        idLabel.setMinimumSize(new FloatDimension(pageWidth/3, ROW_HEIGHT));    //���óߴ�
        idLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        idLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        idLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(idLabel.createElement());
        LabelElementFactory salesManLabel = new LabelElementFactory();
        salesManLabel.setText("ҵ��Ա��"+salesMan);    //�����ı�����
        salesManLabel.setColor(Color.BLACK);    //������ɫ
        salesManLabel.setAbsolutePosition(new Point2D.Float(pageWidth*2/3, HEADER_HEIGHT+ROW_HEIGHT));    //������ʾλ��
        salesManLabel.setMinimumSize(new FloatDimension(pageWidth/3, ROW_HEIGHT));    //���óߴ�
        salesManLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        salesManLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        salesManLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(salesManLabel.createElement());
        LabelElementFactory customerLabel = new LabelElementFactory();
        customerLabel.setText("������λ��"+customer);    //�����ı�����
        customerLabel.setColor(Color.BLACK);    //������ɫ
        customerLabel.setAbsolutePosition(new Point2D.Float(pageWidth*0/2, HEADER_HEIGHT+2*ROW_HEIGHT));    //������ʾλ��
        customerLabel.setMinimumSize(new FloatDimension(pageWidth/2, ROW_HEIGHT));    //���óߴ�
        customerLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        customerLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        customerLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(customerLabel.createElement());
        
        LabelElementFactory operatorLabel = new LabelElementFactory();
        operatorLabel.setText("�Ƶ��ˣ�"+operator);    //�����ı�����
        operatorLabel.setColor(Color.BLACK);    //������ɫ
        operatorLabel.setAbsolutePosition(new Point2D.Float(pageWidth*1/2, HEADER_HEIGHT+2*ROW_HEIGHT));    //������ʾλ��
        operatorLabel.setMinimumSize(new FloatDimension(pageWidth/2, ROW_HEIGHT));    //���óߴ�
        operatorLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        operatorLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        operatorLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(operatorLabel.createElement());
        
        LabelElementFactory commentLabel = new LabelElementFactory();
        commentLabel.setText("��ע��"+comment);    //�����ı�����
        commentLabel.setColor(Color.BLACK);    //������ɫ
        commentLabel.setAbsolutePosition(new Point2D.Float(pageWidth*0/4, HEADER_HEIGHT+3*ROW_HEIGHT));    //������ʾλ��
        commentLabel.setMinimumSize(new FloatDimension(pageWidth/2, ROW_HEIGHT));    //���óߴ�
        commentLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        commentLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        commentLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(commentLabel.createElement());
        
        LabelElementFactory senderLabel = new LabelElementFactory();
        senderLabel.setText("�ͻ��ˣ�"+sender);    //�����ı�����
        senderLabel.setColor(Color.BLACK);    //������ɫ
        senderLabel.setAbsolutePosition(new Point2D.Float(pageWidth*2/4, HEADER_HEIGHT+3*ROW_HEIGHT));    //������ʾλ��
        senderLabel.setMinimumSize(new FloatDimension(pageWidth*1/4, ROW_HEIGHT));    //���óߴ�
        senderLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        senderLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        senderLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(senderLabel.createElement());
        
        LabelElementFactory distributorLabel = new LabelElementFactory();
        distributorLabel.setText("����ˣ�"+distributor);    //�����ı�����
        distributorLabel.setColor(Color.BLACK);    //������ɫ
        distributorLabel.setAbsolutePosition(new Point2D.Float(pageWidth*3/4, HEADER_HEIGHT+3*ROW_HEIGHT));    //������ʾλ��
        distributorLabel.setMinimumSize(new FloatDimension(pageWidth/4, ROW_HEIGHT));    //���óߴ�
        distributorLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        distributorLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        distributorLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        header.addElement(distributorLabel.createElement());
        
        report.setPageHeader(header);
        float tmpWidth = pageWidth/(columns.length+1);
        float[] dataWidth = {tmpWidth,tmpWidth,tmpWidth*2,tmpWidth,tmpWidth,tmpWidth,tmpWidth,tmpWidth};
        float positionx = 0;
        if (columns != null && columns.length > 0) {
              report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));    //���Ʊ��ĺ���
              //���屨��ͷ
              ReportHeader reportHeader = new ReportHeader();

              for (int i = 0; i < columns.length; i++) {
                  //�ֶ���Ԫ��
            	  if(i > 0) positionx += dataWidth[i-1];
                  LabelElementFactory col = new LabelElementFactory();
                  col.setName(columns[i]);
                  col.setColor(Color.BLACK);
                  col.setHorizontalAlignment(ElementAlignment.CENTER);
                  col.setVerticalAlignment(ElementAlignment.MIDDLE);
                  col.setDynamicHeight(true);
                  col.setAbsolutePosition(new Point2D.Float(positionx, 0));
               	  col.setMinimumSize(new FloatDimension(dataWidth[i], ROW_HEIGHT));    //������С�ߴ� 
                  col.setBold(true);    //�����Ƿ������ʾ
                  col.setText(columns[i]);
                  reportHeader.addElement(col.createElement());
                  reportHeader.addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), col.getAbsolutePosition().getX()));    //Ԫ���������
                  reportHeader.addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));    //Ԫ���Ϸ�����
                  reportHeader.addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), pageWidth));    //Ԫ���Ҳ�����
                  report.setReportHeader(reportHeader);
                  
                  //�ֶ�����Ԫ��
                  TextFieldElementFactory data = new TextFieldElementFactory();
                  data.setName(columns[i]);
     
                  data.setColor(Color.BLACK);
                  data.setAbsolutePosition(new Point2D.Float(positionx, 0));
                  data.setMinimumSize(new FloatDimension(dataWidth[i], ROW_HEIGHT)); 
                  data.setHorizontalAlignment(ElementAlignment.CENTER);
     
                  data.setVerticalAlignment(ElementAlignment.MIDDLE);
                  data.setDynamicHeight(true);
                  data.setWrapText(new Boolean(true));
                  data.setNullString("-");    //����ֶ�����Ϊ�գ���ʾ���ı�
                  data.setFieldname(columns[i]);
                  report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));
                  report.getItemBand().addElement(data.createElement());
                  report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), -100));
                  report.getItemBand().addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), data.getAbsolutePosition().getX()));
                  report.getItemBand().addElement(StaticShapeElementFactory.createHorizontalLine(null, Color.BLACK, new BasicStroke(1), 0));
               }// end for(int i=0;i<columnNames.length;i++)
               
               //��������
               report.getItemBand().addElement(StaticShapeElementFactory.createVerticalLine(null, Color.BLACK, new BasicStroke(1), pageWidth));
        }
        PageFooter footer = new PageFooter();
        
        //����ҳ��
        PageOfPagesFunction pageFunction = new PageOfPagesFunction("PAGE_NUMBER");    //����һ��ҳ�ź�������
        pageFunction.setFormat("{0} / {1}ҳ");    //����ҳ����ʾ��ʽ���˴���ʾ�ĸ�ʽΪ��1/5ҳ����
        report.addExpression(pageFunction);
        
        LabelElementFactory totalNumberLabel = new LabelElementFactory();
        totalNumberLabel.setText("�ϼ�������"+totalNumber);    //�����ı�����
        totalNumberLabel.setColor(Color.BLACK);    //������ɫ
        totalNumberLabel.setAbsolutePosition(new Point2D.Float(0, 0));    //������ʾλ��
        totalNumberLabel.setMinimumSize(new FloatDimension(pageWidth*1/2, ROW_HEIGHT));    //���óߴ�
        totalNumberLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        totalNumberLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        totalNumberLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        footer.addElement(totalNumberLabel.createElement());
        
        if(hasUnitPrice){
        	LabelElementFactory totalMoneyLabel = new LabelElementFactory();
            totalMoneyLabel.setText("ʵ�ս�"+totalMoney);    //�����ı�����
            totalMoneyLabel.setColor(Color.BLACK);    //������ɫ
            totalMoneyLabel.setAbsolutePosition(new Point2D.Float(pageWidth*1/2, 0));    //������ʾλ��
            totalMoneyLabel.setMinimumSize(new FloatDimension(pageWidth*1/2, ROW_HEIGHT));    //���óߴ�
            totalMoneyLabel.setHorizontalAlignment(ElementAlignment.LEFT);
            totalMoneyLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
            totalMoneyLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
            footer.addElement(totalMoneyLabel.createElement());
        }
        
        LabelElementFactory confirmLabel = new LabelElementFactory();
        confirmLabel.setText("����ȷ��ǩ�֣�");    //�����ı�����
        confirmLabel.setColor(Color.BLACK);    //������ɫ
        confirmLabel.setAbsolutePosition(new Point2D.Float(0, 20));    //������ʾλ��
        confirmLabel.setMinimumSize(new FloatDimension(pageWidth*1/2, 20));    //���óߴ�
        confirmLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        confirmLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        confirmLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        footer.addElement(confirmLabel.createElement());
        
        LabelElementFactory phoneLabel = new LabelElementFactory();
        phoneLabel.setText("�绰��025-86612181");    //�����ı�����
        phoneLabel.setColor(Color.BLACK);    //������ɫ
        phoneLabel.setAbsolutePosition(new Point2D.Float(pageWidth*1/2, 20));    //������ʾλ��
        phoneLabel.setMinimumSize(new FloatDimension(pageWidth*1/2, 20));    //���óߴ�
        phoneLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        phoneLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        phoneLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
        footer.addElement(phoneLabel.createElement());
        
        LabelElementFactory addressLabel = new LabelElementFactory();
        addressLabel.setText("�Ͼ����껨�����ź�̫��װ�γ�B7��2��2¥231");    //�����ı�����
        addressLabel.setColor(Color.BLACK);    //������ɫ
        addressLabel.setAbsolutePosition(new Point2D.Float(0, 60));    //������ʾλ��
        addressLabel.setMinimumSize(new FloatDimension(pageWidth, ROW_HEIGHT));    //���óߴ�
        addressLabel.setHorizontalAlignment(ElementAlignment.LEFT);
        addressLabel.setVerticalAlignment(ElementAlignment.MIDDLE);
        addressLabel.setDynamicHeight(true);    //�����Ƿ�̬�����߶ȣ����Ϊtrue�����ı����ݳ�����ʾ��Χʱ�߶��Զ��ӳ���
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
    	// ��ô���������Ҫ�õ�������
        final TableModel data = createData();
        //��ñ���Ҫ�õ��ı���������
        final JFreeReport report = createReportDefinition();
        //������������ݽ��
        report.setData(model);
        try {
            //�����ɵı���ŵ�Ԥ��������
            final PreviewDialog preview = new PreviewDialog(report);
            preview.addWindowListener(new CloseHandler());
            preview.pack();
            //��ʾ����Ԥ������
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
