package com.air;

import java.util.Hashtable;

public class bubble {
	
	//HELLO
	Hashtable bubblex = new Hashtable();
	Hashtable bubbley = new Hashtable();
	Hashtable bubblebursted = new Hashtable();
	
	public bubble()
	{
		// TODO Auto-generated constructor stub
	}
	
	public void setx(String bubblename, String x)
	{
		bubblex.put(bubblename, x);
		
	}
	
	public void sety(String bubblename, String y)
	{
		bubbley.put(bubblename, y);
	}
	
	public int getx(String bubblename)
	{
		
		return Integer.parseInt( (String) bubblex.get(bubblename));
		
	}
	
	public int gety(String bubblename)
	{
		return  Integer.parseInt( (String) bubbley.get(bubblename));
	}
	
	public void burst(String bubblename)
	{
		
		bubblebursted.put(bubblename, "bursted");
	}
	
	
	public boolean isBursted(String bubblename)
	{
		
		if(bubblebursted.get(bubblename)!= null)
		{
			if(bubblebursted.get(bubblename).equals("bursted"))
			{
				return true;
			}
		}
		
		return false;
	}
	
	
	
	

}
