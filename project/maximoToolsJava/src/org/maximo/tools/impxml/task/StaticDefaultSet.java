package org.maximo.tools.impxml.task;

import java.util.HashMap;
import java.util.Map;

import org.maximo.app.MTException;

public class StaticDefaultSet extends Task{
	private Map<String,SDSColumn>  defaultSet=new HashMap<String,SDSColumn>();
	public StaticDefaultSet() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.maximo.tools.impxml.task.Task#parseElement()
	 */
	public void parseElement() throws MTException{
		super.parseElement();
		while (hasNextKoElement()) {
			Task koTask = parseNextKoElement();
			if(koTask instanceof SDSColumn){
				SDSColumn column=(SDSColumn) koTask;
				defaultSet.put(column.getName(), column);
				om.info("表'"+table.getName()+"' 字段 '" +column.getName()+
						"'的  STATICDEFAULTSET 配置:"+column.getCdata());
			}
		}
	}
	
	public Map<String,SDSColumn> getSDSMap(){
		return defaultSet;
	}
}
