package com.maximo.tools.impexcle.task;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.dom4j.*;
import org.dom4j.tree.DefaultAttribute;

import com.maximo.app.MTException;
import com.maximo.app.OutMessage;
import com.maximo.tools.impxml.task.IXFormat;
import com.shoukaiseki.syso.TerminalCursorControl;

public class TaskSAX{
	 protected TerminalCursorControl tcc=null;
	 protected Connection con=null;
	 protected String taskName=null;
	 protected TaskMapSAX taskMap=null;
	 protected TableSAX rootTable=null;
	 protected OutMessage om=null;
	 protected Element element=null;
	 /**
	 * 子任务的
	 */
	protected List<Element> kos=null;
	/**
	 * 子任务的 Iterator
	 */
	protected Iterator<Element> kosIt=null;
	 TableSAX table=null;
	 /**
	 * 该标签的 CDATA 
	 */
	protected String cdata=null;
	 /**
	 * 该标签所有属性
	 */
	protected Map<String,String> propertys=new HashMap<String,String>();
	public TaskSAX(){
		taskMap=TaskMapSAX.getTaskMap();
		tcc=new TerminalCursorControl();
	}
	
	public void init(){
		
	}
	
	public TableSAX getRootTable() {
		return rootTable;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}
	
	/**
	 * 銷毀
	 */
	public void destroyElement(){
		this.element=null;
	}

	public OutMessage getOutMessage() {
		return om;
	}

	public void setOutMessage(OutMessage om) {
		this.om = om;
	}
	
	public void setTable(TableSAX table){
		this.table=table;
	}

	public TableSAX getTable() {
		return table;
	}

	/** 解析 xml 的 该 Element 的属性 ,一个 任务 只能解析一次
	 * @throws MTException
	 */
	public void parseElement() throws MTException{
		Iterator<DefaultAttribute> attsIt = element.attributeIterator();
		while(attsIt.hasNext()){
			DefaultAttribute da = attsIt.next();
			String proName = IXFormat.trimUpperCase(da.getName());
			if(IXFormat.isIn(proName, getFormatTrimUpperCaseProperty())){
				propertys.put(proName, IXFormat.trimUpperCase(da.getValue()));
			}else{
				propertys.put(proName, da.getValue());
			}
		}
		if(toBeFormatTrimCDATA()){
			cdata=element.getText();
		}else{
			cdata=element.getText().trim();
		}
		kos = element.elements();
		kosIt = kos.iterator();
	}
	
	/** 需要值进行 清首尾空并转换为大写 方式格式化的属性名称
	  * 在{@link #parseElement()}方法中調用
	  *  如果有要清的字段,就在繼承該類時覆寫該方法
	 * @return	需要清首位空的字符數組,不可返回為null,可以為 new String[]{}
	 */
	public String[] getFormatTrimUpperCaseProperty(){
		return new String[]{};
	}
	
	/**是否将 CDATA 进行清首尾空 ,默认清 
	  * 在{@link #parseElement()}方法中調用
	  *  如果不想清,就在繼承該類時覆寫該方法
	 * @return	是否清首位空
	  */
	public boolean toBeFormatTrimCDATA(){
		return true;
	}
	
	/**
	 * 一般用于解析完成后执行,不会自动执行,需要调用
	 */
	public void execute(){
		
	}
	
	public String getCdata() {
		return cdata;
	}
	
	public boolean hasNextKoElement(){
		return kosIt.hasNext();
	}
	
	/** 解析子任务
	 * @return	下一個子任務對象
	 * @throws MTException 
	 */
	public TaskSAX parseNextKoElement() throws MTException{
		Element e = kosIt.next();
		TaskSAX task=null;
		task=taskMap.createNewTask(e.getName());
		task.setOutMessage(om);
		task.setElement(e);
		task.setConnection(con);
		task.setTable(table);
		task.parseElement();
		return task;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getProperty(String name){
		return propertys.get(name);
	}
	
	public Connection getConnection() {
		return con;
	}

	public void setConnection(Connection con) {
		this.con = con;
	}
	
	
}
