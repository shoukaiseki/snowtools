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
import psdi.workflow.WFNodeRemote;

public class CopyWFProcess extends CopyMaximoTemplate implements MaximoShell {
	MaximoToolsLogger mtl = null;

	public CopyWFProcess() {
		// TODO Auto-generated constructor stub
		super();
	}

	public CopyWFProcess(MXSession mxSession, Connection con) {
		super(mxSession, con);
	}

	public void copyJdbcToMXServer(String fromProcessName, int fromProcessRev,
			String toProcessName) throws MTException {
		if (fromProcessName == null) {
			return;
		}
		try {

			mtl = new MaximoToolsLogger(con, om);
			StringBuffer tableNamesWhere = null;
			tableNamesWhere = new StringBuffer("PROCESSNAME in ('").append(
					fromProcessName).append(
					"') and processrev = " + fromProcessRev);
			MboSetRemote maxProcessSet = mxSession.getMboSet("WFPROCESS");
			maxProcessSet.setApp("WFDESIGN");
			maxProcessSet.setWhere("PROCESSNAME='" + toProcessName + "'");
			maxProcessSet.reset();
			OracleSqlDetabese osd = new OracleSqlDetabese(con);
			osd.setTableRoot("WFPROCESS");
			osd.setWhere(tableNamesWhere.toString());
			osd.format();
			om.println(osd.getSql());
			ResultSet rs = osd.executeQuery();
			if (rs.next()) {
				// maxProcessSet.count()==0 存在則不複製
				if (maxProcessSet.count() < 100) {
					MboRemote maxProcess = maxProcessSet.add();
					om.println(toProcessName);
					maxProcess.setValue("PROCESSNAME", toProcessName);
					maxProcess.setValue("DESCRIPTION", rs.getString("DESCRIPTION"));
					maxProcess.setValue("OBJECTNAME", rs.getString("OBJECTNAME"));
					long toProcessrev = maxProcess.getLong("PROCESSREV");
					mtl.addMaximoToolsLogger(maxProcess);
					MboSetRemote nodeSet = maxProcess.getMboSet("NODES");
					OracleSqlDetabese osdNode = new OracleSqlDetabese(con);
					osdNode.setTableRoot("WFNODE");
					osdNode.setWhere("processname = '" + fromProcessName
							+ "' and processrev = " + fromProcessRev);
					osdNode.format();
					ResultSet rsNode = osdNode.executeQuery();
					nodeSet.deleteAll();
					while (rsNode.next()) {
						MboRemote node = nodeSet.add();
						int nodeId = rsNode.getInt("NODEID");
						node.setValue("NODEID", nodeId, 2L);
						node.setValue("NODETYPE", rsNode.getString("NODETYPE"));
						node.setValue("XCOORDINATE", rsNode.getInt("XCOORDINATE"));
						node.setValue("YCOORDINATE", rsNode.getInt("YCOORDINATE"));
						node.setValue("IMAGEFILE", rsNode.getString("IMAGEFILE"));
						node.setValue("TITLE", rsNode.getString("TITLE"));
						node.setValue("DESCRIPTION", rsNode.getString("DESCRIPTION"));
						mtl.addMaximoToolsLogger(node);
						if ("任务".equalsIgnoreCase(rsNode.getString("NODETYPE"))) {
							// 添加任务节点属性的其它信息 start
							addWFTask(node, fromProcessName, fromProcessRev, toProcessName, toProcessrev, nodeId);
//							om.println("nodeId=" + node.getString("NODEID"));
//							om.println("PROCESSNAME=" + node.getString("PROCESSNAME"));
							// 添加任务节点属性的任务分配角色组 end
							addWFAssignment(node, fromProcessName, fromProcessRev, toProcessName, toProcessrev, nodeId);
						} else if ("子进程".equalsIgnoreCase(rsNode .getString("NODETYPE"))) {
							addWFSubProcess(node, fromProcessName, fromProcessRev, toProcessName, toProcessrev, nodeId);

						} else if ("条件".equalsIgnoreCase(rsNode .getString("NODETYPE"))) {
							MboSetRemote wfcSet = node.getMboSet("CONDITION");
							OracleSqlDetabese osdWfc = new OracleSqlDetabese( con);
							osdWfc.setTableRoot("WFCONDITION");
							osdWfc.setWhere("processname='" + fromProcessName
									+ "' and processrev=" + fromProcessRev
									+ " and nodeid=" + nodeId);
							osdWfc.format();
							ResultSet rsWfc = osdWfc.executeQuery();
							if (rsWfc.next()) {
								wfcSet.reset();
								MboRemote wfc = null;
								if (wfcSet.count() == 0) {
									wfc = wfcSet.add();
									wfc.setValue("PROCESSNAME", toProcessName,2L);
									wfc.setValue("NODEID", nodeId, 2L);
									wfc.setValue("PROCESSREV", node.getLong("PROCESSREV"), 2L);
									wfc.setValue("CUSTOMCLASS", rsWfc.getString("CUSTOMCLASS"));
									wfc.setValue("CONDITION", rsWfc.getString("CONDITION"));
									mtl.addMaximoToolsLogger(wfc);
									wfcSet.save();
								}
							}
							wfcSet.close();
							osdWfc.close();

						} else if ("输入".equalsIgnoreCase(rsNode
								.getString("NODETYPE"))) {
							MboSetRemote wfiSet = node.getMboSet("INPUT");
							wfiSet.reset();
							OracleSqlDetabese osdInput = new OracleSqlDetabese(
									con);
							osdInput.setTableRoot("WFINPUT");
							osdInput.setWhere("processname='" + fromProcessName
									+ "' and processrev=" + fromProcessRev
									+ " and nodeid=" + nodeId);
							osdInput.format();
							ResultSet rsInput = osdInput.executeQuery();
							if (rsInput.next()) {
								if (wfiSet.count() == 0) {
									MboRemote wfi = wfiSet.add();
									wfi.setValue("DISPLAYONE",
											rsInput.getLong("DISPLAYONE"));
									mtl.addMaximoToolsLogger(wfi);
									wfiSet.save();
								}
							}
							wfiSet.close();
							osdInput.close();
						}
						MboSetRemote actionSet = node.getMboSet("ACTIONS");
						OracleSqlDetabese osdAction = new OracleSqlDetabese(con);
						osdAction.setTableRoot("WFACTION");
						osdAction.setWhere("processname = '" + fromProcessName
								+ "' and processrev = " + fromProcessRev
								+ " and membernodeid=" + nodeId);
						osdAction.format();
						ResultSet rsAction = osdAction.executeQuery();
						while (rsAction.next()) {
							MboRemote action = actionSet.add();
							CopyAction ca = new CopyAction(mxSession, con);
							ca.setOutMessage(om);
							ca.copyJdbcToMXServer(rsAction.getString("ACTION"));
							CopyActionBsh abs=new CopyActionBsh(mxSession, con);
							abs.setOutMessage(om);
							abs.copyJdbcToMXServer(rsAction.getString("ACTION"));
							action.setValue("ACTIONID", rsAction.getString("ACTIONID"), 2L);
							action.setValue("OWNERNODEID", rsAction.getString("OWNERNODEID"), 2L);
							action.setValue("MEMBERNODEID", rsAction.getString("MEMBERNODEID"), 2L);
							action.setValue("ACTION", rsAction.getString("ACTION"), 11L);
							action.setValue("INSTRUCTION", rsAction.getString("INSTRUCTION"), 2L);
							action.setValue("ISPOSITIVE", rsAction.getString("ISPOSITIVE"), 11L);
							action.setValue("SEQUENCE", rsAction.getString("SEQUENCE"), 11L);
							action.setValue("CONDITION", rsAction.getString("CONDITION"), 2L);
							action.setValue("CONDITIONCLASS", rsAction.getString("CONDITIONCLASS"), 2L);
							mtl.addMaximoToolsLogger(action);
						}
						actionSet.save();
						actionSet.close();
						osdAction.close();
					}
					nodeSet.save();
					nodeSet.close();
					osdNode.close();
				}
			}
			maxProcessSet.save();
			maxProcessSet.close();
			osd.close();
		} catch (Exception e) {
			// TODO: handle exception
			throw new MTException(e);
		}

	}

	private void addWFSubProcess(MboRemote node, String fromProcessName,
			int fromProcessRev, String toProcessName, long toProcessrev,
			int nodeId) throws Exception {
		// 添加任务节点属性的其它信息 start
		MboSetRemote wfsSet = node.getMboSet("SUBPROCESS");
		OracleSqlDetabese osdWft = new OracleSqlDetabese(con);
		osdWft.setTableRoot("WFSUBPROCESS");
		osdWft.setWhere("processname='" + fromProcessName + "' and processrev="
				+ fromProcessRev + " and nodeid=" + nodeId);
		osdWft.format();
		ResultSet rsWfs = osdWft.executeQuery();
		if (rsWfs.next()) {
			wfsSet.reset();
			if (wfsSet.count() == 0) {
				MboRemote wfs = wfsSet.add();
				wfs.setValue("PROCESSNAME", toProcessName, 2L);
				wfs.setValue("NODEID", nodeId, 2L);
				wfs.setValue("PROCESSREV", toProcessrev, 2L);
				wfs.setValue("SUBPROCESSNAME", rsWfs.getString("SUBPROCESSNAME"), 11L);
				mtl.addMaximoToolsLogger(wfs);
				wfsSet.save();
			}
			// 不需要关闭,在 WFAssignment类中的add方法中还需要调用
			// wftSet.close();
			osdWft.close();
		}
		// 添加任务节点属性的其它信息 end
		
	}

	private void addWFAssignment(MboRemote node, String fromProcessName,
			long fromProcessRev, String toProcessName, long toProcessrev,
			int nodeId) throws RemoteException, MXException, SQLException, MTException {
		// 添加任务节点属性的任务分配角色组 start
		MboSetRemote wfaSet = node.getMboSet("DEFINEDASSIGNMENTS");
		OracleSqlDetabese osdWfa = new OracleSqlDetabese(con);
		osdWfa.setTableRoot("WFASSIGNMENT");
		osdWfa.setWhere("processname='" + fromProcessName + "' and processrev="
				+ fromProcessRev + " and nodeid=" + nodeId + " and wfid = 0 ");
		osdWfa.format();
		ResultSet rsWfa = osdWfa.executeQuery();
		while (rsWfa.next()) {
			CopyMAXRole cr = new CopyMAXRole(mxSession, con);
			cr.setOutMessage(om);
			cr.copyJdbcToMXServer(rsWfa.getString("ROLEID"));
			om.println(cr.getSqllogs());
			om.println(cr.getLogs());
			MboRemote wfa = wfaSet.add();
			wfa.setValue("ROLEID", rsWfa.getString("ROLEID"));
			wfa.setValue("RELATIONSHIP", rsWfa.getString("RELATIONSHIP"));
			wfa.setValue("APP", rsWfa.getString("APP"), 11L);
			wfa.setValue("DESCRIPTION", rsWfa.getString("DESCRIPTION"));
			wfa.setValue("ESCROLE", rsWfa.getString("ESCROLE"));
			wfa.setValue("TEMPLATEID", rsWfa.getString("TEMPLATEID"));
			wfa.setValue("CONDITION", rsWfa.getString("CONDITION"));
			wfa.setValue("TIMELIMIT", rsWfa.getString("TIMELIMIT"));
			wfa.setValue("PRIORITY", rsWfa.getString("PRIORITY"));
			wfa.setValue("EMAILNOTIFICATION", rsWfa.getString("EMAILNOTIFICATION"));
			wfa.setValue("CALENDARBASED", rsWfa.getString("CALENDARBASED"));
			// wfa.setValue("ASSIGNID", rsWfa.getLong("ASSIGNID"));
			mtl.addMaximoToolsLogger(wfa);

			wfaSet.save();

		}
		wfaSet.close();
		osdWfa.close();
		// 添加任务节点属性的任务分配角色组 end

	}

	private void addWFTask(MboRemote node, String fromProcessName,
			long fromProcessRev, String toProcessName, long toProcessrev,
			int nodeId) throws RemoteException, MXException, SQLException, MTException {
		// TODO Auto-generated method stub
		// 添加任务节点属性的其它信息 start
		MboSetRemote wftSet = node.getMboSet("TASK");
		OracleSqlDetabese osdWft = new OracleSqlDetabese(con);
		osdWft.setTableRoot("WFTASK");
		osdWft.setWhere("processname='" + fromProcessName + "' and processrev="
				+ fromProcessRev + " and nodeid=" + nodeId);
		osdWft.format();
		ResultSet rsWft = osdWft.executeQuery();
		if (rsWft.next()) {
			wftSet.reset();
			if (wftSet.count() == 0) {
				MboRemote wft = wftSet.add();
				wft.setValue("PROCESSNAME", toProcessName, 2L);
				wft.setValue("NODEID", nodeId, 2L);
				wft.setValue("PROCESSREV", toProcessrev, 2L);
				// 将为所有用户读取与该任务关联的应用程序，还是只为那些已分配任务的人员读取？
				wft.setValue("READONLY", rsWft.getBoolean("READONLY"), 2L);
				// 执行接受操作
				// 1:当任何任务分配被接受时 0:当所有任务分配都被接受时。
				wft.setValue("FIRSTCOMPLETE", rsWft.getBoolean("FIRSTCOMPLETE"));
				wft.setValue("APP", rsWft.getString("APP"), 11L);
				// 时限:在任务上报之前可以持续的小时数。
				wft.setValue("TIMELIMIT", rsWft.getFloat("TIMELIMIT"), 2L);
				// 是否基於日历
				wft.setValue("CALENDARBASED", rsWft.getBoolean("CALENDARBASED"), 2L);
				// 显示一个
				wft.setValue("DISPLAYONE", rsWft.getBoolean("DISPLAYONE"), 2L);
				// 任务类型
				wft.setValue("WFTASKTYPE", rsWft.getString("WFTASKTYPE"), 2L);
				mtl.addMaximoToolsLogger(wft);
				wftSet.save();
			}
			// 不需要关闭,在 WFAssignment类中的add方法中还需要调用
			// wftSet.close();
			osdWft.close();
		}
		// 添加任务节点属性的其它信息 end

	}

	public void rollback() throws SQLException {
		con.rollback();
	}

	public void close() throws SQLException, RemoteException, MXException {
		// TODO Auto-generated method stub
		if (con != null)
			con.close();
		if (mxSession != null)
			mxSession.disconnect();
	}

	public static void main(String[] args) throws RemoteException, MXException,
			SQLException, MTException {
		CopyWFProcess cw = new CopyWFProcess();
		cw.init();
		cw.setOutMessage(new MessageOnTerminal());
		cw.copyJdbcToMXServer("SR", 1, "SR");
		cw.copyJdbcToMXServer("XZ_SR", 1, "XZ_SR");
//		cw.copyJdbcToMXServer("WO", 1, "WO");
//		cw.copyJdbcToMXServer("XZ_WO", 1, "XZ_WO");
//		cw.copyJdbcToMXServer("XZ_WOTRACK", 1, "XZ_WOTRACK");
//		cw.copyJdbcToMXServer("XZ_WO_CM", 1, "XZ_WO_CM");
//		cw.copyJdbcToMXServer("XZ_WT", 1, "XZ_WT");
//		cw.copyJdbcToMXServer("XZ_WT_DDDL", 1, "XZ_WT_DDDL");
//		cw.copyJdbcToMXServer("XZ_WT_DH1", 1, "XZ_WT_DH1");
//		cw.copyJdbcToMXServer("XZ_WT_DH2", 1, "XZ_WT_DH2");
//		cw.copyJdbcToMXServer("XZ_WT_DQ1", 1, "XZ_WT_DQ1");
//		cw.copyJdbcToMXServer("XZ_WT_DQ2", 1, "XZ_WT_DQ2");
//		cw.copyJdbcToMXServer("XZ_WT_JDBH", 1, "XZ_WT_JDBH");
//		cw.copyJdbcToMXServer("XZ_WT_RJ", 1, "XZ_WT_RJ");
//		cw.copyJdbcToMXServer("XZ_WT_RK1", 1, "XZ_WT_RK1");
//		cw.copyJdbcToMXServer("XZ_WT_RK2", 1, "XZ_WT_RK2");
//		cw.copyJdbcToMXServer("XZ_WT_SXKJ", 1, "XZ_WT_SXKJ");
		cw.close();
		psdi.workflow.FldConditionAttribute asd;
		psdi.workflow.virtual.FldUserSql ad;
		psdi.workflow.WFAssignmentSet adsd;
		psdi.workflow.virtual.ShowWFInputSet ads;
	}

	public void execution(String[] strs) throws MTException {
		// TODO Auto-generated method stub
		if (strs == null) {
			help();
		} else {
			if (strs.length == 3) {
				for (String string : strs) {
					om.println(string);
				}
				copyJdbcToMXServer(strs[0], Integer.parseInt(strs[1]), strs[2]);
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

	public String getName() {
		// TODO Auto-generated method stub
		return "pro";
	}

	public String getClassName() {
		// TODO Auto-generated method stub
		return CopyWFProcess.class.getName();
	}

	public void setMXCon(MXSession mxSession, Connection con) {
		// TODO Auto-generated method stub
		this.mxSession = mxSession;
		this.con = con;

	}

	public void help() {
		om.println("根據源過程名稱及版本號新增一個目標過程名");
		om.println("命令格式:");
		om.println("\t" + getName()
				+ " 源过程名称(PROCESSNAME) 源版本号(PROCESSREV) 目标过程名称(PROCESSNAME)");
	}

}
