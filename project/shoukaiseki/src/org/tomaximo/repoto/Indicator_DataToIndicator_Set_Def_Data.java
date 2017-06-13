package org.tomaximo.repoto;

import java.io.UnsupportedEncodingException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;


import org.shoukaiseki.file.md5.FileMD5;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
import org.shoukaiseki.sql.oracle.SqlFormat;
import org.shoukaiseki.tuuyou.anngoukatofukugouka.MD5;

public class Indicator_DataToIndicator_Set_Def_Data {

		  static final Logger log = Logger.getLogger(Indicator_DataToIndicator_Set_Def_Data.class);
	public Indicator_DataToIndicator_Set_Def_Data() throws SQLException,
			NoSuchAlgorithmException, UnsupportedEncodingException,
			ClassNotFoundException {
		// TODO Auto-generated constructor stub
		PropertyConfigurator.configure("e:/java/classlib/sql/log4j.properties");
		setDefuorutoConnection();
		updateToDatabesu();
	}

	public void updateToDatabesu() throws SQLException,
			NoSuchAlgorithmException, UnsupportedEncodingException,
			ClassNotFoundException {
		//月报表
		String insertSql = ""
				+"insert into INDICATOR_SET_DEF_I_DATA("
				+"ID,VERSION,INDICATOR_DATA,DEFINITION_ID,CODE,INTERMEDIATE,F_TYPE,PARAMS,F_CONTENT,UNTO_ZERO,ACTIVE,SEQUENCE_NO,CREATOR,CREATED,MODIFIER,MODIFIED"
				+")values("
				+"?,'1',?,'fc332a6586364b3faf9f0112e995713f',?,'0','表达式',?,?,'0','1','19','MAXADMIN',sysdate,'MAXADMIN',sysdate"
				+")"
				;
				
		//日报表		
		insertSql=""
				+"insert into INDICATOR_SET_DEF_I_DATA("
				+"ID,VERSION,INDICATOR_DATA,DEFINITION_ID,CODE,INTERMEDIATE,F_TYPE,PARAMS,F_CONTENT,UNTO_ZERO,ACTIVE,SEQUENCE_NO,CREATOR,CREATED,MODIFIER,MODIFIED"
				+")values("
				+"?,'1',?,'10b4c9de46a747a9a4604c2668cca563',?,'0','表达式',?,?,'0','1','19','MAXADMIN',sysdate,'MAXADMIN',sysdate"
				+")"
				;
		
		//经营月报	
		insertSql=""
				+"insert into INDICATOR_SET_DEF_I_DATA("
				+"ID,VERSION,INDICATOR_DATA,DEFINITION_ID,CODE,INTERMEDIATE,F_TYPE,PARAMS,F_CONTENT,UNTO_ZERO,ACTIVE,SEQUENCE_NO,CREATOR,CREATED,MODIFIER,MODIFIED"
				+")values("
				+"?,'1',?,'b34b02cb2beb4cf3b8eeee32af232079',?,'0','表达式',?,?,'0','1','19','MAXADMIN',sysdate,'MAXADMIN',sysdate"
				+")"
				;
		String sql = "select * from INDICATOR_DATA where parent_id='a659180aa2f14f3c9b753eea717e0bf2'"
//				+" and rownum <5"
				;//月报表
		sql = "select * from INDICATOR_DATA where parent_id='cf33ba76c006407180bfb175137ebc7f'"
//				+" and rownum <5"
				;//日报表
		
		sql = "select * from INDICATOR_DATA where parent_id='e39484b9bd614eb691a3db3e626c8e9f'"
//				+" and rownum <5"
				;//日报表
		System.out.println(sql);
		/*
		 * 查询数据
		 */
		Statement st = con.createStatement();
		st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet r = st.executeQuery(sql);
		r.last();
		System.out.println("行数" + r.getRow());
		r.beforeFirst();// 将光标移动到此 ResultSet 对象的开头，正好位于第一行之前。

		ResultSetMetaData rsmd = r.getMetaData();
		int column = rsmd.getColumnCount();
		System.out.println("列数为" + column);
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			System.out.print(rsmd.getColumnName(i + 1) + "\t");
		}
		System.out.println();
		DecimalFormat df = new DecimalFormat("000,000,000");
		df = new DecimalFormat("0000");

		OracleSqlDetabese osd = new OracleSqlDetabese(url, user, password,
				driver);
		url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.6)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.7)(PORT=1521)))(CONNECT_DATA=(SERVER=dedicated)(SERVICE_NAME=eam)))";
		user = "maximo";
		password = "maximo";
		OracleSqlDetabese osd2 = new OracleSqlDetabese(url, user, password, driver);
		int i = 0;
		PreparedStatement pst = con.prepareStatement(insertSql);
		int count=0;
		while (r.next()) {
			String id = null;
			String Indicator_Data = r.getString("id");
			String seru = r.getString("F_CODE");
			String siki = null;
			FileMD5 md5 = new FileMD5();

			int rowCount = 1;
			for (; rowCount > 0; ++i) {
				SqlFormat sqlFormat = osd.setTableRoot("INDICATOR_SET_DEF_I_DATA");
				String idMD5 = MD5.MD5(df.format(i));
				id = idMD5;
				System.out.println(i + "==" + idMD5);
				sqlFormat.setDoutouWhere("ID", id);
				ResultSet rs = osd.executeQuery();
				rowCount = osd.getRowCount();
				System.out.println("count=" + rowCount + "\t" + osd.getSql());
				osd.close();
			}
			
			SqlFormat sqlFormat = osd2.setTableRoot("SHOUKAISEKI_ONGXT_BIRT");
			sqlFormat.setDoutouWhere("seru", seru);
			ResultSet rs = osd2.executeQuery();
			if(rs.next()){
				siki=rs.getString("atai");
			}else{
				siki=null;
			}
			

			String message=++count+" \tINDICATOR_DATA=" + Indicator_Data + ",SERU=" + seru
					+ ",INTERMEDIATE=" + siki ;
			log.debug(message);
			pst.setString(1, id);
			pst.setString(2, Indicator_Data);
			pst.setString(3, seru);
			pst.setString(4, siki);
			pst.setString(5, siki);
			pst.addBatch();
		}

		System.out.println(insertSql);
		int[] iii = null;
		int errorCode=0;
		try {
			iii = pst.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
			con.rollback();
			log.warn("log");
			int updateCount = pst.getUpdateCount();
			System.out.println("-----"+updateCount);
			errorCode = e.getErrorCode();
			String s = e.getSQLState();
			System.out.println("s="+s);
		}
		//
//		con.rollback();
		System.out.println("获取此 SQLException 对象的特定于供应商的异常代码="+errorCode);
		System.out.println(con.getAutoCommit());
		if(iii==null){
			System.out.println("return");
//			直接return，当退出想不提交必需con.rollback();
//			return;
		}
		System.out.println("111111111");
		pst.clearBatch();
		pst.close();
		r.close();
		st.close();
		
//		即使设置setAutoCommit()false 还会提交
//		不提交必需con.rollback();
		con.close();
	}

	static Connection con = null;
	static Statement sm = null;
	static ResultSet rs = null;
	static String tableName = null;
	static String cName = null;
	static String result = null;
	static String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	// private String url = "jdbc:oracle:thin:@10.59.67.22:1521:test22";
	static String driver = "oracle.jdbc.driver.OracleDriver";
	static String user = "maximo75";
	static String password = "maximo75";

	public static void setDefuorutoConnection() {
		try {
			url = "jdbc:oracle:thin:@10.59.67.22:1521:test22";
			user = "orclzhjq";
			password = "orclzhjq";
			url = "jdbc:oracle:thin:@127.0.0.1:1521:maximo75";
			user = "luyang";
			password = "luyang";
			 url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.6)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.7)(PORT=1521)))(CONNECT_DATA=(SERVER=dedicated)(SERVICE_NAME=eam)))";
			 user = "maximo";
			 password = "maximo";
			// driver="oracle.jdbc.OracleDriver";
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
			 * maximo.properties放在bin目录
			 */
			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			println(ex.getMessage(), true);
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

	/**
	 * @param args
	 * @throws SQLException
	 * @throws UnsupportedEncodingException
	 * @throws NoSuchAlgorithmException
	 * @throws ClassNotFoundException
	 */
	public static void main(String[] args) throws SQLException,
			NoSuchAlgorithmException, UnsupportedEncodingException,
			ClassNotFoundException {
		// TODO Auto-generated method stub
		new Indicator_DataToIndicator_Set_Def_Data();
	}
}


