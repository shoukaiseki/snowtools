package com.maximo.app;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.maximo.app.config.MaximoConfig;
import com.maximo.app.resources.MXServerConfig;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

import psdi.mbo.*;
import psdi.util.MXException;
import psdi.util.MXSession;

public class CopyMAXRole extends CopyMaximoTemplate{
	
	public CopyMAXRole() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public CopyMAXRole(MXSession mxSession,Connection con) {
		super(mxSession,con);
	}
	
	public void copyJdbcToMXServer(String fromMaxRole) throws MXException, RemoteException, SQLException{
		if(fromMaxRole==null){
			return  ;
		}
		StringBuffer tableNamesWhere=null;
		tableNamesWhere=new StringBuffer("MAXROLE in ('").append(fromMaxRole).append("') ");
		MboSetRemote maxRoleSet = mxSession.getMboSet("MAXROLE");
		maxRoleSet.setApp("ROLE");
		maxRoleSet.setWhere("MAXROLE='"+fromMaxRole+"'");
		maxRoleSet.reset();
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setTableRoot("MAXROLE");
		osd.setWhere(tableNamesWhere.toString());
		osd.format();
		sqlloggerln(osd.getSql());
		ResultSet rs=osd.executeQuery();
		if(rs.next()){
			if(maxRoleSet.count()==0){
				MboRemote maxRole = maxRoleSet.add();	
				loggerln("新建角色 "+fromMaxRole);
				maxRole.setValue("MAXROLE", fromMaxRole);
				maxRole.setValue("DESCRIPTION", rs.getString("DESCRIPTION"));
				maxRole.setValue("OBJECTNAME", rs.getString("OBJECTNAME"));
//				maxRole.setValue("TYPE", formatType(rs.getString("TYPE")));
				maxRole.setValue("TYPE", rs.getString("TYPE"));
				maxRole.setValue("VALUE", rs.getString("VALUE"));
				maxRole.setValue("PARAMETER", rs.getString("PARAMETER"),2L);
				maxRole.setValue("ISEMAILDATASET", rs.getBoolean("ISEMAILDATASET"),2L);
				maxRole.setValue("ISBROADCAST", rs.getBoolean("ISBROADCAST"),2L);
				if("人员组".equals(rs.getString("TYPE"))&&rs.getString("VALUE")!=null){
					CopyPersonGroup cpg=new CopyPersonGroup(mxSession, con);
					cpg.setOutMessage(om);
					cpg.copyJdbcToMXServer(rs.getString("VALUE"), rs.getString("VALUE"));
				}
			}
		}
		maxRoleSet.save();	
		maxRoleSet.close();
		osd.close();	
		
	}
	
	private String formatType(String string) {
		// TODO Auto-generated method stub
		if(string==null){
			return null;
		}
		//^\(.*\)\t\(.*\)$/if(string.equals("\1")){\rs="\2";\r}else
		String s=string;
		if (string.equals("值")) {
			s = "描述";
		} else if (string.equals("定制")) {
			s = "定制类";
		} else if (string.equals("数据集")) {
			s = "与记录相关的数据集";
		} else if (string.equals("电子邮件地址")) {
			s = "电子邮件地址";
		} else if (string.equals("人员")) {
			s = "人员";
		} else if (string.equals("人员组")) {
			s = "人员组";
		} else if (string.equals("用户数据")) {
			s = "与登录用户相关的数据集";
		}
		return s;
	}
}
