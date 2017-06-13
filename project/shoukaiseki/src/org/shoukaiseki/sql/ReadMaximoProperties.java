package org.shoukaiseki.sql;

import java.io.*;
import java.net.InetAddress;
import java.net.URL;
import java.sql.*;
import java.util.*;

import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
import org.shoukaiseki.tuuyou.logger.PrintLogs;
import org.shoukaiseki.tuuyou.logger.config.ConfigPath;


public class ReadMaximoProperties {


	private PrintLogs logger=new PrintLogs();
	private String url =null;
	private String driver = null;
	private String user = null;
	private String password = null;
	private String sessionName=null;
	private String propertiesName="maximo.properties";
	public ReadMaximoProperties() {
		// TODO Auto-generated constructor stub
		logger.debug("ReadMaximoProperties new");
	}

	public void setProperty(Properties prop){
		url=  prop.getProperty("mxe.db.url");
		user=prop.getProperty("mxe.db.user");
		password=prop.getProperty("mxe.db.password");
		sessionName=prop.getProperty("mxe.db.schemaowner");
		driver=prop.getProperty("mxe.db.driver");
		logger.debug("url="+url);
		logger.debug("user="+user);
		logger.debug("password="+password);
		logger.debug("sessionName="+sessionName);
	}
	
	public  OracleSqlDetabese readProperties() {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		OracleSqlDetabese osd=null;
		try {
			logger.debug("ConfigFile="+ConfigPath.PATH_CONFIG+propertiesName);  
			InputStream stream =null;
			stream = new FileInputStream(ConfigPath.PATH_CONFIG+propertiesName);
			
			prop.load(stream);
			setProperty(prop);
			stream.close();
			osd=new OracleSqlDetabese(url,user,password,driver,"JapanAV");
			
			String sql="	alter session set current_schema = "+sessionName;
			
			if(!osd.update(sql)){
				logger.error("mxe.db.schemaowner="+sessionName+" Error無法切換到該用戶");
				logger.error(sql);
			}else{
				logger.debug("session 已經成功切換至"+sessionName);
			}
			sql="ALTER SESSION SET NLS_DATE_FORMAT ='yyyy-dd-mm hh24:mi:ss'";
			sql="ALTER SESSION SET NLS_DATE_FORMAT ='dd-mm-yyyy'";
			logger.debug(sql,7);
			osd.update(sql);
			sql="ALTER SESSION SET NLS_TIMESTAMP_FORMAT ='yyyy-dd-mm hh24:mi:ss'";
			sql="ALTER SESSION SET NLS_TIMESTAMP_FORMAT ='dd-mm-yyyy hh24:mi:ss'";
			logger.debug(sql,7);
			osd.update(sql);
//			sql="ALTER SESSION SET NLS_TIMESTAMP_TZ_FORMAT = 'YYYY-MM-DD HH24:MI:SS.FF TZH:TZM'";
//			logger.debug(sql,7);
//			osd.update(sql);


		} catch (IOException ex) { 
			logger.error("OracleSqlDetabese",ex);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("加載 SQL 驅動失敗:",e);
		}
		return osd;
	}


	public String getUrl(){
		return this.url;
	}
	public String getDriver(){
		return this.driver;
	}
	public String getUser(){
		return this.user;
	}
	public String getPassword(){
		return this.password;
	}
	public String getSchemaowner(){
		return this.sessionName;
	}
}


