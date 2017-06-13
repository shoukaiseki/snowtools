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

public class Mxs extends ListTemplate{
	
	 ShellScript  shellScript=null;
	 
	 public Mxs(ShellScript  shellScript) {
		// TODO Auto-generated constructor stub
		 this.shellScript=shellScript;
	}

	public void list(String[] args) {
		// TODO Auto-generated method stub
		Hashtable<String, MaximoShell> shellApps = shellScript.getShellApps();
		MaximoShell ms = shellApps.get("set");
		if(ms instanceof SetProperties){
			SetProperties set=(SetProperties) ms;
			ReadMXServerConfig readMXServerConfig = set.getReadMXServerConfig();
			Map<String, MXServerConfig> databaseConfigs = readMXServerConfig.getMXServerConfig();
			Set<String> keySet = databaseConfigs.keySet();
			boolean all=false;
			if(args.length>=2&&args[1]!=null){
				if(args[1].indexOf("a")!=-1){
					all=true;
				}
			}
			System.out.println("MXServer-configure.xml 配置有:");
			for (String key : keySet) {
				MXServerConfig msc=databaseConfigs.get(key);
				System.out.println("\t"+key);
				if(all){
					System.out.println("\t\t"+key+".username="+msc.getUsername());
					System.out.println("\t\t"+key+".password="+msc.getPassword());
					System.out.println("\t\t"+key+".server="+msc.getServer());
					System.out.println("\t\t"+key+".ddsname="+msc.getDdsname());
				}
			}
		}
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "mxs";
	}

	@Override
	public void help(String[] args) {
		// TODO Auto-generated method stub
		if(args==null){
			System.out.print("\t\t");
			System.out.println("mxs[參數]\t\t\t\t列出 MXServer-configure.xml 配置文件的信息.");
			System.out.print("\t\t\t");
			System.out.println("a\t\t\t\t  同時顯示詳細配置信息,例:tssls mxs a");
		}else{
			System.out.println("");
		}
		
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return Mxs.class.getName();
	}

}
