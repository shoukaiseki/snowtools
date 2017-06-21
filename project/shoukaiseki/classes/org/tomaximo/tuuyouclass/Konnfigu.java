package org.tomaximo.tuuyouclass;

import java.util.HashMap;
import java.util.Map;

public class Konnfigu{
	/**
	 * 应用名称
	 */
	private String appName="";
	/**
	 * 表名
	 */
	private String tableName="";
	/**
	 * 流程名称
	 */
	private String processName="";
	/**
	 * 流程修订名称
	 */
	private String processrevName="";

	public Konnfigu(){
		
	 }
	public Konnfigu(String tableName,String processrevName,String processName,String appName){
		setAll(tableName, processrevName, processName, appName);
	}
	
	public void setAll(String tableName,String processrevName,String processName,String appName){
		 this.tableName=tableName;
		 this.processrevName=processrevName;
		 this.processName=processName;
		 this.appName=appName;
	 }
	
	public void setTableName(String tableName){
		this.tableName=tableName;
	}
	public void setProcessrevName(String processrevName){
		this.processrevName=processrevName;
	}
	public void setProcessName(String processName){
		this.processName=processName;
	}
	public void setAppName(String appName){
		this.appName=appName;
	}

	public String getTableName(){
		if(this.tableName==null){
			return "";
		}else{
			return this.tableName;
		}
	}
	public String getProcessrevName(){
		if(this.processrevName==null){
			return "";
		}else{
			return this.processrevName;
		}
	}
	public String getProcessName(){
		if(this.processName==null){
			return "";
		}else{
			return this.processName;
		}
	}
	public String getAppName(){
		if(this.appName==null){
			return "";
		}else{
			return this.appName;
		}
	}
	public static void main(String ages[]){

		 Map<String,Konnfigu> actionKonnfigu=new HashMap();
		 actionKonnfigu.put("1",new Konnfigu("阿斯蒂芬","撒旦法","电饭锅","阿斯蒂芬"));
		 actionKonnfigu.put("2",new Konnfigu(null,null,null,null));
		 Konnfigu ko=actionKonnfigu.get("2");
		 System.out.println(ko.getAppName());
		
	}
}

