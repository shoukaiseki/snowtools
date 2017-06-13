package org.maximo.tools.impxml.task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.maximo.app.MTException;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class QuerySet extends Task{
	
	public static final String NAME="NAME";
	private OracleSqlDetabese osd=null;
	private PreparedStatement ps=null;
	private ResultSet rs=null;
	public QuerySet() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		osd=new OracleSqlDetabese(con);
		osd.setSql(cdata);
		try {
			ps = osd.prepareStatement();
			om.info("QuerySet: ["+getName()+"]{"+cdata+"}已初始化");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(getName()+":"+getCdata()+"初始化失败:",e);
		}
	}
	
	/** 是否含有結果集,如果報錯則返回false
	 * @throws MTException
	 */
	public boolean hasNext() {
		try {
			 rs = ps.executeQuery();
			 return rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			om.error("",e);
		}
		return false;
	}
	/** 取消報錯設置值 PreparedStatement 的值
	 * @param index
	 * @param o
	 */
	public void setObject(int index ,Object o){
		try {
			ps.setObject(index, o);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			om.error("",e);
		}
	}
	
	/**取消報錯獲取值,報錯返回null
	 * @param columnLabel
	 */
	public Object getObject(String columnLabel){
		
		try {
			return rs.getObject(columnLabel);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			om.error("", e);
		}
		return null;
	}
	/**取消報錯獲取值,報錯返回null
	 * @param index
	 */
	public Object getObject(int index){
		
		try {
			return rs.getObject(index);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			om.error("", e);
		}
		return null;
	}
	
	public ResultSet getResultSet(){
		return rs;
	}

	public String getName(){
		 return getProperty(NAME);
	}

	
	public OracleSqlDetabese getOracleSqlDetabese(){
		return osd;
	}
	
	public PreparedStatement getPreparedStatement(){
		return ps;
	}
}
