package org.maximo.tools.impexcle.task;

import org.maximo.app.MTException;
import org.maximo.tools.impxml.task.IXFormat;

public class TriggerBeanShellSAX extends TaskSAX{
	public static final String ISENABLED="ISENABLED";
	public static final String CLASS="CLASS";
	private boolean bshDataIsEnabled=true;
	
	/**
	 *cdata 作为  beanshell腳本(TRIGGERBEANSHELL標籤定義的)
	 */

	public TriggerBeanShellSAX() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		setBshDataIsEnabled(propertys.get(ISENABLED));
		if(isBshDataIsEnabled()){
			om.info("表'"+table.getName()+"' 的 TRIGGERBEANSHELL 腳本(已啟用).");
		}else{
			om.info("表'"+table.getName()+"' 的 TRIGGERBEANSHELL 腳本(已禁用). ");
		}
	}
	
	/*  TriggerBeanShell 子节点不解析
	 * (non-Javadoc)
	 * @see org.maximo.tools.impxml.task.Task#hasNextKoElement()
	 */
	@Override
	public boolean hasNextKoElement() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isBshDataIsEnabled() {
		return bshDataIsEnabled;
	}

	public void setBshDataIsEnabled(String isEnabled) {
		this.bshDataIsEnabled=IXFormat.isTrueOrNull(isEnabled);
	}
	
}
