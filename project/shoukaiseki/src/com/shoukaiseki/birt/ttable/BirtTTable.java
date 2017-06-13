package com.shoukaiseki.birt.ttable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import com.shoukaiseki.birt.BirtProperty;
import com.shoukaiseki.birt.BirtRepoto;
import com.shoukaiseki.birt.BirtTable;


import nu.xom.Attribute;
import nu.xom.Element;

public class BirtTTable extends BirtTable{
	
	public BirtTTable(int columns,int headers,String dataSet) {
		// TODO Auto-generated constructor stub
		super(columns, headers, dataSet);
		element.setLocalName("table");
		
	}

	public BirtTTable(Element reportRoot) {
		// TODO Auto-generated constructor stub
		super(reportRoot);
	}
}


