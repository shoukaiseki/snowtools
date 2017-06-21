package org.shoukaiseki.sql.oracle;

import java.util.List;
import java.io.*;
import java.net.*;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;

import org.shoukaiseki.tuuyou.logger.PrintLogs;

import oracle.sql.CLOB;

public class OracleSqlDetabese implements SqlFormat {
	private PreparedStatement stmt = null;

	private PrintLogs logger = new PrintLogs();
	private Connection con = null;
	private ResultSetMetaData rsmd;
	private ResultSet r;

	private SqlFormat sqlFormat = this;
	private String sql = null;
	private String tableRoot = null;
	private String where = null;
	private Statement st;
	private int rowCount = 0;
	private int columnCount = 0;
	private HashMap<String,String>  columnMap = new HashMap<String,String>();
	/**
	 * 格式化插入語句時設置某些字段爲默認值
	 */
	private HashMap<String,String> defuorutoFinalMap =new HashMap<String,String>();
	private String orderBy=null;

	public OracleSqlDetabese(Connection con) {
		this.con = con;
	}

	public OracleSqlDetabese(OracleSqlDetabese osd) {
		this.con = osd.getConnection();
	}

	public OracleSqlDetabese(String url, String user, String password,
			String driver) {
		// TODO Auto-generated constructor stub
		try {
			// （1）装载并注册数据库的JDBC驱动程序
			// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			logger.debug("正在试图加载驱动程序 " + driver);
			Class.forName(driver);
			logger.debug("驱动程序已加载");
			// 注册JDBC驱动：有些地方可不用此句
			logger.debug("url=" + url);
			logger.debug("user=" + user);
			logger.debug("password=" + password);
			logger.debug("正在试图连接数据库--------");
			java.sql.DriverManager
					.registerDriver(new oracle.jdbc.driver.OracleDriver());
			con = DriverManager.getConnection(url, user, password);
			/* 关闭自动更新 */
			con.setAutoCommit(false);
			logger.debug("成功連接至數據庫!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Error 加載驅動" + driver + "失敗...", e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error 連接數據庫失敗....:", e);
		}
	}

	public OracleSqlDetabese(String url, String user, String password,
			String driver, String program) throws ClassNotFoundException,
			UnknownHostException {
		// TODO Auto-generated constructor stub
		try {
			// （1）装载并注册数据库的JDBC驱动程序
			// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			logger.debug("正在试图加载驱动程序 " + driver);
			Class.forName(driver);
			logger.debug("驱动程序已加载");
			// 注册JDBC驱动：有些地方可不用此句
			logger.debug("url=" + url);
			logger.debug("user=" + user);
			logger.debug("password=" + password);
			logger.debug("正在试图连接数据库--------");
			Properties props = new Properties();
			props.setProperty("password", password);
			props.setProperty("user", user);
			props.put("v$session.osuser", System.getProperty("user.name")
					.toString());
			try {
				//採用無限網卡是可能會報錯,jdk1.7可以使用 InetAddress.getLoopbackAddress().getCanonicalHostName();
				String hostName=InetAddress.getLocalHost().getCanonicalHostName();
				props.put("v$session.machine", hostName);
			} catch (UnknownHostException e) {
				// TODO: handle exception
			}
			props.put("v$session.program", program);
			DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
			con = DriverManager.getConnection(url, props);
			con.setAutoCommit(false);
			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
			logger.debug("成功連接至數據庫!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			logger.error("Error 加載驅動" + driver + "失敗...", e);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("Error 連接數據庫失敗....:", e);
		}

	}

	/**
	 * 採用參數查詢方式,返回 PreparedStatement 使用其setString方法進行設置參數值
	 * 
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement prepareStatement() throws SQLException {
		try {
			stmt = con.prepareStatement(sql);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("con.prepareStatement Error!", e);
			throw new SQLException(e);
		}
		return stmt;
	}

	public ResultSet executeQueryPreparedStatement() throws SQLException {
		try {
			r = stmt.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("con.executeQueryPreparedStatement Error!", e);
			throw new SQLException(e);
		}
		return r;
	}

	public ResultSet executeQueryUpdate() throws SQLException {
		String[] name = getColumnName();
		format(name);
		close();
		/*
		 * 查询数据
		 */
		// ResultSet.CONCUR_READ_ONLY //只读
		st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
				ResultSet.CONCUR_UPDATABLE);
		r = st.executeQuery(sql);

		rsmd = r.getMetaData();
		columnCount = rsmd.getColumnCount();

		return r;
	}

	/**
	 * 自定义ResultSet权限,注意这样获取行数不能使用OracleSqlDatabesu.getRowCount()了;
	 * 
	 * @param kasoru
	 *            カーソル ResultSet.TYPE_FORWORD_ONLY 指针只可以向前移动
	 *            ResultSet.TYPE_SCROLL_INSENSITIVE 指针可滚动，但是不受其他用户对数据库更改的影响
	 *            ResultSet.TYPE_SCORLL_SENSITIVE 指针可滚动，当其它用户更改数据库时这个记录也会改变
	 * @param kyoka
	 *            許可 ResultSet.CONCUR_READ_ONLY 只读 ResultSet.CONCUR_UPDATABLE
	 *            可更新
	 * @return
	 * @throws SQLException
	 */
	public ResultSet executeQuery(int kasoru, int kyoka) throws SQLException {
		close();
		/*
		 * 查询数据
		 */
		// ResultSet.CONCUR_READ_ONLY //只读
		st = con.createStatement(kasoru, kyoka);
		r = st.executeQuery(sql);

		rsmd = r.getMetaData();
		columnCount = rsmd.getColumnCount();
		return r;
	}

	/*
	 * createStatement()缺省参数等价于:
	 * createStatement(ResultSet.TYPE_FORWORD_ONLY,ResultSet.CONCUR_READ_ONLY);
	 * 
	 * @see org.shoukaiseki.sql.oracle.SqlFormat#executeQuery()
	 */
	public ResultSet executeQuery() throws SQLException {
		try {
			close();
			/*
			 * 查询数据
			 */
			st = con.createStatement();
			// ResultSet.CONCUR_READ_ONLY //只读
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			r = st.executeQuery(sql);
			r.last();
			rowCount = r.getRow();
			// System.out.logger.debug("行数" + r.getRow());
			r.beforeFirst();// 将光标移动到此 ResultSet 对象的开头，正好位于第一行之前。
			rsmd = r.getMetaData();
			columnCount = rsmd.getColumnCount();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			logger.error("executeQuery.sql=" + sql, e);
			throw new SQLException(e);
		}
		return r;
	}

	public String[] getColumnName() throws SQLException {
		String[] columnName = new String[columnCount];
		for (int i = 0; i < columnCount; i++) {
			columnName[i] = rsmd.getColumnName(i + 1);
			columnMap.put(columnName[i].toUpperCase(),columnName[i].toUpperCase());
		}
		return columnName;
	}
	
	/**插入模式下返回有效的輸入字段
	 * @return
	 */
	public Set<String> getColumnYuukou(){
		return columnMap.keySet();
	}

	public ResultSet getResultSet() {
		return r;
	}
	
	public PreparedStatement getPreparedStatement() {
		return stmt;
	}


	public int getColumnCount() {
		return columnCount;
	}

	public int getRowCount() {
		return rowCount;
	}

	public SqlFormat setTableRoot(String tableRoot) {
		this.tableRoot = tableRoot.toUpperCase();
		return sqlFormat;
	}
	
	public void setTableRootInsert(String tableRoot){
		try {
			this.tableRoot = tableRoot.toUpperCase();
			setWhere("rownum=0");
			format();
			close();
			executeQuery();
			getColumnName();
			close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("獲取列名失敗!", e);
		}
	}

	public void setDoutouWhere(String column, String atai) {
		// TODO Auto-generated method stub
		this.where = column + " ='" + atai + "' ";

	}

	public void addDoutouWhere(String column, String atai) {
		if (where == null) {
			this.where = column + " ='" + atai + "' ";
		}
	}

	/**
	 * 定义列名的SQL
	 * 
	 * @param column
	 */
	public void format(String[] column) {
		String columns = "";
		for (String string : column) {
			if (!columns.isEmpty()) {
				columns += ",";
			}
			columns += string;
		}
		sql = "select " + columns + " from \"" + tableRoot + "\"";
		if (where != null) {
			sql += " where " + where;
		}
		if(orderBy!=null){
			sql +=" order by "+orderBy;
		}
	}

	public void format() {
		sql = "select * from \"" + tableRoot + "\"";
		if (where != null) {
			sql += " where " + where;
		}
		if(orderBy!=null){
			sql +=" order by "+orderBy;
		}
	}

	/**
	 * 格式化成 Insert 批量插入語句,之後調用採用 prepareStatement() 返回 PreparedStatement 接口對象進行操作 
	 * 調用 getInsertColumnIndex(field) 方法返回 字段在插入語句中的索引位置
	 */
	public void formatInsert() {
		Set<String> columns = columnMap.keySet();
		String fields = "";
		String gimonnfu = "";
		int size = columns.size();
		int i = 0;
		for (String column : columns) {
			fields += column;
			gimonnfu += "?";
			if (++i != size) {
				fields += ",";
				gimonnfu += ",";
			}
		}
		columns = defuorutoFinalMap.keySet();
		logger.debug(" size=" + defuorutoFinalMap.size());
		for (String key : columns) {
			fields += "," + key;
			gimonnfu += "," + defuorutoFinalMap.get(key);
			logger.debug(key + "====" + defuorutoFinalMap.get(key));
		}
		sql = "insert into " + tableRoot + " (" + fields + ") values("
				+ gimonnfu + ")";
		logger.debug(sql);

	}

	/**
	 * 插入時忽略插入的列
	 * 
	 * @param musi
	 */
	public void formatInsert(String[] musi) {
		for (String string : musi) {
			columnMap.remove(string.toUpperCase());
		}
		formatInsert();
	}
	
	/**設置字段默認值,將在insert語句中設置,如果爲字符串需要加 ''
	 * @param field
	 * @param value
	 */
	public void setInsertDefuorudoFinalColumn(String field,String value){
		defuorutoFinalMap.put(field, value);
		columnMap.remove(field.toUpperCase());
	}
	
	/**
	 * @param field	 	字段名
	 * @return	字段在插入語句中的索引位置,之後採用 pst.setString(4, value) 設置值.  返回-1說明無該字段 
	 */
	public int getInsertColumnIndex(String field){
		Set<String> columns = columnMap.keySet();
		int i=0;
		for (String column : columns) {
			i++;
			if(column.equalsIgnoreCase(field)){
				return i;
			}
		}
		return -1;
	}

	public Connection getConnection() {
		return this.con;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}
	
	public void closeAllRollBack(){
		try {
			if (r != null)
				r.close();
			if (st != null)
				st.close();
			if (stmt!=null)
				stmt.close();
			if(con!=null){
				con.rollback();
				con.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void close()  {
		try {
			if (r != null)r.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			if (st != null) st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try{
			if (stmt!=null) stmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setOrderBy(String orderBy){
		this.orderBy=orderBy;
	}

	public void setWhere(String where) {
		// TODO Auto-generated method stub
		this.where = where;
	}

	/**
	 * 更新命令,成功执行后返回true
	 * 
	 * @param con
	 * @param sql
	 */
	public boolean update(Connection con, String sql) {
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("update Error sql=" + sql);
			logger.error("update Error", e);
			return false;
		}
		return true;
	}

	/**
	 * 更新命令,成功执行后返回true
	 * 
	 * @param con
	 * @param sql
	 */
	public boolean update(String sql) {
		try {
			PreparedStatement pst = con.prepareStatement(sql);
			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
		} catch (Exception e) {
			// TODO: handle exception
			logger.error("update Error sql=" + sql);
			logger.error("update Error", e);
			return false;
		}
		return true;
	}

	// public String getClobToString(String columnLabel) throws
	// ClassNotFoundException, SQLException,
	// IOException {
	// // TODO Auto-generated method stub
	// CLOB clob = null;
	// String content = "";
	// clob = (oracle.sql.CLOB) r.getClob(columnLabel); // 获得CLOB字段str
	// // 注释： 用 rs.getString("str")无法得到 数据 ，返回的 是 NULL;
	// content = ClobToString(clob);
	// // 输出结果
	// return content;
	// }
	//
	public String getClobToString(String columnLabel) throws IOException,
			SQLException {
		Clob clob = r.getClob(columnLabel);// java.sql.Clob

		return clob.getSubString(1L, (int) clob.length());
		//
		// int i = 0;
		// String detailinfo = "";
		// if(clob != null){
		// InputStream input = clob.getAsciiStream();
		// int len = (int)clob.length();
		// byte by[] = new byte[len];
		// while(-1 != (i = input.read(by, 0, by.length))){
		// input.read(by, 0, i);
		// }
		// detailinfo = new String(by, "UTF-8");
		// }
		// return detailinfo;
	}

	// 将字CLOB转成STRING类型
	public String clobToString(CLOB clob) throws IOException, SQLException {
		try {
			Reader reader = clob.getCharacterStream();
			CharArrayWriter writer = new CharArrayWriter();
			int i = -1;

			while ((i = reader.read()) != -1) {
				writer.write(i);
			}
			// return new String(writer.toCharArray());
			return new String(writer.toCharArray().toString().getBytes(),
					"UTF-8");
		} catch (Exception ex) {
			logger.error("SimpleMessageListener.onMessage(): got exception: ",
					ex);
			throw new RuntimeException(ex);

		}
	}

	// 将字CLOB转成STRING类型
	public String ClobToString(CLOB clob) throws SQLException, IOException {
		Reader reader = clob.getCharacterStream();
		CharArrayWriter writer = new CharArrayWriter();
		int i = -1;

		while ((i = reader.read()) != -1) {
			writer.write(i);
		}
		return new String(writer.toCharArray());
	}

	/**
	 * フォーマットSQL日付と時間
	 * 
	 * @param date
	 * @param pattern
	 *            yyyy-MM-dd HH:mm:ss:ms 爲null時將默認採用該格式yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public String formatSqlDate(java.sql.Date date, String pattern) {
		String pat = pattern;
		if (pattern == null)
			pat = "yyyy-MM-dd HH:mm:ss";
		String dateTime = MessageFormat.format("{0,date," + pat + "}",
				new Object[] { date });
		return dateTime;
	}

	/**
	 * ResultSet.next()
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean next() throws SQLException {
		return r.next();
	}
	
	/** 下一個序列
	 * @param seqName	序列名
	 * @return
	 */
	public long getSequenceNext(String seqName){
		long seq=0;
		String sql="select "+seqName+".NEXTVAL from dual";
		Statement statement =null;
		ResultSet rs=null;
		try {
			statement = con.createStatement();
			rs = statement.executeQuery(sql);
			rs.next();
			seq=rs.getLong(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if(rs!=null){
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(statement!=null){
				try {
					statement.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return seq;
	}

	/**
	 * 測試讀取數據庫後並輸出
	 * 
	 * @param level
	 *            輸出日誌等級
	 */
	public void testReadData(int level) {
		try {
			/*
			 * 查询数据
			 */
			Statement st = con.createStatement();
			st = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,
					ResultSet.CONCUR_READ_ONLY);
			ResultSet r = st
					.executeQuery("SELECT ROWNUM,UTL_INADDR.get_host_name() ,sysdate,user,dbms_random.random"
							+ ",SYS_CONTEXT('USERENV','NLS_DATE_FORMAT') nls_date_format"
							+ ",SYS_CONTEXT('USERENV','NLS_DATE_LANGUAGE') nls_date_language"
							+ ", SYS_CONTEXT('USERENV','NLS_SORT') nls_sort"
							+ ", SYS_CONTEXT('USERENV','NLS_TERRITORY') nls_territory"
							+ ", SYS_CONTEXT('USERENV','NLS_CURRENCY') nls_currency"
							+ ", SYS_CONTEXT('USERENV','NLS_CALENDAR') nls_calendar"
							+ "  FROM dual" + " CONNECT BY LEVEL <= 1");
			r.last();
			logger.debug("行数" + r.getRow());
			r.beforeFirst();// 将光标移动到此 ResultSet 对象的开头，正好位于第一行之前。
			printResultSet(r, level);
			r = st.executeQuery("SELECT SCHEMANAME,username,sid,program FROM V$SESSION WHERE AUDSID = USERENV('SESSIONID') ");
			printResultSet(r, level);
			st.close();
			logger.debug("查询结束!");
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error("ReadDate Error ", ex);
		}
	}
	
	/**ログ出力レベルを設定します
	 * @param level
	 */
	public void setLevel(int level){
		logger.setLevel(level);
	}

	/**
	 * Resultet 遍歷輸出
	 * 
	 * @param r
	 * @throws SQLException
	 */
	public void printResultSet(ResultSet r, int level) throws SQLException {
		ResultSetMetaData rsmd = r.getMetaData();
		int column = rsmd.getColumnCount();
		logger.debug("列数为" + column, level);
		String[] strs = new String[column];
		for (int i = 0; i < rsmd.getColumnCount(); i++) {
			// logger.debug(rsmd.getColumnName(i+1)+"\t");
			// logger.debug(rsmd.getColumnName(i+1));
			strs[i] = rsmd.getColumnName(i + 1);
		}
		while (r.next()) {
			for (int i = 0; i < column; i++) {
				String field = rsmd.getColumnName(i + 1);
				if (rsmd.getColumnType(i + 1) == Types.DATE) {
					Timestamp time = r.getTimestamp(i + 1);
					logger.debug(field + "(TimeStamp)= " + time, level);
					Date date = r.getDate(i + 1);
					logger.debug(field + "(Date)= " + date, level);

					String value = r.getString(i + 1);
					logger.debug(field + "(String)= " + value, level);
				} else {
					String value = r.getString(i + 1);
					// logger.debug(r.getString(i+1)+"\t");
					// logger.debug(r.getString(i+1));
					logger.debug(field + " = " + value, level);
				}
			}
		}
		r.close();

	}

}
