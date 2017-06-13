package com.maximo.tools.impxml.task;

import java.util.HashMap;
import java.util.Map;

import com.maximo.app.MTException;

public class LinkColumnSet extends Task{
	/** 根据  DBName 获得 ColumnName
	 * Map<DBName,ColumnName>
	 */
	private Map<String, String> lnColumns=new HashMap<String, String>();
	
	/** 根据 ColumnName 获得 DBName
	 * Map<ColumnName,DBName>
	 */
	private Map<String, String> lnColumnsReverse=new HashMap<String, String>();
	public LinkColumnSet() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		while (hasNextKoElement()) {
			Task task = parseNextKoElement();
			if(task instanceof LnColumn){
				LnColumn lc=(LnColumn) task;
				lnColumns.put(lc.getDBName(), lc.getColumnName());
				lnColumnsReverse.put(lc.getColumnName(),lc.getDBName());
				om.info("LINKCOLUMNSET 数据库使用的字段名称:"+lc.getDBName()+"=列名称:"+lc.getColumnName());
			}
		}
	}
	
	public String getDBName(String columnName){
		String name= lnColumnsReverse.get(IXFormat.trimUpperCase(columnName));
		return name;
	}
	
	public String getColumnName(String dbName){
		String name=lnColumns.get(IXFormat.trimUpperCase(dbName));
		return name;
	}

}
