package com.maximo.app;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import psdi.mbo.*;
import psdi.util.MXException;
import psdi.util.MXSession;
import test.TestOracle;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.maximo.app.config.MaximoConfig;
import com.maximo.app.resources.MXServerConfig;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

/** com.maximo.app.CopyMAXObjectCFG
 * @author 蒋カイセキ    Japan-Tokyo  2017年6月10日
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class CopyMAXObjectCFG extends CopyMaximoTemplate implements MaximoShell {

	public CopyMAXObjectCFG() {
		// TODO Auto-generated constructor stub
		super();
	}

	public void copyJdbcToMXServer(String tableNames) throws MTException {
		if (tableNames == null || tableNames.isEmpty()) {
			return;
		}
		StringBuffer autoKey = null;
		StringBuffer tableNamesWhere = null;
		MboSetRemote maxAttSet = null;
		MboSetRemote maxObjectSet =null;

		try {
			for (String string : tableNames.split(",")) {
				string = string.trim();
				if (string.isEmpty()) {
					continue;
				}
				if (tableNamesWhere == null) {
					tableNamesWhere = new StringBuffer("objectname in ('")
							.append(string).append("'");
				} else {
					tableNamesWhere.append(",'").append(string).append("'");
				}
			}
			tableNamesWhere.append(")");

			maxObjectSet = mxSession.getMboSet("MAXOBJECTCFG");
			maxObjectSet.setApp("CONFIGUR");

			OracleSqlDetabese osd = new OracleSqlDetabese(con);
			osd.setTableRoot("MAXOBJECTCFG");
			osd.setWhere(tableNamesWhere.toString());
			osd.format();
			System.out.println(osd.getSql());
			ResultSet rs = osd.executeQuery();
			MaximoJyouhou mj = new MaximoJyouhou(con);
			while (rs.next()) {
				String objectName = rs.getString("OBJECTNAME");
				String uniqueColumnName = mj.getUniqueColumnName(objectName)
						.getName();
				maxObjectSet.setWhere("OBJECTNAME='" + objectName + "'");
				maxObjectSet.reset();
				System.out.println(maxObjectSet.getCompleteWhere());
				boolean isNew = false;
				MboRemote maxobject = null;
				if (maxObjectSet.count() == 0) {
					System.out.println("to add");
					maxobject = maxObjectSet.add();
					maxobject.setValue("OBJECTNAME", objectName);
					maxobject.setValue("DESCRIPTION",
							rs.getString("DESCRIPTION"));
					maxobject.setValue("SERVICENAME",
							rs.getString("SERVICENAME"));
					maxobject.setValue("CLASSNAME", rs.getString("CLASSNAME"));
					maxobject.setValue("SITEORGTYPE",
							rs.getString("SITEORGTYPE"));
					maxobject.setValue("MAINOBJECT",
							rs.getBoolean("MAINOBJECT"));
					isNew = true;
				} else {
					maxobject = maxObjectSet.getMbo(0);
				}
				OracleSqlDetabese osdAtt = new OracleSqlDetabese(con);
				osdAtt.setTableRoot("MAXATTRIBUTECFG");
				osdAtt.setWhere("OBJECTNAME='" + objectName + "'");
				osdAtt.format();
				ResultSet rsAtt = osdAtt.executeQuery();
				maxAttSet = maxobject.getMboSet("MAXATTRIBUTECFG");
				for (; rsAtt.next();) {
					if (isNew) {
						if (rsAtt.getString("ATTRIBUTENAME").equals(
								uniqueColumnName)
								|| rsAtt.getString("ATTRIBUTENAME").equals(
										"DESCRIPTION")
								|| rsAtt.getString("ATTRIBUTENAME").equals(
										"HASLD")
								|| rsAtt.getString("ATTRIBUTENAME").equals(
										"DESCRIPTION_LONGDESCRIPTION")) {
							continue;
						}
						System.out.println("attributename="
								+ rsAtt.getString("attributename"));
						MboRemote maxAtt = maxAttSet.add();
						maxAtt.setValue("attributename",
								rsAtt.getString("attributename"));
						maxAtt.setValue("PERSISTENT",
								rsAtt.getBoolean("PERSISTENT"));
						maxAtt.setValue("REMARKS", rsAtt.getString("REMARKS"),
								2L);
						maxAtt.setValue("TITLE", rsAtt.getString("TITLE"), 2L);
						maxAtt.setValue("MAXTYPE", rsAtt.getString("MAXTYPE"),
								2L);
						maxAtt.setValue("LENGTH", rsAtt.getInt("LENGTH"), 2L);
						maxAtt.setValue("SCALE", rsAtt.getInt("SCALE"), 2L);
						maxAtt.setValue("SAMEASOBJECT",
								rsAtt.getString("SAMEASOBJECT"), 2L);
						maxAtt.setValue("SAMEASATTRIBUTE",
								rsAtt.getString("SAMEASATTRIBUTE"), 2L);
						maxAtt.setValue("SEARCHTYPE",
								rsAtt.getString("SEARCHTYPE"), 2L);
						maxAtt.setValue("LOCALIZABLE",
								rsAtt.getBoolean("LOCALIZABLE"), 2L);
						maxAtt.setValue("TEXTDIRECTION",
								rsAtt.getString("TEXTDIRECTION"), 2L);
						maxAtt.setValue("COMPLEXEXPRESSION",
								rsAtt.getString("COMPLEXEXPRESSION"), 2L);
						maxAtt.setValue("CLASSNAME",
								rsAtt.getString("CLASSNAME"), 2L);
						maxAtt.setValue("DOMAINID",
								rsAtt.getString("DOMAINID"), 2L);
						maxAtt.setValue("DEFAULTVALUE",
								rsAtt.getString("DEFAULTVALUE"), 11L);
						maxAtt.setValue("AUTOKEYNAME",
								rsAtt.getString("AUTOKEYNAME"), 11L);
						if (rsAtt.getString("AUTOKEYNAME") != null) {
							if (autoKey == null) {
								autoKey = new StringBuffer(
										rsAtt.getString("AUTOKEYNAME"));
							} else {
								autoKey.append(",").append(rsAtt.getString("AUTOKEYNAME"));
							}
						}

					}
				}
				maxObjectSet.save();
				if (isNew) {
					for (int j = 0; j < maxAttSet.count(); j++) {
						MboRemote maxAtt = maxAttSet.getMbo(j);
						if (maxAtt.getString("ATTRIBUTENAME").equals(
								uniqueColumnName)) {
							maxAtt.setValue("TITLE", "ID");
							maxAtt.setValue("REMARKS", "ID", 2L);
						} else if (maxAtt.getString("ATTRIBUTENAME").equals(
								"DESCRIPTION")) {
						} else if (maxAtt.getString("ATTRIBUTENAME").equals(
								"DESCRIPTION_LONGDESCRIPTION")) {
							maxAtt.setValue("TITLE", "详细描述");
							maxAtt.setValue("REMARKS", "详细描述", 2L);
						}
					}
					maxObjectSet.save();
				}
			}

			maxObjectSet.close();
			osd.close();
			if(autoKey!=null){
				om.println(autoKey + "等字段启用了自动编号,请手动設置自動編號");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			closeMboSet(maxObjectSet);
			closeMboSet(maxAttSet);
			
			throw new MTException(e);
		}
	}

	public List getObjectNames() throws SQLException {
		OracleSqlDetabese osd = new OracleSqlDetabese(con);
		osd.setTableRoot("MAXOBJECTCFG");
		// internal=0 不是内部
		// maximo默认的过滤
		osd.setWhere("((entityname is null or entityname not in (select tablename from maxtablecfg where isaudittable = 1))) and ((internal = 0))");
		osd.format();
		ResultSet rs = osd.executeQuery();
		List array = new ArrayList<String>();
		while (rs.next()) {
			array.add(rs.getString("OBJECTNAME"));
		}
		osd.close();
		return array;
	}

	public static void main(String[] args) throws MTException, RemoteException, MXException, SQLException {
		CopyMAXObjectCFG cmo = new CopyMAXObjectCFG();
		cmo.init();
		cmo.setOutMessage(new MessageOnTerminal());
		// cmo.testConnection();
		String str = null;
		cmo.copyJdbcToMXServer("BEANSHELLCLASSFUNCTION");
		cmo.copyJdbcToMXServer("BEANSHELLTRIGGER");
		cmo.copyJdbcToMXServer("BEANSHELLTRIGGERLOG");
		cmo.copyJdbcToMXServer("BEANSHELLLOGS");
		cmo.copyJdbcToMXServer("AUTODATESITENUM");
		

		cmo.close();

	}

	@Override
	public void execution(String[] strs) throws MTException {
		// TODO Auto-generated method stub
		if (strs == null) {
			help();
		} else {
			if (strs.length == 1) {
				for (String string : strs) {
					om.println("CopyMAXAttributecfg參數=" + string);
				}
				copyJdbcToMXServer(strs[0]);
			} else {
				StringBuffer sb = new StringBuffer();
				for (String string : strs) {
					sb.append(string).append("\t");
				}
				om.println("传递的参数有误:" + sb.toString());
				help();
			}

		}

	}

	/**
	 * 幫助信息
	 */
	public void help() {
		om.println("根據提供的源信息複製表到目標表中");
		om.println("命令格式:");
		om.println("\t" + getName() + " 表名稱(OBJECTNAME) ");
		om.println("可以為多個字段,多個時用,分開");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "obj";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return CopyMAXObjectCFG.class.toString();
	}

	@Override
	public void setMXCon(MXSession mxSession, Connection con) {
		// TODO Auto-generated method stub
		this.mxSession = mxSession;
		this.con = con;
	}
}
