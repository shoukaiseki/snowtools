package org.maximo.app.config;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;

import psdi.util.MXException;
import psdi.util.MXSession;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.alibaba.druid.util.JMXUtils;
import org.maximo.app.MTException;
import org.maximo.app.OutMessage;
import org.maximo.app.resources.MXServerConfig;

public class MaximoConfig {
	
	private DruidDataSource dataSource;
	private MXServerConfig mxsc;
	private static int initialSize = 10;
	private static int minPoolSize = 15;
	private static int maxPoolSize = 30;
	private static int maxActive = 25;
	private OutMessage om=null;

	public MaximoConfig() {
		// TODO Auto-generated constructor stub
		mxsc=new MXServerConfig();
//		mxsc.setServer("172.22.48.100:13400/MXServer");
		mxsc.setServer("172.22.48.70:13400/MXServer");
//		mxsc.setServer("172.22.48.101:13471/MXServer");
//		mxsc.setServer("10.59.245.183:13402/MXServer");
		mxsc.setServer("10.59.245.186:13402/MXServer");
		mxsc.setServer("10.59.3.130:13405/MXServer");
		mxsc.setUsername("CRP_EAMWENZHOU");
		mxsc.setPassword("Pass1234567789");
		mxsc.setServer("localhost:13400/MAXIMO");
		mxsc.setUsername("maxadmin");
		mxsc.setPassword("123456");
		dataSource = new DruidDataSource();
		String jdbcUrl = "jdbc:oracle:thin:@172.22.48.70:1521:orcl";
		String user = "maximodemo";
		String password = "maximo";
		String driverClass = "oracle.jdbc.driver.OracleDriver";
		JMXUtils.register("com.alibaba:type=DruidDataSource", dataSource);
		
		jdbcUrl = "jdbc:oracle:thin:@172.22.48.70:1521:crep";
		user = "maximo";
		password = "maximo";
		
//		jdbcUrl = "jdbc:oracle:thin:@172.22.48.254:1522:eamcrpjs";
//		user = "maxdb";
//		password = "maximo";
		jdbcUrl = "jdbc:oracle:thin:@172.22.48.101:1521:orcl";
		user = "maximo7001";
		password = "maximo";
		
		jdbcUrl = "jdbc:oracle:thin:@10.59.245.183:1521:eamcrpwz";
		user = "maximo";
		password = "maximo";
		
		jdbcUrl = "jdbc:oracle:thin:@10.59.245.184:1521:crpxdj";
		user = "maximo";
		password = "maximo";
		
		jdbcUrl = "jdbc:oracle:thin:@10.59.245.184:1521:crpwzrl";
		user = "maximo";
		password = "maximo";
		
		jdbcUrl = "jdbc:oracle:thin:@172.16.115.217:1521:maxoct";
		user = "maximo";
		password = "maximo";
		dataSource.setInitialSize(initialSize);
		dataSource.setMaxActive(maxActive);
		dataSource.setMinIdle(minPoolSize);
		dataSource.setMaxIdle(maxPoolSize);
		dataSource.setPoolPreparedStatements(true);
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(jdbcUrl);
		dataSource.setPoolPreparedStatements(true);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		dataSource.setValidationQuery("SELECT 'asus' from dual");
		dataSource.setTestOnBorrow(true);

	}
	
	public void setOutMessage(OutMessage om){
		this.om=om;
	}
	
	
	public MXServerConfig getMXServerConfig(){
		return mxsc;
	}
			
	public DruidDataSource getDruidDataSource(){
		return dataSource;
	}
}
