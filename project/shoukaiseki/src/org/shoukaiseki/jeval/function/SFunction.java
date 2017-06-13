package org.shoukaiseki.jeval.function;

public interface SFunction {

	/**
	 * @return 函數名
	 */
	public String getName() ;
	
	/**
	 * @param arg函數裏的內容
	 * @return 結果
	 */
	public String execute(String arg);
}
