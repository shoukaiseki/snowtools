package com.shoukaiseki.jeval.function.math;

import com.shoukaiseki.jeval.function.SFunction;

/**獲取owner表的字段的內容
 *com.shoukaiseki.jeval.function.math.Owner
 * @author 蒋カイセキ    Japan-Tokyo 2012-9-3
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class Owner implements SFunction{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "owner";
	}

	public String execute(String arg){
		System.out.println("execute="+arg);
		return null;
	}
}
