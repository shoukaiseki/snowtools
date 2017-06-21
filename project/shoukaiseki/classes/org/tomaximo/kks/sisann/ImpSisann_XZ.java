package org.tomaximo.kks.sisann;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.DocumentException;

import org.shoukaiseki.sql.ReadDatabaseConfigure;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

public class ImpSisann_XZ {
	public PrintLogs logger=new PrintLogs();
	public OracleSqlDetabese osd=null;
	public OracleSqlDetabese osdAsset=null;
	private String siteid="'徐州项目'";
	private String orgid="'CRPXZ'";
//	public static String siteid = "'华鑫电厂'";
//	public static String orgid = "'CRPHX'";
	private String status="'活动'";
	private PreparedStatement ps;
	public ImpSisann_XZ() throws DocumentException, SQLException {
		// TODO Auto-generated constructor stub
		logger.level=7;
		// TODO Auto-generated constructor stub
		ReadDatabaseConfigure rdc=new ReadDatabaseConfigure();
		rdc.readDatabaseConfigureXML();
//		osd = rdc.getOracleSqlDatabese("阜陽測試庫","ImpSisann");
//		osd = rdc.getOracleSqlDatabese("crpxz_101_7001","ImpSisann");//101
//		osd = rdc.getOracleSqlDatabese("crpxz_zsk_maxdb","ImpSisann");//70
		osd = rdc.getOracleSqlDatabese("crpnr","ImpSisann");//70
		osd.setLevel(7);
		osd.testReadData(7);
		insertAssetInit();
		exec();
		osd.getConnection().commit();
		osd.close();
	}
	
	public void exec() throws SQLException{
		 ps = osdAsset.prepareStatement();
		OracleSqlDetabese osdExec=new OracleSqlDetabese(osd);
		osdExec.setTableRoot("SHOUKAISEKI_INSERT_SISANN");
		osdExec.setWhere(" column23 is not null AND not exists(select * from asset where asset.assetnum=SHOUKAISEKI_INSERT_SISANN.column23)");
		osdExec.setOrderBy("id");
		osdExec.format();
		ResultSet r = osdExec.executeQuery();
		String assetNum="";
		String location="";
		String description="";
		String classStructureId="ASSET";
		int i=0;
		
		osd.setSql("select CLASSSTRUCTUREID from CLASSSTRUCTURE where CLASSIFICATIONID=? and orgid="+orgid);
		PreparedStatement ps1 = osd.prepareStatement();
		while(r.next()){
			classStructureId=null;
			assetNum=r.getString("column23");//资产编码
			location=r.getString("column14");
			description=r.getString("column7");
			logger.debug(++i+":"+assetNum+"-"+location+"-"+description,6);
			ps1.setObject(1, assetNum.substring(0, 6));
			logger.info("assetNum.substring(0, 6)="+assetNum.substring(0, 6));
			
			ResultSet rs = ps1.executeQuery();
			if(rs.next()){
				classStructureId=rs.getString(1);
			}
			rs.close();
			insertAsset(assetNum, location, description, classStructureId);
			ps.addBatch();
			if(i%1000==0){
				ps.executeBatch();
				ps.clearBatch();
				logger.debug("提交一批",6);
			}
		}
		ps.executeBatch();
		ps.clearBatch();
		logger.debug("提交一批",6);
		osd.close();
	}
	
	public void insertAsset(String assetNum, String location, String description, String classStructureId) throws SQLException{
		ps.setString(osdAsset.getInsertColumnIndex("ASSETNUM"),assetNum);
		ps.setString(osdAsset.getInsertColumnIndex("LOCATION"),location);
		ps.setString(osdAsset.getInsertColumnIndex("DESCRIPTION"),description);
		ps.setString(osdAsset.getInsertColumnIndex("CLASSSTRUCTUREID"),classStructureId);
	}
	
	public void insertAssetInit(){
		osdAsset=new OracleSqlDetabese(osd);
		osdAsset.setTableRootInsert("ASSET");
		osdAsset.setInsertDefuorudoFinalColumn("PURCHASEPRICE", "0");
		osdAsset.setInsertDefuorudoFinalColumn("REPLACECOST", "0");
		osdAsset.setInsertDefuorudoFinalColumn("TOTALCOST", "0");
		osdAsset.setInsertDefuorudoFinalColumn("YTDCOST", "0");
		osdAsset.setInsertDefuorudoFinalColumn("BUDGETCOST", "0");
		osdAsset.setInsertDefuorudoFinalColumn("ISRUNNING", "1");
		osdAsset.setInsertDefuorudoFinalColumn("UNCHARGEDCOST", "0");
		osdAsset.setInsertDefuorudoFinalColumn("TOTUNCHARGEDCOST", "0");
		osdAsset.setInsertDefuorudoFinalColumn("TOTDOWNTIME", "0");
		osdAsset.setInsertDefuorudoFinalColumn("STATUSDATE", "sysdate");
		osdAsset.setInsertDefuorudoFinalColumn("CHANGEDATE", "sysdate");
		osdAsset.setInsertDefuorudoFinalColumn("CHANGEBY", "'MAXADMIN'");
		osdAsset.setInsertDefuorudoFinalColumn("INVCOST", "0");
		osdAsset.setInsertDefuorudoFinalColumn("CHILDREN", "0");
		osdAsset.setInsertDefuorudoFinalColumn("DISABLED", "0");
		osdAsset.setInsertDefuorudoFinalColumn("SITEID",siteid );
		osdAsset.setInsertDefuorudoFinalColumn("ORGID",orgid );
		osdAsset.setInsertDefuorudoFinalColumn("AUTOWOGEN", "0");
		osdAsset.setInsertDefuorudoFinalColumn("STATUS", status);
		osdAsset.setInsertDefuorudoFinalColumn("MAINTHIERCHY", "0");
		osdAsset.setInsertDefuorudoFinalColumn("ASSETID", "ASSETIDSEQ.NEXTVAL");
		osdAsset.setInsertDefuorudoFinalColumn("MOVED", "0");
		osdAsset.setInsertDefuorudoFinalColumn("ASSETUID", "ASSETSEQ.NEXTVAL");
		osdAsset.setInsertDefuorudoFinalColumn("LANGCODE", "'ZH'");
		osdAsset.setInsertDefuorudoFinalColumn("HASLD", "0");
		osdAsset.setInsertDefuorudoFinalColumn("ISLINEAR", "0");
		osdAsset.setInsertDefuorudoFinalColumn("RETURNEDTOVENDOR", "0");
		osdAsset.setInsertDefuorudoFinalColumn("TLOAMPARTITION", "0");
		osdAsset.setInsertDefuorudoFinalColumn("PLUSCISCONTAM", "0");
		osdAsset.setInsertDefuorudoFinalColumn("PLUSCISINHOUSECAL", "0");
		osdAsset.setInsertDefuorudoFinalColumn("PLUSCISMTE", "0");
		osdAsset.setInsertDefuorudoFinalColumn("PLUSCPMEXTDATE", "0");
		osdAsset.setInsertDefuorudoFinalColumn("PLUSCSOLUTION", "0");
		osdAsset.setInsertDefuorudoFinalColumn("ISCALIBRATION", "0");
		
		String[] musi = { "PARENT", "SERIALNUM", "ASSETTAG", "VENDOR",
				"FAILURECODE", "MANUFACTURER", "INSTALLDATE",
				"WARRANTYEXPDATE", "CALNUM", "ITEMNUM", "EQ1", "EQ2", "EQ3",
				"EQ4", "EQ5", "EQ6", "EQ7", "EQ8", "EQ9", "EQ10", "EQ11",
				"EQ12", "EQ23", "EQ24", "PRIORITY", "GLACCOUNT", "ROTSUSPACCT",
				"BINNUM", "SOURCESYSID", "OWNERSYSID", "EXTERNALREFID",
				"SITEID", "ORGID", "ITEMSETID", "CONDITIONCODE", "GROUPNAME",
				"ASSETTYPE", "USAGE", "TOOLRATE", "ITEMTYPE", "ANCESTOR",
				"SENDERSYSID", "SHIFTNUM", "TOOLCONTROLACCOUNT", "DIRECTION",
				"STARTMEASURE", "ENDMEASURE", "ENDDESCRIPTION",
				"STARTDESCRIPTION", "ROWSTAMP", "LRM", "DEFAULTREPFACSITEID",
				"DEFAULTREPFAC", "TLOAMHASH", "PLUSCASSETDEPT", "PLUSCCLASS",
				"PLUSCDUEDATE", "PLUSCISCONDESC", "PLUSCISMTECLASS",
				"PLUSCLOOPNUM", "PLUSCMODELNUM", "PLUSCOPRGEEU",
				"PLUSCOPRGEFROM", "PLUSCOPRGETO", "PLUSCPHYLOC", "PLUSCSUMDIR",
				"PLUSCSUMEU", "PLUSCSUMREAD", "PLUSCSUMSPAN", "PLUSCSUMURV",
				"PLUSCVENDOR", "TEMPLATEID", "PLUSCLPLOC","S_RUNDESC","S_MODELNUM","S_PROFESSION","S_FZPERSON",
				"S_TECHCOND","S_FACTORYNUM","S_COORDINATE","S_FIGURENUM","S_INSTLOC","S_FIXPLACE","S_OLDASSETNUM",
				"CREATEPERSON","REMARK","CREATEDATE","S_JIZU","JIZU"};
		
		osdAsset.formatInsert(musi);
		
		System.out.println(osdAsset.getSql());
		
		
	}
	
	
	public static void main(String[] args) throws DocumentException, SQLException {
		new ImpSisann_XZ();
		System.out.println("已完成");
	}

}
