package org.maximo.tools.impxml.task;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sound.midi.MidiDevice.Info;

import org.dom4j.Element;

import com.alibaba.druid.pool.DruidDataSource;
import org.maximo.app.MTException;
import org.maximo.app.OutMessage;
import org.maximo.app.config.ReadDruidDataSourceKonnfigu;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class Row extends Task{
	
	/**
	 * 数据库字段存放的數據
	 */
	private Map<String,Column>  dbColumns=null;
	
	/**
	 * 行號
	 */
	private long rownum=0L;
	/**
	 * 插入到數據庫的行號
	 */
	private int includeRownum=0;
	
	/**
	 * 作廢標識 該行如果在 (TRIGGERBEANSHELL)beanshell 腳本中過濾,設置成 true , 作為無效
	 */
	private boolean invalid=false;
	private LinkColumnSet lcs=null;
	
	public Row() {
		// TODO Auto-generated constructor stub
		dbColumns=new HashMap<String, Column>();
	}
	public Row(Table table) {
		this();
		this.table=table;
	}
	
	public void setValue(String dbName,Object value) throws MTException{
		if(!IXFormat.isNullOrTrimEmpty(dbName)){
			String dbNameNew=IXFormat.trimUpperCase(dbName);
			Column column = dbColumns.get(dbNameNew);
			if(column==null){
				column=new Column(dbNameNew);
				column.setConnection(con);
				column.setOutMessage(om);
				column.setTable(table);
				dbColumns.put(dbNameNew, column);
			}
			column.setValue(value);
		}else{
			throw new MTException("COLUMN標籤的NAME為空.");
		}
	}
	
	
	
	public int getColumnType(String attName){
		Column column = dbColumns.get(IXFormat.trimUpperCase(attName));
		if(column==null){
			return 0;
		}
		return column.getType();
	}
	
	public String getString(String attName){
		Column column = dbColumns.get(IXFormat.trimUpperCase(attName));
		if(column==null){
			return null;
		}
		return column.getValue();
	}

	public long getRownum() {
		return rownum;
	}

	public void setRownum(long rownum) {
		this.rownum = rownum;
	}

	public Table getTable() {
		return table;
	}

	public boolean isNull(String attName) {
		// TODO Auto-generated method stub
		if(dbColumns.get(attName.toUpperCase())==null){
			return true;
		}else{
			return dbColumns.get(attName.toUpperCase()).isNull();
		}
	}
	

	public boolean isInvalid() {
		return invalid;
	}

	public void setInvalid(boolean invalid) {
		this.invalid = invalid;
	}

	
	/** 獲取合併區域的首行
	  *  一般用於像 excle 層次結構,比如B,C,D,E為一個主表,F,G為子表
	  *  第一行這幾列都會有值,而第2-5行B,C,D,E沒有值,F,G有值,實際上,B,C,D,E的值應該要跟第1行一樣,相當與這些區域是合併的
	  *  該方法即是可以在第2-5行獲取第1行的某字段值
	 * @param attNames						區域字段集,這些字段都為空時,判定為非新區域首行,會向上查找區域首行,
	 * 				比如:主表的某行C列為空,而D,E列都有值得,該行屬於一個新區域,如果單單判定C列會誤認為該行不作為一條新記錄,所以採用多字段為空判定主表(區域首行)
	 * @return	行
	 */
	public Row getMergedRegionValue(String[]  attNames){
		Row ixr=this;
		Table ixTable = ixr.getTable();
		for (int index=ixTable.indexOf(ixr);true;) {
			om.debug(ixr.getRownum()+"索引"+index);
			if(!ixr.isNulls(attNames)){
				om.debug(ixr.getRownum()+"索引"+index);
				return ixr;
			}
			if(index>0){
				ixr=ixTable.getImpXmlRow(--index);
			}else{
				om.debug(ixr.getRownum()+"索引"+index);
				return ixr;
			}
		}
	}
	
	/** 粘貼,將某一行對應字段(attNames)的值粘貼到該行中
	 * @param ixr				
	 * @param attNames			字段數組
	 * @param force					是否強制覆蓋,為 false 時如果該行某字段有值將會保留,為 true 時強制替換
	 * @throws MTException 
	 */
	public void paste(Row ixr,String[] attNames,boolean force) throws MTException{
		for (String key : getColumns().keySet()) {
			if(IXFormat.isIn(key, attNames)){
//				System.out.println("複製字段值["+key+"]{"+getColumns().get(key).getValue()+"}");
//				System.out.println("源字段值["+key+"]{"+ixr.getColumns().get(key).getValue()+"}");
			}
		}
		for (String attName : attNames) {
			if((getString(attName)==null||getString(attName).trim().isEmpty()||force)&&!this.equals(ixr)){
				setValue(attName, ixr.getString(attName));
//				om.info("copy "+attName+" ["+ixr.getString(attName)+"] to ["+getString(attName)+"]");
			}else{
//				om.info("not copy");
			}
		}
	}
	
	/** 獲取之前的未作廢的行
	 * @return	之前的未作廢的行
	 */
	public Row getPreviousAvailableRow(){
		Row ixr=this;
		Table ixTable = ixr.getTable();
		for (int index=ixTable.indexOf(ixr);index>0;) {
			index--;
			ixr=ixTable.getImpXmlRow(index);
			if(!ixr.isInvalid()){
				return  ixr;
			}
		}
		return null;
	}
	
	
	/** 最開始值是否為空
	 * @param attName
	 * @return true:為空或null,false:不為空
	 */
	public boolean isBeginNullAuto(String attName){
		om.debug("isBeginNullAuto="+dbColumns.get(IXFormat.trimUpperCase(getDBNameAuto(attName))));
		if(dbColumns.get(IXFormat.trimUpperCase(getDBNameAuto(attName)))==null){
			return true;
		}else{
			return dbColumns.get(IXFormat.trimUpperCase(getDBNameAuto(attName))).isBeginNull();
		}
	}
	
	/** 這些字段最開始值是否都為空
	 * @param attNames
	 * @return true:為空或null,false:不為空
	 */
	public boolean isBeginNullsAuto(String[] attNames){
		for (String attName : attNames) {
//			om.info(getRownum()+"["+attName+"]["+getDBNameAuto(attName)+"]:"+isBeginNullAuto(attName));
			if(!this.isBeginNullAuto(attName)){
				return false;
			}
		}
//		om.info(getRownum()+":isBeginNullAuto");
		return true;
	}
	
	/** 該行所有字段最開始值是否都為空
	 * @return	true:都為空,false:有得不為空
	 */
	public boolean isBeginNullAll(){
		for(String name:dbColumns.keySet()){
			if(!dbColumns.get(name).isBeginNull()){
				return false;
			}
		}
		return true;
	}
	
	/** 這些字段是否都為空
	 * @param attNames
	 * @return true:都為空或null,false:有不為空的
	 */
	public boolean isNulls(String[] attNames){
		for (String attName : attNames) {
			if(!this.isNull(attName)){
				return false;
			}
		}
		return true;
	}

	public int getIncludeRownum() {
		return includeRownum;
	}

	public void setIncludeRownum(int includeRownum) {
		this.includeRownum = includeRownum;
	}
	
	public Map<String, Column> getColumns() {
		return dbColumns;
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		while (hasNextKoElement()) {
			Task koTask = parseNextKoElement();
			if(koTask instanceof Column){
				Column column=((Column) koTask);
				String dbName =getDBName(column.getName());
				dbColumns.put(dbName, column);
				setValue(dbName, column.getCdata());
			}
		}
	}
	
	public void init(){
		lcs=table.getLinkColumnSet();
	}
	
	/** 自動判断 字段名称 獲取值 ,檢索順序 LINKCOLUMN 的DBName,COLUMNNAME
	 * @param name
	 * @return 值
	 */
	public String getStringAuto(String name){
		return getString(getDBNameAuto(name));
	}
	
	/** 獲取最開始的值,字段經過 getDBNameAuto 處理
	 * @param name
	 * @return	最開始的值
	 */
	public String getBeginValueAuto(String name){
		return getBeginValue(getDBNameAuto(name));
	}
	
	/** 獲取最開始的值
	 * @param dbName
	 * @return	最開始的值,xml中的CDATA
	 */
	public String getBeginValue(String dbName) {
		// TODO Auto-generated method stub
		Column column = dbColumns.get(IXFormat.trimUpperCase(dbName));
		if(column==null){
			return null;
		}
		return column.getBeginValue();
	}
	
	
	
	public void setValueAuto(String name,Object value) throws MTException{
		setValue(getDBNameAuto(name), value);
	}
	
	/**獲取数据库的字段名称  自動判断  ,檢索順序 LINKCOLUMN 的DBName,COLUMNNAME
	 * 例如:下面映射后字段A已經不存在的了.
	 * LINK 列:A	    字段:ID			實際存放字段為 ID
	 * LINK 列:ID 字段:TEMP01		實際存放字段為 TEMP01
	 * 		列:B  無映射			實際存放字段 B
	 * getDBNameAuto(ID) 		實際獲取 ID(A列数据)
	 * getDBNameAuto(TEMP01)	實際獲取TEMP01(ID)列
	 * getDBNameAuto(A)			實際獲取ID(A列数据)
	 * getDBNameAuto(B)			實際獲取B(B列数据)
	 * @param name
	 * @return	值
	 */
	public String getDBNameAuto(String name){
		String dbName=name;
		String columnName = lcs.getColumnName(name);
		if(columnName==null){
			if(lcs.getDBName(name)!=null){
				dbName=lcs.getDBName(name);
			}
		}
		return dbName;
	}
	
	/** 通過 源数据列名称  获取 實際插入到数据库的字段名称,如果無映射則返回回該字段名称
	 * @param columnName
	 * @return 值
	 */
	public String getDBName(String columnName){
		String dbName=lcs.getDBName(columnName);
		if(dbName==null){
			dbName=columnName;
		}
		return dbName;
	}
	
	
}
