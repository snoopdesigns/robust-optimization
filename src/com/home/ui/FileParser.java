package com.home.ui;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Vector;

public class FileParser {
	private String filename;
	FileParser(String filename) {
		this.filename = filename;
	}
	public Vector<ParseData> beginParse() {
		Vector<ParseData> vc = new Vector<ParseData>();
		try{
			  FileInputStream fstream = new FileInputStream(filename);
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  while ((strLine = br.readLine()) != null)   {
			  System.out.println (strLine);
			  vc.add(new ParseData(strLine.split(",")[0], strLine.split(",")[1]));
			  }
			  in.close();
			    }catch (Exception e){
			  System.err.println("Error: " + e.getMessage());
			  }
		return vc;
	}
}
