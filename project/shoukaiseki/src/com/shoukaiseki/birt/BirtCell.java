package com.shoukaiseki.birt;

import java.util.LinkedList;



import nu.xom.Element;

public abstract class BirtCell extends BirtProperty implements BirtSeru{
	protected LinkedList<Object> cellObject = new LinkedList<Object>(); 
	
	public BirtCell() {
		// TODO Auto-generated constructor stub
		super("cell");
	}
	
	
	public BirtCell(Element ele) {
		// TODO Auto-generated constructor stub
		super(ele);
	}


	public BirtData addData(){
		BirtData data=new BirtData();
		element.appendChild(data.getElement());
		cellObject.add(data);
		return data;
	}
	
	public Object getTableCellObject(int index){
		return  cellObject.get(index);
	}
	
	public BirtLabel addLabel(){
		BirtLabel label = new BirtLabel();
		element.appendChild(label.getElement());
		cellObject.add(label);
		return label;
	}
	
}


