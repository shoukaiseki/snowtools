package org.maximo.tools.impxml.task;

import org.maximo.app.MTException;
import org.maximo.tools.impxml.bsh.TriggerBSH;
import org.maximo.tuuyou.ClassLoaderForWeb;

public class TriggerBeanShell extends Task{
	public static final String ISENABLED="ISENABLED";
	public static final String CLASS="CLASS";
	private boolean bshDataIsEnabled=true;
	
	/**
	 *cdata 作为  beanshell腳本(TRIGGERBEANSHELL標籤定義的)
	 */

	public TriggerBeanShell() {
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
//		if(isEnabled!=null&&(isEnabled.equalsIgnoreCase("NO")||isEnabled.equalsIgnoreCase("OFF")||isEnabled.equalsIgnoreCase("FALSE"))){
//			this.bshDataIsEnabled = false;
//		}else{
//			this.bshDataIsEnabled=true;
//		}
	}
	
	public TriggerBSH createClass(){
		TriggerBSH tbsh=null;
		if(!IXFormat.isNullOrTrimEmpty(getProperty(CLASS))){
			try {
				Class<?> clss = ClassLoaderForWeb.loadClass(getProperty(CLASS));
				tbsh = (TriggerBSH) clss.newInstance();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				om.debug("TRIGGERBEANSHELL的綁定類"+getProperty(CLASS)+"無法初始化,或不繼承 TriggerBSH:"+e);
			}
				
			
		}
		return tbsh;
	}
}
