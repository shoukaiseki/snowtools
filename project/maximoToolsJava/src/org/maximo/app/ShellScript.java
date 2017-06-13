package org.maximo.app;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;

import psdi.util.MXException;
import psdi.util.MXSession;

import org.maximo.app.config.MaximoConfig;
import org.maximo.app.resources.MXServerConfig;


public class ShellScript {
	MXSession mxSession=null;
	SetProperties set=null;
	MaximoConfig mc=null;
	Connection con=null;
	OutMessage om=null;
	Hashtable<String, MaximoShell> shellTable=new Hashtable<String, MaximoShell>();
	
	public ShellScript(OutMessage om) {
		// TODO Auto-generated constructor stub
		this.om=om;
		initMaximoShell();
	}
	
	public void testInit() throws MTException {
		mc=new MaximoConfig();
		mxSession = MXSession.getSession();
		MXServerConfig mxsc = mc.getMXServerConfig();
		om.println(mxsc.getServer());
		mxSession.setHost(mxsc.getServer());
		mxSession.setUserName(mxsc.getUsername());
		mxSession.setPassword(mxsc.getPassword());
		try {
			mxSession.connect();
			con = mc.getDruidDataSource().getConnection();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new MTException(e);
		} catch (MXException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new MTException(e);
		} catch ( SQLException e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			throw new MTException(e);
		}
	}
	

	private void initMaximoShell() {
		// TODO Auto-generated method stub
		set=new SetProperties();
		set.setOutMessage(om);
		addMaximoShell(set);
		addMaximoShell(new CopyWFProcess());
		addMaximoShell(new CopyPersonGroup());
		addMaximoShell(new CopyMAXAttributecfg());
		addMaximoShell(new CopyMAXRelationship());
		addMaximoShell(new CopyMAXObjectCFG());
		addMaximoShell(new CopyAction());
		
	}
	
	public void addMaximoShell(MaximoShell ms){
		if(ms==null)
			return;
		if(shellTable.get(ms.getName())!=null){
			om.println(shellTable.get(ms.getName()).getClassName()+"类与"+ms.getClassName()+"类的命令重复");
		}else{
			om.println(ms.getName()+"命令成功注册");
			shellTable.put(ms.getName(),ms);
		}
	}
	
	public void eval(String arg) throws MTException{
		parse(arg);
	}

	/**
	 * @param arg
	 * @throws MTException 
	 */
	public void parse(String arg) throws MTException{
		//行分割
		String[] shells = arg.split("\n");
		for (String shell : shells) {
			shell=shell.trim();
			if(shell.isEmpty()){
				continue;
			}
			ArrayList<String> arr= split(shell);
			String bash =arr.get(0);
			arr.remove(0);
			if(bash!=null&& shellTable.get(bash)!=null){
				MaximoShell ms = shellTable.get(bash);
				if(!ms.getName().equals("set")){
					ms.setOutMessage(om);
					ms.setMXCon(mxSession, con);
					ms.execution(arr.toArray(new String[arr.size()]));
				}else{
					ms.execution(arr.toArray(new String[arr.size()]));
					con=set.getConnection();
					mxSession=set.getMXSession();
				}
			}else{
				om.warn("命令["+bash+"]不存在!");
			}
		}
	}
	
	public ArrayList<String> split(String shellLine){
		ArrayList<String> arr=new ArrayList<String>();
		for (String string : shellLine.split(" ")) {
			string=string.trim();
			if(string.isEmpty()){
				continue;
			}
			arr.add(string);
		}
		return arr;
	}

	public static void main(String[] args) throws MTException {
		
		ShellScript ss=new ShellScript(new MessageOnTerminal());
		ss.testInit();
		ss.eval("pro  XZ_AJHTA  1    XZ_AJHTA\n");
		
	}

	private Connection getThisConnection() {
		// TODO Auto-generated method stub
		return con;
	}

	private MXSession getThisMXSession() {
		// TODO Auto-generated method stub
		return mxSession;
	}
	
	public Hashtable<String, MaximoShell> getShellApps(){
		return shellTable;
	}

	public void close() {
		// TODO Auto-generated method stub
		om.info("正在斷開數據庫鏈接和MXSession鏈接");
		if (con!=null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				om.error("", e);
			}
		}
		if(mxSession!=null){
			try {
				mxSession.disconnect();
			} catch (RemoteException | MXException e) {
				// TODO Auto-generated catch block
				om.error("",e);
			}
		}
		om.info("數據庫鏈接和MXSession鏈接已斷開");
	}

}
