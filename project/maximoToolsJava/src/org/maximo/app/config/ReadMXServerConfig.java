package org.maximo.app.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.*;
import org.dom4j.io.SAXReader;

import psdi.util.MXSession;

import org.maximo.app.MTException;
import org.maximo.app.OutMessage;
import org.maximo.app.resources.DruidDataSourceKonnfigu;
import org.maximo.app.resources.MXServerConfig;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

public class ReadMXServerConfig {

	private static Map<String, MXServerConfig> mxsConfigure =null;
	/**
	 * 正式庫鏈接名稱
	 */
	static String seisikiName=null;
	/**
	 * 正式庫鏈接池存放庫,屬於正式庫連接池的都壓入到該庫中,所以為靜態變量
	 */
	static HashMap<MXSession,String> seisikiMap=new HashMap<MXSession,String>(); 
	private String filename = PrintLogs.USER_CONFIG + "/MXServer-configure.xml";
	OutMessage om;

	public ReadMXServerConfig(OutMessage om) {
		this.om=om;
		// TODO Auto-generated constructor stub
		try {
			if(mxsConfigure==null){
				 mxsConfigure = new HashMap<String, MXServerConfig>();
				readMXSessionConfigureXML();
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
	public Map<String, MXServerConfig> getMXServerConfig() {
		return mxsConfigure;
	}

	public MXSession getMXSession(String name) throws MTException {
		// TODO Auto-generated method stub
		Properties prop = new Properties();
		MXSession mxSession;
		mxSession = MXSession.getNewSession();
		MXServerConfig mxsc = mxsConfigure.get(name);
		System.out.println(mxsc.getServer());
		mxSession.setHost(mxsc.getServer());
		mxSession.setUserName(mxsc.getUsername());
		mxSession.setPassword(mxsc.getPassword());
		try {
			mxSession.connect();
			if(seisikiName!=null&&seisikiName.equals(name)){
					seisikiMap.put(mxSession, name);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
		return mxSession;
	}

	private void readMXSessionConfigureXML() throws DocumentException {
		// 读取XML文件
		SAXReader reader = new SAXReader();
		Document doc = reader.read(filename);
		// 获取XML根元素
		Element root = doc.getRootElement();
		om.println("rootElement=" + root.getName());
		seisikiName=root.attributeValue("seisiki");
		om.println("seisikiName="+seisikiName);
		List elements = root.elements("Reference");
		Iterator it = elements.iterator();
		while (it.hasNext()) {
			Element reference = (Element) it.next();
			String name = reference.attributeValue("name");
			Element refAddresses = reference.element("RefAddresses");
			List refAddressesList = refAddresses.elements("StringRefAddr");
			Iterator refAddressesIterator = refAddressesList.iterator();
			String server = null;
			String user = null;
			String password = null;
			String driver = null;
			String ddsname=null;
			while (refAddressesIterator.hasNext()) {
				Element stringRefAddr = (Element) refAddressesIterator.next();
				String addrType = stringRefAddr.attributeValue("addrType");
				switch (addrType) {
				case "server":
					server = stringRefAddr.element("Contents").getText();
					break;
				case "user":
					user = stringRefAddr.element("Contents").getText();
					break;
				case "password":
					password = stringRefAddr.element("Contents").getText();
					break;
				case "ddsname":
					ddsname = stringRefAddr.element("Contents").getText();
					break;
				default:
					break;
				}
				if(driver==null)
					driver=DruidDataSourceKonnfigu.DRIVER_ORACLE;
			}
			MXServerConfig msc=new MXServerConfig(server,  user, password);
			msc.setDdsname(ddsname);
			mxsConfigure.put(name, msc);
			om.println("name=" + name + "\turl=" + server + "\tuser=" + user
					+ "\tpassword=" + password + "\tdriver=" + driver +"\tddsname="+ddsname);
		}

	}
	
	public static String getSeisikiName(){
		return seisikiName;
	}
	
	public static boolean isSeisiki(MXSession dds){
		return seisikiMap.get(dds)!=null;
	}

}
