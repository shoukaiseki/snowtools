package com.maximo.tools.impxml;

import com.maximo.app.OutMessage;

import psdi.util.MXSession;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import com.alibaba.druid.pool.DruidDataSource;
import com.maximo.app.MTException;
import com.maximo.app.OutMessage;
import com.maximo.app.resources.DruidDataSourceKonnfigu;

public class ImpXmlToMXServer extends ImpXmlTemplate{
	MXSession mxSession;
	
	public ImpXmlToMXServer(MXSession mxSession,OutMessage om) {
		// TODO Auto-generated constructor stub
		super(om);
		this.mxSession=mxSession;
	}
	
	public void execution(String xmlName) throws DocumentException{
		readDatabaseConfigureXML(xmlName);
		
	}
	
	/**
	 * @param xmlName
	 * @throws DocumentException
	 */
	private void readDatabaseConfigureXML(String xmlName) throws DocumentException {
		// 读取XML文件
		SAXReader reader = new SAXReader();
		Document doc = reader.read(xmlName);
		// 获取XML根元素
		Element root = doc.getRootElement();
		//插入的主表
		String rootTable=root.attributeValue("TABLE");
		//maximo的app應用名稱
		String appName=root.attributeValue("APPNAME");
		root.attributeValue("EXCLUDEATTS");
		om.println("rootElement=" + root.getName());
		Iterator<?> rowsIt = root.elements("ROW").iterator();
		while (rowsIt.hasNext()) {
			Element reference = (Element) rowsIt.next();
			Iterator columnsIt = reference.elements("COLUMN").iterator();
			while(columnsIt.hasNext()){
				Element column = (Element)columnsIt.next();
				String attName = column.attributeValue("NAME");
				om.println(attName+":"+column.getText());
			}
			
			
		}

	}

}
