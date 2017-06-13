package com.maximo.tools.impxml.task;

import java.util.HashMap;
import java.util.Map;

import com.maximo.app.MTException;
import com.maximo.tuuyou.ClassLoaderForWeb;

public class TaskMap {
	private ClassLoaderForWeb clf=null;
	private static TaskMap taskMap=null;
	/**
	 * Map<标签名称,执行该标签任务的类名称>
	 */
	private Map<String,String> tasks=new HashMap<>();
	
	private TaskMap(){
		clf=new ClassLoaderForWeb();
	}
	
	private void init(){
		tasks.put("DEFAULTSET", DefaultSet.class.getName());
		tasks.put("DSCOLUMN", DSColumn.class.getName());
		tasks.put("ROW", Row.class.getName());
		tasks.put("COLUMN", Column.class.getName());
		tasks.put("STATICDEFAULTSET", StaticDefaultSet.class.getName());
		tasks.put("SDSCOLUMN", SDSColumn.class.getName());
		tasks.put("TRIGGERBEANSHELL", TriggerBeanShell.class.getName());
		tasks.put("PREPAREDSTATEMENTSET", PreparedStatementSet.class.getName());
		tasks.put("LINKCOLUMNSET", LinkColumnSet.class.getName());
		tasks.put("LNCOLUMN", LnColumn.class.getName());
		tasks.put("SEQUENCESET", SequenceSet.class.getName());
		tasks.put("EXCLEDATA", ExcleData.class.getName());
		tasks.put("EXCLEDATASAX", ExcleDataSAX.class.getName());
		tasks.put("EDCOLUMN", EDColumn.class.getName());
		tasks.put("QUERYSET", QuerySet.class.getName());
		tasks.put("VARIABLE", Variable.class.getName());
		tasks.put("ORACLEDATABASEDATA", OracleDataBaseData.class.getName());
		tasks.put("ODDSELECTSQL", ODDSelectSql.class.getName());
		tasks.put("ODDCOLUMN", ODDColumn.class.getName());
		tasks.put("INITDATABASESEQUENCE", InitDatabaseSequence.class.getName());
	}

	public static TaskMap getTaskMap() {
		if(taskMap==null){
			taskMap=new TaskMap();
			taskMap.init();
		}
		return taskMap;
	}
	
	/**创建一个新的 Task对象,在{@link Task#parseNextKoElement()}調用
	 * @param taskName	任务名称
	 * @return	返回一個新建的Task對象
	 * @throws MTException 
	 */
	public Task createNewTask(String taskName) throws MTException{
		Task task=null;
		String className = tasks.get(taskName);
		if(className!=null){
			try {
				Class<?> clss = clf.loadClass(className);
				task=(Task) clss.newInstance();
				task.setTaskName(taskName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new MTException(e);
			}
		}else{
			throw new MTException("任务标签["+taskName+"]未绑定解析类,无法初始化该类.");
		}
		return task;
	}


}
