package org.maximo.tools.impxml.task;

public class SDSColumn extends Task{
	public final static String NAME="NAME" ;
	public SDSColumn() {
		// TODO Auto-generated constructor stub
		super();
	}

	public String getName() {
		return propertys.get(NAME);
	}
	
	@Override
	public String[] getFormatTrimUpperCaseProperty() {
		// TODO Auto-generated method stub
		return new String[]{NAME};
	}
}
