package com.home.ui;

import java.awt.Color;
import java.util.Vector;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class XYGraphic {
	final ChartPanel panel;
	static Vector<DataType> results;
	private static double minY=10000;
    private static double maxY=0;
	XYGraphic(Vector<DataType> vec) {
		results = new Vector<DataType>(vec);
		final XYDataset dataset = createDataset();
        final JFreeChart chart = createChart(dataset);
        panel = new ChartPanel(chart);
	}
	ChartPanel getPanel() {
		return panel;
	}
	private static XYDataset createDataset() {
        final XYSeries series1 = new XYSeries("Sample Graphic");
        
        for(int i=0;i<results.size();i++) {
        	series1.add(results.get(i).x, results.get(i).y);
        	if(results.get(i).y>maxY) maxY=results.get(i).y;
        	if(results.get(i).y<minY) minY=results.get(i).y;
        }
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series1);
                
        return dataset;
        
    }

    private static JFreeChart createChart(final XYDataset dataset) {
  
        final JFreeChart chart = ChartFactory.createXYLineChart("Model by Poss","Epsilon","Path length",
        			dataset,PlotOrientation.VERTICAL,true,true,false);
        chart.setBackgroundPaint(Color.white); 
        final XYPlot plot = chart.getXYPlot();
        plot.getRangeAxis().setRange(minY-10,maxY+10);
        plot.setBackgroundPaint(Color.lightGray);
        plot.setDomainGridlinePaint(Color.white);
        plot.setRangeGridlinePaint(Color.white);
        
        final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesShapesVisible(1, false);
        plot.setRenderer(renderer);
        final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        return chart;
        
    }
}
