import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.JMXUtils;

public class DruidTest {
	private static String jdbcUrl;
	private static String user;
	private static String password;
	private static String driverClass;
	private static int initialSize = 10;
	private static int minPoolSize = 15;
	private static int maxPoolSize = 30;
	private static int maxActive = 25;
	public static void main(String[] args) throws Exception {
		DruidDataSource dataSource = new DruidDataSource();

		jdbcUrl = "jdbc:oracle:thin:@172.22.48.101:1521:orcl";
		user = "maximo7001";
		password = "maximo";
		driverClass = "oracle.jdbc.driver.OracleDriver";
		JMXUtils.register("com.alibaba:type=DruidDataSource", dataSource);
		dataSource.setInitialSize(initialSize);
		dataSource.setMaxActive(maxActive);
		dataSource.setMinIdle(minPoolSize);
		dataSource.setMaxIdle(maxPoolSize);
		dataSource.setPoolPreparedStatements(true);
		dataSource.setDriverClassName(driverClass);
		dataSource.setUrl(jdbcUrl);
		dataSource.setPoolPreparedStatements(true);
		dataSource.setUsername(user);
		dataSource.setPassword(password);
		dataSource.setValidationQuery("SELECT 'asus' from dual");
		dataSource.setTestOnBorrow(true);

		Connection con = null;
		con = dataSource.getConnection();
		Statement st = con.createStatement();
		ResultSet rs = st.executeQuery("select * from person");
		int cnt = 1;
		System.out.println((cnt++)+". userName");
		rs.close();
		st.close();
		DruidTest to=new DruidTest();
		String string = to.readOracleTestQuartz(con);
		System.out.println(string);
		con.close();
		dataSource.close();
		
	}
	
	
	public String readOracleTestQuartz(Connection con){
		String s="";
		try {
			//取Connection
			String sql="SELECT SCHEMANAME,username,sid,program FROM V$SESSION WHERE AUDSID = USERENV('SESSIONID') ";
			
			Statement st = con.createStatement();
			ResultSet r = st.executeQuery(sql);
			s = printResultSet(r);
			st.close();
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			s=e.getMessage();
		}
		return s;
		
	}

	public String printResultSet(ResultSet r) throws SQLException{
		String s="";
		ResultSetMetaData rsmd=r.getMetaData();
		int column=rsmd.getColumnCount();
		s="列数为"+column+"<br>";
		String[] strs=new String[column];
		for (int i = 0; i < rsmd.getColumnCount() ; i++) {
			//				logger.debug(rsmd.getColumnName(i+1)+"\t");
			//				logger.debug(rsmd.getColumnName(i+1));
			strs[i]=rsmd.getColumnName(i+1);
		}
		while (r.next()) {
			for (int i = 0; i < column; i++) {
				String field=rsmd.getColumnName(i+1);
				String value=r.getString(i+1);
				//					logger.debug(r.getString(i+1)+"\t");
				//					logger.debug(r.getString(i+1));
				s+=field+" = "+value+"<br>";
			}
		}
		r.close();
		
		return s;

	}

	
}
