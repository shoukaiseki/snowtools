package com.maximo.tools.impxml.task;

public class ODDColumn extends Task{
	private static final String COLUMNNAME = "COLUMNNAME";
	private static final String COLUMNLABEL = "COLUMNLABEL";

	public ODDColumn() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public String getColumnName(){
		return propertys.get(COLUMNNAME);
	}
	
	public String getColumnLabel(){
		return propertys.get(COLUMNLABEL);
	}
	
	@Override
	public String[] getFormatTrimUpperCaseProperty() {
		// TODO Auto-generated method stub
		return new String[]{COLUMNNAME,COLUMNLABEL};
	}
}
