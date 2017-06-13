package org.maximo.app;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import org.maximo.app.config.MaximoConfig;
import org.maximo.app.config.ReadDruidDataSourceKonnfigu;
import org.maximo.app.resources.MXServerConfig;

import psdi.mbo.MboSetRemote;
import psdi.util.MXException;
import psdi.util.MXSession;
public class CopyMaximoTemplate {
	OutMessage om=null;
	Logger logs=Loggers.getLogger();
	MXSession mxSession;
	MaximoConfig mc;
	Connection con;
	
	public CopyMaximoTemplate() {
		// TODO Auto-generated constructor stub
		mc=new MaximoConfig();
	}
	
	public CopyMaximoTemplate(MXSession mxSession,Connection con) {
		this();
		this.mxSession=mxSession;
		this.con=con;
	}
	
	public void init() throws RemoteException, MXException, SQLException {
		// TODO Auto-generated method stub
		mxSession = MXSession.getSession();
		MXServerConfig mxsc = mc.getMXServerConfig();
		System.out.println(mxsc.getServer());
		mxSession.setHost(mxsc.getServer());
		mxSession.setUserName(mxsc.getUsername());
		mxSession.setPassword(mxsc.getPassword());
		mxSession.connect();
		con = mc.getDruidDataSource().getConnection();
	}

	public void close() throws SQLException, RemoteException, MXException {
		// TODO Auto-generated method stub
		if(con!=null)
			con.close();
		if(mxSession!=null){
			mxSession.disconnect();
		}
	}
	
	public String getLogs(){
		return logs.getLogs();
	}
	
	public void logger(String log){
		logs.logger(log);
	}
	public void loggerln(String log){
		logs.loggerln(log);
	}

	public void cleanLogs(){
		logs.cleanLogs();
	}
	
	public String getSqllogs(){
		return logs.getSqllogs();
	}
	public void sqllogger(String log){
		logs.sqllogger(log);
	}
	public void sqlloggerln(String log){
		logs.sqlloggerln(log);
	}
	
	public void cleanSqllogs(){
		logs.cleanSqllogs();
	}
	
	public Connection getThisConnection(){
		return this.con;
	}
	
	public MXSession getThisMXSession(){
		return this.mxSession;
	}

	
	public void setOutMessage(OutMessage om) {
		// TODO Auto-generated method stub
		this.om=om;
	}
	
	public void closeMboSet(MboSetRemote msr){
		if(msr!=null){
			try {
				msr.rollback();
				msr.close();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (MXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
