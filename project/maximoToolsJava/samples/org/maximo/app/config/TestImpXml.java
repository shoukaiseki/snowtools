package org.maximo.app.config;

import java.io.File;
import java.util.Date;

import org.apache.log4j.Level;
import org.dom4j.DocumentException;

import org.maximo.app.MTException;
import org.maximo.app.MessageOnTerminal;
import org.maximo.app.OutMessage;
import org.maximo.tools.impxml.*;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

public class TestImpXml {
	
	
	public static void main(String[] args) throws Exception {
//		testMXServer();
		PrintLogs logger=new PrintLogs();
		logger.setConsoleAppenderLevel(Level.INFO );
		testJbc();
	}

	private static void testJbc() throws Exception {
		// TODO Auto-generated method stub
		ImpXmlToJdbc ixtm=new ImpXmlToJdbc(null,new MessageOnTerminal());
		String path="/tmp/log";
//		 path="Z:/";
		String xmlName = path+"/SHOUKAISEKI_TEST1.xml";
		 xmlName = path+"/test1.xml";
//		 xmlName = path+"/test1a.xml";
//		 xmlName = path+"/test2.xml";
//		 xmlName = path+"/test3.xml";
		 xmlName = path+"/test4.xml";
		 xmlName = path+"/hazard.xml";
		 xmlName = path+"/hazard_excleOK.xml";
		 xmlName = path+"/hazard_excle01.xml";
//		 xmlName = path+"/test11a.xml";
//		 xmlName = path+"/weixian.xml";
		 xmlName = "/media/develop/antProject/javaant/javaProject/maximoToolsJava/samples/impxml/gzdm/gzdm.xml";
//		 xmlName = "/media/develop/antProject/javaant/javaProject/maximoToolsJava/samples/impxml/gzdm/test1.xml";
//		 xmlName="/media/develop/antProject/javaant/javaProject/maximoToolsJava/samples/impxml/weixian/hazard_excle01.xml";
		 System.setProperty("user.dir", new File(xmlName).getParent());
		 PrintLogs pl=new PrintLogs();
		 long l=new Date().getTime();
		ixtm.execution(xmlName);
		long end=new Date().getTime();
		String toukeiJikann = pl.toukeiJikann(l, end);
		OutMessage om=new MessageOnTerminal();
		om.info("共消耗時間:"+toukeiJikann);
		
		
	}
	private static void testMXServer() throws DocumentException {
		// TODO Auto-generated method stub
		ImpXmlToMXServer ixtm=new ImpXmlToMXServer(null,new MessageOnTerminal());
		ixtm.execution("/tmp/log/SHOUKAISEKI_TEST1.xml");
		
	}
	

}
