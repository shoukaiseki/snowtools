package com.maximo.app;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import psdi.mbo.*;
import psdi.util.MXException;
import psdi.util.MXSession;

import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class CopyMAXAttributecfg extends CopyMaximoTemplate
implements MaximoShell{

	public CopyMAXAttributecfg() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public CopyMAXAttributecfg(MXSession mxSession,Connection con) {
		super(mxSession,con);
	}

	/** 從jdb驅動方式複製到MXServer
	 * @param objectName				表名稱
	 * @param attributeNames		字段名稱
	 * @throws MTException
	 */
	private void copyJdbcToMXServer(String objectName, String attributeNames) throws  MTException {
		// TODO Auto-generated method stub
		if(objectName==null){
			return  ;
		}
		try {
			String objectNameTmp=objectName.toUpperCase();

			StringBuffer autoKey=null;
			StringBuffer tableNamesWhere=null;
			if(attributeNames==null||attributeNames.isEmpty()){
				tableNamesWhere=new StringBuffer("ATTRIBUTENAME not in (select name from MAXSEQUENCE where tbname = MAXATTRIBUTEcfg.objectname ) and OBJECTNAME in ('").append(objectNameTmp).append("') ");
			}else{
				StringBuffer attrNames=null;
				for(String str:attributeNames.split(",")){
					str=str.trim();
						if(str.isEmpty()){
							continue;
						}
					if(attrNames==null){
							attrNames=new StringBuffer("'").append(str.toUpperCase()).append("'");
					}else{
							attrNames.append(",'").append(str.toUpperCase()).append("'");
					}
				}
				tableNamesWhere = new StringBuffer(
						"ATTRIBUTENAME not in (select name from MAXSEQUENCE where tbname = MAXATTRIBUTEcfg.objectname ) and OBJECTNAME in ('")
				.append(objectNameTmp).append("') and ATTRIBUTENAME in(")
				.append(attrNames).append(")");
			}
			System.out.println(tableNamesWhere.toString());
			MboSetRemote maxObjectSet = mxSession.getMboSet("MAXOBJECTCFG");
			maxObjectSet.setApp("CONFIGUR");
			maxObjectSet.setWhere("OBJECTNAME='"+objectNameTmp+"'");
			maxObjectSet.reset();
			MboRemote maxobject=null;
			if(maxObjectSet.count()==0){
				maxObjectSet.close();
				throw new MTException("目標表'"+objectNameTmp+"'不存在,請使用表複製命令");
			}else{
				maxobject=maxObjectSet.getMbo(0);
			}
			MboSetRemote maxAttSet = maxobject.getMboSet("MAXATTRIBUTECFG");
			OracleSqlDetabese osd=new OracleSqlDetabese(con);
			osd.setTableRoot("MAXATTRIBUTECFG");
			osd.setWhere(tableNamesWhere.toString());
			osd.format();
			om.println(osd.getSql());
			ResultSet rsAtt=osd.executeQuery();
			System.out.println("where="+maxAttSet.getCompleteWhere());
			String maxAttSetWhere=maxAttSet.getAppWhere();
			while(rsAtt.next()){
				maxAttSet.setAppWhere(new StringBuffer("ATTRIBUTENAME='").append(rsAtt.getString("ATTRIBUTENAME")).append("'").toString());
				maxAttSet.reset();
				if(maxAttSet.count()==0){
					//檢索 maxAttSet 時如果沒有SITEID 回報錯
					maxAttSet.setAppWhere(maxAttSetWhere);
					maxAttSet.reset();
					MboRemote maxAtt = maxAttSet.add();
					om.println(new StringBuffer("表'").append(objectNameTmp).append("'添加字段:").append(rsAtt.getString("ATTRIBUTENAME")));
					maxAtt.setValue("attributename",rsAtt.getString("ATTRIBUTENAME"));
					maxAtt.setValue("REMARKS",rsAtt.getString("REMARKS"),2L);
					maxAtt.setValue("TITLE",rsAtt.getString("TITLE"),2L);
					maxAtt.setValue("MAXTYPE",rsAtt.getString("MAXTYPE"),2L);
					maxAtt.setValue("LENGTH",rsAtt.getInt("LENGTH"),2L);
					maxAtt.setValue("SCALE",rsAtt.getInt("SCALE"),2L);
					maxAtt.setValue("SAMEASOBJECT",rsAtt.getString("SAMEASOBJECT"),2L);
					maxAtt.setValue("SAMEASATTRIBUTE",rsAtt.getString("SAMEASATTRIBUTE"),2L);
					maxAtt.setValue("SEARCHTYPE",rsAtt.getString("SEARCHTYPE"),2L);
					maxAtt.setValue("LOCALIZABLE",rsAtt.getBoolean("LOCALIZABLE"),2L);
					maxAtt.setValue("TEXTDIRECTION",rsAtt.getString("TEXTDIRECTION"),2L);
					maxAtt.setValue("COMPLEXEXPRESSION",rsAtt.getString("COMPLEXEXPRESSION"),2L);
					maxAtt.setValue("CLASSNAME",rsAtt.getString("CLASSNAME"),2L);
					maxAtt.setValue("DOMAINID",rsAtt.getString("DOMAINID"),11L);
					if(rsAtt.getString("AUTOKEYNAME")!=null){
						om.warn(new StringBuffer("'").append(rsAtt.getString("attributename")).append("'字段的自動編號'").append(rsAtt.getString("AUTOKEYNAME")).append("'不存在,請手動添加."));
					}else{
						maxAtt.setValue("DEFAULTVALUE",rsAtt.getString("DEFAULTVALUE"),2L);
					}
					if(rsAtt.getString("DOMAINID")!=null){
						om.warn(new StringBuffer("'").append(rsAtt.getString("attributename")).append("'字段的域'").append(rsAtt.getString("DOMAINID")).append("'如果不存在,請手動添加."));
					}
					maxAttSet.save();
				}
			}
			maxAttSet.close();
			maxObjectSet.close();
			osd.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}

	}
	
	@Override
	public void execution(String[] strs) throws MTException {
		// TODO Auto-generated method stub
		if(strs==null){
			help();
		}else{
			if(strs.length==1||strs.length==2){
				for (String string : strs) {
					om.println("CopyMAXAttributecfg參數="+string);
				}
				if(strs.length==1){
					copyJdbcToMXServer(strs[0],null);
				}else{
					copyJdbcToMXServer(strs[0], strs[1]);
				}
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

	public static void main(String[] args) throws MTException {
		CopyMAXAttributecfg cma=new CopyMAXAttributecfg();
		try {
			cma.init();
			cma.setOutMessage(new MessageOnTerminal());
//			cma.execution(new String[]{"opticket","YN1,YN2,YN3,YN4,YN5,YN6,YN7,YN8,YN9,YN10,YN11,YN12,YN13,YN14,YN15,YN16,YN17,YN18,YN19,YN20"});
			cma.execution(new String[]{"opticket"});
			cma.close();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		} catch (MXException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
	}

	/**
	 *  幫助信息
	 */
	public void help(){
		om.println("根據提供的源信息複製表的字段到目標表中");
		om.println("命令格式:");
		om.println("\t"+getName()+" 表名稱(OBJECTNAME) [字段名稱(ATTRIBUTENAMES)]");
		om.println("字段名稱可以為空,為空時將默認複製所有目標沒有的字段");
		om.println("也可以為多個字段,多個時用,分開");
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "att";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return CopyMAXAttributecfg.class.toString();
	}

	@Override
	public void setMXCon(MXSession mxSession, Connection con) {
		// TODO Auto-generated method stub
		this.mxSession=mxSession;
		this.con=con;
		
	}

}
