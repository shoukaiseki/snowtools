package org.maximo.app.config;

import java.util.Map;

import org.maximo.app.MessageOnTerminal;
import org.maximo.app.resources.MXServerConfig;

import junit.framework.Test;
import junit.framework.TestSuite;

public class TestReadReadMXServerConfigTestSuite  extends TestSuite {

	public static Test suite() {
		TestSuite suite = new TestSuite( "TestSuite TestReadReadMXServerConfigTestSuite");
		System.out.println("測試 TestReadReadMXServerConfigTestSuite.suite()");
		ReadMXServerConfig rmxsc=new ReadMXServerConfig(new MessageOnTerminal());
		Map<String, MXServerConfig> dc = rmxsc.getMXServerConfig();
		System.out.println("MXServerConfig="+dc);
		return suite;

	}
}
