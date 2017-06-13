package com.maximo.app.resources;

public class MXServerConfig {
	/**
	 * 接口MXServer登录地址
	 * 如 localhost:13400/MXServer
	 */
	private String server;
	/**
	 * maximo登录帐号
	 * 如 maxadmin
	 */
	private String username ;
	/**
	 * maximo登录密码
	 * 如 123456
	 */
	private String password ;
	
	/**
	 * MXServer所對應的 DruidDataSource-configure.xml 中的數據庫鏈接名,用於記錄日誌
	 * 如 crpxz_zsk_maxdb
	 */
	private String ddsname;
	public MXServerConfig() {
		// TODO Auto-generated constructor stub
		server=null;
		username=null;
		password=null;
	}
	
	public MXServerConfig(String server,String username,String password){
		this.server=server;
		this.username=username;
		this.password=password;
	}
	
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public String getDdsname() {
		return ddsname;
	}

	public void setDdsname(String ddsname) {
		this.ddsname = ddsname;
	}

}
