package com.maximo.tools.impxml.task;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import com.maximo.app.MTException;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class InitDatabaseSequence extends Task{
	/**
	 * 是否打印輸出信息
	 */
	public static final String ISPRINTINFO="ISPRINTINFO";
	OracleSqlDetabese osd=null;
	
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		osd=new OracleSqlDetabese(con);
		this.execute();
		
		
	}
	
	
	public  void execute() {
		om.info("開始執行序列重置程式");
		try {
			/*
			 * 查询数据
			 */
			Statement st = con.createStatement();
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			String string = "SELECT TBNAME, NAME, MAXRESERVED, SEQUENCENAME, MAXSEQUENCEID FROM MAXSEQUENCE";
			//增加过滤,只显示有这个表这个字段的行
			string+=" where exists(select * from dba_tab_columns where owner=(select user from dual) and table_name=MAXSEQUENCE.TBNAME and column_name=MAXSEQUENCE.NAME)";
			ResultSet r = st
					.executeQuery(string);
			
			Statement st1 = con.createStatement();
			st1 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			
			Statement st2 = con.createStatement();
			st2 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
			int i=0;
			while (r.next()) {
				++i;
				String tbName = r.getString("TBNAME");
				String name = r.getString("NAME");
				long maxReserved = r.getLong("MAXRESERVED");
				String sequenceName=r.getString("SEQUENCENAME");
				long maxSequenceid=r.getLong("MAXSEQUENCEID");
				
//				om.info(i+"\t\t"+tbName);
				ResultSet r1 =null;
				ResultSet r2 =null;
				try {
					StringBuffer sql=new StringBuffer("select max(").append(name).append(") from ").append(tbName);
//					System.out.println(sql.toString());
					r1 = st1.executeQuery(sql.toString());
					if(r1.next()){
						sql=new StringBuffer("alter sequence ").append(sequenceName).append(" increment by 1");
//						System.out.println(sql.toString());
						osd.update(sql.toString());
						sql=new StringBuffer("select ").append(sequenceName).append(".nextval from dual");
//						System.out.println(sql.toString());
						r2 = st2.executeQuery(sql.toString());
						
						long maxId=r1.getLong(1);
//						System.out.print("当前最大值="+maxId);
						r2.next();
						long sequenceId=r2.getLong(1);
//						System.out.println(",当前序列值="+sequenceId);
						
						replaceLineMessageInTerminal("正在解析第["+new DecimalFormat("00000000").format(i)+"]行數據"
								+",当前最大值="+new DecimalFormat("0000000000").format(maxId)+",当前序列值="+new DecimalFormat("0000000000").format(sequenceId)
								+",SEQ="+sequenceName+"                ");
						if(isPrintInfo()){
							System.out.println();
						}
						
						
						long offset=maxId-sequenceId;
						if(maxId>0L&&offset!=0){
							sql=new StringBuffer("alter sequence ").append(sequenceName).append(" increment by ").append(offset);
//							System.out.println(sql.toString());
							osd.update(sql.toString());
							sql=new StringBuffer("select ").append(sequenceName).append(".nextval from dual ");
//							System.out.println(sql.toString());
							osd.setSql(sql.toString());
							osd.executeQuery();
							osd.close();
						}
						sql=new StringBuffer("alter sequence ").append(sequenceName).append(" increment by 1");
//						System.out.println(sql.toString());
						osd.update(sql.toString());
					}
					
				} catch (Exception e) {
					// TODO: handle exception
					if(e instanceof SQLException){
						if(((SQLException)e).getErrorCode()==2289){
							om.error("sequence "+sequenceName+" does not exist");
						}
					}else{
						e.printStackTrace();
					}
				}
				if(r1!=null){
					r1.close();
				}
				if(r2!=null){
					r2.close();
				}
				
				
				
				
			}
			st1.close();

			r.close();
			st.close();
			con.close();
			System.out.println("查询结束!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	/** 替換該行信息,支線linux終端
	 * @param s
	 */
	public void replaceLineMessageInTerminal(String s){
		tcc.moveToFirstLine();
		tcc.clearToEndOfLine();
		tcc.print(s);
	}

	/**是否打印輸出信息
	 * @return true 打印
	 */
	public boolean isPrintInfo() {
		return IXFormat.isTrueOrNull(propertys.get(ISPRINTINFO));
	} 
}


