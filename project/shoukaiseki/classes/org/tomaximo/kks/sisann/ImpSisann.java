package org.tomaximo.kks.sisann;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.DocumentException;

import org.shoukaiseki.sql.ReadDatabaseConfigure;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

public class ImpSisann {
	public PrintLogs logger=new PrintLogs();
	public OracleSqlDetabese osd=null;
	public OracleSqlDetabese osdAsset=null;
	private String siteid="'FYDC'";
	private String orgid="'FYCRP'";
	private String status="'操作中'";
	private PreparedStatement ps;
	public ImpSisann() throws DocumentException, SQLException {
		// TODO Auto-generated constructor stub
		logger.level=7;
		// TODO Auto-generated constructor stub
		ReadDatabaseConfigure rdc=new ReadDatabaseConfigure();
		rdc.readDatabaseConfigureXML();
		osd = rdc.getOracleSqlDatabese("阜陽測試庫","ImpSisann");
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
		osdExec.setWhere(" column23 is not null");
		osdExec.format();
		ResultSet r = osdExec.executeQuery();
		String assetNum="";
		String location="";
		String description="";
		String classStructureId="ASSET";
		int i=0;
		while(r.next()){
			classStructureId=null;
			assetNum=r.getString("column23");
			location=r.getString("column14");
			description=r.getString("column7");
			logger.debug(++i+":"+assetNum+"-"+location+"-"+description,6);
			osd.setSql("select CLASSSTRUCTUREID from CLASSSTRUCTURE where CLASSIFICATIONID='"+assetNum.substring(0,6)+"'");
			ResultSet rs = osd.executeQuery();
			if(rs.next()){
				classStructureId=rs.getString(1);
			}
			osd.close();
			insertAsset(assetNum, location, description, classStructureId);
			ps.addBatch();
			if(i%1000==0){
				ps.executeBatch();
				ps.clearBatch();
				logger.debug("提交一批",6);
			}
		}
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
				"PLUSCVENDOR", "TEMPLATEID", "PLUSCLPLOC" };
		
		osdAsset.formatInsert(musi);
		
		System.out.println(osdAsset.getSql());
		
		
	}
	
	
	public static void main(String[] args) throws DocumentException, SQLException {
		new ImpSisann();
		System.out.println("已完成");
	}

}
