package org.maximo.tools.impxml.task;

import org.maximo.app.MTException;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class SequenceSet extends Task{
	private static final String NAME = "NAME";
	private static final String SEQUENCE = "SEQUENCE";
	
	private OracleSqlDetabese osd=null;
	private Long sequences=null;
	private String sequence=null;
	public SequenceSet() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		osd=new OracleSqlDetabese(con);
	}

	@Override
	public boolean hasNextKoElement() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public void execute(){
		
	}
	
	/** 下一個序列
	 * @return		序列下個值
	 */
	public long nextVal() {
		long seq=osd.getSequenceNext(getSequence());
		sequences=new Long(seq);
		return seq;
	}
	
	/** 獲取當前序列值
	 * @return 	序列當前值
	 */
	public long currval(){
		return sequences.longValue();
	}
	
	public OracleSqlDetabese getOracleSqlDetabese(){
		return osd;
	}
	
	public String getName(){
		return propertys.get(NAME);
	}
	
	public String getSequence(){
		return propertys.get(SEQUENCE);
	}
	
	@Override
	public String[] getFormatTrimUpperCaseProperty() {
		// TODO Auto-generated method stub
		return new String[]{NAME,SEQUENCE};
	}
	
}
