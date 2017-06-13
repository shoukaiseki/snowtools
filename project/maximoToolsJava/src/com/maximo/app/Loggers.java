package com.maximo.app;

import com.shoukaiseki.tuuyou.logger.PrintLogs;

public class Loggers implements Logger{
	PrintLogs printLog=new PrintLogs();
	private static Logger logger=null;
	private StringBuffer logs=new StringBuffer();
	private StringBuffer sqllogs=new StringBuffer();
	/**
	 * 自动Syso输出Sqllogs
	 */
	private boolean autoPrintSqllogs=true;
	/**
	 * 自动Syso输出logs
	 */
	private boolean autoPrintLogs=true;
	
	public static Logger getLogger(){
		if(logger==null){
			logger=new Loggers();
		}
		return logger;
	}
	
	private Loggers(){
		
	}
	
	public void logger(String log){
		logs.append(log);
		println(log);
	}
	public void loggerln(String log){
		logs.append(log).append("\r\n");
		println(log);
	}

	public void cleanLogs(){
		logs.delete(0, logs.length());
	}
	
	public String getSqllogs(){
		return sqllogs.toString();
	}
	public void sqllogger(String log){
		sqllogs.append(log);
		if(autoPrintSqllogs)
			println(log);
	}
	public void sqlloggerln(String log){
		sqllogs.append(log).append("\r\n");
		if(autoPrintLogs)
			println(log);
	}
	
	public void cleanSqllogs(){
		sqllogs.delete(0, sqllogs.length());
	}

	public void println(String log){
//		System.out.println(log);
		printLog.debug(log);
	}

	public String getLogs() {
		// TODO Auto-generated method stub
		return logs.toString();
	}

	public void setAutoPrintSqllogs(boolean auto) {
		// TODO Auto-generated method stub
		this.autoPrintSqllogs=auto;
		
	}

	public void setAutoPrintLogs(boolean auto) {
		// TODO Auto-generated method stub
		this.autoPrintLogs=auto;
		
	}

	public boolean isAutoPrintSqllogs() {
		// TODO Auto-generated method stub
		return autoPrintSqllogs;
	}

	public boolean isAutoPrintLogs() {
		// TODO Auto-generated method stub
		return autoPrintLogs;
	}
}
