package org.maximo.tools.impxml.task;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.maximo.app.MTException;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class OracleDataBaseData  extends Task{
	long rowCount=0;
	String selectSql=null;
	
	/**
	 * 存放檢索數據庫 COLUMNLABEL 與 目標素據庫 COLUMNNAME 的 映射,row设置值按照 映射後的column名称设置
	 * Map<COLUMNLABEL,COLUMNNAME>
	 */
	Map<String,String> ccMap=new HashMap<String,String>();
	ResultSet rs=null;
	OracleSqlDetabese osd=null;
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		while (hasNextKoElement()) {
			Task koTask = parseNextKoElement();
			if(koTask instanceof ODDSelectSql){
				ODDSelectSql oddss=(ODDSelectSql) koTask;
				selectSql=oddss.getCdata();
				om.info("selectSql="+oddss.getCdata());
			}else if(koTask instanceof ODDColumn){
				ODDColumn oddc=(ODDColumn) koTask;
				ccMap.put(oddc.getColumnLabel(),oddc.getColumnName());
			}
		}
		if(selectSql!=null&&!ccMap.isEmpty()){
			osd=new OracleSqlDetabese(con);
			osd.setSql(selectSql);
			try {
				rs=osd.executeQuery();
				rowCount=osd.getRowCount();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				osd.close();
				throw new MTException(e);
			}
		}
	}
	
	

	public boolean hasNext() throws MTException{
			try {
				return  rs==null?false:rs.next();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				osd.close();
				throw new MTException(e);
			}
	}
	
	public Row createRow(){
		Row row=new org.maximo.tools.impxml.task.Row();
		row.setConnection(con);
		row.setTable(table);
		row.setOutMessage(om);
		row.init();
		return row;
	}

	public Row next() throws MTException{
		// TODO Auto-generated method stub
			Row ixrow=createRow();
		try {
			for (String columnlabel : ccMap.keySet()) {
				String value=rs.getString(columnlabel);
				String columnname = ccMap.get(columnlabel);
				ixrow.setValueAuto(columnname, value);
				ixrow.setValue(columnname, value);
				Column column = ixrow.getColumns().get(IXFormat.trimUpperCase(ixrow.getDBNameAuto(columnname)));
				if(column!=null){
					column.setBeginValue(value);
				}
				column = ixrow.getColumns().get(columnname);
				//					om.info("column2="+column);
				if(column!=null){
					column.setBeginValue(value);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			osd.close();
				throw new MTException(e);
		}
		return ixrow;

	}
	
	public void destroyElement() {
		// TODO Auto-generated method stub
		super.destroyElement();
		osd.close();
	};
	
	public long getRowCount() {
		return rowCount;
	}
}
