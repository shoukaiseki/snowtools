package com.maximo.app.config;

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
import com.maximo.tools.impxml.task.Task;
import com.shoukaiseki.tuuyou.logger.PrintLogs;

/**
 * 讀取配置文件 com.shoukaiseki.sql.ReadDatabaseConfigure
 *com.maximo.app.config.ReadDruidDataSourceKonnfigu
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-2
 * <P>ブログ http://shoukaiseki.blog.163.com/
 * <P>E-メール jiang28555@Gmail.com
 */
public class ReadDruidDataSourceKonnfigu  {

	private static Map<String, DruidDataSourceKonnfigu> dbConfigure =null;
	/**
	 * 正式庫鏈接名稱
	 */
	static String seisikiName=null;
	/**
	 * 正式庫鏈接池存放庫,屬於正式庫連接池的都壓入到該庫中,所以為靜態變量
	 */
	static HashMap<DruidDataSource,String> seisikiMap=new HashMap<DruidDataSource,String>(); 
	private static String filename = PrintLogs.USER_CONFIG + "/DruidDataSource-configure.xml";
	OutMessage om;

	public ReadDruidDataSourceKonnfigu(OutMessage om) {
		this.om=om;
		// TODO Auto-generated constructor stub
		try {
			if(dbConfigure==null){
				 dbConfigure = new HashMap<String, DruidDataSourceKonnfigu>();
				readDatabaseConfigureXML();
			}
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			om.error(this.getClass() + " 無法打開文件" + filename);
			om.error(PrintLogs.getTrace(e));
		}
	}

	/**
	 * @return 配置文件集 String 爲配置名 DruidDataSourceKonnfigu 對象存放着
	 *         url,user,password,schemaowner 等數據
	 */
	public Map<String, DruidDataSourceKonnfigu> getDatabaseConfigs() {
		return dbConfigure;
	}

	public DruidDataSource getDruidDataSource(String name) throws MTException {
		// TODO Auto-generated method stub
//			Properties prop = new Properties();
			DruidDataSourceKonnfigu ddsk = dbConfigure.get(name);
			DruidDataSource dataSource = new DruidDataSource();
			if(ddsk==null){
				throw new MTException(new StringBuffer("在").append(filename).append("配置文件中找不到 name為").append(name).append("的Reference節點").toString());
			}
			dataSource.setInitialSize(ddsk.getInitialSize());
			dataSource.setMaxActive(ddsk.getMaxActive());
			dataSource.setMinIdle(ddsk.getMinPoolSize());
//			dataSource.setMaxIdle(ddsk.getMaxPoolSize());
			dataSource.setPoolPreparedStatements(true);
			dataSource.setDriverClassName(ddsk.getDriver());
			dataSource.setUrl(ddsk.getUrl());
			dataSource.setPoolPreparedStatements(true);
			dataSource.setUsername(ddsk.getUser());
			dataSource.setPassword(ddsk.getPassword());
			//设置验证查询
			dataSource.setValidationQuery("SELECT 'asus' from dual");
			//从池中取得链接时做健康检查，该做法十分保守
			dataSource.setTestOnBorrow(true);
			if(seisikiName!=null&&seisikiName.equals(name)){
					seisikiMap.put(dataSource, name);
			}
		return dataSource;
	}

	private void readDatabaseConfigureXML() throws DocumentException {
		// 读取XML文件
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filename);
		// 获取XML根元素
		Element root = doc.getRootElement();
		om.debug("rootElement=" + root.getName());
		seisikiName=root.attributeValue("seisiki");
		om.debug("seisikiName="+seisikiName);
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
			String driver = null;
			int initialSize=10;
			int minPoolSize=15;
			int maxPoolSize=30;
			int maxActive=25;
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
				case "driver":
					driver = stringRefAddr.element("Contents").getText();
					break;
				case "initialSize":
					initialSize = Integer.parseInt(stringRefAddr.element("Contents").getText());
					break;
				case "minPoolSize":
					minPoolSize = Integer.parseInt(stringRefAddr.element("Contents").getText());
					break;
				case "maxPoolSize":
					maxPoolSize = Integer.parseInt(stringRefAddr.element("Contents").getText());
					break;
				case "maxActive":
					maxActive = Integer.parseInt(stringRefAddr.element("Contents").getText());
					break;
				default:
					break;
				}
				if(driver==null)
					driver=DruidDataSourceKonnfigu.DRIVER_ORACLE;
			}
			dbConfigure.put(name, new DruidDataSourceKonnfigu(url,driver,  user, password,initialSize,maxActive,minPoolSize,maxPoolSize));
			om.debug("name=" + name + "\turl=" + url + "\tuser=" + user
					+ "\tpassword=" + password + "\tdriver=" + driver );
		}

	}
	
	public static String getSeisikiName(){
		return seisikiName;
	}
	
	public static boolean isSeisiki(DruidDataSource dds){
		return seisikiMap.get(dds)!=null;
	}

}
