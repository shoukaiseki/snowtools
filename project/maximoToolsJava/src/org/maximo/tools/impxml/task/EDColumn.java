package org.maximo.tools.impxml.task;

public class EDColumn extends Task{

	private static final String COLUMNNAME = "COLUMNNAME";
	private static final String CELLNAME = "CELLNAME";

	public EDColumn() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public String getColumnName(){
		return propertys.get(COLUMNNAME);
	}
	
	public String getCellName(){
		return propertys.get(CELLNAME);
	}
	
	@Override
	public String[] getFormatTrimUpperCaseProperty() {
		// TODO Auto-generated method stub
		return new String[]{COLUMNNAME,CELLNAME};
	}
}
