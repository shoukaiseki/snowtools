package com.maximo.tools.impxml.task;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.*;

import com.maximo.app.MTException;

/**存放 DEFAULTSET 標籤下 COLUMN 標籤信息
 *com.maximo.tools.impxml.ImpXmlDefaultSet
 * @author 蒋カイセキ    Japan-Tokyo 2013-6-26
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class DefaultSet extends Task{
	
	private Map<String,DSColumn>  defaultSet=null;
	
	public DefaultSet() {
		// TODO Auto-generated constructor stub
		defaultSet=new HashMap<String, DSColumn>();
	}

	private void addDefColumn(String attName,DSColumn column) throws MTException{
		if(attName!=null){
			defaultSet.put(attName.trim().toUpperCase(), column);
		}else{
			throw new MTException("COLUMN標籤的NAME為空.");
		}
	}
	
	public DSColumn getDefColumn(String attName){
		return defaultSet.get(attName.toUpperCase());
	}

	
	public void parseElement() throws MTException{
		super.parseElement();
		while (hasNextKoElement()) {
			Task koTask = parseNextKoElement();
			if(koTask instanceof DSColumn){
				DSColumn column=(DSColumn) koTask;
				addDefColumn(column.getName(), column);
				om.info("表'"+table.getName()+"' 字段 '" +column.getName()+ "'已啟用 DEFAULTSET 默認值配置.");
//				om.info("表'"+table.getName()+"' 字段 '" +column.getName()+ "'的 DEFAULTSET 配置:");
//				om.info("bsh腳本:"+column.getData());
			}
		}
	}

}


