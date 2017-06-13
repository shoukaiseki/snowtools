import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Properties;

import jxl.demo.Write;

import com.ibm.icu.text.SimpleDateFormat;
import com.shoukaiseki.sql.ReadDatabaseConfigure;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;


public class test {
	static SimpleDateFormat sdf=new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

	public static void main(String[] args) throws SQLException, ParseException {
		read();
		writea();
		
	}
	
	private static void read() throws SQLException {
		// TODO Auto-generated method stub
		ReadDatabaseConfigure rdc=new ReadDatabaseConfigure();
		OracleSqlDetabese osd=null;
		osd = rdc.getOracleSqlDatabese("crpnr","ImpSisann");//101
		osd.setTableRoot("TEST");
		osd.format();
		System.out.println(osd.getSql());
		
		ResultSet rs = osd.executeQuery();
		while(rs.next()){
			System.out.println(rs.getString("TDATE"));
			System.out.println("getTimestamp="+rs.getTimestamp("TDATE"));
		}
		osd.close();
		System.out.println("OK");
		
		
	}

	public static	void writea() throws SQLException, ParseException{
		ReadDatabaseConfigure rdc=new ReadDatabaseConfigure();
		OracleSqlDetabese osd=null;
		osd = rdc.getOracleSqlDatabese("crpnr","ImpSisann");//101
		osd.setTableRootInsert("TEST");
		osd.formatInsert(new String[]{"NAME",  "PERSONID"});
		System.out.println(osd.getSql());
		PreparedStatement prepareStatement = osd.prepareStatement();
//		prepareStatement.setString(1, "");
		
		prepareStatement.setString(2, "2");
		String format = sdf.format(new java.util.Date());
		format = sdf.format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2013-07-06 12:04:10"));
		System.out.println(format);
//		prepareStatement.setTimestamp(1, Timestamp.valueOf("2013-07-06 12:04:10"));
//		prepareStatement.setString(1, "2013-07-06 12:04:10");
//		prepareStatement.setString(1, "2010-13-02 16:50:11");
		prepareStatement.setString(1, "2010-13-02");
//		prepareStatement.setString(1, "03/03/003");
//		prepareStatement.setString(2, "1");
//		prepareStatement.setTimestamp(1, new java.sql.Timestamp(new java.util.Date().getTime()));
		prepareStatement.addBatch();
		prepareStatement.executeBatch();
		osd.getConnection().commit();
		osd.close();
		System.out.println("OK");
		
	}
}
