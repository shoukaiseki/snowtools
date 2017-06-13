package com.maximo.app.config;

import com.maximo.app.MTException;
import com.maximo.tools.impxml.bsh.TriggerBSH;
import com.maximo.tools.impxml.task.*;
import java.sql.*;

/**
 * com.maximo.app.config.TestTriggerBSH1
 * 
 * @author Administrator
 * 
 */
public class TestTriggerBSH2 extends TriggerBSH {
	@Override
	public Object execute() throws MTException {
		// TODO Auto-generated method stub
		return super.execute();
	}

	public boolean asusasss() throws MTException {
		boolean b = true;
		try {
			
		Object ownerId=ixt.getVariable("ownerId").getValue();
		Object owner02=ixt.getVariable("owner02").getValue();
		Object ownernum=ixt.getVariable("ownernum").getValue();
		Object parentId=ixt.getVariable("parentId").getValue();
		Object parent02=ixt.getVariable("parent02").getValue();
		Object parentnum=ixt.getVariable("parentnum").getValue();

		String classIficationId=null;
		String classIficationDescription=null;
		Object superAssetId=ixt.getVariable("superAssetId").getValue();
		Object superAssetDescription=ixt.getVariable("superAssetDescription").getValue();
		PreparedStatementSet classancestorPs = ixt.getPreparedStatement("classancestor");
		PreparedStatementSet classusewithPs = ixt.getPreparedStatement("classusewith");
		PreparedStatementSet classificationPs = ixt.getPreparedStatement("classification");
		//如果最高级资产分类为空则初始化
		if(superAssetId==null){
			QuerySet querySet = ixrow.getTable().getQuerySet("superAssetId");
			querySet.hasNext();
			superAssetId=querySet.getObject("classstructureid");
			ixrow.setValue("superAssetId",superAssetId);
			superAssetDescription=querySet.getObject("DESCRIPTION");
			ixrow.setValue("superAssetDescription",superAssetDescription);
			ixt.setVariableValue("superAssetId",superAssetId);
			ixt.setVariableValue("superAssetDescription",superAssetDescription);
		}

		SequenceSet classstructureid = ixt.getSequenceSet("classstructureid");
		classstructureid.nextVal();
		if(!ixrow.isBeginNullsAuto(new String[]{"A"})){
			ownernum=ixrow.getStringAuto("A");
			owner02=ixrow.getStringAuto("B");
			ixt.setVariableValue("ownernum",ownernum);
			ixt.setVariableValue("owner02",owner02);
			ixt.setVariableValue("ownerId",classstructureid.currval());

			ixrow.setValue("PARENT", superAssetId);
			classIficationId=ixrow.getStringAuto("A");
			classIficationDescription=ixrow.getStringAuto("B");
			ixrow.setValue("HASCHILDREN", 1);
			classancestorPs.setObject(1, classstructureid.currval());
			classancestorPs.setObject(2, classIficationId);
			classancestorPs.setObject(3, classstructureid.currval());
			classancestorPs.setObject(4,classIficationDescription);
			classancestorPs.setObject(5,"0");
			classancestorPs.addBatchAndExecuteBatch();
			classancestorPs.setObject(1, classstructureid.currval());
			classancestorPs.setObject(2, classIficationId);
			classancestorPs.setObject(3, superAssetId);
			classancestorPs.setObject(4,superAssetDescription);
			classancestorPs.setObject(5,"1");
			classancestorPs.addBatchAndExecuteBatch();
			
		}else if(!ixrow.isBeginNullsAuto(new String[]{"C"})){
			classIficationId=ownernum+ixrow.getStringAuto("C");
			classIficationDescription=ixrow.getStringAuto("D");
			ixrow.setValue("HASCHILDREN", 1);
			ixrow.setValue("PARENT",ownerId);

			classancestorPs.setObject(1, classstructureid.currval());
			classancestorPs.setObject(2, classIficationId);
			classancestorPs.setObject(3, classstructureid.currval());
			classancestorPs.setObject(4,classIficationDescription);
			classancestorPs.setObject(5,"0");
			classancestorPs.addBatchAndExecuteBatch();

			classancestorPs.setObject(1, classstructureid.currval());
			classancestorPs.setObject(2, classIficationId);
			classancestorPs.setObject(3,ownerId);
			classancestorPs.setObject(4,owner02);
			classancestorPs.setObject(5,"1");
			classancestorPs.addBatchAndExecuteBatch();
			classancestorPs.setObject(1, classstructureid.currval());
			classancestorPs.setObject(2, classIficationId);
			classancestorPs.setObject(3,superAssetId);
			classancestorPs.setObject(4,superAssetDescription);
			classancestorPs.setObject(5,"1");
			classancestorPs.addBatchAndExecuteBatch();
		}else{
			return false;
		}
		classusewithPs.setObject(1,classstructureid.currval());
		classusewithPs.addBatchAndExecuteBatch();
		classificationPs.setObject(1,classIficationId);
		classificationPs.setObject(2,classIficationDescription);
		classificationPs.addBatchAndExecuteBatch();

		ixrow.setValue("CLASSSTRUCTUREID", classstructureid.currval());
		ixrow.setValue("CLASSIFICATIONID", classIficationId);
		ixrow.setValue("DESCRIPTION", classIficationDescription);
		return b;
		} catch (Exception e) {
			// TODO: handle exception
			throw new MTException(e);
		}
	}
}
