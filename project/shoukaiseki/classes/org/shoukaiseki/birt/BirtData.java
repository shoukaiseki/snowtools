package org.shoukaiseki.birt;

import nu.xom.Element;


public class BirtData extends BirtProperty {

	public BirtData() {
		super("data");
	}
	
	public void setColumnBindingName(String resultSetColumn){
		addProperty("resultSetColumn", resultSetColumn);
		
	}
}


