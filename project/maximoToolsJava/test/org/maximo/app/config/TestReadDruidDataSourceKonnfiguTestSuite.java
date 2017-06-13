package org.maximo.app.config;

import java.sql.SQLException;
import java.util.Map;


import org.maximo.app.MTException;
import org.maximo.app.MessageOnTerminal;
import org.maximo.app.resources.MXServerConfig;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestReadDruidDataSourceKonnfiguTestSuite extends TestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"TestSuite TestReadDruidDataSourceKonnfigu");
		System.out.println("測試 TestReadDruidDataSourceKonnfigu.suite()");
		ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(new MessageOnTerminal());
		try {
			String ddsName="localhost_gbkorcl";
//			ddsName="crpxz_101_7001";
			OracleSqlDetabese osd = new OracleSqlDetabese(rddsk.getDruidDataSource(ddsName).getConnection());
			osd.testReadData(1);
			osd.close();
		System.out.println("測試 TestReadReadMXServerConfigTestSuite.suite()");
		ReadMXServerConfig rmxsc=new ReadMXServerConfig(new MessageOnTerminal());
		Map<String, MXServerConfig> dc = rmxsc.getMXServerConfig();
		System.out.println("MXServerConfig="+dc);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		} catch (MTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
		return suite;

	}
}
