package com.shoukaiseki.tuuyou.logger.config;

import java.net.URL;

public class ConfigPath {
	/**
	 * 類運行目錄所在父目錄下的.config(win下爲config)目錄
	 * 例如:類放置於/webapp/birt/WEB-INF/classes目錄下
	 * 配置文件存放於/webapp/birt/WEB-INF/.config/
	 * CLASS_PATH_CONFIG就是這個/webapp/birt/WEB-INF/.config/
	 */
	public static  String PATH_CONFIG;
	/**
	 * 用戶下 HOME 目錄,爲 {user.home}, 無法取到時將使用 $CLASS_PATH_CONFIG 像 GWT 無法取到
	 */
	public static  String USER_HOME;
	/**
	 * 用戶下CONFIG目錄,爲 $USER_HOME/.config/shoukaiseki/java/
	 */
	public static  String USER_CONFIG_PATH;
	static{
		URL fileURL=ConfigPath.class.getClassLoader().getResource("/");
		String classDir = ConfigPath.class.getResource("/").getPath();
		String classConfig=null;
		if(fileURL!=null){
			classConfig=fileURL.getPath();
		}else{
			classConfig=classDir;
		}
		if(System.getProperty("os.name").equalsIgnoreCase("Linux")){
			PATH_CONFIG=classConfig+"/../.config/";
		}else{
			PATH_CONFIG=classConfig+"/../.config/";
		}
		String userHome="" ;
		try {
			userHome=System.getProperties().getProperty("user.home");
		} catch (Exception e) {
			// TODO: handle exception
			userHome=classConfig;
			
		}finally{
			USER_HOME=userHome;
			USER_CONFIG_PATH=USER_HOME+"/.config/shoukaiseki/java/";
		}
		System.out.println("USER_SHOUKAISEKI_CONFIG_PATH="+USER_CONFIG_PATH);
		System.out.println("CLASS_PATH_CONFIG="+PATH_CONFIG);
	}
	
	public static String CONFIG_PATH=USER_CONFIG_PATH;
	private static boolean webService=false;
	
	public ConfigPath(){
		
	}
	
	/**主要是因爲 Web Service 的配置文件隨包而帶 
	 * @param webService		true 類 運行與 webservice
	 */
	public ConfigPath(boolean webService){
		if(webService){
			CONFIG_PATH=PATH_CONFIG;
		}else{
			CONFIG_PATH=USER_CONFIG_PATH;
		}
		this.webService=webService;
		System.out.println("実行に Web Service CONFIG_PATH="+CONFIG_PATH);
	}
	
	public boolean isWebServer(){
		return webService;
	}

}
