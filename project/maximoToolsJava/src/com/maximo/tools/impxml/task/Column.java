package com.maximo.tools.impxml.task;

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.maximo.app.MTException;

public class Column extends Task{
	public final static String NAME="NAME" ;
	public final static int TIMESTAMP=Types.TIMESTAMP;
	public final static int NULL=Types.NULL;
	/**
	 * 最開始的值
	 */
	private String beginValue="";
	/**
	 * 字段的值
	 */
//	private Object value=null;
	/**
	 * 插入字段採用的類型
	 */
	private int type=Types.NULL;
	public Column(){
		
	}
	
	public Column(String cellName){
		propertys.put(NAME, IXFormat.trimUpperCase(cellName));
	}
	
	public String getName() {
		return propertys.get(NAME).toUpperCase();
	}


	public String getValue() {
		return cdata.toString();
	}

	public void setValue(Object value) {
		this.cdata = IXFormat.transColumnValue(formatObject(value));
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public boolean isNull(){
		return IXFormat.isNullOrTrimEmpty(cdata);
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		setValue(cdata);
		setBeginValue(cdata);
		om.debug(getName()+":"+getValue());
	}
	
	@Override
	public String[] getFormatTrimUpperCaseProperty() {
		// TODO Auto-generated method stub
		return new String[]{NAME};
	}
	
	@Override
	public boolean hasNextKoElement() {
		// TODO Auto-generated method stub
		return false;
	}

	/** 最開始的值
	 * @return
	 */
	public String getBeginValue() {
		return beginValue;
	}

	public void setBeginValue(Object beginValue) {
		this.beginValue = formatObject(beginValue);
	}

	public boolean isBeginNull() {
		// TODO Auto-generated method stub
		return IXFormat.isNullOrTrimEmpty(beginValue);
	}

	/**
	 * @param value
	 * @return
	 */
	public String formatObject(Object value){
		String str=null;
		if(value!=null){
			if(value instanceof Date){
				str=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value);
				if(type==NULL){
					type=TIMESTAMP;
				}
			}else{
				str=value.toString();
			}
		}
		return str;	
	};
}
