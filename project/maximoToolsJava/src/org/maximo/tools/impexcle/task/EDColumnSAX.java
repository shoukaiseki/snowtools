package org.maximo.tools.impexcle.task;

public class EDColumnSAX extends TaskSAX{
	private static final String COLUMNNAME = "COLUMNNAME";
	private static final String CELLNAME = "CELLNAME";

	public EDColumnSAX() {
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
