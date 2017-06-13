package com.tomaximo.finddetabash;
//oraclesql.OracleClob
/**  oracle CLOB http://www.newasp.cn/tech/java/15784.html
 *
 *操作oracle数据库的CLOB字段，包括读和写 效果不如ClobTest,出现乱码
 *作者：令少爷
 * */

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.shoukaiseki.characterdetector.CharacterEncoding;
import com.shoukaiseki.constantlib.CharacterEncodingName;

import oracle.jdbc.OracleResultSet;
import oracle.jdbc.driver.OracleDriver;
import oracle.sql.*;

public class OracleClob {

	String tableName = null; // 表名
	String primaryKey = null; // 表的主键名
	String primaryValue = null; // 表的主键值
	String fieldName = null; // 表的CLOB字段名
	String clobValue = null; // 表的CLOB字段值

	static Connection conn = null; // 与oracle的连接

	static String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	static String user = "maximo75";
	static String password = "maximo75";

	/**
	 * 
	 *用于测试用
	 * 
	 * */
	public static void main(String[] args) {
		try {
			OracleClob jc = new OracleClob(getConnection(), "maxpresentation",
					"app", "'ASUS'", "presentation", "<font>你好</font>");
			jc = new OracleClob(getConnection(), "maxpresentation",
					"app", "'OPTICKET'", "presentation", "<font>你好</font>");
//			 jc.write();
			String s = "";
			s = jc.read();
//			s=clobExport();
			// s=new String(jc.read().getBytes("US-ASCII"));
			System.out.println(s);
			String code= getStringEncode(s);
			System.out.println("字符编码格式为" +code);

			byte[] bytes = s.getBytes(code);
			String sa = new String(bytes);
			System.out.println(sa);
			
			/**
			 * 文件先检查一次后即可检查 其它类型
			 */
			s="中国国民党";
			System.out.println(s);
			code=getStringEncode(s);
			System.out.println("String字符编码格式为:"+code);
			
			
		} catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}

	/**
	 * 
	 *构造方法
	 * 
	 * */
	public OracleClob(Connection connection, String tableName,
			String primaryKey, String primaryValue, String fieldName,
			String clobValue) {
		this.conn = connection;
		this.tableName = tableName;
		this.primaryKey = primaryKey;
		this.primaryValue = primaryValue;
		this.fieldName = fieldName;
		this.clobValue = clobValue;
	}

	/**
	 * 
	 *构造方法，但不必传clobValue值 一般构造出的实例用来读Clob字段
	 * 
	 * */
	public OracleClob(Connection connection, String tableName,
			String primaryKey, String primaryValue, String fieldName) {
		this.conn = connection;
		this.tableName = tableName;
		this.primaryKey = primaryKey;
		this.primaryValue = primaryValue;
		this.fieldName = fieldName;
	}

	/**
	 * 
	 *用于测试
	 * 
	 * */
	public static Connection getConnection() throws SQLException,
			ClassNotFoundException {
		System.out.println("正在试图加载驱动程序");
		Class.forName("oracle.jdbc.OracleDriver");
		System.out.println("驱动程序已加载");

		System.out.println("正在试图连接数据库--------");
		Connection conn = DriverManager.getConnection(url, user, password);
		System.out.println("OK,成功连接到数据库");
		return conn;
	}

	/**
	 * 
	 *读数据库中clob字段的内容
	 * 
	 * @return clob字段值
	 * 
	 * */
	public String read() throws SQLException, IOException {
		String rtn = null;
		String rtna = null;
		try {
			String sql = "select " + fieldName + " from " + tableName
					+ " where " + primaryKey + "=" + primaryValue;
			// Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			// int v = Integer.parseInt(primaryValue);
			// pstmt.setInt(1,v);
			ResultSet rs = pstmt.executeQuery();

			CLOB clob = null;
			if (rs.next()) {
				clob = (oracle.sql.CLOB)rs.getClob(fieldName);
				
				rtna=ClobToString(clob);
//				System.out.println("ClobToString(clob)"+ClobToString(clob));
				// clob = ((OracleResultSet)rs).getCLOB(fieldName);
				// clob =
				// ((org.apache.commons.dbcp.DelegatingResultSet)rs).getClob(fieldName);
				// Reader in = clob.getCharacterStream();
				InputStream input = clob.getAsciiStream();
				int len = (int) clob.length();
				byte[] by = new byte[len];
				int i;// = input.read(by,0,len);
				while (-1 != (i = input.read(by, 0, by.length))) {
					input.read(by, 0, i);
				}
				
//				System.out.println();
				String code = getByteEncode(by);
//				System.out.println(code);
				rtn = new String(by, code);
			}
		} catch (SQLException e) {
			throw e;
		} catch (Exception ee) {
			ee.printStackTrace();
		}

//		return rtn;
		return rtna;
	}

	/**
	 * 
	 *葱数据库中clob字段的内容
	 * 
	 * */
	public void write() throws SQLException, IOException {
		String sql = "update " + tableName + " set " + fieldName
				+ "=empty_clob() where " + primaryKey + "=" + primaryValue;
		// Connection conn = getConnection();
		conn.setAutoCommit(false);

		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.executeUpdate();

		sql = "select " + fieldName + " from " + tableName + " where "
				+ primaryKey + "=" + primaryValue;
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);

		java.sql.Clob clob;
		if (rs.next()) {
			clob = ((oracle.jdbc.OracleResultSet) rs).getClob(fieldName);
			// clob =
			// ((org.apache.commons.dbcp.DelegatingResultSet)rs).getClob(fieldName);
			oracle.sql.CLOB my_clob = (oracle.sql.CLOB) clob;
			OutputStream writer = my_clob.getAsciiOutputStream();
			byte[] contentStr = this.getContent().getBytes();
			writer.write(contentStr);
			writer.flush();
			writer.close();
		}

		conn.commit();
		rs.close();
		st.close();
		pstmt.close();
		conn.setAutoCommit(true);
	}

	/**
*
*
* */
	private String getContent() {
		return this.clobValue;
	}

	/**
*
*
* */
	public void setClobValue(String clobValue) {
		this.clobValue = clobValue;
	}

	/**
	 * 字符串编码转换的实现方法
	 * 
	 * @param str
	 *            待转换编码的字符串
	 * @param oldCharset
	 *            原编码
	 * @param newCharset
	 *            目标编码
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String changeCharset(String str, String oldCharset,
			String newCharset) throws UnsupportedEncodingException {
		if (str != null) {
			// 用旧的字符编码解码字符串。解码可能会出现异常。
			byte[] bs = str.getBytes(oldCharset);
			// 用新的字符编码生成字符串
			return new String(bs, newCharset);
		}
		return null;
	}

	public static String getByteEncode(byte[] str) throws IOException {
		if (str.length == 0) {
			return "utf-8";
		}
		System.out.println("字节数为" + str.length);
		CodepageDetectorProxy detector = null;
		detector = CodepageDetectorProxy.getInstance();
		// detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		InputStream myIn = new ByteArrayInputStream(str);
		try {
			charset = detector.detectCodepage(myIn, 3);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		myIn.close();
		if (charset != null) {
			return charset.name();
		} else {
			return "utf-8";
		}
	}

	/**
	 * 都
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 * @throws IOException
	 */
	private static String clobExport() throws ClassNotFoundException, SQLException,
			IOException {
		// TODO Auto-generated method stub
		CLOB clob = null;
		String sql = "select * from maxpresentation where app='ASUS'";
		DriverManager.registerDriver(new OracleDriver());
		PreparedStatement stmt = conn.prepareStatement(sql);
		ResultSet rs = stmt.executeQuery();
		String id = "";
		String content = "";
		if (rs.next()) {
			id = rs.getString("app");// 获得app
			clob = (oracle.sql.CLOB) rs.getClob("presentation"); // 获得CLOB字段presentation
			// 注释： 用 rs.getString("str")无法得到 数据 ，返回的 是 NULL;
			content = ClobToString(clob);
		}
		stmt.close();
		conn.close();
		// 输出结果
		System.out.println(id);
//		System.out.println(content);
		return content;
	}

	// 将字CLOB转成STRING类型
	public static String ClobToString(CLOB clob) throws SQLException, IOException {

		String reString = "";
		Reader is = clob.getCharacterStream();// 得到流
		BufferedReader br = new BufferedReader(is);
		String s = br.readLine();
		StringBuffer sb = new StringBuffer();
		while (s != null) {// 执行循环将字符串全部取出付值给StringBuffer,由StringBuffer转成STRING
			sb.append(s);
			s = br.readLine();
		}
		reString = sb.toString();
		return reString;
	}
	/**
	 * 获得字符串的编码格式
	 */
	public static String getStringEncode(String str) {
		if (str == null) {
			return "utf-8";
		}
		CodepageDetectorProxy detector = CodepageDetectorProxy.getInstance();
		detector.add(new ParsingDetector(false));
		detector.add(JChardetFacade.getInstance());
		detector.add(ASCIIDetector.getInstance());
		detector.add(UnicodeDetector.getInstance());
		java.nio.charset.Charset charset = null;
		InputStream myIn = new ByteArrayInputStream(str.getBytes());
		try {
			charset = detector.detectCodepage(myIn, 3);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (charset != null) {
			return charset.name();
		} else {
			return "utf-8";
		}
	}
}


