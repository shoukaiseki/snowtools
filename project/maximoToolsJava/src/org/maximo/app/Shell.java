package org.maximo.app;


public interface Shell {
	
	/**命令的参数 
	 * @param strs
	 * @throws MTException 
	 */
	public void execution(String[] strs) throws MTException;
	
	/** 注册的命令名称
	 * 如 ls,dir,mv,rm等
	 * @return 返回命令名稱
	 */
	public String getName();
	
	/** 类的名称
	 * 如果两个不同的类注册同一个命令,通过这个方法找到哪两个类
	 * @return 返回類名稱
	 */
	public String getClassName();

	/** 设置输出显示的组件
	 * @param om
	 */
	public void setOutMessage(OutMessage om);
	
}
