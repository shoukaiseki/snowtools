package com.maximo.app.list;


import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;

import com.maximo.app.*;
import com.maximo.app.config.ReadDruidDataSourceKonnfigu;
import com.maximo.app.config.ReadMXServerConfig;
import com.maximo.app.resources.DruidDataSourceKonnfigu;
import com.maximo.app.resources.MXServerConfig;
import com.shoukaiseki.syso.TerminalCursorControl;
import com.shoukaiseki.tuuyou.logger.PrintLogs;

public class Jdbc extends ListTemplate{
	 ShellScript  shellScript=null;
	 
	 public Jdbc(ShellScript  shellScript) {
		// TODO Auto-generated constructor stub
		 this.shellScript=shellScript;
	}
	
	public void list(String[] args) {
		// TODO Auto-generated method stub
		Hashtable<String, MaximoShell> shellApps = shellScript.getShellApps();
		MaximoShell ms = shellApps.get("set");
		if(ms instanceof SetProperties){
			SetProperties set=(SetProperties) ms;
			ReadDruidDataSourceKonnfigu rdds = set.getReadDruidDataSourceKonnfigu();
			Map<String, DruidDataSourceKonnfigu> databaseConfigs = rdds.getDatabaseConfigs();
			Set<String> keySet = databaseConfigs.keySet();
			boolean all=false;
			if(args.length>=2&&args[1]!=null){
				if(args[1].indexOf("a")!=-1){
					all=true;
				}
			}
			System.out.println("DruidDataSource-configure.xml 配置有:");
			for (String key : keySet) {
				DruidDataSourceKonnfigu ddsk = databaseConfigs.get(key);
				System.out.println("\t"+key);
				if(all){
					System.out.println("\t\t"+key+".customUrl="+ddsk.getUrl());
					System.out.println("\t\t"+key+".user="+ddsk.getUser());
					System.out.println("\t\t"+key+".password="+ddsk.getPassword());
					System.out.println("\t\t"+key+".initialSize="+ddsk.getInitialSize());
					System.out.println("\t\t"+key+".minPoolSize="+ddsk.getMinPoolSize());
					System.out.println("\t\t"+key+".maxPoolSize="+ddsk.getMaxPoolSize());
					System.out.println("\t\t"+key+".maxActive="+ddsk.getMaxActive());
				}
			}
		}
	}
	
	public void init(){
		
	}
	
	public void help(String[] args){
		if(args==null){
			System.out.print("\t\t");
			System.out.println("jdbc[參數]\t\t\t\t列出 DruidDataSource-configure.xml 配置文件的信息.");
			System.out.print("\t\t\t");
			System.out.println("a\t\t\t\t  同時顯示詳細配置信息,例:tssls jdbc a");
		}else{
			System.out.println("");
		}
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "jdbc";
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return Jdbc.class.getName();
	}

}
