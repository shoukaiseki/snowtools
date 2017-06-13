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

public class CopyActionBsh extends CopyMaximoTemplate
implements MaximoShell{
				MaximoToolsLogger mtl=null;
	public CopyActionBsh() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public CopyActionBsh(MXSession mxSession,Connection con) {
		super(mxSession,con);
	}
	
	public void copyJdbcToMXServer(String fromAction) throws  MTException{
		if(fromAction==null){
			return  ;
		}
		try {
			mtl=new MaximoToolsLogger(con, om);
			
		StringBuffer tableNamesWhere=null;
		tableNamesWhere=new StringBuffer("TABLEFIELD in ('").append(fromAction).append("') ");
		MboSetRemote actionSet = mxSession.getMboSet("BEANSHELLTRIGGER");
		actionSet.setApp("BSHT");
		actionSet.setWhere("TABLEFIELD='"+fromAction+"'");
		actionSet.reset();
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setTableRoot("BEANSHELLTRIGGER");
		osd.setWhere(tableNamesWhere.toString());
		osd.format();
//		System.out.println(osd.getSql());
		ResultSet rs=osd.executeQuery();
		if(rs.next()){
			if(actionSet.count()==0){
				MboRemote bsh = actionSet.add();	
				loggerln("新建操作BeanShell脚本 "+fromAction);
				bsh.setValue("TABLEFIELD", fromAction);
				bsh.setValue("DESCRIPTION", rs.getString("DESCRIPTION"),2L);
				bsh.setValue("APPNAME", rs.getString("APPNAME"),2L);
				bsh.setValue("CLASS", rs.getString("CLASS"),2L);
				bsh.setValue("FUNCTION", rs.getString("FUNCTION"),2L);
				bsh.setValue("WHEN", rs.getString("WHEN"),2L);
				bsh.setValue("TABLEFIELD", rs.getString("TABLEFIELD"),2L);
				bsh.setValue("SEQUENCE", rs.getString("SEQUENCE"),2L);
				bsh.setValue("SOURCE", osd.getClobToString("SOURCE"),2L);
				mtl.addMaximoToolsLogger(bsh);
			}
			actionSet.save();	
		}
		actionSet.close();
		osd.close();	
		} catch (Exception e) {
			// TODO: handle exception
			throw new MTException(e);
		}
	}

	
	public static void main(String[] args) throws RemoteException, MXException, SQLException, MTException {
		CopyAction ca=new CopyAction();
		ca.init();
		ca.copyJdbcToMXServer("XZ_AJH_001");
		ca.copyJdbcToMXServer("XZ_AJH_002");
		ca.copyJdbcToMXServer("XZ_AJH_003");
		ca.copyJdbcToMXServer("XZ_AJH_004");
		ca.copyJdbcToMXServer("XZ_AJH_005");
		ca.copyJdbcToMXServer("XZ_AJH_006");
		ca.copyJdbcToMXServer("XZ_AJH_007");
		ca.copyJdbcToMXServer("XZ_AJH_008");
		ca.copyJdbcToMXServer("XZ_AJH_009");
		ca.copyJdbcToMXServer("XZ_AJH_010");
		ca.copyJdbcToMXServer("XZ_AJH_020");
		ca.copyJdbcToMXServer("XZ_AJH_021");

	}

	@Override
	public void execution(String[] strs) throws MTException {
		// TODO Auto-generated method stub
		if(strs==null){
			help();
		}else{
			if(strs.length==1){
				for (String string : strs) {
					om.println(string);
				}
				copyJdbcToMXServer(strs[0]);
			}else{
				StringBuffer sb=new StringBuffer();
				for (	String string : strs) {
					sb.append(string).append("\t");
				}
				om.println("传递的参数有误:"+sb.toString());
				help();
			}
		}
		
	}

	public void help(){
		om.println("根據源操作名稱新增一個目標操作BeanShell脚本, 如果目標存在則不添加");
		om.println("命令格式:");
		om.println("\t"+getName()+" 源操作名(ACTION) ");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "abs";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return CopyAction.class.getName();
	}

	@Override
	public void setMXCon(MXSession mxSession, Connection con) {
		// TODO Auto-generated method stub
		this.mxSession=mxSession;
		this.con=con;
	}

}
