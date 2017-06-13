package com.maximo.tools.impxml.task;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.maximo.app.MTException;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

/**   通過 con.prepareStatement(sql); 生成一個 PreparedStatement;
 * <P>主要用於插入數據到數據庫.
 * <P><B>例如:</B>
 * <PRE>
 * {@code 
  * 	<!--
  *	 生成一個 PreparedStatementSet 對象,在一些有ixt可用變量的bsh腳本值哦功中可以通過ixt.getPreparedStatement("PreparedStatementSet對象名称");獲取該對象,然後對數據庫進行插入數據操作
 *			<PREPAREDSTATEMENTSET 
 *				NAME="PreparedStatementSet對象名称"><![CDATA[
 *				insert 語句
 *		]]></PREPAREDSTATEMENTSET>-->
 *	<PREPAREDSTATEMENTSET NAME="asus"><![CDATA[insert into test(a,b,c,d,e) values('asus',1,test.nextval,?,?)]]></PREPAREDSTATEMENTSET>
  *  <!--在BSH中調用-->
 *  <TRIGGERBEANSHELL ISENABLED="NO"><![CDATA[
 *   PreparedStatementSet listPs = ixt.getPreparedStatement("asus");
 *   	listPs.setObject(1,"linux");
 *    listPs.setObject(2,new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
 *    listPs.addBatchAndExecuteBatch();
 *    }
  *    ]]>
 *	</TRIGGERBEANSHELL>
  *
 * </PRE>
 * com.maximo.tools.impxml.task.PreparedStatementSet
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-2
 * <P>ブログ http://shoukaiseki.blog.163.com/
 * <P>E-メール jiang28555@Gmail.com
 */
public class PreparedStatementSet extends Task{
	public static final String NAME="NAME";
	private OracleSqlDetabese osd=null;
	private PreparedStatement ps=null;
	
	public PreparedStatementSet() {
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
			om.info("PreparedStatementSet: ["+getName()+"]{"+cdata+"}已初始化");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(getName()+":"+getCdata()+"初始化失败:",e);
		}
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
	
	public void executeBatch() throws MTException{
		try {
			ps.executeBatch();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
	}

	public void setObject(int i, Object object) throws SQLException {
		// TODO Auto-generated method stub
		ps.setObject(i, object);
	}
	
	public void addBatchAndExecuteBatch() throws SQLException{
		ps.addBatch();
		ps.executeBatch();
	}

}
