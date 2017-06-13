package org.maximo.app.resources;

public class DruidDataSourceKonnfigu {
	public static final  String DRIVER_MICROSOFT_SERVER="sun.jdbc.odbc.JdbcOdbcDriver";
	public static final  String DRIVER_ORACLE="oracle.jdbc.driver.OracleDriver";
	private  int initialSize = 10;
	private  int minPoolSize = 15;
	private  int maxPoolSize = 30;
	private  int maxActive = 25;
	
	private String url=null;
	private String driver=null;
	private String user=null;
	private String password=null;
	public DruidDataSourceKonnfigu(){
		 this.url="";
		 this.user="";
		 this.driver=DRIVER_ORACLE;
		 this.password="";
	 }

	public DruidDataSourceKonnfigu(String url,String driver,String user,String password){
		 this.url=url;
		 this.driver=driver;
		 this.user=user;
		 this.password=password;
	 }
	public DruidDataSourceKonnfigu(String url,String driver,String user,String password,int initialSize,int maxActive,int minPoolSize,int maxPoolSize){
		 this.url=url;
		 this.driver=driver;
		 this.user=user;
		 this.password=password;
		 this.setInitialSize(initialSize);
		 this.setMaxActive(maxActive);
		 this.setMinPoolSize(minPoolSize);
		 this.setMaxPoolSize(maxPoolSize);
//		 System.out.println("ConnectionKonnfigu="+this.url);
//		 System.out.println("ConnectionKonnfigu="+this.driver);
//		 System.out.println("ConnectionKonnfigu="+this.user);
//		 System.out.println("ConnectionKonnfigu="+this.password);
	 }

	public DruidDataSourceKonnfigu(String url,String user,String password){
		 this.url=url;
		 this.user=user;
		 this.password=password;
	 }
	public void setUrl(String url){
		this.url=url;
	}
	public void setDriver(String driver){
		this.url=driver;
	}
	public void setUser(String user){
		this.url=user;
	}
	public void setPassword(String password){
		this.url=password;
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

	public int getInitialSize() {
		return initialSize;
	}

	public void setInitialSize(int initialSize) {
		this.initialSize = initialSize;
	}

	public int getMinPoolSize() {
		return minPoolSize;
	}

	public void setMinPoolSize(int minPoolSize) {
		this.minPoolSize = minPoolSize;
	}

	public int getMaxPoolSize() {
		return maxPoolSize;
	}

	public void setMaxPoolSize(int maxPoolSize) {
		this.maxPoolSize = maxPoolSize;
	}

	public int getMaxActive() {
		return maxActive;
	}

	public void setMaxActive(int maxActive) {
		this.maxActive = maxActive;
	}
 }



