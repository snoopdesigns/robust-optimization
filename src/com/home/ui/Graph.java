package com.home.ui;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.jfree.chart.ChartPanel;

public class Graph extends JFrame{
	private static final long serialVersionUID = 1L;
	private static double minY=10000;
    private static double maxY=0;
	public Graph(Vector<ParseData> resVec){
		//this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1,2));
        this.setSize(800, 300);
        Vector<DataType> vc = new Vector<DataType>();
        DataType data;//добавление данных для графика
        for(int i=0;i<resVec.size();i++) {
        	data = new DataType(resVec.get(i).eps,resVec.get(i).ans);
        	vc.add(data);
        }
        XYGraphic graphic = new XYGraphic(vc);//создание графика по данным из вектора vc
        final ChartPanel chartPanel = graphic.getPanel();//получение панели с графиком, которую потом можно будет добавить на JFrame
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 300));
        this.getContentPane().setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.add(chartPanel);
        for(int i=0;i<resVec.size();i++) {
        	if(resVec.get(i).ans>maxY) maxY=resVec.get(i).ans;
        	if(resVec.get(i).ans<minY) minY=resVec.get(i).ans;
        }
        String strRes = "";
        for(int i=0;i<resVec.size();i++) {
        	if(resVec.get(i).ans == minY)
        		strRes += String.valueOf(resVec.get(i).eps) + ", ";
        }
        this.add(new JLabel("Optimal epsilons:"+strRes.substring(0, strRes.length()-2)));
        //this.pack();
        this.setVisible(true);
	}
	
}
