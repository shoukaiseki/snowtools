package org.maximo.tools.impxml;


import bsh.EvalError;
import bsh.Interpreter;

import org.maximo.app.OutMessage;

import psdi.util.MXSession;

import java.io.File;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import org.maximo.app.MTException;
import org.maximo.app.MessageOnTerminal;
import org.maximo.app.OutMessage;
import org.maximo.app.config.ReadDruidDataSourceKonnfigu;
import org.maximo.app.resources.DruidDataSourceKonnfigu;
import org.maximo.tools.impxml.task.DSColumn;
import org.maximo.tools.impxml.task.DefaultSet;
import org.maximo.tools.impxml.task.ImpXmlDataSet;
import org.maximo.tools.impxml.task.Row;
import org.maximo.tools.impxml.task.Table;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class ImpXmlToJdbc extends ImpXmlTemplate{
	
	public ImpXmlToJdbc(Connection con,OutMessage om) {
		// TODO Auto-generated constructor stub
		super(om);
		this.setConnection(con);
	}
	
	public void execution(String xmlName) throws DocumentException, MTException, SQLException{
		//改變工作目錄為xml所在目錄
		File file = new File(xmlName);
		try {
			file = file.getCanonicalFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
		System.setProperty("user.dir",file.getParent());
		readDatabaseConfigureXML(xmlName);
		closeFile();	
	}
	
	/**
	 * @param xmlName
	 * @throws DocumentException
	 * @throws MTException 
	 * @throws SQLException 
	 */
	private void readDatabaseConfigureXML(String xmlName) throws DocumentException, MTException, SQLException {
		super.readXml(xmlName);
		Table rootTT=new Table();
		rootTT.setElement(root);
		rootTT.setOutMessage(om);
		rootTT.setTaskName("RESULTS");
		rootTT.setTable(rootTT);
		rootTT.parseElement();
	}

}
