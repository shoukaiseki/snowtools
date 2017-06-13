package org.maximo.tools.impexcle.task;

import java.util.HashMap;
import java.util.Map;

import org.maximo.app.MTException;
import org.maximo.tuuyou.ClassLoaderForWeb;

public class TaskMapSAX {
	private ClassLoaderForWeb clf=null;
	private static TaskMapSAX taskMap=null;
	/**
	 * Map<标签名称,执行该标签任务的类名称>
	 */
	private Map<String,String> tasks=new HashMap<>();
	
	private TaskMapSAX(){
		clf=new ClassLoaderForWeb();
	}
	
	private void init(){
//		tasks.put("DEFAULTSET", DefaultSet.class.getName());
//		tasks.put("DSCOLUMN", DSColumn.class.getName());
//		tasks.put("ROW", Row.class.getName());
//		tasks.put("COLUMN", Column.class.getName());
//		tasks.put("STATICDEFAULTSET", StaticDefaultSet.class.getName());
//		tasks.put("SDSCOLUMN", SDSColumn.class.getName());
		tasks.put("TRIGGERBEANSHELL", TriggerBeanShellSAX.class.getName());
//		tasks.put("PREPAREDSTATEMENTSET", PreparedStatementSet.class.getName());
//		tasks.put("LINKCOLUMNSET", LinkColumnSet.class.getName());
//		tasks.put("LNCOLUMN", LnColumn.class.getName());
//		tasks.put("SEQUENCESET", SequenceSet.class.getName());
		tasks.put("EXCLEDATASAX", ExcleDataSAXSAX.class.getName());
		tasks.put("EDCOLUMN", EDColumnSAX.class.getName());
//		tasks.put("QUERYSET", QuerySet.class.getName());
//		tasks.put("VARIABLE", Variable.class.getName());
	}

	public static TaskMapSAX getTaskMap() {
		if(taskMap==null){
			taskMap=new TaskMapSAX();
			taskMap.init();
		}
		return taskMap;
	}
	
	/**创建一个新的 Task对象,在{@link TaskSAX#parseNextKoElement()}調用
	 * @param taskName	任务名称
	 * @return	返回一個新建的Task對象
	 * @throws MTException 
	 */
	public TaskSAX createNewTask(String taskName) throws MTException{
		TaskSAX task=null;
		String className = tasks.get(taskName);
		if(className!=null){
			try {
				Class<?> clss = clf.loadClass(className);
				task=(TaskSAX) clss.newInstance();
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
