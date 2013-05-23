package com.home.ui;

import java.lang.reflect.Field;

public class RobustOptimizationCore {
	private MainForm callbackMainForm;
	RobustOptimizationCore(String path) {
		this.setLibraryPath(path);
	}
	public void run(MainForm mainForm, Object[] vc) {
		this.callbackMainForm = mainForm;
		System.out.println("call robust run");
		Object[] results = RobustNativeWrapper.startWork(vc);
		System.out.println("RESULTS:");
		for(int i=0;i<results.length;i++)
		{
			System.out.println(results[i]);
		}
		String path = "";
		for(int i=0;i<results.length-1;i++) {
			path += results[i].toString()+", ";
		}
		this.callbackMainForm.showResults("Results: minimum path = "+results[results.length-1].toString()+", path = "+path);
	}
	private void setLibraryPath(String path) {
	    System.setProperty("java.library.path", path);
	    Field sysPathsField;
		try {
			sysPathsField = ClassLoader.class.getDeclaredField("sys_paths");
			sysPathsField.setAccessible(true);
			sysPathsField.set(null, null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	public void getGraph(MainForm mainForm, Object[] vc) {
		RobustNativeWrapper.getGraphResults(vc);
	}
}
