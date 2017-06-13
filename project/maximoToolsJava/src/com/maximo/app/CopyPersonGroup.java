package com.maximo.app;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXException;
import psdi.util.MXSession;

public class CopyPersonGroup extends CopyMaximoTemplate
implements MaximoShell{
	OutMessage om=null;
	
	public CopyPersonGroup() {
		// TODO Auto-generated constructor stub
	}
	
	public CopyPersonGroup(MXSession mxSession,Connection con) {
		super(mxSession,con);
	}
	
	public void copyJdbcToMXServer(String fromPersonGroup,String toPersonGroup) throws MXException, RemoteException, SQLException{
		if(fromPersonGroup==null){
			return  ;
		}
		StringBuffer tableNamesWhere=null;
		tableNamesWhere=new StringBuffer("PERSONGROUP = '").append(fromPersonGroup).append("'");
		MboSetRemote pgSet = mxSession.getMboSet("PERSONGROUP");
		pgSet.setApp("PERSONGR");
		pgSet.setWhere("PERSONGROUP='"+toPersonGroup+"'");
		pgSet.reset();
		OracleSqlDetabese osd=new OracleSqlDetabese(con);
		osd.setTableRoot("PERSONGROUP");
		osd.setWhere(tableNamesWhere.toString());
		osd.format();
		om.println(osd.getSql());
		ResultSet rs=osd.executeQuery();
		if(rs.next()){
			if(pgSet.count()==0){
				MboRemote pg = pgSet.add();
				pg.setValue("PERSONGROUP", rs.getString("PERSONGROUP"));
				pg.setValue("DESCRIPTION", rs.getString("DESCRIPTION"));
				MboSetRemote pgtSet = pg.getMboSet("PERSONGROUP_PRIMARYMEMBERS");
				OracleSqlDetabese osdPgt=new OracleSqlDetabese(con);
				osdPgt.setTableRoot("PERSONGROUPTEAM");
				osdPgt.setWhere("persongroup='"+fromPersonGroup+"' and   resppartygroup=respparty");
				osdPgt.format();
				ResultSet rsPgt = osdPgt.executeQuery();
				while(rsPgt.next()){
					MboRemote pgt = pgtSet.add();
					pgt.setValue("RESPPARTYGROUP", rsPgt.getString("RESPPARTYGROUP"),11L);
					pgt.setValue("USEFORORG", rsPgt.getString("USEFORORG"));
					pgt.setValue("USEFORSITE", rsPgt.getString("USEFORSITE"));
					pgt.setValue("GROUPDEFAULT", rsPgt.getBoolean("GROUPDEFAULT"));
					pgt.setValue("RESPPARTYGROUPSEQ", rsPgt.getLong("RESPPARTYGROUPSEQ"));
					MboSetRemote pgtaSet = pgt.getMboSet("PRIMARYMEMBER_ALTERNATES");
					OracleSqlDetabese osdPgta=new OracleSqlDetabese(con);
					osdPgta.setTableRoot("PERSONGROUPTEAM");
					osdPgta.setWhere("persongroup='"+fromPersonGroup+"' and resppartygroup='"+rsPgt.getString("resppartygroup")+"' and resppartygroup!=respparty");
					osdPgta.format();
					ResultSet rsPgta = osdPgta.executeQuery();
					while(rsPgta.next()){
						MboRemote pgta = pgtaSet.add();
						pgta.setValue("RESPPARTY", rsPgta.getString("RESPPARTY"));
						pgta.setValue("RESPPARTYSEQ", rsPgta.getLong("RESPPARTYSEQ"));
					}
					osdPgta.close();
				}
				osdPgt.close();
				pgtSet.close();
				pgSet.save();
			}
		}
		pgSet.close();
		osd.close();
		
	}

	@Override
	public void execution(String[] strs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "pg";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return CopyPersonGroup.class.toString();
	}

	@Override
	public void setOutMessage(OutMessage om) {
		// TODO Auto-generated method stub
		this.om=om;
		
	}

	@Override
	public void setMXCon(MXSession mxSession, Connection con) {
		// TODO Auto-generated method stub
		om.println("根據源對應的人員組名稱,在目標新增一個人員組");
		om.println("命令格式:");
		om.println("\t"+getName()+" 源人員組名称(PROCESSNAME)  目标人員組名称(PROCESSNAME)");
		
	}

}
