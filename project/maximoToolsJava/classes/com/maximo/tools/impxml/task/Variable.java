package com.maximo.tools.impxml.task;

import bsh.EvalError;
import bsh.Interpreter;

import com.maximo.app.MTException;

public class Variable extends Task{
	private static final String NAME = "NAME";
	private Object value=null;

	public Variable() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		if(!IXFormat.isNullOrTrimEmpty(getCdata())){
			Interpreter i = new Interpreter();  // Construct an interpreter
			try {
				value=i.eval(getCdata());
				om.info("初始化自定義變量(VARIABLE)["+getName()+"]="+value);
			} catch (EvalError e) {
				// TODO Auto-generated catch block
				throw new MTException("bsh腳本錯誤,變量[ "+getName()+"]初始化失敗,請在腳本中return一個Object對象:",e);
			}
		}
	}
	
	public String getName(){
		return propertys.get(NAME);
	}
	
	public void setValue(Object value){
		this.value=value;
	}
	
	public Object getValue(){
		return value;
	}
}
