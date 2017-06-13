package com.maximo.tools.impxml;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;

import com.maximo.app.MTException;
import com.maximo.app.OutMessage;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

/** 常用數據,如序列,當前數據庫時間等等...
 *com.maximo.tools.impxml.ImpXmlSequences
 * @author 蒋カイセキ    Japan-Tokyo 2013-6-27
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class ImpXmlDefData {
	private Connection con=null;
	OutMessage om=null;
	private HashMap<String,OracleSqlDetabese> seqs=null;
	/**
	 * 最後一次取過的序列值
	 */
	private HashMap<String,Long> sequences=null;
	public ImpXmlDefData(OutMessage om) {
		// TODO Auto-generated constructor stub
		this.om=om;
		 seqs= new HashMap<String, OracleSqlDetabese>();
		 sequences=new HashMap<String, Long>();
	}
	
	/** 不要隨意更換連接池,因為不同連接池序列號不同
	 * @param con
	 */
	public  void setConnection(Connection con){
		this.con=con;
	}

	/** 下一個序列
	 * @param seqName	序列名
	 * @return				序列下個值
	 */
	public long getSequenceNext(String seqName) {
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		long seq=osd.getSequenceNext(seqName);
		sequences.put(seqName.toUpperCase(),new Long(seq));
		return seq;
	}
	
	/** 獲取當前序列值
	 * @param seqName			序列名稱
	 * @return						序列當前值
	 */
	public Long getSequenceVal(String seqName){
		return sequences.get(seqName.toUpperCase());
	}
	
	public Timestamp getDBTimestamp(){
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setSql("select sysdate from dual");
		ResultSet rs;
		Timestamp timestamp=new Timestamp(new Date().getTime());
		try {
			rs = osd.executeQuery();
			rs.next();
			timestamp = rs.getTimestamp(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			om.warn("獲取數據庫當前時間失敗:",e);
		}finally{
				osd.close();
		}
		return timestamp;
	}
}
