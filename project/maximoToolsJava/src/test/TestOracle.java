package test;

import java.sql.*;

public class TestOracle {
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
