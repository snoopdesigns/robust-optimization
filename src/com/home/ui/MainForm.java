package com.home.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


public class MainForm extends JFrame implements  ActionListener{
	private JButton button1 = new JButton("browse");
	private JButton button2 = new JButton("start");
	private JButton button3 = new JButton("graph");
	private JButton button4 = new JButton("exit");
	private JLabel label1 = new JLabel("Findng the shortest path with a fixed epsilon");
	private JLabel label2 = new JLabel("browse file");
	private JLabel label3 = new JLabel("initial state");
	private JLabel label4 = new JLabel("final state");
	private JLabel label5 = new JLabel("epsilon");
	private JLabel label6 = new JLabel("");
	private JRadioButton rb1 = new JRadioButton("Bertsimas&Sim");
	private JRadioButton rb2 = new JRadioButton("Poss");
	private JTextField textfield1 = new JTextField();
	private JTextField textfield2 = new JTextField(5);
	private JTextField textfield3 = new JTextField(5);
	private JTextField textfield4 = new JTextField(5);
	private File file;
	private RobustOptimizationCore robustWorker = new RobustOptimizationCore("C:\\Users\\liza\\workspace\\RobustOpt\\jni");
	
	private static final long serialVersionUID = 1L;
	public MainForm(){
		super("Robust combinatorical optimisation");
		this.setSize(550, 250);
		
		JPanel pane = new JPanel();
		pane.setBorder(BorderFactory.createLineBorder(Color.black));
		Container container = this.getContentPane();
		GridBagLayout gbl = new GridBagLayout();
		container.setLayout(gbl);
		
		GridBagConstraints c = new GridBagConstraints();
		c.anchor = GridBagConstraints.NORTH;
		c.fill = GridBagConstraints.HORIZONTAL; 
		c.gridheight = 1;
		c.gridwidth = 1; 
		c.gridx = 0; 
		c.gridy = 0; 
		c.insets = new Insets(20, 10, 10, 10);
		c.gridwidth = 3;
		gbl.setConstraints(label1, c);
		container.add(label1);
		c.insets = new Insets(2, 10, 2, 10);
		c.gridx=0;
		c.gridy=1;
		c.gridwidth = 1;
		gbl.setConstraints(label2, c);
		container.add(label2);
		c.gridx = 0;
		c.gridy=2;
		gbl.setConstraints(button1, c);
		container.add(button1);
		c.gridx = 1;
		c.gridy=2;
		c.gridwidth = 2;
		gbl.setConstraints(textfield1, c);
		container.add(textfield1);
		c.gridx = 0;
		c.gridy=3;
		c.gridwidth = 1;
		gbl.setConstraints(label3, c);
		container.add(label3);
		c.gridx = 1;
		c.gridy=3;
		gbl.setConstraints(textfield2, c);
		container.add(textfield2);
		c.gridx = 0;
		c.gridy=4;
		gbl.setConstraints(label4, c);
		container.add(label4);
		c.gridx = 1;
		c.gridy=4;
		gbl.setConstraints(textfield3, c);
		container.add(textfield3);
		c.gridx = 0;
		c.gridy=5;
		gbl.setConstraints(label5, c);
		container.add(label5);
		c.gridx = 1;
		c.gridy=5;
		gbl.setConstraints(textfield4, c);
		container.add(textfield4);
		c.gridx = 0;
		c.gridy=6;
		c.gridwidth = 3;
		gbl.setConstraints(label6, c);
		container.add(label6);
		c.gridx = 0;
		c.gridy=7;
		c.gridwidth = 2;
		ButtonGroup group = new ButtonGroup();
		group.add(rb1);
		group.add(rb2);
		gbl.setConstraints(rb1, c);
		container.add(rb1);
		c.gridx = 0;
		c.gridy=8;
		c.gridwidth = 2;
		gbl.setConstraints(rb2, c);
		container.add(rb2);
		c.gridx = 0;
		c.gridy=9;
		c.gridwidth = 1;
		gbl.setConstraints(button2, c);
		container.add(button2);
		c.gridx = 1;
		c.gridy=9;
		c.gridwidth = 1;
		gbl.setConstraints(button3, c);
		container.add(button3);
		c.gridx = 2;
		c.gridy=9;
		c.gridwidth = 1;
		gbl.setConstraints(button4, c);
		container.add(button4);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);
		button4.addActionListener(this);
		rb1.addActionListener(this);
		rb2.addActionListener(this);
		
		this.pack();
		
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == button1){
			JFileChooser fileopen = new JFileChooser();
			int ret = fileopen.showDialog(null, "Открыть файл");
			if (ret == JFileChooser.APPROVE_OPTION) {
			    //File file = fileopen.getSelectedFile();
				file = fileopen.getSelectedFile();
			    textfield1.setText(file.getPath());  
			}
		}
		if(e.getSource() == button2) {
			 if (textfield1.getText().length()!=0 & textfield2.getText().length()!=0 &
					 textfield3.getText().length()!=0 & textfield4.getText().length()!=0 & 
					 (rb1.isSelected()||rb2.isSelected() )){
				 /*FileParser parser = new FileParser(file.getPath());
				 ResultForm form = new ResultForm();
				 form.setVisible(true);*/
				 Vector<String> vc = new Vector<>();
				 String filename = textfield1.getText().replace("\\", "/");
				 vc.add(filename);
				 vc.add(textfield2.getText());
				 vc.add(textfield3.getText());
				 vc.add(textfield4.getText());
				 if(rb1.isSelected()) {
					 vc.add("1"); 
				 } else {
					 vc.add("2");
				 }
				 System.out.println(vc.size());
				 this.robustWorker.run(this,vc.toArray());
			    }
			 else JOptionPane.showMessageDialog(null, "Enter all fields");
			
		}
		if(e.getSource() == button3) {
			if (textfield1.getText().length()!=0 & textfield2.getText().length()!=0 &
					 textfield3.getText().length()!=0 ) {
				Vector<String> vc = new Vector<>();
				String filename = textfield1.getText().replace("\\", "/");
				vc.add(filename);
				vc.add(textfield2.getText());
				vc.add(textfield3.getText());
				vc.add(textfield4.getText());
				if(rb1.isSelected()) {
					vc.add("1"); 
				} else {
					vc.add("2");
				}
				this.robustWorker.getGraph(this,vc.toArray());
				System.out.println("JAVA DONE");
				FileParser parser = new FileParser("jni//out_g.txt");
				Vector<ParseData> inputData = parser.beginParse();
				System.out.println(inputData);
				@SuppressWarnings("unused")
				Graph graphForm = new Graph(inputData);
			}
		}
		if(e.getSource() == button4) {
			System.exit(0);
		}
	    if(e.getSource()==rb1)
	    {
	    	rb1.setBorderPainted(true);
	    	rb2.setBorderPainted(false);
	    }
	    else if(e.getSource()==rb2)
		{
	    	rb1.setBorderPainted(false);
	    	rb2.setBorderPainted(true);
		}
		        
      }
	public void showResults(String results) {
		JOptionPane.showMessageDialog(null, results);
	}
	
}

