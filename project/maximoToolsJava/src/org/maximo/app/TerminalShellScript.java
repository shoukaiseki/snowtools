package org.maximo.app;

import java.io.File;

import org.shoukaiseki.file.SousaFile;

/** 命令
 *org.maximo.app.TerminalShellScript
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-1
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class TerminalShellScript {
	ShellScript  shellScript=null;
	
		MessageOnTerminal om = null;
	public TerminalShellScript(String[] args) throws MTException {
		// TODO Auto-generated constructor stub
		om = new MessageOnTerminal();
		if(args==null||args.length<1){
			help();
		}else{
			if(new File(args[0]).isFile()){
				SousaFile sf=new SousaFile();
				String str = sf.readFileToString(args[0]);
				shellScript=new ShellScript(om);
//				shellScript.testInit();
				shellScript.eval(str);
				shellScript.close();
			}else{
				help();
			}
		}
	}
	
	public void help(){
		om.println("命令格式:");
		om.println("\t"+" 腳本文件名,如 tss ma.mss");
	}
	
	public static void main(String[] args) throws MTException {
		String[] strs=args;
		if(args==null){
			strs=new String[]{"/tmp/win/test.mss"};
		}
		TerminalShellScript tss=new TerminalShellScript(strs);
		
	}
}
