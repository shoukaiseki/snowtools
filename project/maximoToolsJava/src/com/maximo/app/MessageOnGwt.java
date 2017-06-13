package com.maximo.app; 

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

import com.shoukaiseki.tuuyou.logger.PrintLogs;

public class MessageOnGwt  implements OutMessage{
	
	PrintLogs logger=new PrintLogs();
	StringBuffer message=new StringBuffer();
	private MaximoToolsLogger mtl=null;

	@Override
	public void println(String s) {
		// TODO Auto-generated method stub
		message.append(s).append("\n");
		logger.debug(s);
	}

	@Override
	public void println(Object s) {
		// TODO Auto-generated method stub
		message.append(s).append("\n");
		logger.debug(s);
		
	}

	@Override
	public StringBuffer getMessage() {
		// TODO Auto-generated method stub
		return message;
	}

	public void cleanMessage() {
		// TODO Auto-generated method stub
		message.delete(0, message.length());
	}

	@Override
	public void print(Object s) {
		// TODO Auto-generated method stub
		message.append(s);
		logger.debug(s);
	}

	@Override
	public void warn(Object s) {
		// TODO Auto-generated method stub
		message.append(s).append("\n");
		logger.warn(s);
	}

	@Override
	public void warn(Object s, Throwable t) {
		// TODO Auto-generated method stub
		message.append(s).append("\n");
		logger.warn(s,t);
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

	/* (non-Javadoc)
	 * @see com.maximo.app.OutMessage#setMaximoToolsLoggerConnection(java.sql.Connection)
	 */
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
	 * @see com.maximo.app.OutMessage#addMaximoToolsLogger(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean addMaximoToolsLogger(String objectName, String attributeName,long ownerid) {
		// TODO Auto-generated method stub
		if(mtl==null){
			logger.println("正式庫記錄連接池為空,請先 setMaximoToolsLoggerConnection(Connection con)之後再執行該方法操作");
			return false;
		}else{
			
		}
		
		return true;
	}

	@Override
	public void debug(Object message) {
		// TODO Auto-generated method stub
		logger.debug(message);
	}

	@Override
	public void debug(Object message, Throwable e) {
		// TODO Auto-generated method stub
		logger.debug(message,e);
	}

	@Override
	public void info(Object message) {
		// TODO Auto-generated method stub
		logger.info(message);
	}

	@Override
	public void info(Object message, Throwable e) {
		// TODO Auto-generated method stub
		logger.info(message,e);
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
