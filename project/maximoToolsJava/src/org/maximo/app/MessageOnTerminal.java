package org.maximo.app;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

import org.shoukaiseki.tuuyou.logger.PrintLogs;

public class MessageOnTerminal implements OutMessage{
	/**
	 * 是否輸出終端信息,像 TerminalShellScriptList 不需要輸出 Outessage 輸出信息,默認有輸出
	 */
	private boolean isPrint=true;
	PrintLogs logger=new PrintLogs();
	private MaximoToolsLogger mtl=null;
	
	public void setIsPrint(boolean isPrint){
		this.isPrint=isPrint;
	}

	public void println(String s) {
		// TODO Auto-generated method stub
		if(isPrint){
			System.out.println(s);
			logger.debug(s);
		}
	}

	public void println(Object s) {
		// TODO Auto-generated method stub
		if(isPrint){
			System.out.println(s);
			logger.debug(s);
		}
	}
	

	@Override
	public StringBuffer getMessage() {
		// TODO Auto-generated method stub
		return new StringBuffer();
	}

	@Override
	public void cleanMessage() {
		// TODO Auto-generated method stub
		
	}

	public void print(Object s) {
		// TODO Auto-generated method stub
		if(isPrint){
			System.out.print(s);
			logger.debug(s);
		}
		
	}

	@Override
	public void warn(Object message) {
		// TODO Auto-generated method stub
		logger.warn(message);
		
	}

	@Override
	public void warn(Object message, Throwable t) {
		// TODO Auto-generated method stub
		logger.warn(message,t);
		
	}

	@Override
	public void error(Object message) {
		// TODO Auto-generated method stub
		logger.error(message);
	}

	@Override
	public void error(Object message, Throwable e) {
		// TODO Auto-generated method stub
		logger.error(message, e);
	}
	
	@Override
	public void setMaximoToolsLoggerConnection(Connection con) throws MTException {
		// TODO Auto-generated method stub
		if(mtl==null){
			mtl=new MaximoToolsLogger(con,this);
		}else{
			logger.println("記錄連接池已經存在,不能重複設置,");
		}
	}

	/* (non-Javadoc)
	 * @see org.maximo.app.OutMessage#addMaximoToolsLogger(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addMaximoToolsLogger(String objectName, String attributeName,long ownerid) throws MTException {
		// TODO Auto-generated method stub
		if(mtl==null){
			logger.println("正式庫記錄連接池為空,請先 setMaximoToolsLoggerConnection(Connection con)之後再執行該方法操作");
			return false;
		}else{
			return mtl.addMaximoToolsLogger(objectName, attributeName,ownerid);
		}
	}

	@Override
	public void debug(Object message) {
		// TODO Auto-generated method stub
		if(isPrint){
			logger.debug(message);
		}
	}

	@Override
	public void debug(Object message, Throwable e) {
		// TODO Auto-generated method stub
		if(isPrint){
			logger.debug(message,e);
		}
	}

	@Override
	public void info(Object message) {
		// TODO Auto-generated method stub
		if(isPrint){
			logger.info(message);
		}
	}

	@Override
	public void info(Object message, Throwable e) {
		// TODO Auto-generated method stub
		if(isPrint){
			logger.info(message,e);
		}
	}

	@Override
	public void setConsoleAppenderLevel(Level level){
		logger.setConsoleAppenderLevel(level);
	}
	
	@Override
	public void setConsoleAppenderLayout(PatternLayout layout){
		logger.setConsoleAppenderLayout(layout);
	}

	@Override
	 public String getTrace(Throwable t) {
	        StringWriter stringWriter= new StringWriter();
	        PrintWriter writer= new PrintWriter(stringWriter);
	        t.printStackTrace(writer);
	        StringBuffer buffer= stringWriter.getBuffer();
	        return buffer.toString();
	    }
	
}
