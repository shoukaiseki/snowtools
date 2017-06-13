package org.shoukaiseki.gui.tuuyou;

import java.awt.Color;

public class ColorHouhou {
	
	
	 
	public static Color hexToRGB(String hex){
		int index=hex.indexOf("#")+1;
		hex=hex.substring(index,index+6);
		int red=Integer.parseInt(hex.substring(0,2),16);
		int green=Integer.parseInt(hex.substring(2,4),16);
		int blue=Integer.parseInt(hex.substring(4,6),16);
		return new Color(red,green,blue);
		
	}
}


