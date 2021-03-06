package org.tomaximo.kks.sisann;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.dom4j.DocumentException;

import org.shoukaiseki.sql.ReadDatabaseConfigure;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

public class ImpItemFenlei {
	public PrintLogs logger=new PrintLogs();
	public OracleSqlDetabese osd=null;
	public OracleSqlDetabese osdClassStructure=null;
	public OracleSqlDetabese osdClassUseWith=null;
	private OracleSqlDetabese osdClassAncestor;
	private OracleSqlDetabese osdClassIfication;
//	public String superAssetId="1018";//資產總分類id
	public String superAssetId="1021";//資產總分類id
	public String superAssetDescription="ITEM";
	private PreparedStatement psClassAncestor;
	private PreparedStatement psClassIfication;
	private PreparedStatement psClassUseWith;
	private PreparedStatement psClassStructure;
	public ImpItemFenlei() throws DocumentException, SQLException {
		logger.level=7;
		// TODO Auto-generated constructor stub
		ReadDatabaseConfigure rdc=new ReadDatabaseConfigure();
		rdc.readDatabaseConfigureXML();
		osd = rdc.getOracleSqlDatabese("阜陽測試庫","ImpSisann");//201
//		osd = rdc.getOracleSqlDatabese("阜陽測試庫maximo_75","ImpSisann");/222
//		osd = rdc.getOracleSqlDatabese("阜陽集羣庫","ImpSisann");
		osd.setLevel(7);
		osd.testReadData(7);
		insertClassStructureInit();
		insertClassUseWithInit();
		insertClassIficationInit();
		insertClassAncestorInit();
		
		exec();
		
		osd.getConnection().commit();
		osd.close();
	}
	
	public void exec() throws SQLException{
		 psClassStructure= osdClassStructure.prepareStatement();
		 psClassUseWith = osdClassUseWith.prepareStatement();
		 psClassIfication=osdClassIfication.prepareStatement();
		 psClassAncestor = osdClassAncestor.prepareStatement();
		osd.setTableRoot("SHOUKAISEKI_INSERT_ITEM_FL");
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
		String ownerMap="";
		String parentMap="";
		String koMap="";
		while(r.next()){
			String column1=r.getString("COLUMN1");
			String column2=r.getString("COLUMN2");
			String column3=r.getString("COLUMN3");
			String column4=r.getString("COLUMN4");
			String column5=r.getString("COLUMN5");
			String column6=r.getString("COLUMN6");
			String column7=r.getString("COLUMN7");
			String column8=r.getString("COLUMN8");
			
			
			//將結構轉換爲空的財產的分類格式
			
			
			
			strss=new String[][]{{column1,column2},{column3,column4},{column5,column6},{column7,column8}};
			
			
			logger.debug(++i+":"+column1+"-"+column2+"-"+column3+"-"+column4+"-"+column5+"-"+column6+"-"+column7+"-"+column8,5);
			logger.debug(++i+":::"+ownerId+"-"+ownernum+"-"+owner02+"-"+parentId+"-"+parentnum+"-"+parent02+"-"+koId+"-"+konum,5);
			if (column1!=null) {
				ownernum=column1;
				owner02=column2;
				ownerId =insertAll(ownerId, owner02, ownernum, parentId, parent02,parentnum,koId,ko02,konum, strss)+"";
			}else if(column3!=null){
					parentnum=column3;
					parent02=column4;
					parentId =insertAll(ownerId, owner02, ownernum, parentId, parent02, parentnum,koId,ko02,konum ,strss)+"";
			}else if(column5!=null){
					konum=column5;
					ko02=column6;
					koId =insertAll(ownerId, owner02, ownernum, parentId, parent02, parentnum,koId,ko02,konum ,strss)+"";
			}else{
				insertAll(ownerId, owner02, ownernum, parentId, parent02, parentnum,koId,ko02,konum, strss);
			}
			
		}
		
			psClassUseWith.executeBatch();
			psClassUseWith.clearBatch();
			psClassIfication.executeBatch();
			psClassIfication.clearBatch();
			psClassAncestor.executeBatch();
			psClassAncestor.clearBatch();
			psClassStructure.executeBatch();
			psClassStructure.clearBatch();
//		System.out.println(classStructureuId);
		
	}
	
	public long  insertAll(String ownerId, String owner02, String ownernum,
			String parentId, String parent02, String parentnum,String koId,String ko02,String konum,String[][] strss)
			throws SQLException {
		String classIficationId=null;
		String classIficationDescription=null;
		long classStructureuId = osd.getSequenceNext("TESTCLASSSTRUCTUREIDSEQ");
		logger.debug("CLASSSTRUCTURESEQ="+classStructureuId, 6);
		if(strss[0][0]==null){
			if(strss[1][0]==null){
				if(strss[2][0]==null){
					logger.debug("insert koko;ownernum+parentnum+konum="+ownernum+parentnum+konum+";parentId="+parentId,6);
					psClassStructure.setInt(osdClassStructure.getInsertColumnIndex("HASCHILDREN"), 0);//有子分類時爲1

					classIficationId=ownernum+parentnum+konum+strss[3][0];
					classIficationDescription=strss[3][1];
					psClassStructure.setString(osdClassStructure.getInsertColumnIndex("PARENT"), koId);

					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), classStructureuId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), classIficationDescription);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 0);
					psClassAncestor.addBatch();

					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId); 
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), ownerId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), owner02);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 1);
					psClassAncestor.addBatch();

					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId); 
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), parentId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), parent02);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 2);
					psClassAncestor.addBatch();

					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId); 
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), koId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), ko02);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 3);
					psClassAncestor.addBatch();
					
					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), superAssetId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), superAssetDescription);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 4);
					psClassAncestor.addBatch();

					classIficationDescription=owner02+"\\"+parent02+"\\"+ko02+"\\"+strss[3][1];
					
				}else{
					logger.debug("insert ko;ownernum+parentnum="+ownernum+parentnum+";parentId="+parentId,6);
					psClassStructure.setInt(osdClassStructure.getInsertColumnIndex("HASCHILDREN"), 0);//有子分類時爲1

					classIficationId=ownernum+parentnum+strss[2][0];
					classIficationDescription=strss[2][1];
					psClassStructure.setString(osdClassStructure.getInsertColumnIndex("PARENT"), parentId);

					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), classStructureuId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), classIficationDescription);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 0);
					psClassAncestor.addBatch();

					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId); 
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), ownerId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), owner02);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 1);
					psClassAncestor.addBatch();

					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId); 
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), parentId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), parent02);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 2);
					psClassAncestor.addBatch();

					psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), superAssetId);
					psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), superAssetDescription);
					psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 3);
					psClassAncestor.addBatch();

					classIficationDescription=owner02+"\\"+parent02+"\\"+strss[2][1];

				}
				
			}else{
				logger.debug("insert parent;ownernum="+ownernum+";ownerId="+ownerId,6);
				psClassStructure.setInt(osdClassStructure.getInsertColumnIndex("HASCHILDREN"), 1);//有子分類時爲1
				classIficationId=ownernum+strss[1][0];
				classIficationDescription=strss[1][1];
				psClassStructure.setString(osdClassStructure.getInsertColumnIndex("PARENT"), ownerId);
				
				psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
				psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
				psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), classStructureuId);
				psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), classIficationDescription);
				psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 0);
				psClassAncestor.addBatch();
				
				psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId); 
				psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
				psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), ownerId);
				psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), owner02);
				psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 1);
				psClassAncestor.addBatch();
				
				psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
				psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
				psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), superAssetId);
				psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), superAssetDescription);
				psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 2);
				psClassAncestor.addBatch();

				classIficationDescription=owner02+"\\"+strss[1][1];
			}
		}else{
			logger.debug("insert owner",6);
			psClassStructure.setInt(osdClassStructure.getInsertColumnIndex("HASCHILDREN"), 1);//有子分類時爲1
			classIficationId=strss[0][0];
			classIficationDescription=strss[0][1];
			psClassStructure.setString(osdClassStructure.getInsertColumnIndex("PARENT"), superAssetId);
			psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
			psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
			psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), classStructureuId);
			psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), classIficationDescription);
			psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 0);
			psClassAncestor.addBatch();
			psClassAncestor.setLong(osdClassAncestor.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
			psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
			psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTOR"), superAssetId);
			psClassAncestor.setString(osdClassAncestor.getInsertColumnIndex("ANCESTORCLASSID"), superAssetDescription);
			psClassAncestor.setInt(osdClassAncestor.getInsertColumnIndex("HIERARCHYLEVELS"), 1);
			psClassAncestor.addBatch();
			
			classIficationDescription=strss[0][1];
		}
		
		
			psClassStructure.setLong(osdClassStructure.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
			psClassStructure.setString(osdClassStructure.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
			psClassStructure.setString(osdClassStructure.getInsertColumnIndex("DESCRIPTION"), classIficationDescription);
			psClassStructure.addBatch();
			
			psClassUseWith.setLong(osdClassUseWith.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
			psClassUseWith.addBatch();
			
			psClassIfication.setString(osdClassIfication.getInsertColumnIndex("CLASSIFICATIONID"), classIficationId);
			psClassIfication.setString(osdClassIfication.getInsertColumnIndex("DESCRIPTION"), classIficationDescription);
			psClassIfication.addBatch();
			
		return classStructureuId;
	}
	

	public void insertClassStructure() throws SQLException{
		
		long classStructureuId = osd.getSequenceNext("CLASSUSEWITHSEQ");
		logger.debug("CLASSSTRUCTURESEQ="+classStructureuId, 6);
		PreparedStatement psClassStructure= osdClassStructure.prepareStatement();
		psClassStructure.setLong(osdClassStructure.getInsertColumnIndex("CLASSSTRUCTUREID"), classStructureuId);
		psClassStructure.setString(osdClassStructure.getInsertColumnIndex("PARENT"), null);
		psClassStructure.setString(osdClassStructure.getInsertColumnIndex("CLASSIFICATIONID"), "TEST");
		psClassStructure.setString(osdClassStructure.getInsertColumnIndex("DESCRIPTION"), "テスト");
		psClassStructure.setInt(osdClassStructure.getInsertColumnIndex("HASCHILDREN"), 0);//有子分類時爲1
		psClassStructure.addBatch();
		psClassStructure.executeBatch();
		psClassStructure.clearBatch();
		
	}
	
	public void insertClassIficationInit(){
		osdClassIfication=new OracleSqlDetabese(osd);
		osdClassIfication.setTableRootInsert("CLASSIFICATION");
		osdClassIfication.setInsertDefuorudoFinalColumn("CLASSIFICATIONUID", "CLASSIFICATIONSEQ.NEXTVAL");
		osdClassIfication.setInsertDefuorudoFinalColumn("HASLD", "0");
		String[] musi={"ROWSTAMP",  "SITEID", "ORGID"};
		osdClassIfication.formatInsert(musi);
		System.out.println(osdClassIfication.getSql());
	}
	public void insertClassAncestorInit(){
		osdClassAncestor=new OracleSqlDetabese(osd);
		osdClassAncestor.setTableRootInsert("CLASSANCESTOR");
		osdClassAncestor.setInsertDefuorudoFinalColumn("CLASSANCESTORID", "CLASSANCESTORUSEQ.NEXTVAL");
		
		String[] musi={"ROWSTAMP",  "SITEID", "ORGID"};
		osdClassAncestor.formatInsert(musi);
		System.out.println("----"+osdClassAncestor.getSql());
	}
	
	public void insertClassStructureInit(){
		osdClassStructure=new OracleSqlDetabese(osd);
		osdClassStructure.setTableRootInsert("CLASSSTRUCTURE");
		//創建臨時序列,用於給 CLASSSTRUCTUREUID 編號
		// create sequence TESTCLASSSTRUCTUREIDSEQ minvalue 1000 maxvalue
		// 99999999 start with 1003 increment by 1 nocache;
		osdClassStructure.setInsertDefuorudoFinalColumn("CLASSSTRUCTUREUID", "CLASSUSEWITHSEQ.NEXTVAL");
		osdClassStructure.setInsertDefuorudoFinalColumn("GENASSETDESC", "0");//使用该类结构时是否生成资产描述？ 
		osdClassStructure.setInsertDefuorudoFinalColumn("USECLASSINDESC", "1");
		osdClassStructure.setInsertDefuorudoFinalColumn("HASLD", "0");
		osdClassStructure.setInsertDefuorudoFinalColumn("SHOWINASSETTOPO", "0");
		osdClassStructure.setInsertDefuorudoFinalColumn("LANGCODE", "'ZH'");
		String[] musi={"ROWSTAMP", "TYPE", "SITEID", "ORGID"};
		osdClassStructure.formatInsert(musi);
		System.out.println(osdClassStructure.getSql());
		
	}
	
	public void insertClassUseWithInit(){
		osdClassUseWith=new OracleSqlDetabese(osd);
		osdClassUseWith.setTableRootInsert("CLASSUSEWITH");
		osdClassUseWith.setInsertDefuorudoFinalColumn("OBJECTNAME", "'ITEM'");
		osdClassUseWith.setInsertDefuorudoFinalColumn("OBJECTVALUE", "'项目'");
		osdClassUseWith.setInsertDefuorudoFinalColumn("DESCRIPTION", "'用于库存项目'");
		osdClassUseWith.setInsertDefuorudoFinalColumn("CLASSUSEWITHID", "CLASSUSEWITHSEQ.NEXTVAL");
		osdClassUseWith.setInsertDefuorudoFinalColumn("TOPLEVEL", "0");
		String[] musi={"ROWSTAMP"};
		osdClassUseWith.formatInsert(musi);
	}
	
	public static void main(String[] args) throws DocumentException, SQLException {
		ImpItemFenlei itemFenlei = new ImpItemFenlei();
		System.out.println("已完成");
	}

}
