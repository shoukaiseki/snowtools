package com.shoukaiseki.birt.ttable;

import java.util.LinkedList;

import nu.xom.Attribute;
import nu.xom.Element;

import com.shoukaiseki.birt.BirtRepoto;
import com.shoukaiseki.birt.BirtRow;

public class BirtTTableRow extends BirtRow{
	LinkedList<BirtTTableCell> tableCell = new LinkedList<BirtTTableCell>(); 
	
	public BirtTTableRow(int column) {
		// TODO Auto-generated constructor stub
		super(column);
	}

	public BirtTTableRow(Element ele) {
		// TODO Auto-generated constructor stub
		super(ele);
	}

}


