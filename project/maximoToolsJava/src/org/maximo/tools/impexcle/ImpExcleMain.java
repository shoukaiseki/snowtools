package org.maximo.tools.impexcle;

import java.sql.SQLException;
import java.util.Date;

import org.apache.log4j.Level;
import org.dom4j.DocumentException;

import org.maximo.app.MTException;
import org.maximo.app.MessageOnTerminal;
import org.maximo.app.OutMessage;
import org.maximo.tools.impexcle.task.TableSAX;
import org.maximo.tools.impxml.ImpXmlToJdbc;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

/** org.maximo.tools.impexcle.ImpExcleMain
 * @author 蒋カイセキ    Japan-Tokyo  2013-11-29
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class ImpExcleMain {
			OutMessage om=new MessageOnTerminal();
	
	/** 
	 * @param strs		{xml文件名}
	 * @throws DocumentException
	 * @throws MTException
	 * @throws SQLException
	 * @throws IOException 
	 */
	public ImpExcleMain(String[] strs) throws MTException {
		// TODO Auto-generated constructor stub
		PrintLogs logger=new PrintLogs();
		logger.setConsoleAppenderLevel(Level.INFO );
		try {
			if(strs!=null&&strs.length>0){
				TableSAX ixtm=new TableSAX(null,new MessageOnTerminal());
				String xmlName = strs[0];
				long l=new Date().getTime();
				ixtm.setOutMessage(om);
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
		om.info("\t"+" iem [filename]");
		om.info("");
		om.info("\t"+" [filename]\t導入配置文件名稱");
		
	}
	
	public static void main(String[] args) throws DocumentException, MTException, SQLException {
		
		new ImpExcleMain(args);
//		new ImpExcleMain(new String[]{"F:/SVN/CRPXZ/7 配置开发/導入数据/徐州项目/kks/shoukaiseki_insert_kks.xml"});
//		new ImpExcleMain(new String[]{"C:/temp/shoukaiseki_insert_kks.xml"});
		
	}

}
