package org.tomaximo.puroguramu.interfaces;


import java.sql.Connection;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.shoukaiseki.gui.owern.swing.risuna.ApurikeshonnShare;
import org.shoukaiseki.sql.ConnectionKonnfigu;
/**
 * 数据库连接配置
 * @author 蒋カイセキ
 */
public interface DetabesuKonnfiguInterFace {
	/**
	 * 
	 * @return 数据库配置
	 */
	public Map<String, ConnectionKonnfigu> getConnectionKonnfigu();
	/**
	 * 获取ConnectionKonnfigu对应的配置名称
	 * @param ck
	 * @return
	 */
	public String getKonnfiguMeishou(ConnectionKonnfigu ck);
	/**
	 * 获取后的ConnectionKonnfigu,用于不同的插件使用同一 Connection 对象当前持有的所有数据库锁
	 * @param konnfigumeishou
	 * @return
	 */
	public ConnectionKonnfigu getConnectionKonnfigu(String konnfigumeishou);
	/**
	 * 提供配置名称,返回Connection
	 */
	public Connection getConnection(String konnfigumeishou);
	
	/**
	 * 获取连接后的Connection,用于不同的插件使用同一 Connection 对象当前持有的所有数据库锁
	 * @param ck	
	 * @return
	 */
	public Connection getConnection(ConnectionKonnfigu ck);

	/**
	 * 增加到数据库连接应用
	 * @param app
	 */
	public void addAppDatabesu(DefaultMutableTreeNode[] app);
	
	/**
	 * 返回选中数据库连接项
	 * @return
	 */
	public ConnectionKonnfigu getSelectDatabesu();
}





















