package org.tomaximo.kks.sisann;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.DocumentException;

import org.shoukaiseki.sql.ReadDatabaseConfigure;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

/** 將結構轉換爲空的財產的分類格式
 *com.tomaximo.kks.sisann.ImpFenleiHennkou
 * @author 蒋カイセキ    Japan-Tokyo 2012-9-6
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class ImpFenleiHennkou {
	public PrintLogs logger=new PrintLogs();
	public OracleSqlDetabese osd=null;
	public OracleSqlDetabese osdSwap=null;
	public String superAssetId="1018";//資產總分類id
	public String superAssetDescription="ITEM";
	private PreparedStatement psSwap;
	public ImpFenleiHennkou() throws DocumentException, SQLException {
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
		osd.setTableRoot("SHOUKAISEKI_INSERT_ITEM_FLSWAP");
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
			String column9=r.getString("COLUMN9");
			if(ownerMap.get(column1)==null){
				ownerMap.put(column1, column1);
				psSwap.setInt(osdSwap.getInsertColumnIndex("ID"), ++id);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN1"), column1);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN2"), column2);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN3"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN4"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN5"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN6"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN7"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN8"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN9"), null);
				psSwap.addBatch();
			}
			if(parentMap.get(column1+column3)==null){
				parentMap.put(column1+column3, column1+column3);
				psSwap.setInt(osdSwap.getInsertColumnIndex("ID"), ++id);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN1"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN2"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN3"), column3);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN4"), column4);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN5"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN6"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN7"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN8"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN9"), null);
				psSwap.addBatch();
				
			}
			if(koMap.get(column1+column3+column5)==null){
				koMap.put(column1+column3+column5, column1+column3+column5);
				psSwap.setInt(osdSwap.getInsertColumnIndex("ID"), ++id);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN1"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN2"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN3"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN4"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN5"), column5);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN6"), column6);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN7"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN8"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN9"), null);
				psSwap.addBatch();
			}
				psSwap.setInt(osdSwap.getInsertColumnIndex("ID"), ++id);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN1"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN2"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN3"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN4"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN5"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN6"), null);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN7"), column7);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN8"), column8);
				psSwap.setString(osdSwap.getInsertColumnIndex("COLUMN9"), column9);
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
		osdSwap.setTableRootInsert("SHOUKAISEKI_INSERT_ITEM_FL");
		osdSwap.formatInsert();
		System.out.println(osdSwap.getSql());
		
	}

	public static void main(String[] args) throws DocumentException, SQLException {
		new ImpFenleiHennkou();
		System.out.println("已完成");
	}

}
