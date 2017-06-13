package com.maximo.database.sequence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class ResetMaximoSequence {

	static Connection con = null;
	static Statement sm = null;
	static ResultSet rs = null;
	static String tableName = null;
	static String cName = null;
	static String result = null;
	static String url = "jdbc:oracle:thin:@10.59.245.183:1521:eamcrpwz";
	// private String url = "jdbc:oracle:thin:@192.168.2.101:1521:zhjqmaximo";
	static String driver = "oracle.jdbc.driver.OracleDriver";
	static String user = "maximo";
	static String password = "maximo";
	
	OracleSqlDetabese osd=null;

	public void setDefuorutoConnection() {
		try {
			// （1）装载并注册数据库的JDBC驱动程序
			// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			println("正在试图加载驱动程序 " + driver);
			Class.forName(driver);
			println("驱动程序已加载");
			// 注册JDBC驱动：有些地方可不用此句
			println("url=" + url);
			println("user=" + user);
			println("password=" + password);
			println("正在试图连接数据库--------");
			java.sql.DriverManager
					.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, user, password);

			println("OK,成功连接到数据库");

			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
			osd=new OracleSqlDetabese(con);
		} catch (Exception ex) {
			ex.printStackTrace();
			println(ex.getMessage(), true);
		}
	}



	public static void main(String[] args) throws SQLException {
		ResetMaximoSequence jd = new ResetMaximoSequence();
		jd.setDefuorutoConnection();
		jd.readData();
	}

	public  void readData() {
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
				String tbName = r.getString("TBNAME");
				String name = r.getString("NAME");
				String sequenceName=r.getString("SEQUENCENAME");
				
				System.out.println(++i+"\t\t"+tbName);
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
						System.out.print("当前最大值="+maxId);
						r2.next();
						long sequenceId=r2.getLong(1);
						System.out.println(",当前序列值="+sequenceId);
						
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
					e.printStackTrace();
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


	public static void println() {

	}

	public static void println(String s, boolean b) {
		System.out.println(s);
	}

	public static void println(String s) {
		System.out.println(s);
	}
}


