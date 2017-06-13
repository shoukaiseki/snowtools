package org.maximo.tools.expxml;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Level;
import org.dom4j.DocumentException;

import org.maximo.app.MTException;
import org.maximo.app.MessageOnTerminal;
import org.maximo.app.OutMessage;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

/**
 *org.maximo.tools.expxml.ExpXmlMain
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-4
 * <P>ブログ http://shoukaiseki.blog.163.com/
 * <P>E-メール jiang28555@Gmail.com
 */
public class ExpXmlMain {
			OutMessage om=new MessageOnTerminal();
	
	/** 
	 * @param strs		{xml文件名}
	 * @throws DocumentException
	 * @throws MTException
	 * @throws SQLException
	 * @throws IOException 
	 */
	public ExpXmlMain(String[] strs) throws MTException {
		// TODO Auto-generated constructor stub
		PrintLogs logger=new PrintLogs();
		logger.setConsoleAppenderLevel(Level.INFO );
		try {
			if(strs!=null&&strs.length>0){
				ExpXmlFromJdbc ixtm=new ExpXmlFromJdbc(new MessageOnTerminal());
				
				String xmlName = strs[0];
				long l=new Date().getTime();
				ixtm.execution(xmlName);
				long end=new Date().getTime();
				String toukeiJikann = logger.toukeiJikann(l, end);
				om.info("共消耗時間:"+toukeiJikann);

			}else{
				help();
			}
		} catch (Exception e) {
			// TODO: handle exception
			throw new MTException(e);
		}
	}
	
	public void help(){
		om.info("將數據庫結果集導出到xml數據或者excle文件數據");
		om.info("命令格式:");
		om.info("\t"+" xml 導出配置文件名稱");
	}
	
	public static void main(String[] args) throws DocumentException, MTException, SQLException {
		
		new ExpXmlMain(args);
	}

}
