package com.maximo.app;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Level;

import com.maximo.app.config.ReadDruidDataSourceKonnfigu;
import com.maximo.app.config.ReadMXServerConfig;
import com.maximo.app.list.Jdbc;
import com.maximo.app.list.ListTemplate;
import com.maximo.app.list.Mxs;
import com.maximo.app.resources.MXServerConfig;
import com.shoukaiseki.syso.TerminalCursorControl;
import com.shoukaiseki.tuuyou.logger.PrintLogs;


public class TerminalShellScriptList {
	Hashtable<String, ListTemplate> listTs=new Hashtable<String, ListTemplate>();
	 protected TerminalCursorControl tcc=null;
	 ShellScript  shellScript=null;
	 MessageOnTerminal om = null;
	public TerminalShellScriptList(String[] args) throws MTException {
		// TODO Auto-generated constructor stub
		
		om = new MessageOnTerminal();
		new PrintLogs().setConsoleAppenderLevel(Level.INFO );
		om.setIsPrint(false);
//		om.setIsPrint(true);
		shellScript=new ShellScript(om);
		initListTemplate();
		tcc=new TerminalCursorControl();
		if(args==null||args.length<1){
			help();
		}else{
			String p1=args[0];
			ListTemplate lt = listTs.get(p1);
			if(lt!=null){
				lt.list(args);
			}else{
				help();
			}
		}
	}
	
	public void initListTemplate(){
		addListTemplate(new Jdbc(shellScript));
		addListTemplate(new Mxs(shellScript));
	}
	

	public void addListTemplate(ListTemplate lt){
		if(lt==null)
			return;
		if(listTs.get(lt.getName())!=null){
			om.println(listTs.get(lt.getName()).getClassName()+"类与"+lt.getClassName()+"类的命令重复");
		}else{
			listTs.put(lt.getName(),lt);
		}
	}

	public void help(){
		om.setIsPrint(true);
		om.println("用法: tssls [選項]");
		om.println("功能:列出與tss命令相關的信息");
		om.println("可用選項有:");
		for(String key:listTs.keySet()){
			ListTemplate listTemplate = listTs.get(key);
			listTemplate.help(null);
		}
	}
	
	public static void main(String[] args) throws MTException {
		String[] strs=args;
		if(args==null){
			strs=new String[]{"jdbc"};
		}
		TerminalShellScriptList tss=new TerminalShellScriptList(strs);
		
	}

}
