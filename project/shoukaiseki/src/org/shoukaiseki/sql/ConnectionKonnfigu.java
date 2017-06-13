package org.shoukaiseki.sql;

/**
 * 存放Connection设置数据
 *org.shoukaiseki.sql.ConnectionKonnfigu
 * @author 蒋カイセキ    Japan-Tokyo 2012-6-29
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class ConnectionKonnfigu{
	
	public static final  String DRIVER_MICROSOFT_SERVER="sun.jdbc.odbc.JdbcOdbcDriver";
	public static final  String DRIVER_ORACLE="oracle.jdbc.driver.OracleDriver";
	
	private String url=null;
	private String driver=null;
	private String user=null;
	private String password=null;
	private String schemaowner=null;
	public ConnectionKonnfigu(){
		 this.url="";
		 this.user="";
		 this.driver=DRIVER_ORACLE;
		 this.password="";
		 this.schemaowner="";
	 }

	public ConnectionKonnfigu(String url,String driver,String user,String password,String schemaowner){
		 this.url=url;
		 this.driver=driver;
		 this.user=user;
		 this.password=password;
		 this.schemaowner=schemaowner;
	 }
	public ConnectionKonnfigu(String url,String driver,String user,String password){
		 this.url=url;
		 this.driver=driver;
		 this.user=user;
		 this.password=password;
//		 System.out.println("ConnectionKonnfigu="+this.url);
//		 System.out.println("ConnectionKonnfigu="+this.driver);
//		 System.out.println("ConnectionKonnfigu="+this.user);
//		 System.out.println("ConnectionKonnfigu="+this.password);
	 }

	public ConnectionKonnfigu(String url,String user,String password){
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
	public void setSchemaowner(String schemaowner){
		this.schemaowner=schemaowner;
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
		return this.schemaowner;
	}
 }



