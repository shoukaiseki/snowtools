import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.zip.ZipFile;

import com.alibaba.druid.pool.DruidDataSource;
import org.maximo.app.MTException;
import org.maximo.app.MessageOnTerminal;
import org.maximo.app.OutMessage;
import org.maximo.app.config.ReadDruidDataSourceKonnfigu;
import org.maximo.tools.impxml.task.Table;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;


public class TestDetabesu {
	public static void main(String[] args) throws Exception {
		System.out.println(new DecimalFormat("0000.0").parse("1.0"));;
		System.out.print(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));
		System.out.println(Table.BSHIMPORT);
		test1();
	}
	
	private static void test1() throws Exception {
		// TODO Auto-generated method stub
		OutMessage om=new MessageOnTerminal();
		ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(om);
		String refName = "localhost_gbkorcl";
//		refName = "crpxz_101_shoukaiseki";
		refName = "crpxz_maximo6";
		DruidDataSource druid = rddsk.getDruidDataSource(refName);
		OracleSqlDetabese osd = new OracleSqlDetabese(druid.getConnection());
		osd.setSql("select * from test where a=? and b=?");
		osd.setSql("select * from HAZARD where DESCRIPTION=:DESCRIPTION and HAZARDTYPE=:HAZARDTYPE and HAZ01=:HAZ01 and (DESCRIPTION is not null and HAZARDTYPE is not null and HAZ01 is not null)");
		osd.setSql("select sysdate from dual");
		PreparedStatement ps = osd.prepareStatement();
//		ps.setObject(1, null);
//		ps.setObject(2, null);
//		ps.setObject(1, "坠落风险");
//		ps.setObject(2, "高空坠落");
//		ps.setObject(3, "公用");
		
		
		ResultSet rs = ps.executeQuery();
		if (rs.next()) {
			om.info("true");
		} else {
			om.info("false");
		}
		osd.close();
		druid.close();
	}

}
