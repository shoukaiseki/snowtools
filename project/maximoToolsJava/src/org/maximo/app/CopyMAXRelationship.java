package org.maximo.app;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.maximo.app.config.MaximoConfig;
import org.maximo.app.resources.MXServerConfig;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

import psdi.mbo.*;
import psdi.util.MXException;
import psdi.util.MXSession;

public class CopyMAXRelationship extends CopyMaximoTemplate implements
		MaximoShell {
	
	public CopyMAXRelationship() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public CopyMAXRelationship(MXSession mxSession,Connection con) {
		super(mxSession,con);
	}


	public void copyJdbcToMXServer(String[] tableNames) throws MTException {
		if (tableNames == null) {
			return;
		}
		try {
			StringBuffer tableNamesWhere = null;
			for (String string : tableNames) {
				string=string.trim().toUpperCase();
				if (tableNamesWhere == null) {
					tableNamesWhere = new StringBuffer("objectname in ('")
							.append(string).append("'");
				} else {
					tableNamesWhere.append(",'").append(string).append("'");
				}
			}
			tableNamesWhere.append(")");

			MboSetRemote maxObjectSet = mxSession.getMboSet("MAXOBJECTCFG");
			maxObjectSet.setApp("CONFIGUR");

			OracleSqlDetabese osd = new OracleSqlDetabese(con);
			osd.setTableRoot("MAXOBJECTCFG");
			osd.setWhere(tableNamesWhere.toString());
			osd.format();
			om.println(osd.getSql());
			ResultSet rs = osd.executeQuery();
			while (rs.next()) {
				String objectName = rs.getString("OBJECTNAME");
				maxObjectSet.setWhere("OBJECTNAME='" + objectName + "'");
				maxObjectSet.reset();
				om.println(maxObjectSet.getCompleteWhere());
				om.println(new StringBuffer("檢索表:'").append(objectName).append("'").toString());
				MboRemote maxobject = null;
				if (maxObjectSet.count() == 0) {
					continue;
				}
				maxobject = maxObjectSet.getMbo(0);
				OracleSqlDetabese osdRelation = new OracleSqlDetabese(con);
				osdRelation.setTableRoot("MAXRELATIONSHIP");
				osdRelation.setWhere("PARENT='" + objectName + "'");
				osdRelation.format();
				ResultSet rsRelation = osdRelation.executeQuery();
				MboSetRemote maxRelationshipSet = maxobject
						.getMboSet("MAXRELATIONSHIP");
				while (rsRelation.next()) {
					String name = rsRelation.getString("NAME");
					String child = rsRelation.getString("CHILD");
//					om.println("name=" + name);
					maxRelationshipSet.setWhere("name='" + name
							+ "' and PARENT='" + objectName + "' and child='"
							+ child + "'");
					maxRelationshipSet.reset();
					if (maxRelationshipSet.count() == 0) {
						om.println(new StringBuffer("'").append(objectName).append("'表新增關係名'").append(rsRelation.getString("NAME")).append("'").toString());
						MboRemote maxRelationship = maxRelationshipSet.add();
						maxRelationship.setValue("NAME",
								rsRelation.getString("NAME"));
						// maxRelationship.setValue("PARENT",
						// rsRelation.getString("PARENT"));
						maxRelationship.setValue("CHILD",
								rsRelation.getString("CHILD"));
						maxRelationship.setValue("WHERECLAUSE",
								rsRelation.getString("WHERECLAUSE"));
						maxRelationship.setValue("REMARKS",
								rsRelation.getString("REMARKS"));
						maxRelationshipSet.save();
					}
				}
				osdRelation.close();
				maxRelationshipSet.close();
			}
			maxObjectSet.close();
			osd.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}

	}


	public static void main(String[] args) throws SQLException,
			RemoteException, MXException, MTException {
		CopyMAXRelationship cmr = new CopyMAXRelationship();
		cmr.init();
			cmr.setOutMessage(new MessageOnTerminal());
		// cmr.copyJdbcToMXServer(new String[]{"AJHQY","AJHYS"});
		// cmr.copyJdbcToMXServer(new String[]{"AJHTAJYHPC"});
		// cmr.copyJdbcToMXServer(new
		// String[]{"BEANSHELLCLASSFUNCTION","BEANSHELLLOGS"});
		// cmr.copyJdbcToMXServer(new String[]{"BEANSHELLTRIGGER"});
		// cmr.copyJdbcToMXServer(new String[]{"BEANSHELLTRIGGERLOG"});
		// cmr.copyJdbcToMXServer(new
		// String[]{"XZ_ZZ","ZZPERSON_PERSON","XZ_ZBFZR"});
//		cmr.copyJdbcToMXServer(new String[] { "OPTICKET" });
//		cmr.execution(new String[] { "OPTICKET" });
//		cmr.execution(new String[] { "BEANSHELLCLASSFUNCTION","BEANSHELLTRIGGER","BEANSHELLTRIGGERLOG","BEANSHELLLOGS" });
//		cmr.execution(new String[] { "RQREPORT","RQREPORTQX" });
			
//		cmr.execution(new String[] { "APKVERSION","DUTY","PI_AREA","PI_AREA_DEVICE","PI_AREA_ITEM","PI_DEVICE","PI_ITEM","PI_ITEM_UNIT","PI_LOG","PI_POST","PI_RFID","PI_SITE","PI_TASK","PI_TASKCFG","PI_TASKCFG_AREA","PI_TASKCFG_USER","PI_TASKDATETIME","PI_TASK_AREA","PI_TASK_DEVICE","PI_TASK_ITEM","PI_TASK_USER","PI_USER" });
		cmr.execution(new String[] { "CONTRACTPAYMENTLINE_FKLX" });
		cmr.close();
	}

	@Override
	public void execution(String[] strs) throws MTException {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if (strs == null||strs.length==0) {
			help();
		} else {
				for (String string : strs) {
					om.println("CopyMAXRelationship參數=" + string);
				}
				copyJdbcToMXServer(strs);
		}
	}

	/**
	 * 幫助信息
	 */
	public void help() {
		// TODO Auto-generated method stub
		om.println("表的關係複製,複製源中表的關係到目標表關係,目標存在該關係名則不複製");
		om.println("命令格式:");
		om.println("\t" + getName() + " 表名稱(OBJECTNAME) 表名稱(OBJECTNAME)]");
		om.println("也可以為多個字段,多個時用空格分開");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "rel";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return CopyMAXRelationship.class.toString();
	}

	@Override
	public void setMXCon(MXSession mxSession, Connection con) {
		// TODO Auto-generated method stub
		this.mxSession=mxSession;
		this.con=con;
	}
}
