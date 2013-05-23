package com.home.ui;

public class ParseData {
	float eps;
	int ans;
	ParseData(String eps, String ans)
	{
		this.eps = Float.parseFloat(eps);
		this.ans = Integer.parseInt(ans);
	}
}
