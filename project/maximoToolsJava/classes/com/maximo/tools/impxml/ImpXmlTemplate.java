package com.maximo.tools.impxml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import bsh.EvalError;
import bsh.Interpreter;

import com.maximo.app.MTException;
import com.maximo.app.OutMessage;
import com.maximo.tools.impxml.task.DSColumn;
import com.maximo.tools.impxml.task.DefaultSet;
import com.maximo.tools.impxml.task.Row;
import com.maximo.tools.impxml.task.Table;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

public class ImpXmlTemplate {
	private Connection con;
	OutMessage om=null;
	Element root =null;
	Document doc=null;
	public ImpXmlTemplate(OutMessage om) {
		// TODO Auto-generated constructor stub
		this.om=om;
	}
	
	public void readXml(String xmlName) throws MTException{
		// 读取XML文件
		SAXReader reader= new SAXReader();
		try {
			doc = reader.read(xmlName);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			throw new MTException("讀取xml文件失敗:",e);
		}
		// 获取XML根元素
		 root = doc.getRootElement();
	}
	
	public void closeFile(){
		doc.clearContent();
	}

	public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}

}
