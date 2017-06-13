package org.maximo.tools.impxml;

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
 *org.maximo.tools.impxml.ImpXmlMain
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-2
 * <P>ブログ http://shoukaiseki.blog.163.com/
 * <P>E-メール jiang28555@Gmail.com
 */
public class ImpXmlMain {
			OutMessage om=new MessageOnTerminal();
	
	/** 
	 * @param strs		{xml文件名}
	 * @throws DocumentException
	 * @throws MTException
	 * @throws SQLException
	 * @throws IOException 
	 */
	public ImpXmlMain(String[] strs) throws MTException {
		// TODO Auto-generated constructor stub
		PrintLogs logger=new PrintLogs();
		logger.setConsoleAppenderLevel(Level.INFO );
		try {
			if(strs!=null&&strs.length>0){
				ImpXmlToJdbc ixtm=new ImpXmlToJdbc(null,new MessageOnTerminal());
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
		om.info("將xml數據或者excle文件數據按照指定要求導入數據庫中");
		om.info("命令格式:");
		om.info("\t"+" xml 導入配置文件名稱");
	}
	
	public static void main(String[] args) throws DocumentException, MTException, SQLException {
		
		new ImpXmlMain(args);
//		new ImpXmlMain(new String[]{"Z:/jobplan.xml"});
	}

}
