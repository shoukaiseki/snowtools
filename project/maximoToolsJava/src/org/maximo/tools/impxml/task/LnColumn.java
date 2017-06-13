package org.maximo.tools.impxml.task;

import java.util.Set;

import org.maximo.app.MTException;

public class LnColumn extends Task{
	
	private static final String DBNAEME = "DBNAME";
	private static final String COLUMNNAME = "COLUMNNAME";

	public LnColumn() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public String getDBName(){
		return getProperty(DBNAEME);
	}

	public String getColumnName(){
		return getProperty(COLUMNNAME);
	}
	
	@Override
	public String[] getFormatTrimUpperCaseProperty() {
		// TODO Auto-generated method stub
		return  new String[]{DBNAEME,COLUMNNAME};
	}
	
	/*  不解析子任务标签
	 * (non-Javadoc)
	 * @see org.maximo.tools.impxml.task.Task#hasNextKoElement()
	 */
	@Override
	public boolean hasNextKoElement() {
		// TODO Auto-generated method stub
		return false;
	}
}
