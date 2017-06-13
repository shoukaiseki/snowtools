package org.maximo.tools.impxml.task;

import org.maximo.app.MTException;

public class DSColumn extends Task{
	public final static String NAME="NAME" ;
	public final static String FROMTYPE="FROMTYPE";
	public final static String SEQUENCE="SEQUENCE";
	public final static String FORCE="FORCE";
	public final static String DEFAULTVALUE="DEFAULTVALUE";
	public boolean force=false;
	
	public DSColumn(){
		super();
	}
	
	
	public String getName() {
		return propertys.get(NAME).toUpperCase();
	}

	public String getData() {
		return cdata;
	}

	public String getType() {
		return propertys.get(FROMTYPE);
	}

	public void setType(String type) {
		propertys.put(FROMTYPE, type);
	}

	public String getSequence() {
		String sequence=propertys.get(SEQUENCE);
//		sequence= sequence!=null&&!sequence.trim().isEmpty()?sequence.trim().toUpperCase():null;
		return sequence;
	}

	public void setSequence(String sequence) {
		propertys.put(SEQUENCE, sequence);
	}

	public boolean isForce() {
		return force;
	}

	public void setForce(String force) {
		this.force=IXFormat.isTrueAntNotNull(force);
	}

	public String getDefaultValue() {
		String defaultValue=propertys.get(DEFAULTVALUE);
		return defaultValue;
	}

	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		setForce(propertys.get(FORCE));
	}
	
	@Override
	public String[] getFormatTrimUpperCaseProperty() {
		// TODO Auto-generated method stub
		return new String[]{NAME,DEFAULTVALUE,FORCE,SEQUENCE};
	}
	
	/* 不解析子节点
	 * (non-Javadoc)
	 * @see org.maximo.tools.impxml.task.Task#hasNextKoElement()
	 */
	@Override
	public boolean hasNextKoElement() {
		// TODO Auto-generated method stub
		return false;
	}
}
