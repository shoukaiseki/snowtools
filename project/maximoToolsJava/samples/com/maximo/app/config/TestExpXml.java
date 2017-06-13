package com.maximo.app.config;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.DocumentException;

import com.alibaba.druid.pool.DruidDataSource;
import com.maximo.app.MTException;
import com.maximo.app.MessageOnTerminal;
import com.maximo.app.OutMessage;
import com.maximo.tools.expxml.ExpXmlFromJdbc;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;
import com.shoukaiseki.tuuyou.logger.PrintLogs;
public class TestExpXml {
	public TestExpXml() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) throws DocumentException, MTException, SQLException {
		PrintLogs logger=new PrintLogs();
		logger.setConsoleAppenderLevel(Level.INFO );
		OutMessage om=new MessageOnTerminal();
		long l=new Date().getTime();
		ExpXmlFromJdbc exf=new ExpXmlFromJdbc(om);
		exf.execution("/media/develop/antProject/javaant/javaProject/maximoToolsJava/samples/expxml/maximo6item.xml");
		long end=new Date().getTime();
		String toukeiJikann = logger.toukeiJikann(l, end);
		om.info("共消耗時間:"+toukeiJikann);
	}
	static HSSFCellStyle cellStyle = null; 
}
