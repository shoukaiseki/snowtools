package com.tomaximo.kks;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;

import com.shoukaiseki.sql.ReadDatabaseConfigure;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;
import com.shoukaiseki.syso.TerminalCursorControl;
import com.shoukaiseki.tuuyou.logger.PrintLogs;

public class UpdateKksTree {
	TerminalCursorControl tcc=new TerminalCursorControl();
	public PrintLogs logger=new PrintLogs();
	public OracleSqlDetabese osd=null;
	PreparedStatement pstLocHiEraRchy =null;
	PreparedStatement pstLocHiEraRchySelect=null; 
	public UpdateKksTree() throws SQLException {
		// TODO Auto-generated constructor stub
		ReadDatabaseConfigure rdc=new ReadDatabaseConfigure();
//		osd = rdc.getOracleSqlDatabese("crpxz_101_7001","ImpSisann");//101
//		osd = rdc.getOracleSqlDatabese("crpxz_zsk_maxdb","ImpSisann");//70
		osd = rdc.getOracleSqlDatabese("crpwz_crpjs_maximo","ImpSisann");//70
		osd.setLevel(7);
		osd.testReadData(7);
		
		/**
		 * lochierarchy
		 */
		String sql = "insert into  lochierarchy  (" + "Location,"
				+ "Parent," + "SystemId,"
				+ "Children,"
				+ // 父子标识,如果XGGYM为子值,则为0,父值则为1
				"SiteId," + "Orgid," + "lochierarchyid ) values(?,?,?,'1',?,?,lochierarchyseq.nextval)"; //
		OracleSqlDetabese osdLocHiEraRchy=new OracleSqlDetabese(osd);
		osdLocHiEraRchy.setSql(sql);
		pstLocHiEraRchy =osdLocHiEraRchy.prepareStatement();
		
		sql="select * from Lochierarchy where location=?  and orgid=?";
		pstLocHiEraRchySelect=osd.getConnection().prepareStatement(sql);
		
		
		exec();
		pstLocHiEraRchy.executeBatch();
		osd.getConnection().commit();
		osd.close();
	}

	private void exec() throws SQLException {
		// TODO Auto-generated method stub
		osd.setTableRoot("Lochierarchy");
		osd.setWhere("not exists(select * from lochierarchy b where b.location=lochierarchy.parent and lochierarchy.orgid=B.Orgid and lochierarchy.SYSTEMID=b.SYSTEMID ) and parent is not null" +
				" and orgid='CRPXZ'");
		osd.format();
		osd.setSql("Select Parent, Systemid, Siteid, Orgid From Lochierarchy Where Not Exists(Select * From Lochierarchy B Where B.Location=Lochierarchy.Parent And Lochierarchy.Orgid=B.Orgid And Lochierarchy.Systemid=B.Systemid ) And Parent Is Not Null" +
//				" and orgid='CRPXZ' "+
				" group by PARENT, SYSTEMID, SITEID, ORGID");
		ResultSet r = osd.executeQuery();
		int rowCount = osd.getRowCount();
		System.out.println("开始更新，共"+rowCount+"条数据");
		int i=1;
		while(r.next()){
//			tcc.moveToFirstLine();
//			tcc.clearToEndOfLine();
			tcc.print("正在更新第["+new DecimalFormat("00000000").format(i++)+"]行數據"+";共["+rowCount+"]条数据");
			System.out.println();
			String parent=r.getString("parent");
			String systemid=r.getString("systemid");
			String siteid=r.getString("siteid");
			String orgid=r.getString("orgid");
			pstLocHiEraRchySelect.setString(1, parent);
			pstLocHiEraRchySelect.setString(2,orgid);
			ResultSet rs = pstLocHiEraRchySelect.executeQuery();
			if(rs.next()){
				System.out.println("location="+parent);
				System.out.println("parent="+rs.getString("parent"));
				System.out.println("systemid="+systemid);
				System.out.println("siteid="+siteid);
				System.out.println("orgid="+orgid);
				pstLocHiEraRchy.setString(1, parent);
				pstLocHiEraRchy.setString(2, rs.getString("parent"));
				pstLocHiEraRchy.setString(3, systemid);
				pstLocHiEraRchy.setString(4, siteid);
				pstLocHiEraRchy.setString(5, orgid);
				pstLocHiEraRchy.addBatch();
			}
			if(i%1000==0){
				pstLocHiEraRchy.executeBatch();
			}
			rs.close();
		}
		
	}

	
	public static void main(String[] args) throws SQLException {
		new UpdateKksTree();
		System.out.println("已完成");
		
	}
}
