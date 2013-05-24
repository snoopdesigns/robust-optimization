package com.home.ui;

public class RobustNativeWrapper {
	static
    {
       System.loadLibrary("robust");
    }
	
    native public static Object[] startWork(Object[] vc);
}
