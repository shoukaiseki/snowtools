package com.maximo.app.list;

/** tssls 參數命令的模板
*com.maximo.app.list.ListTemplate
* @author 蒋カイセキ Japan-Tokyo 2013-7-21
* <P>ブログ http://shoukaiseki.blog.163.com/
* <P>E-メール jiang28555@Gmail.com
*/
public abstract class ListTemplate {
	
	/**
	 * 初始化 
	 */
	public abstract void init() ;
	
	/** 列出相關信息
	 * @param args
	 */
	public abstract void list(String[] args);
	
	/** 
	 * @return 命令名稱
	 */
	public abstract String getName();

	/**
	 * 該命令幫助
	 */
	public abstract void help(String[] args);

	/**
	 * @return 類名稱
	 */
	public abstract String getClassName() ;
	
}
