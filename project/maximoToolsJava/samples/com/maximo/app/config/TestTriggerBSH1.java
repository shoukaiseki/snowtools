package com.maximo.app.config;

import com.maximo.app.MTException;
import com.maximo.app.OutMessage;
import com.maximo.tools.impxml.bsh.TriggerBSH;
import com.maximo.tools.impxml.task.*;
import com.maximo.tools.impxml.*;
import java.sql.*;

/**
 *com.maximo.app.config.TestTriggerBSH1
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-2
 * <P>ブログ http://shoukaiseki.blog.163.com/
 * <P>E-メール jiang28555@Gmail.com
 */
public class TestTriggerBSH1 extends TriggerBSH {
	public TestTriggerBSH1() {
		// TODO Auto-generated constructor stub
		super();
	}

	public Object execute() throws MTException {
		// TODO Auto-generated method stub
		boolean b = true;
		if (ixrow.isBeginNullsAuto(new String[] { "ZY", "WXNAME", "CSNAME" })) {
			om.info("isBeginNullsAuto.false");
			return false;
		}
		SequenceSet ss = ixt.getSequenceSet("precautionseq");
		QuerySet qs = ixt.getQuerySet("cuoshi");
		qs.setObject(1, ixrow.getStringAuto("CSNAME"));
		String id =null;
		if(qs.hasNext()){
			id=qs.getObject("PRECAUTIONID").toString();
		}
		if (id != null) {
			ixrow.setValueAuto("PRECAUTIONID", id);
		} else {
			try {
				PreparedStatementSet ps1 = ixt.getPreparedStatement("pre");
				id = new java.text.DecimalFormat("XZ_CS00000").format(ss.nextVal());
				ixrow.setValueAuto("PRECAUTIONID", id);
				om.debug("asus.PS1設置值:PRECAUTION=["
						+ ixrow.getString("PRECAUTIONID") + "]");
				om.debug("asus.PS1設置值:PRECAUTION=[" + ixrow.getString("CSNAME")
						+ "]");
				ps1.setObject(1, ixrow.getStringAuto("PRECAUTIONID"));
				ps1.setObject(2, ixrow.getStringAuto("CSNAME"));
				ps1.setObject(3, ss.currval());
				ps1.addBatchAndExecuteBatch();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				om.error("", e);
			}
		}
		String[] dbAttNames = new String[] { "DESCRIPTION", "HAZARDTYPE","HAZ01" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, false);
		dbAttNames = new String[] { "HAZARDID" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, true);
		dbAttNames = new String[] { "HAZ01" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, false);
		dbAttNames = new String[] { "DESCRIPTION" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, false);
		dbAttNames = new String[] { "HAZARDTYPE" };
		ixrow.paste(ixrow.getMergedRegionValue(dbAttNames), dbAttNames, false);
		ixt = ixrow.getTable();
		
		QuerySet querySet = ixt.getQuerySet("weixian");
		querySet.setObject(1, ixrow.getStringAuto("DESCRIPTION"));
		querySet.setObject(2, ixrow.getStringAuto("HAZARDTYPE"));
		querySet.setObject(3, ixrow.getStringAuto("HAZ01"));
		b = !querySet.hasNext();
		if(!b){
			ixrow.setValue("HAZARDID", querySet.getObject("HAZARDID"));
			
		}
		PreparedStatementSet ps = ixt.getPreparedStatement("asus");
		if(!ixrow.isNulls(new String[]{"DESCRIPTION","CSNAME","HAZ01","HAZARDTYPE"})){
			QuerySet cswx = ixt.getQuerySet("cswx");
			cswx.setObject(1, ixrow.getStringAuto("HAZARDID"));
			cswx.setObject(2, ixrow.getStringAuto("PRECAUTIONID"));
			if(!cswx.hasNext()){
				try {
						om.debug("asus.PS設置值:HAZARD=[" + ixrow.getString("HAZARDID")
								+ "]");
						om.debug("asus.PS設置值:CSNUM=[" + ixrow.getString("PRECAUTIONID")
								+ "]");
						ps.setObject(1, ixrow.getString("HAZARDID"));
						ps.setObject(2, ixrow.getString("PRECAUTIONID"));
						ps.addBatchAndExecuteBatch();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					om.error("", e);
				}
			}
		}
//		 om.info("bsh.return:"+b);
		return b;
	}
}
