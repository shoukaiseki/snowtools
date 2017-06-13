package com.maximo.tools.impxml.task;


import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.dom4j.*;

import bsh.EvalError;
import bsh.Interpreter;

import com.alibaba.druid.pool.DruidDataSource;
import com.maximo.app.MTException;
import com.maximo.app.OutMessage;
import com.maximo.app.config.ReadDruidDataSourceKonnfigu;
import com.maximo.tools.impxml.ImpXmlDefData;
import com.maximo.tools.impxml.bsh.TriggerBSH;
import com.maximo.tools.impxml.config.ImpXmlExcleConfig;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

/** TABLE 標籤,也是root標籤
 *com.maximo.tools.impxml.task.Table
 * @author 蒋カイセキ    Japan-Tokyo 2013-7-2
 * <P>ブログ http://shoukaiseki.blog.163.com/
 * <P>E-メール jiang28555@Gmail.com
 */
public class Table extends Task{
	/**
	 * 是否打印table信息
	 */
	public static final String ISPRINTTABLEINFO="ISPRINTTABLEINFO";
	public static final String TABLE="TABLE";
	public static final String INCLUDEATTS="INCLUDEATTS";
	public static final String EXCLUDEATTS="EXCLUDEATTS";
	public static final String JDBCNAME="JDBCNAME";
	public static final String EXCLE="EXCLE";
	
	/**
	 * Bsh引入的包名
	 */
	public static final String BSHIMPORT=""+
										" import com.maximo.tools.impxml.task.*;" +"\n"+
										" import com.maximo.tools.impxml.*;"+"\n"+
										" import java.sql.Connection;"+"\n"+
										" import java.sql.PreparedStatement;"+"\n"+
										" import java.sql.ResultSet;"+"\n"+
										" import java.sql.SQLException;"+"\n"+
										"";
	
	private Interpreter dsColumnI = new Interpreter();  //DSCOLUMN 使用的 bsh主類 ,因為實例化需要話費10多毫秒
	private Interpreter triggerBeanShellI = new Interpreter();  // TRIGGERBEANSHELL 使用的 bsh主類 ,因為實例化需要話費10多毫秒
	
	OracleSqlDetabese osd=null;
	PreparedStatement ps =null;
	private LinkColumnSet lcs=null;
	
	//要插入的字段
	List<String> incolumns=new ArrayList<String>();
	//要忽略的字段
	List<String> excolumns=new ArrayList<String>();
	
	private DefaultSet defaultSet=null;;
	private StaticDefaultSet staticDefaultSet=null;
	private TriggerBeanShell triggerBeanShell=null;
	private Row owner=null;
	/**
	 * 主要用於 TRIGGERBEANSHELL bsh 調用,用於同級別表對其它表的插入數據,根據id存放 PreparedStatement
	 */
	private Map<String,PreparedStatementSet> pssMap=null;
	
	private Map<String,QuerySet> qsMap=null;
	
	/**
	 * 自定義的變量
	 */
	private Map<String,Variable> vaMap=null;
	
	/**
	 *  主要用於  bsh 調用的序列集,採用
	 */
	private Map<String,SequenceSet> ssMap=null;
	/**
	 * 存放數據行信息
	 */
	private List<Row>  columns=null;
	/**
	 * 表名稱
	 */
	/**
	 * 當前行索引
	 */
	private int index=-1;
	
	ImpXmlDefData ixs=null;
	/****************************************
	 ***excle 用到的變量
	 ****************************************/
	
	/**
	 * EXCLECONFIG 存放的信息
	 */
//	private ImpXmlExcleConfig ixec=new ImpXmlExcleConfig();
	private int includeRowCount=0;
	private int excludeRowCount=0;
	private int rownum=0;
	
	public Table() {
		// TODO Auto-generated constructor stub
		super();
		columns=new ArrayList<Row>();
		pssMap=new HashMap<String,PreparedStatementSet>();
		ssMap=new HashMap<String,SequenceSet>();
		qsMap=new HashMap<String, QuerySet>();
		vaMap=new HashMap<String, Variable>();
		ixs=new ImpXmlDefData(om);
	}
	
	
	public boolean isPrintTableInfo(){
		return IXFormat.isTrueOrNull(propertys.get(ISPRINTTABLEINFO));
	}
	
	public String getName() {
		return propertys.get(TABLE);
	} 
	
	public Row add(Row ixr){
		columns.add(ixr);
		index=columns.indexOf(ixr);
		return ixr;
	}
	
	/** 獲取指定行的索引位置
	 * @param ixr
	 * @return  行所在的List索引
	 */
	public int indexOf(Row ixr){
		return columns.indexOf(ixr);
	}
	
	/** 
	 * @return			獲取當前行
	 */
	public Row getImpXmlRow(){
		return columns.get(index);
	}
	
	/**
	 * @param index		索引位置
	 * @return				獲取指定行
	 */
	public Row getImpXmlRow(int index){
		return columns.get(index);
	}
	
	public int getIndex(){
		return index;
	}
	
	public String getColumnName(String dbName){
		return lcs.getColumnName(dbName);
	}

	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		String rootTable=getName();
		String rootIncludeAtts = getProperty("INCLUDEATTS");
		String rootExcludeAtts = getProperty("EXCLUDEATTS");
		String jdbcname = getProperty("JDBCNAME");
		if(jdbcname!=null){
			om.info("jdbc鏈接名稱為:" +jdbcname);
		}
		if(isPrintTableInfo()){
			om.info("主表名為:" + rootTable);
			om.info("插入的字段有:"+rootIncludeAtts);
			om.info("忽略的字段有:"+rootExcludeAtts);
		}
		ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(om);
		DruidDataSource druid = rddsk.getDruidDataSource(jdbcname);
		try {
			setConnection(druid.getConnection());
			
			con.setAutoCommit(false);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
		boolean initCon=false;
		while (hasNextKoElement()) {
			Task koTask = parseNextKoElement();
			if(koTask instanceof DefaultSet){
				this.defaultSet=((DefaultSet) koTask);
			}else if(koTask instanceof StaticDefaultSet){
				this.staticDefaultSet=((StaticDefaultSet) koTask);
			}else if(koTask instanceof TriggerBeanShell){
				this.triggerBeanShell=(TriggerBeanShell) koTask;
			}else if(koTask instanceof PreparedStatementSet){
				PreparedStatementSet pss=(PreparedStatementSet) koTask;
				pssMap.put(pss.getName(), pss);
			}else if(koTask instanceof SequenceSet){
				SequenceSet ss=(SequenceSet) koTask;
				ssMap.put(ss.getName(), ss);
			}else if(koTask instanceof LinkColumnSet){
				 lcs=(LinkColumnSet) koTask;
			}else if(koTask instanceof QuerySet){
				 QuerySet qs=(QuerySet) koTask;
				 qsMap.put(qs.getName(), qs);
			}else if(koTask instanceof Variable){
				Variable va=(Variable) koTask;
				vaMap.put(va.getName(), va);
			}else if(koTask instanceof Row){
				if(!initCon){
					try {
						formatSQL();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						throw new MTException(e);
					}
					initCon=true;
				}
				Row row=(Row) koTask;
				add(row);
				row.setRownum(++rownum);
				row.setIncludeRownum(includeRowCount+1);
				om.debug("索引:"+row.getIncludeRownum());
				replaceLineMessageInTerminal("正在解析第["+new DecimalFormat("00000000").format(rownum)+"]行數據");
				if(setJdbcRowData(ps, incolumns, defaultSet, row)){
					includeRowCount++;
				}else{
					excludeRowCount++;
				}
			}else if(koTask instanceof ExcleData){
				if(!initCon){
					try {
						formatSQL();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						throw new MTException(e);
					}
					initCon=true;
				}
				ExcleData ed=(ExcleData) koTask;
				while(ed.hasNext()){
					Row row=ed.next();
					if(row!=null){
						add(row);
						row.setRownum(++rownum);
						replaceLineMessageInTerminal("正在解析第["+new DecimalFormat("00000000").format(rownum)+"]行數據");
						row.setIncludeRownum(includeRowCount+1);
						om.debug("索引:"+row.getIncludeRownum());
						if(setJdbcRowData(ps, incolumns, defaultSet, row)){
							includeRowCount++;
						}else{
							excludeRowCount++;
						}
						row.destroyElement();
					}
				}
			}else if(koTask instanceof ExcleDataSAX){
				if(!initCon){
					try {
						formatSQL();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						throw new MTException(e);
					}
					initCon=true;
				}
				ExcleDataSAX ed=(ExcleDataSAX) koTask;
				while(ed.hasNext()){
					Row row=ed.next();
					if(row!=null){
						add(row);
						row.setRownum(++rownum);
						replaceLineMessageInTerminal("正在解析第["+new DecimalFormat("00000000").format(rownum)+"]行數據");
						row.setIncludeRownum(includeRowCount+1);
						om.debug("索引:"+row.getIncludeRownum());
						if(setJdbcRowData(ps, incolumns, defaultSet, row)){
							includeRowCount++;
						}else{
							excludeRowCount++;
						}
						row.destroyElement();
					}
				}
			}else if(koTask instanceof OracleDataBaseData){
				OracleDataBaseData odbd=(OracleDataBaseData) koTask;
				if(!initCon){
					try {
						formatSQL();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						throw new MTException(e);
					}
					initCon=true;
				}
				while(odbd.hasNext()){
					Row row=odbd.next();
					if(row!=null){
						add(row);
						row.setRownum(++rownum);
						replaceLineMessageInTerminal("正在解析第["+new DecimalFormat("00000000").format(rownum)+"]行數據,共["+odbd.getRowCount()+"]行");
						row.setIncludeRownum(includeRowCount+1);
						om.debug("索引:"+row.getIncludeRownum());
						if(setJdbcRowData(ps, incolumns, defaultSet, row)){
							includeRowCount++;
						}else{
							excludeRowCount++;
						}
						row.destroyElement();
					}
					
				}
			}
		}
		System.out.println();
		try {
			if(ps!=null){
				updateData();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
	}
	
	public void formatSQL() throws MTException, SQLException{
		Element root=element;
		//插入的主表
		String rootTable=getName();
		String rootIncludeAtts = getProperty("INCLUDEATTS");
		String rootExcludeAtts = getProperty("EXCLUDEATTS");
		if(rootIncludeAtts!=null&&!rootIncludeAtts.trim().isEmpty()){
			for(String attName:rootIncludeAtts.trim().toUpperCase().split(",")){
				if(!attName.trim().isEmpty()){
					attName=attName.trim().toUpperCase();
					incolumns.add(attName);
					om.debug("插入的字段名稱:"+attName);
				}
			}
		}
		if(rootExcludeAtts!=null&&!rootExcludeAtts.trim().isEmpty()){
			for(String attName:rootExcludeAtts.trim().toUpperCase().split(",")){
				if(!attName.trim().isEmpty()){
					attName=attName.trim().toUpperCase();
					excolumns.add(attName);
					om.debug("忽略的字段名稱:"+attName);
				}
			}
		}
		if(incolumns.size()==0){
			 Iterator<?> columnsIt = root.element("ROW").elements("COLUMN").iterator();
			while(columnsIt.hasNext()){
				Element column = (Element) columnsIt.next();
				String attName = column.attributeValue("NAME");
				if(attName!=null&&!attName.trim().isEmpty()){
					attName=attName.trim().toUpperCase();
					if(excolumns.indexOf(attName)==-1){
						incolumns.add(attName);
						om.debug("插入的字段名稱:"+attName);
					}
				}else{
					throw new MTException(new StringBuffer("內容為[").append(column.getTextTrim()).append("]的字段名稱為空.").toString());
				}
			}
		}
		if(incolumns.size()==0){
			throw new MTException(new StringBuffer(rootTable).append("表的有效插入字段未定義").toString());
		}
		StringBuffer insertSql =null;
		StringBuffer valuesSql=null;
		Map<String, SDSColumn> rootStaticDefaultSet =getStaticDefaultSet().getSDSMap();
		if(getStaticDefaultSet()!=null){
		}else{
			rootStaticDefaultSet=new HashMap<String, SDSColumn>();
		}
			
			for(String column:rootStaticDefaultSet.keySet()){
				incolumns.remove(column.trim().toUpperCase());
				if(insertSql==null){
					insertSql=new StringBuffer("insert into ").append(rootTable).append("(").append(column);
					valuesSql=new StringBuffer(rootStaticDefaultSet.get(column).getCdata());
				}else{
					insertSql.append(",").append(column);
					valuesSql.append(",").append(rootStaticDefaultSet.get(column).getCdata());
				}

			}
			for(String column:incolumns){
				if(rootStaticDefaultSet.get(column)==null){
					if(insertSql==null){
						insertSql=new StringBuffer("insert into ").append(rootTable).append("(").append(column);
						valuesSql=new StringBuffer("?");
					}else{
						insertSql.append(",").append(column);
						valuesSql.append(",?");
					}
				}
			}
		insertSql.append(") values(").append(valuesSql).append(")");
		om.info("插入sql語句為:"+insertSql);
		osd = new OracleSqlDetabese(con);
		osd.setSql(insertSql.toString());
		ps = osd.prepareStatement();
	}
	
	public void updateData() throws SQLException{
		om.info(getName()+" 表共計行數:"+(includeRowCount+excludeRowCount));
		om.info(getName()+" 表插入行數:"+includeRowCount);
		om.info(getName()+" 表過濾行數:"+excludeRowCount);
		try {
			om.info("提交'"+getName()+"'表的數據");
			int[] updateCounts =ps.executeBatch();
			for(int i=0;i<updateCounts.length;i++){
//					om.debug(rootTable+":row"+(i+1)+",更新狀態:"+updateCounts[i]);
			}
		} catch (BatchUpdateException e) {
			// TODO: handle exception
			int[] updateCounts = e.getUpdateCounts();
			for(int i=0;i<updateCounts.length;i++){
//				om.error(rootTable+":row"+(i+1)+",更新狀態:"+updateCounts[i]);
			}
			om.error("表 "+getName()+"數據插入失敗:",e);
			getConnection().rollback();
		}
		try {
			closePreparedStatements();
		} catch (MTException e) {
			// TODO: handle exception
			om.error(om.getTrace(e));
			getConnection().rollback();
		}
		osd.close();
		getConnection().commit();
		
		
	}
	
		
	/**
	 * @param tablePs						插入table的PS
	 * @param incolumns					插入table的必需插入的列()
	 * @param tableDefaultSet		插入table的默認值集
	 * @param ixrow							要插入的行
	 * @throws MTException			
	 */
	public boolean setJdbcRowData(PreparedStatement tablePs,List<String> incolumns,DefaultSet tableDefaultSet,Row ixrow) throws MTException{
		boolean canAdd=true;
			try {
				//設置默認值,及執行字段bsh腳本
				for (String attName : incolumns) {
					if (tableDefaultSet!=null){
						DSColumn defColumn = tableDefaultSet.getDefColumn(attName);
						Object value=null;
						if(defColumn!=null&&(defColumn.isForce()||ixrow.isNull(attName))){
							String defaultValue = defColumn.getDefaultValue();
							if(defaultValue!=null){
								value=parseDefaultValue(defColumn ,ixrow,defaultValue);
								ixrow.setValue(attName, value);
								om.debug("序列:"+value);
							}
							String bshData = defColumn.getData();
							if(bshData!=null){
								try {
									dsColumnI.set("ixrow", ixrow);
									dsColumnI.set("ixt", table);
									dsColumnI.set("om", om);
									dsColumnI.set("attName", attName);
									dsColumnI.set("defColumn", defColumn);
									dsColumnI.eval(BSHIMPORT );
									dsColumnI.eval(bshData);
								} catch (EvalError e) {
									// TODO Auto-generated catch block
									throw new MTException("表 "+ixrow.getTable().getName()+"."+attName+" 的 (DEFAULTSET.COLUMN)beanshell腳本錯誤[位於第"+ixrow.getRownum()+"行數據]:",e);
								}
							}
						}
					}
				}
				
				//執行 TRIGGERBEANSHELL 標籤的bsh腳本
				if(triggerBeanShell!=null&&triggerBeanShell.getCdata()!=null&&!triggerBeanShell.getCdata().isEmpty()&&triggerBeanShell.isBshDataIsEnabled()){
					Object eval=null;
					try {
						triggerBeanShellI.set("ixrow", ixrow);
						triggerBeanShellI.set("ixt", table);
						triggerBeanShellI.set("om", om);
						triggerBeanShellI.set("defaultSet", tableDefaultSet);
						triggerBeanShellI.eval(BSHIMPORT);
						TriggerBSH tbsh = triggerBeanShell.createClass();
						if(tbsh!=null){
							tbsh.setRow(ixrow);
							tbsh.setOutMessage(om);
							tbsh.setDefaultSet(tableDefaultSet);
							tbsh.setTable(table);
							eval=tbsh.execute();
						}else{
							eval = triggerBeanShellI.eval(triggerBeanShell.getCdata());
						}
						if(eval!=null){
							try {
								if(!(boolean) eval){
									canAdd=false;
									om.debug("表 "+ixrow.getTable().getName()+"第"+ixrow.getRownum()+"行數據取消插入.");
								}
							} catch (Exception e) {
								// TODO: handle exception
								om.warn("(TRIGGERBEANSHELL)beanshell腳本錯誤,無效的返回值,如果指定返回值,則必須返回 boolean 類型");
							}
						}
					} catch (EvalError e) {
						// TODO Auto-generated catch block
						throw new MTException("表 "+ixrow.getTable().getName()+" 的 (TRIGGERBEANSHELL)beanshell腳本錯誤[位於第"+ixrow.getRownum()+"行數據]:",e);
					}
				}
				//如果該行屬於有效數據,則 數據加載到數據庫緩存
				for (String key : ixrow.getColumns().keySet()) {
					om.debug("插入時字段值["+key+"]{"+ixrow.getColumns().get(key).getValue()+"}");
				}
				if(canAdd){
					for (String attName : incolumns) {
						if(ixrow.getColumnType(attName)==Column.TIMESTAMP){
								java.sql.Timestamp t1=new Timestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(ixrow.getString(attName)).getTime());
								tablePs.setTimestamp(incolumns.indexOf(attName)+1,t1);
						}else{
							tablePs.setObject(incolumns.indexOf(attName)+1,ixrow.getString(attName));
						}
//						om.debug("PS設置值["+attName+"]{"+ixrow.getString(attName)+"}");
					}
					tablePs.addBatch();
					tablePs.executeBatch();
//					if(rownum%10000==0){
//						con.commit();
//						om.info("con.commit()");
//					}
				}else{
					ixrow.setInvalid(true);
				}
			} catch (SQLException|ParseException e ) {
				// TODO Auto-generated catch block
				throw new MTException(new StringBuffer(ixrow.getTable().getName()).append("表的第").append(ixrow.getRownum()).append("行插入錯誤:").toString(),e);
			}
			return canAdd;
	}
	
	/**解析默認值
	 * @param defaultValue
	 */
	private Object parseDefaultValue(DSColumn defColumn,Row ixrow,String defaultValue) {
		// TODO Auto-generated method stub
		Object object=defaultValue;
		if("${SEQUENCE}".equalsIgnoreCase(defaultValue)){
			om.debug("匹配到變量 SEQUENCE");
			int indexOf = ixrow.getTable().indexOf(ixrow);
			boolean next=true;
			if(indexOf>0){
				if(ixrow.getTable().getImpXmlRow(indexOf-1).isInvalid()){
					next=false;
				}
			}	
			if(next){
				object=ixs.getSequenceNext(defColumn.getSequence());
			}else{
				object=ixs.getSequenceVal(defColumn.getSequence());
			}

		}else if("${DATETIME}".equalsIgnoreCase(defaultValue)){
			om.debug("匹配到變量DATETIME");
			object=ixs.getDBTimestamp();
		}else{
			om.debug("未匹配到變量");
		}
		return object;
	}
	

	/** 創建一個插入數據庫預處理語句對象 PreparedStatement,該對象將會根據 id 被存放在該 ImpXmlTable 中,
	 * 如果id對應的 PS 已存在則直接取出這個 PS,所以如果要插入兩張表,這兩個id不能重複
	  *  等這個 ImpXmlTable 數據全部處理完後會 會執行 PreparedStatement 的 executeBatch() 方法並 commit 
	 * @param name
	 * @return		對應名稱的PreparedStatementSet對象
	 * @throws MTException 
	 */
	public PreparedStatementSet getPreparedStatement(String name) throws MTException{
		PreparedStatementSet pss = pssMap.get(name);
		if(pss==null){
			om.error("NAME為["+name+"]的 PREPAREDSTATEMENTSET 任務標籤不存在");
		}
		return pss;
	}
	
	/** 獲取 QuerySet 對象
	 * @param 	name
	 * @return	對應名稱的 QuerySet 對象
	 */
	public QuerySet getQuerySet(String name){
		if(qsMap.get(name)==null){
			om.error("NAME為["+name+"]的 QUERYSET 任務標籤不存在");
		}
		return qsMap.get(name);
	}
	
	/** 獲取数据库自動序列對象處理類 SequenceSet
	 * @param name	SEQUENCESET 的 name
	 * @return			對應名稱的序列 SequenceSet 管理對象
	 */
	public SequenceSet getSequenceSet(String name){
		SequenceSet ss = ssMap.get(IXFormat.trimUpperCase(name));
		if(ss==null){
			om.error("NAME為["+name+"]的 SEQUENCESET 任務標籤不存在");
		}
		return ss;
	}
	

	public void setConnection(Connection con) {
		super.setConnection(con);
		ixs.setConnection(con);
	}

	public void closePreparedStatements() throws MTException {
		// TODO Auto-generated method stub
		for (String id : pssMap.keySet()) {
			PreparedStatementSet pss = pssMap.get(id);
			OracleSqlDetabese osd=pss.getOracleSqlDetabese();
			osd.close();
		}
		
		for(String id:qsMap.keySet()){
			QuerySet qs = qsMap.get(id);
			qs.getOracleSqlDetabese().close();
		}
		
	}
	public OutMessage getOutMessage(){
		return om;
	}

	public void setOutMessage(OutMessage om) {
		// TODO Auto-generated method stub
		this.om=om;
	}
	
	public Row getOwner() {
		return owner;
	}




	public void setOwner(Row owner) {
		this.owner = owner;
	}
	
	public DefaultSet getDefaultSet() {
		if(defaultSet==null){
			defaultSet=new DefaultSet();
			defaultSet.setConnection(con);
			defaultSet.setTable(table);
			defaultSet.setOutMessage(om);
		}
		return defaultSet;
	}
	
	public StaticDefaultSet getStaticDefaultSet() {
		if(staticDefaultSet==null){
			staticDefaultSet=new StaticDefaultSet();
			staticDefaultSet.setConnection(con);
			staticDefaultSet.setTable(this);
			staticDefaultSet.setOutMessage(om);
		}
		return staticDefaultSet;
	}
	public LinkColumnSet getLinkColumnSet(){
		if(lcs==null){
			lcs=new LinkColumnSet();
			lcs.setConnection(con);
			lcs.setTable(this);
			lcs.setOutMessage(om);
		}
		return lcs;
	}
	
	/** 獲取自定義變量的 Variable 對象,區分大小寫
	 * @param name			變量名稱
	 * @return					對應名稱的 變量管理類
	 */
	public Variable getVariable(String name){
		if(vaMap.get(name)==null){
			om.error("NAME為["+name+"]的 VARIABLE 任務標籤不存在");
		}
		return vaMap.get(name);
	}
	
	/**獲取自定義變量的 Variable 對象的值,區分大小寫
	 * @param name			變量名稱
	 * @return					對應名稱變量值,相當與{@link #getVariable(String)}.{@link Variable#getValue()}
	 */
	public Object getVariableValue(String name){
		if(vaMap.get(name)==null){
			om.error("NAME為["+name+"]的 VARIABLE 任務標籤不存在");
		}
		return vaMap.get(name).getValue();
	}
	
	/**設置自定義變量的 Variable 對象的值,name區分大小寫
	  * 注意設置值時和取出時候類型要一致 
	 * @param name 			變量名稱
	 * @param value			要設置的值,相當與{@link #getVariable(String)}.{@link Variable#setValue(Object)}
	 */
	public void setVariableValue(String name,Object value){
		if(vaMap.get(name)==null){
			om.error("NAME為["+name+"]的 VARIABLE 任務標籤不存在");
		}
		vaMap.get(name).setValue(value);
	}
	
	/** 替換該行信息,支線linux終端
	 * @param s
	 */
	public void replaceLineMessageInTerminal(String s){
		tcc.moveToFirstLine();
		tcc.clearToEndOfLine();
		tcc.print(s);
	}
	
}
