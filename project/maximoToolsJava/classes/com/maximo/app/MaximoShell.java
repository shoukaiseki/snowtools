package com.maximo.app;

import java.sql.Connection;

import psdi.util.MXSession;
public interface MaximoShell extends Shell{

	/** 设置命令运行是的连接池
	 * @param mxSession
	 * @param con
	 */
	public void  setMXCon(MXSession mxSession,Connection con);
}
