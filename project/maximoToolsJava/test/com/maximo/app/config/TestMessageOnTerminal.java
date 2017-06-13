package com.maximo.app.config;

import java.sql.SQLException;
import java.util.Map;

import com.alibaba.druid.pool.DruidDataSource;
import com.maximo.app.MTException;
import com.maximo.app.MessageOnTerminal;
import com.maximo.app.OutMessage;
import com.maximo.app.resources.MXServerConfig;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class TestMessageOnTerminal {
	public static void main(String[] args) {
			OutMessage om=new MessageOnTerminal();
		ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(om);
		try {
			String refName="localhost_gbkorcl" ;
//			refName="crpxz_101_7001" ;
			om.debug("TestMessageOnTerminal.start");
			DruidDataSource druid = rddsk.getDruidDataSource(refName);
			om.debug("rddsk.getDruidDataSource");
			OracleSqlDetabese osd = new OracleSqlDetabese(druid.getConnection());
			om.debug("osd.init");
			osd.testReadData(1);
			osd.close();
			om.setMaximoToolsLoggerConnection(druid.getConnection());
			om.addMaximoToolsLogger("AJH", "AJHID",1);
			om.addMaximoToolsLogger("AJH", "AJHID",2);
			om.addMaximoToolsLogger("AJH", "AJHID",3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}

	}

}
