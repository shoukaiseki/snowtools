package org.maximo.app;

import java.rmi.RemoteException;
import java.sql.*;

import psdi.mbo.MboRemote;
import psdi.util.MXException;

import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class MaximoToolsLogger {
	OutMessage om=null;
	Long id=null;
	Connection con=null;
	public MaximoToolsLogger(Connection con,OutMessage om) throws MTException {
		// TODO Auto-generated constructor stub
		if(con==null){
			throw new MTException("參數'Connection'為'null'");
		}else if(om==null){
			throw new MTException("參數'OutMessage'為'null'");
		}
		this.om=om;
		this.con=con;
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setSql("select * from dba_tab_columns where owner=(select user from dual) and table_name='MAXIMOTOOLS_ADDLOGGER'");
		try {
			ResultSet r = osd.executeQuery();
			if(!r.next()){
				om.println("MAXIMOTOOLS_ADDLOGGER表不存在,正在創建!");
				String sql=""
						+"CREATE TABLE MAXIMOTOOLS_ADDLOGGER "
						+"("
						+"  OBJECTNAME VARCHAR2(50) "
						+", UNIQUEATTRIBUTENAME VARCHAR2(50) "
						+", ENTRYDATE DATE NOT NULL"
						+", OWNERID NUMBER NOT NULL "
						+", ID NUMBER NOT NULL "
						+")" ;
				if(!osd.update(sql)){
					osd.close();
					throw new MTException("創建表'MAXIMOTOOLS_ADDLOGGER'失敗,請查看日誌文件.");
				}
				sql=""
						+"CREATE OR REPLACE TRIGGER MAXIMOTOOLS_ADDLOGGER_T BEFORE"
						+"  INSERT OR"
						+"  insert"
						+"    ON MAXIMOTOOLS_ADDLOGGER FOR EACH ROW DECLARE imadate date;"
						+"  BEGIN"
						+"    SELECT"
						+"      sysdate"
						+"    INTO"
						+"      imadate"
						+"    FROM"
						+"      DUAL;"
						+"    :NEW.entrydate := imadate;"
						+"  END;";
				try {
					Statement pst = con.createStatement();
					pst.execute(sql);
					pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
				} catch (Exception e) {
					// TODO: handle exception
					throw new MTException("觸發器'MAXIMOTOOLS_ADDLOGGER_T'創建失敗",e);
				}
				sql=""
						+"COMMENT ON COLUMN MAXIMOTOOLS_ADDLOGGER.ID IS '批次ID,取數據庫當前時間'" ;
				osd.update(sql);
				sql=""
						+"COMMENT ON COLUMN MAXIMOTOOLS_ADDLOGGER.OWNERID IS '插入數據的唯一ID'" ;
				osd.update(sql);
				sql=""
						+"COMMENT ON COLUMN MAXIMOTOOLS_ADDLOGGER.OBJECTNAME IS '表名稱'" ; 
				osd.update(sql);
				sql=""
						+"COMMENT ON COLUMN MAXIMOTOOLS_ADDLOGGER.UNIQUEATTRIBUTENAME IS '唯一ID字段名稱'" ;
				osd.update(sql);
				sql=""
						+"COMMENT ON COLUMN MAXIMOTOOLS_ADDLOGGER.ENTRYDATE IS '插入時間'" ;
				osd.update(sql);
			}
			osd.close();
			while(true){
				id=getNewId();
				osd.setSql(new StringBuffer("select * from MAXIMOTOOLS_ADDLOGGER where id='").append(id).append("'").toString());
				if(!osd.executeQuery().next()){
					osd.close();
					break;
				}
				osd.close();
			}
			om.println(new StringBuffer("'MAXIMOTOOLS_ADDLOGGER'唯一'ID'為:").append(id).toString());
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
	}
	private long getNewId() throws SQLException{
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setSql("select to_char(systimestamp,'yyyy-mm-dd hh24:mi:ss.ff3') from DUAL");
		ResultSet r = osd.executeQuery();
		r.next();
		Timestamp date = r.getTimestamp(1);
		long id=date.getTime();
		osd.close();
		return id;
	}

	public boolean addMaximoToolsLogger(MboRemote mbo) throws  MTException {
		// TODO Auto-generated method stub
		boolean b=true; 
		try {
			b=addMaximoToolsLogger(mbo.getName(), mbo.getUniqueIDName(), mbo.getUniqueIDValue());
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		} catch (MXException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
		return b;
	}
	
	public boolean addMaximoToolsLogger(String objectName, String attributeName,long ownerid) throws MTException {
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setTableRootInsert("MAXIMOTOOLS_ADDLOGGER");
		osd.setInsertDefuorudoFinalColumn("ID", ""+id);
		osd.formatInsert(new String[]{"entrydate"});
		System.out.println(osd.getSql());
		try {
			PreparedStatement ps = osd.prepareStatement();
			ps.setString(osd.getInsertColumnIndex("objectName"), objectName.toUpperCase());
			ps.setString(osd.getInsertColumnIndex("UniqueAttributename"), attributeName.toUpperCase());
			ps.setLong(osd.getInsertColumnIndex("OWNERID"), ownerid);
			ps.addBatch();
			ps.executeBatch();
			con.commit();
			osd.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			osd.close();
			throw new MTException(e);
		}
		return true;
	}
}
