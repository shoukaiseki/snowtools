package com.maximo.tools.impexcle.task;

import java.util.HashMap;
import java.util.Map;

import com.maximo.app.MTException;
import com.maximo.tools.impxml.task.IXFormat;

public class ExcleDataSAXSAX extends TaskSAX{
	private static final String TITLECOUNT = "TITLECOUNT";
	private static final String NAME = "NAME";
	private static final String SHEET = "SHEET";
	
	private int titleCount;
	
	/**
	 * 存放excle cell與 column 的 映射,row设置值按照 映射後的column名称设置
	 * Map<CellName,ColumnName>
	 */
	Map<String,String> ccMap=new HashMap<String,String>();
	
	public ExcleDataSAXSAX() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		while (hasNextKoElement()) {
			TaskSAX koTask = parseNextKoElement();
			if(koTask instanceof EDColumnSAX){
				EDColumnSAX edc=(EDColumnSAX) koTask;
				ccMap.put(edc.getCellName(),edc.getColumnName());
			}
		}
		
		setTitleCount(propertys.get(TITLECOUNT));
	}
	
	public String getName() {
		// TODO Auto-generated method stub
		return IXFormat.trim(propertys.get(NAME));
	}

	
	public int getTitleCount() {
		return titleCount;
	}
	
	private void setTitleCount(String tc){
		if(IXFormat.isNullOrTrimEmpty(tc)){
			titleCount=1;
		}else{
			titleCount= Integer.parseInt(tc);	
		}
	}
	
	public int getExcleSheetAt(){
		String s=propertys.get(SHEET);
		if(!IXFormat.isNullOrTrimEmpty(s)){
			return Integer.parseInt(s);
		}
		return 0;
	}

}
