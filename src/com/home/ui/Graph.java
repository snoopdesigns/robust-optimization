package com.home.ui;

import java.awt.GridLayout;
import java.util.Vector;

import javax.swing.JFrame;

import org.jfree.chart.ChartPanel;

public class Graph extends JFrame{
	public Graph(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridLayout(1,2));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(800, 300);
        Vector<DataType> vc = new Vector<DataType>();
        DataType data;//добавление данных для графика
        for(int i=0;i<10;i++) {
        	data = new DataType(i,10-i);
        	vc.add(data);
        }
        XYGraphic graphic = new XYGraphic(vc);//создание графика по данным из вектора vc
        final ChartPanel chartPanel = graphic.getPanel();//получение панели с графиком, которую потом можно будет добавить на JFrame
        chartPanel.setPreferredSize(new java.awt.Dimension(800, 300));
        this.add(chartPanel);
        this.setVisible(true);
	}
	
}
