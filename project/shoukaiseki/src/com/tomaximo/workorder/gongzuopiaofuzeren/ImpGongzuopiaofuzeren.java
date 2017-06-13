package com.tomaximo.workorder.gongzuopiaofuzeren;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;

import com.shoukaiseki.sql.ReadDatabaseConfigure;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;
import com.shoukaiseki.tuuyou.logger.PrintLogs;

/** 工作票維護
 *com.tomaximo.kks.sisann.ImpFenleiHennkou
 * @author 蒋カイセキ    Japan-Tokyo 2012-9-6
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class ImpGongzuopiaofuzeren {
	public PrintLogs logger=new PrintLogs();
	public OracleSqlDetabese osd=null;
	public OracleSqlDetabese osdSwap=null;
	public String superAssetId="1018";//資產總分類id
	public String superAssetDescription="ITEM";
	private PreparedStatement psSwap;
	private String siteid="'FYDC'";
	private String orgid="'FYCRP'";
	public ImpGongzuopiaofuzeren() throws DocumentException, SQLException {
		logger.level=7;
		// TODO Auto-generated constructor stub
		ReadDatabaseConfigure rdc=new ReadDatabaseConfigure();
		rdc.readDatabaseConfigureXML();
		osd = rdc.getOracleSqlDatabese("阜陽測試庫","ImpSisann");
		osd.setLevel(7);
		osd.testReadData(7);
		
		initSwap();
		exec();
		
		osd.getConnection().commit();
		osd.close();
	}
	
	public void exec() throws SQLException{
		 psSwap = osdSwap.prepareStatement();
		osd.setTableRoot("SHOUKAISEKI_INSERT_WO_FZR");
		osd.setOrderBy("id");
		osd.format();
		ResultSet r = osd.executeQuery();
		String ownerId="1017";
		String owner02="电气设备";
		String ownernum="33";
		String parentId="1030";
		String parent02="发电机系统";
		String parentnum="01";
		String koId="";
		String ko02="";
		String konum="";
		String[][] strss={{null,null},{null,null},{"01","发电机定子"}};
		int i=0;
		Map<String, String> ownerMap=new HashMap<String,String>();
		Map<String, String> parentMap=new HashMap<String, String>();
		Map<String, String> koMap=new HashMap<String, String>();
		int id=0;
		while(r.next()){
			String column1=r.getString("COLUMN1");
			String column2=r.getString("COLUMN2");
			String column3=r.getString("COLUMN3");
			String column4=r.getString("COLUMN4");
			String column5=r.getString("COLUMN5");
			String column6=r.getString("COLUMN6");
			String column7=r.getString("COLUMN7");
			String column8=r.getString("COLUMN8");
				psSwap.setString(osdSwap.getInsertColumnIndex("DESCRIPTION"), column5);//personid 對應的人名
				psSwap.setString(osdSwap.getInsertColumnIndex("GZPTYPE"), column2);
				psSwap.setString(osdSwap.getInsertColumnIndex("PERSONROLE"), column8);
				psSwap.setString(osdSwap.getInsertColumnIndex("WOPROFESS"), column7);
				psSwap.addBatch();
			
			logger.debug(++i+":"+column1+"-"+column2+"-"+column3+"-"+column4+"-"+column5+"-"+column6+"-"+column7+"-"+column8,5);
//			psSwap.addBatch();
		}
		psSwap.executeBatch();
		psSwap.clearBatch();
		psSwap.close();
	}
	
	private void initSwap() {
		// TODO Auto-generated method stub
		osdSwap=new OracleSqlDetabese(osd);
		osdSwap.setTableRootInsert("GZPWHZIB");
		osdSwap.setInsertDefuorudoFinalColumn("SITEID", siteid);
		osdSwap.setInsertDefuorudoFinalColumn("ORGID", orgid);
		osdSwap.setInsertDefuorudoFinalColumn("GZPWHZIBID", "GZPWHZIBIDSEQ.NEXTVAL");
		osdSwap.setInsertDefuorudoFinalColumn("HASLD", "0");
		osdSwap.setInsertDefuorudoFinalColumn("ACTIVE", "0");
		String[] musi={"ROWSTAMP", "PERSONID", "GZPWHZBNUM", "ZJDQR", "FZRZG", "PERSONDESC"};
		osdSwap.formatInsert(musi);
		osdSwap.formatInsert();
		System.out.println(osdSwap.getSql());
		
	}

	public static void main(String[] args) throws DocumentException, SQLException {
		new ImpGongzuopiaofuzeren();
		System.out.println("已完成");
	}

}
