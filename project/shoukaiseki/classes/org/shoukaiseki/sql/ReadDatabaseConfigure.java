package org.shoukaiseki.sql;

import java.io.*;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import org.shoukaiseki.file.SousaFairu;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

/**
 * 讀取配置文件 org.shoukaiseki.sql.ReadDatabaseConfigure
 * 
 * @author 蒋カイセキ Japan-Tokyo 2012-6-29
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class ReadDatabaseConfigure extends PrintLogs {

	private Map<String, ConnectionKonnfigu> dbConfigure = new HashMap<String, ConnectionKonnfigu>();
//	private String filename = USER_CONFIG + "/database-configure.xml";
	private String filename = USER_CONFIG + "/DruidDataSource-configure.xml";

	public ReadDatabaseConfigure() {
		level = 7;
		// TODO Auto-generated constructor stub
		try {
			readDatabaseConfigureXML();
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			error(this.getClass() + " 無法打開文件" + filename);
			error("如果無此文件請用 java " + ReadDatabaseConfigure.class
					+ "命令生成默認配置文件!");
			error(getTrace(e));
		}
	}

	/**
	 * @return 配置文件集 String 爲配置名 ConnectionKonnfigu 對象存放着
	 *         url,user,password,schemaowner 等數據
	 */
	public Map<String, ConnectionKonnfigu> getDatabaseConfigs() {
		return dbConfigure;
	}

	public OracleSqlDetabese getOracleSqlDatabese(String name,String program){
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		OracleSqlDetabese osd=null;
		try{
			ConnectionKonnfigu connectionKonnfigu = dbConfigure.get(name);
			String user = connectionKonnfigu.getUser();
			String password = connectionKonnfigu.getPassword();
			String url = connectionKonnfigu.getUrl();
			String driver = connectionKonnfigu.getDriver();
			String sessionName = connectionKonnfigu.getSchemaowner();
			osd=new OracleSqlDetabese(url,user,password,driver,program);

			String sql="	alter session set current_schema = "+sessionName;

			if(!osd.update(sql)){
				logger.error("mxe.db.schemaowner="+sessionName+" Error無法切換到該用戶");
				logger.error(sql);
			}else{
				logger.debug("session 已經成功切換至"+sessionName);
			}
			sql="ALTER SESSION SET NLS_DATE_FORMAT ='yyyy-dd-mm hh24:mi:ss'";
			osd.update(sql);
			sql="ALTER SESSION SET NLS_TIMESTAMP_FORMAT ='yyyy-dd-mm hh24:mi:ss'";
			osd.update(sql);

		} catch (IOException ex) { 
			logger.error("OracleSqlDetabese",ex);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("加載 SQL 驅動失敗:",e);
		}

		return osd;

	}
	public OracleSqlDetabese getOracleSqlDatabese(String name) {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		OracleSqlDetabese osd=null;
		try {
			ConnectionKonnfigu connectionKonnfigu = dbConfigure.get(name);
			String user = connectionKonnfigu.getUser();
			String password = connectionKonnfigu.getPassword();
			String url = connectionKonnfigu.getUrl();
			String driver = connectionKonnfigu.getDriver();
			String sessionName = connectionKonnfigu.getSchemaowner();
			osd=new OracleSqlDetabese(url,user,password,driver,"JapanAV");

			String sql="	alter session set current_schema = "+sessionName;

			if(!osd.update(sql)){
				logger.error("mxe.db.schemaowner="+sessionName+" Error無法切換到該用戶");
				logger.error(sql);
			}else{
				logger.debug("session 已經成功切換至"+sessionName);
			}
			sql="ALTER SESSION SET NLS_DATE_FORMAT ='yyyy-dd-mm hh24:mi:ss'";
			osd.update(sql);
			sql="ALTER SESSION SET NLS_TIMESTAMP_FORMAT ='yyyy-dd-mm hh24:mi:ss'";
			osd.update(sql);


		} catch (IOException ex) { 
			logger.error("OracleSqlDetabese",ex);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("加載 SQL 驅動失敗:",e);
		}
		return osd;

	}

	public void readDatabaseConfigureXML() throws DocumentException {
		// 读取XML文件
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filename);
		// 获取XML根元素
		Element root = doc.getRootElement();
		debug("rootElement=" + root.getName(), 7);
		List elements = root.elements("Reference");
		Iterator it = elements.iterator();
		while (it.hasNext()) {
			Element reference = (Element) it.next();
			String name = reference.attributeValue("name");
			Element refAddresses = reference.element("RefAddresses");
			List refAddressesList = refAddresses.elements("StringRefAddr");
			Iterator refAddressesIterator = refAddressesList.iterator();
			String url = null;
			String user = null;
			String password = null;
			String schemaowner = null;
			String driver = null;
			while (refAddressesIterator.hasNext()) {
				Element stringRefAddr = (Element) refAddressesIterator.next();
				String addrType = stringRefAddr.attributeValue("addrType");
				switch (addrType) {
				case "customUrl":
					url = stringRefAddr.element("Contents").getText();
					break;
				case "user":
					user = stringRefAddr.element("Contents").getText();
					break;
				case "password":
					password = stringRefAddr.element("Contents").getText();
					break;
				case "schemaowner":
					schemaowner = stringRefAddr.element("Contents").getText();
					break;
				case "driver":
					driver = stringRefAddr.element("Contents").getText();
					break;
				default:
					break;
				}
				if (schemaowner == null)
					schemaowner = user;
				if(driver==null)
					driver=ConnectionKonnfigu.DRIVER_ORACLE;
			}
			dbConfigure.put(name, new ConnectionKonnfigu(url,driver,  user, password,
					schemaowner));
			debug("name=" + name + "\turl=" + url + "\tuser=" + user
					+ "\tpassword=" + password + "\tdriver=" + driver
					+ "\tschemaowner=" + schemaowner, 4);
		}

	}

	public void mkDatabaseConfigureXML() {
		debug("mkDatabaseConfigureXML filename=" + filename);
		error("mkDatabaseConfigureXML filename=" + filename);
		String encoding = "UTF-8";
		// encoding="GBK";
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(encoding);
		Element references = document.addElement("References");
		Element reference = references.addElement("Reference");
		reference.addAttribute("name", "localhost_orcl");
		Element refAddresses = reference.addElement("RefAddresses");
		addStringRefAddr(refAddresses, "customUrl",
				"jdbc:oracle:thin:@localhost:1521:orcl");
		addStringRefAddr(refAddresses, "user", "maximo");
		addStringRefAddr(refAddresses, "password", "maximo");
		addStringRefAddr(refAddresses, "schemaowner", "maximo");
		/** 将document中的内容写入文件中 */
		SousaFairu sousaFile = new SousaFairu();
		sousaFile.mkdir(USER_CONFIG);
		XMLWriter xmlwriter = null;
		try {
			// 字符串
			// StringWriter writer = new StringWriter();
			// 文件輸出
			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(new File(filename)), encoding);
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding(encoding);
			// xmlwriter = new XMLWriter(new FileWriter(new File(filename)));
			xmlwriter = new XMLWriter(writer, format);
			xmlwriter.write(document);
			xmlwriter.flush();
			debug(formatXml(document.asXML()), 7);
			println("成功創建配置文件" + filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			error(getTrace(e));
			println("創建配置文件" + filename + "失敗!");
			println("請檢查是否有寫入權限!");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (xmlwriter != null) {
				try {
					xmlwriter.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void addStringRefAddr(Element refAddresses, String addrType,
			String Contents) {
		Element stringRefAddr = refAddresses.addElement("StringRefAddr");
		stringRefAddr.addAttribute("addrType", addrType);
		Element elementContents = stringRefAddr.addElement("Contents");
		elementContents.addText(Contents);
	}

	/**
	 * 格式化XML字符串
	 * 
	 * @param xml
	 * @return
	 * @throws DocumentException
	 * @throws IOException
	 */
	private String formatXml(String xml) throws DocumentException, IOException {

		SAXReader saxReader = new SAXReader();

		Document document = saxReader.read(new ByteArrayInputStream(xml
				.getBytes()));
		// 创建输出格式
		OutputFormat format = OutputFormat.createPrettyPrint();
		// 制定输出xml的编码类型
		format.setEncoding("UTF-8");

		StringWriter writer = new StringWriter();
		// 创建一个文件输出流
		XMLWriter xmlwriter = new XMLWriter(writer, format);
		// 将格式化后的xml串写入到文件
		xmlwriter.write(document);
		String returnValue = writer.toString();
		writer.close();

		// 返回编译后的字符串格式
		return returnValue;
	}

	public static void main(String args[]) {
		ReadDatabaseConfigure rdc = new ReadDatabaseConfigure();
		if (args.length == 1) {
			if (args[0].equals("--mkDatabaseConfigureXML")) {
				rdc.mkDatabaseConfigureXML();
				System.exit(0);
			}
		}
		rdc.println("語法錯誤請加參數 --mkDatabaseConfigureXML 進行生成配置文件");
	}

}
