package com.maximo.app;

import java.sql.Connection;

import org.apache.log4j.Level;
import org.apache.log4j.PatternLayout;

/** 计时信息输出接口
 * 有个能在terminal,有可能web窗口
 * com.maximo.app.OutMessage
 * @author 蒋カイセキ    Japan-Tokyo  2013-6-13
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public interface OutMessage {
	
	/** 輸出信息,換行
	 */
	public void println(String s);
	/** 輸出信息,換行
	 */
	public void println(Object s);
	/** 輸出信息,不換行
	 */
	public void print(Object s);
	/** 獲取信息
	 */
	public StringBuffer getMessage();
	/**
	 * 清楚信息緩存
	 */
	public void cleanMessage();
	/** 輸出警告
	 * @param message
	 */
	public void warn(Object message);
	/** 輸出警告
	 * @param message
	 * @param t
	 */
	public void warn(Object message,Throwable t);
	/** 輸出錯誤
	 * @param message
	 */
	public void error(Object message);
	/**輸出錯誤
	 * @param message
	 * @param e
	 */
	public void error(Object message,Throwable e);
	
	/**設置記錄連接池
	 * @param con
	 */
	public void setMaximoToolsLoggerConnection(Connection con)throws MTException;
	
	/**添加插入記錄
	 * @param objectName
	 * @param attributeName
	 * @return true 插入成功,false 插入失敗.建議插入失敗不要再執行下去
	 * @throws MTException 
	 */
	public boolean addMaximoToolsLogger(String objectName,String attributeName,long ownerid) throws MTException;
	
	/**調試信息
	 * @param message
	 */
	public void debug(Object message);
	/**調試信息
	 * @param message
	 * @param e
	 */
	public void debug(Object message,Throwable e);
	
	/**基本信息
	 * @param message
	 */
	public void info(Object message);
	
	/**基本信息
	 * @param message
	 * @param e
	 */
	public void info(Object message,Throwable e);

	/**設置日誌輸出到控制台的級別,默認為 Level.DEBUG
	 * @param level
	 */
	public void setConsoleAppenderLevel(Level level);
	
	/**設置日誌輸出到控制台的Layout,默認為 new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n")
	 * @param layout
	 */
	public void setConsoleAppenderLayout(PatternLayout layout);
	
	 /** 獲取報錯的詳細信息
	 * @param t
	 * @return		轉化後的報錯信息
	 */
	public String getTrace(Throwable t) ;
}
