package org.maximo.app;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import com.alibaba.druid.pool.DruidDataSource;
import org.maximo.app.config.ReadDruidDataSourceKonnfigu;
import org.maximo.app.config.ReadMXServerConfig;

import psdi.util.MXException;
import psdi.util.MXSession;

public class SetProperties extends CopyMaximoTemplate
implements MaximoShell{
	protected static ReadDruidDataSourceKonnfigu rdds=null;
	private DruidDataSource dataSource;
	protected static ReadMXServerConfig rmsc=null;
	MXSession mxSession=null;
	Connection con=null;

	@Override
	public void execution(String[] strs) throws MTException {
		// TODO Auto-generated method stub
		if(strs==null){
			help();
		}else{
			if(strs.length==2){
				for (String string : strs) {
					om.println(string);
				}
				SetProperty(strs[0],strs[1]);
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

	private void SetProperty(String string, String string2) throws MTException {
		// TODO Auto-generated method stub
		if(string==null||string2==null){
			return;
		}
		switch (string) {
		case "jdbc":
			setDruidDataSource(string2);
			break;
		case "mxs":
			setMxSession(string2);
			break;
		case "close":
			switch (string2) {
			case "jdbc":
				closeJdbc();
				break;
			case "mxs":
				closeMxs();
				break;
			default:
				 om.info("無效的參數,正確參數為 close jdbc 或者 close mxs");
				break;
			}
			break;
		default:
			break;
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "set";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return SetProperties.class.getName();
	}

	@Override
	public void setOutMessage(OutMessage om) {
		// TODO Auto-generated method stub
		this.om=om;
		rdds=new ReadDruidDataSourceKonnfigu(om);
		rmsc=new ReadMXServerConfig(om);
		
	}

	public void setMXCon(MXSession mxSession, Connection con) {
		// TODO Auto-generated method stubgtgt
		this.mxSession=mxSession;
		this.con=con;
		
	}

	public void help(){
		om.println("設置屬性");
		om.println("命令格式:");
		om.println("\t"+getName()+" jdbc 連接名稱							--設置jdbc鏈接");
		om.println("\t"+getName()+" mxs 連接名稱								--設置mxs鏈接");
		om.println("\t"+getName()+" close	jdbc/mxs						--關閉jdbc鏈接/關閉mxs鏈接		");
	}
	
	
	public void closeMxs(){
		if(mxSession!=null){
			try {
				mxSession.connect();
				mxSession=null;
			} catch (RemoteException | MXException e) {
				// TODO Auto-generated catch block
				om.error("",e);
			}
		}
	}
	
	public void closeJdbc(){
		if(dataSource!=null){
			dataSource.close();
			dataSource=null;
			con=null;
		}
	}

	public void setMxSession(String name) throws MTException{
		if(mxSession!=null){
			om.warn("已經設置了JDBC驅動,不允許重新設置,如果想改變鏈接,請先 set close mxs");
		}else{
				try {
					mxSession=rmsc.getMXSession(name);
					om.info("連接已創建");
				} catch (MTException e) {
					// TODO Auto-generated catch block
					om.error("創建mxs連接失敗:",e);
					throw new MTException(e);
				}
		}
	}
	
	public void setDruidDataSource(String name) throws MTException{
		if(con!=null){
			om.warn("已經設置了JDBC驅動,不允許重新設置,如果想改變鏈接,請先 set close jdbc");
		}else{
			dataSource=rdds.getDruidDataSource(name);
			try {
				con = dataSource.getConnection();
				om.info("連接已創建");
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				om.error("創建jdbc連接失敗:",e);
				throw new MTException(e);
			}
		}
	}
	
	public ReadMXServerConfig getReadMXServerConfig(){
		return rmsc;
	}
	public ReadDruidDataSourceKonnfigu getReadDruidDataSourceKonnfigu(){
		return rdds;
	}
	
	public Connection getConnection(){
		return con;
	}
	
	public MXSession getMXSession(){
		return this.mxSession;
	}
	
}
