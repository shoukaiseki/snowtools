package com.maximo.tools.impxml.bsh;

import com.maximo.app.MTException;
import com.maximo.app.OutMessage;
import com.maximo.tools.impxml.task.*;
import com.maximo.tools.impxml.*;
import java.sql.*;

/** TriggerBeanShell 腳本類模板
 * com.maximo.tools.impxml.bsh.TriggerBSH
 * @author 蒋カイセキ    Japan-Tokyo  2013-6-30
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class TriggerBSH {
	protected Row ixrow = null;
	protected Table ixt=null;
	protected OutMessage om;
	protected DefaultSet tableDefaultSet = null;

	public TriggerBSH() {
		// TODO Auto-generated constructor stub
	}

	/**  如果為 false 則該行不會添加進數據庫.
	 * @return 默認為 true;
	 * @throws MTException
	 */
	public Object execute() throws MTException {
		return true;
	}

	public void setRow(Row row) {
		this.ixrow = row;
	}

	public void setOutMessage(OutMessage om) {
		this.om = om;
	}

	public void setDefaultSet(DefaultSet ds) {
		this.tableDefaultSet = ds;
	}
	
	public void setTable(Table ixt){
		this.ixt=ixt;
	}

}
