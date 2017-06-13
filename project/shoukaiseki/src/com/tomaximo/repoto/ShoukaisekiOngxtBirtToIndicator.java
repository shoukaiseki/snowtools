package com.tomaximo.repoto;

//第二步
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


import com.shoukaiseki.file.md5.FileMD5;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;
import com.shoukaiseki.sql.oracle.SqlFormat;
import com.shoukaiseki.tuuyou.anngoukatofukugouka.MD5;

public class ShoukaisekiOngxtBirtToIndicator {

		  static final Logger log = Logger.getLogger(ShoukaisekiOngxtBirtToIndicator.class);
	public ShoukaisekiOngxtBirtToIndicator() throws SQLException,
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
		String insertSql = "insert into INDICATOR_DATA"
				+ "(VERSION,ID,F_NAME,F_CODE,F_NOTE,"
				+ "MODIFIED,GENERATING_SET,MEASURE_UNIT,"
				+ "INUSE,SEQUENCE_NO,CREATOR,CREATED,MODIFIER,"
				+ "F_PRECISION,PARENT_ID) " + "values "
				+ "('2',?,?,?,?,sysdate,?,?,'1','-1',"
				+ "'MAXADMIN',sysdate,'MAXADMIN','0',"
				+ "'e39484b9bd614eb691a3db3e626c8e9f')";//经营月报（分公司）
//				+ "'cf33ba76c006407180bfb175137ebc7f')";//生产日报（广州)
//				+ "'a659180aa2f14f3c9b753eea717e0bf2')";//生产月报（广州）

		String sql = "select * from SHOUKAISEKI_ONGXT_BIRT where obujekuto='生产经营综合月报表'"
//				+" and rownum <2"
				;
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
			String seruName = r.getString("seruName");
			String seru = r.getString("code");
			String fullSeruName = r.getString("fullSeruName");
			String jizu = r.getString("jizu");
			String yunitto = r.getString("yunitto");
			FileMD5 md5 = new FileMD5();

			int rowCount = 1;
			for (; rowCount > 0; ++i) {
				SqlFormat sqlFormat = osd.setTableRoot("INDICATOR_DATA");
				String idMD5 = MD5.MD5(df.format(i));
				id = idMD5;
//				System.out.println(i + "==" + idMD5);
				sqlFormat.setDoutouWhere("ID", id);
				ResultSet rs = osd.executeQuery();
				rowCount = osd.getRowCount();
//				System.out.println("count=" + rowCount + "\t" + osd.getSql());
				osd.close();
			}
			
			
			SqlFormat sqlFormat = osd2.setTableRoot("MEASUREUNIT");
			sqlFormat.setDoutouWhere("MEASUREUNITID", yunitto);
			ResultSet rs = osd2.executeQuery();
			if(rs.next()){
				yunitto=rs.getString("CONTENTUID");
			}else{
				yunitto=null;
			}
			osd2.close();

			String message=++count+" \tSERUNAME=" + seruName + ",SERU=" + seru
					+ ",FULLSERUNAME=" + fullSeruName + ",JIZU=" + jizu
					+ ",YUNITTO=" + yunitto;
			log.debug(message);
			pst.setString(1, id);
			pst.setString(2, seruName);
			pst.setString(3, seru);
			pst.setString(4, fullSeruName);
			pst.setString(5, jizu);
			pst.setString(6, yunitto);
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
//		}catch(BatchUpdateException bue){
//			con.rollback();
//			bue.printStackTrace();
//			errorCode=bue.getErrorCode();
//			iii=bue.getUpdateCounts();
//			int j=0;
//			for (int o : iii) {
//				System.out.println(o);
//			}
//			int updateCount = pst.getUpdateCount();
//			System.out.println("成功更新"+updateCount);
//			System.out.println(Statement.EXECUTE_FAILED);//-3
		}
		//
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
		new ShoukaisekiOngxtBirtToIndicator();
	}
}


