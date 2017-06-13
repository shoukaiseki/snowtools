import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

import org.apache.log4j.Level;

import com.alibaba.druid.pool.DruidDataSource;
import com.maximo.app.MTException;
import com.maximo.app.MessageOnTerminal;
import com.maximo.app.OutMessage;
import com.maximo.app.config.ReadDruidDataSourceKonnfigu;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;
import com.shoukaiseki.tuuyou.logger.PrintLogs;

/** 对账时候碰到金额差距,但是不知道哪几笔导致的怎么办? 就用该程序
 * @author 蒋カイセキ    Japan-Tokyo  2014年8月3日
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class TestWuziJIez {

	static void lottery(int a[], int start_index, int end_index,
			int needed_balls, Set<Integer> already_chosen) {
		if (needed_balls == 0) {
			System.out.println(already_chosen);
			return;
		}
		for (int i = start_index; i <= end_index - needed_balls + 1; i++) {
			already_chosen.add(a[i]);
			lottery(a, i + 1, end_index, needed_balls - 1, already_chosen);
			already_chosen.remove(a[i]);
		}
	}
	
		static PrintLogs logger=new PrintLogs();
	public static void main(String[] args) throws MTException, SQLException {
		
		logger.setConsoleAppenderLevel(Level.INFO );
		OutMessage om=new MessageOnTerminal();
		ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(om);
		DruidDataSource druid = rddsk.getDruidDataSource("crpower");
		OracleSqlDetabese osd=new OracleSqlDetabese(druid.getConnection()) ;
		
		String where=
				 "((issuetype IN " +
				 "(SELECT value " +
				 "FROM synonymdomain " +
				 "WHERE domainid      ='ISSUETYP' " +
				 "AND maxvalue                                      IN ('TRANSFER') " +
				 ") AND fromstoreloc IS NOT NULL) OR (issuetype NOT IN " +
				 "  (SELECT value " +
				 "  FROM synonymdomain " +
				 "  WHERE domainid='ISSUETYP' " +
				 "  AND maxvalue  ='TRANSFER' " +
				 "  )) OR (issuetype IN " +
				 "  (SELECT value " +
				 "  FROM synonymdomain " +
				 "  WHERE domainid     ='ISSUETYP' " +
				 "  AND maxvalue       ='TRANSFER' " +
				 "  ) AND rotassetnum IS NOT NULL AND tostoreloc IS NOT NULL AND fromstoreloc IS NULL)) AND tostoreloc IN " +
				 "(SELECT location FROM INVENTORY GROUP BY location " +
				 ")"
				;

		osd.setTableRoot("MATRECTRANS");
		osd.setWhere("TRANSDATE BETWEEN to_date('2014-05-28 00:00:00','yyyy-mm-dd hh24:mi:ss')  AND to_date('2014-06-30 00:00:00','yyyy-mm-dd hh24:mi:ss')+1  and siteid='温州电厂'");
		osd.setWhere("siteid='温州电厂'");
		osd.setWhere("TRANSDATE BETWEEN to_date('2014-05-28 00:00:00','yyyy-mm-dd hh24:mi:ss')  AND to_date('2014-06-30 00:00:00','yyyy-mm-dd hh24:mi:ss')+1  and siteid='温州电厂'");
		
		osd.setWhere("TRANSDATE BETWEEN to_date('2014-07-01 00:00:00','yyyy-mm-dd hh24:mi:ss')  AND to_date('2014-07-31 00:00:00','yyyy-mm-dd hh24:mi:ss')+1  and siteid='温州电厂'");
		osd.setWhere("(( TRANSDATE BETWEEN to_date('2014-05-27 00:00:00','yyyy-mm-dd hh24:mi:ss') AND to_date('2014-05-28 00:00:00','yyyy-mm-dd hh24:mi:ss')+1 ) OR ( TRANSDATE BETWEEN to_date('2014-06-20 00:00:00','yyyy-mm-dd hh24:mi:ss') AND to_date('2014-07-01 00:00:00','yyyy-mm-dd hh24:mi:ss')+1 )) AND siteid='温州电厂'");
		osd.setWhere("TRANSDATE BETWEEN to_date('2014-06-25 00:00:00','yyyy-mm-dd hh24:mi:ss')  AND to_date('2014-07-02 00:00:00','yyyy-mm-dd hh24:mi:ss')+1  and siteid='温州电厂'");
		
		where+=" and TRANSDATE BETWEEN to_date('2014-06-25 00:00:00','yyyy-mm-dd hh24:mi:ss')  AND to_date('2014-07-01 00:00:00','yyyy-mm-dd hh24:mi:ss')+1  and siteid='温州电厂'";
		
		
		
		
		osd.setWhere(where);
		osd.format();
		
		System.out.println(osd.getSql());
		
		ResultSet rs = osd.executeQuery();
		int rowCount = osd.getRowCount();
		
		
		Map[] map=new HashMap[rowCount];
		BigDecimal[] bds=new BigDecimal[rowCount];
		int i=0;
		while(rs.next()){
			bds[i]=rs.getBigDecimal("linecost");
			map[i]=new HashMap();
			map[i].put("ID", rs.getString("ITEMNUM"));
			map[i].put("DATE", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(rs.getTimestamp("TRANSDATE")));
			map[i].put("BIGDECIMAL", rs.getBigDecimal("linecost"));
			i++;
		}
		
		osd.close();
		for (int j = 1; j <= rowCount; j++) {
			logger.debug("Adding several data:"+j);
//			lottery(bds, 0, rowCount-1, j, new HashSet<BigDecimal>());
			lottery(map, 0, rowCount-1, j, new HashSet<Map>());
		}
		
		
		
	}
	
//	static BigDecimal jizhunshuju=new BigDecimal(-176362.43);
	static BigDecimal jizhunshuju=new BigDecimal(664823.93);
//	static BigDecimal jizhunshuju=new BigDecimal(190231.43);
	
	
	
	static void lottery(Map a[], int start_index, int end_index,
			int needed_balls, Set<Map> already_chosen) {
		if (needed_balls == 0) {
			StringBuffer sb=new StringBuffer();
			StringBuffer sbItem=new StringBuffer();
			BigDecimal bdhe=new BigDecimal(0D);
			for (Map map : already_chosen) {
				if(map!=null){
					bdhe=bdhe.add((BigDecimal)map.get("BIGDECIMAL"));
					sb.append(map.get("BIGDECIMAL"))
					.append("[").append(map.get("ID")).append("][").append(map.get("DATE")).append("]").append("+");
					
					sbItem.append("'").append(map.get("ID")).append("',");
				}
			}
			sb.append("=").append(bdhe.toString());
//			logger.debug(sb.toString());
			if(compareToAbs(bdhe,"0.01")){
				System.err.println(sb.toString());
				System.err.println(sbItem.toString());
			}
			return;
		}
		for (int i = start_index; i <= end_index - needed_balls + 1; i++) {
			already_chosen.add( a[i]);
			lottery(a, i + 1, end_index, needed_balls - 1, already_chosen);
			already_chosen.remove(a[i]);
		}
	}
	static void lottery(BigDecimal a[], int start_index, int end_index,
			int needed_balls, Set<BigDecimal> already_chosen) {
		if (needed_balls == 0) {
			StringBuffer sb=new StringBuffer();
			BigDecimal bdhe=new BigDecimal(0D);
			for (BigDecimal bigDecimal : already_chosen) {
				if(bigDecimal!=null){
					bdhe=bdhe.add(bigDecimal);
					sb.append(bigDecimal).append("+");
				}
			}
			sb.append("=").append(bdhe.toString());
//			logger.debug(sb.toString());
			if(compareToAbs(bdhe,"0.3")){
				System.err.println(sb.toString());
			}
			return;
		}
		for (int i = start_index; i <= end_index - needed_balls + 1; i++) {
			already_chosen.add(a[i]);
			lottery(a, i + 1, end_index, needed_balls - 1, already_chosen);
			already_chosen.remove(a[i]);
		}
	}
	
	
	public static boolean compareToAbs(BigDecimal bd1,String wucha) {
		// TODO Auto-generated method stub
		if(bd1.abs().compareTo(jizhunshuju.abs().add(new BigDecimal("-"+wucha)))==1
				&&bd1.abs().compareTo(jizhunshuju.abs().add(new BigDecimal(wucha)))==-1){
			return true;
		}
			return false;
			
	}
	
}
