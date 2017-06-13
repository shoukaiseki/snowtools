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

public class CopyAction extends CopyMaximoTemplate
implements MaximoShell{
				MaximoToolsLogger mtl=null;
	public CopyAction() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public CopyAction(MXSession mxSession,Connection con) {
		super(mxSession,con);
	}
	
	public void copyJdbcToMXServer(String fromAction) throws  MTException{
		if(fromAction==null){
			return  ;
		}
		try {
			mtl=new MaximoToolsLogger(con, om);
			
		StringBuffer tableNamesWhere=null;
		tableNamesWhere=new StringBuffer("ACTION in ('").append(fromAction).append("') ");
		MboSetRemote actionSet = mxSession.getMboSet("ACTION");
		actionSet.setApp("ACTION");
		actionSet.setWhere("ACTION='"+fromAction+"'");
		actionSet.reset();
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setTableRoot("ACTION");
		osd.setWhere(tableNamesWhere.toString());
		osd.format();
//		System.out.println(osd.getSql());
		ResultSet rs=osd.executeQuery();
		if(rs.next()){
			if(actionSet.count()==0){
				MboRemote maxProcessName = actionSet.add();	
				loggerln("新建操作 "+fromAction);
				maxProcessName.setValue("ACTION", fromAction);
				maxProcessName.setValue("DESCRIPTION", rs.getString("DESCRIPTION"),2L);
				maxProcessName.setValue("OBJECTNAME", rs.getString("OBJECTNAME"),2L);
				maxProcessName.setValue("TYPE", formatType(rs.getString("TYPE")),2L);
//				maxProcessName.setValue("DISPVALUE", rs.getString("DISPVALUE"),2L);
				maxProcessName.setValue("DISPVALUE", rs.getString("VALUE2"),2L);
				maxProcessName.setValue("PARAMETER", rs.getString("PARAMETER"),2L);
				mtl.addMaximoToolsLogger("ACTION", "ACTION", maxProcessName.getUniqueIDValue());
			}
		}
		actionSet.save();	
		actionSet.close();
		osd.close();	
		} catch (Exception e) {
			// TODO: handle exception
			throw new MTException(e);
		}
	}

	private String formatType(String string) {
		// TODO Auto-generated method stub
		if(string==null){
			return null;
		}
		String s=string;
		if(string.equals("组")){
//			s="操作组";
			s="组";
		}else if(string.equals("定制")){
//			s="定制类";
			s="定制";
		}
		return s;
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
		om.println("根據源操作名稱新增一個目標操作, 如果目標存在則不添加");
		om.println("命令格式:");
		om.println("\t"+getName()+" 源操作名(ACTION) ");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "act";
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
