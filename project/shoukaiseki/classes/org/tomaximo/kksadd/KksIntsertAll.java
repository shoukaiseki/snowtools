package org.tomaximo.kksadd;
//将KKS写入五张表
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.shoukaiseki.math.PrintPercent;


public class KksIntsertAll {


	public static Boolean mode=false;//调试输出
	//统计输出百分比对象
	public static PrintPercent pp=new PrintPercent(true);//输出为控制台模式
	//
	
	public static  int error = 0;//记录数据更新失败记录
	public static  int endtj=0;//统计总更新数据条数
	public static  int bfb=0;//统计总进度百分比
	public static  int allbfb=0;//需要更新的总条数
	//locancestor
	public static  int locancestorid=0;
	public static  int locancestorrowstamp=0;
	//locoper
	public static  int locoperid=0;
	public static  int locoperrowstamp=0;
	//lochierarchy
	public static  int lochierarchyid=0;
	public static  int lochierarchyrowstamp=0;
	//locstatus
	public static  int locstatusid=0;
	public static  int locstatusrowstamp=0;
	//locations
	public static  int locationsid=0;
	public static  int locationsrowstamp=0;
	
	public static  String allsiteid="里彦电厂";
	public static  String allorgid="LYDC"; 
	
	public static void main(String[] args) 
	{ 
		DecimalFormat df=new DecimalFormat("0.0000"); //" "内写格式的模式 如保留2位就用"0.00"即可
		Connection con=null; 
		Statement sm=null; 
		String command=null; 
		ResultSet rs=null; 
		String tableName=null; 
		String cName=null; 
		String result=null; 
		String url="jdbc:oracle:thin:@10.37.74.165:1521:lymis";
//		mxe.db.driver=oracle.jdbc.OracleDriver
		String user="maximo";
		String password="maximo"; 
//		String url="jdbc:oracle:thin:@127.0.0.1:1521:orcl";
////		mxe.db.driver=oracle.jdbc.OracleDriver
//		String user="asus";
//		String password="asus";
		BufferedReader input=new BufferedReader(new InputStreamReader(System.in)); 
		try 
		{ 
			//（1）装载并注册数据库的JDBC驱动程序    
			//载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			System.out.println("驱动程序已加载"); 
			//注册JDBC驱动：有些地方可不用此句
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, user, password);
			System.out.println("OK,成功连接到数据库"); 
			
		}
		catch(Exception ex)
		{ 
			ex.printStackTrace(); 
		} 
		try 
		{ 
				
				setIdRowstamp(con);
//				for (int i = 1; i > 0; i++) {
//					System.out.print("\b\b\b\b\b\b\b\b\b\b\b"+i);
//				}
			
				String table="testtheallbakbak";//主表名,不更改

				
	        	Statement tablebfb = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        	command=
	        			"select   ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
	        			"  from " +
	        			table+
	        			" where xggym is not null order by xggym";
				System.out.println(command);
				ResultSet ablebr = tablebfb.executeQuery(command);
				ablebr.last();
	  	        int  tablebc=ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0 
	        	ablebr.close();
	        	tablebfb.close();
	        	allbfb=tablebc;//需要更新的总条数
	        	
	        	pp.setTotal(tablebc);//需要更新的总条数
	        	System.out.println("需要更新的总记录数总共:"+tablebc);
	        	//百分比
//				int supernameint=20;//子表编号
				Statement st = con.createStatement();
				
				System.out.println(command);
				ResultSet r = st.executeQuery(command);
				error = 0;//记录数据更新失败记录
				
				SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal =Calendar.getInstance();
				java.util.Date data = new java.util.Date();
				cal.setTime(data);//设置时间为当前时间
				long  timeOne=cal.getTimeInMillis();
				System.out.println("开始时间为:"+bartDateFormat.format(data));

				int theerror=0;
				while(r.next()){
					String sbmc = r.getString(2);//设备名称
					String xggym = r.getString(3);//相关工艺码
					String scsym = r.getString(6);//上层索引码
					int id=r.getInt(10);//ID
					error=0;
		        	updateLocations(con, table, id, sbmc, xggym,scsym);
		        	updateLocstatus(con, table, id, sbmc, xggym,scsym);
		        	updateLoCoper(con, table, id, sbmc, xggym,scsym);
		        	updateLocHiEraRchy(con, table, id, sbmc, xggym, scsym);
					updateLoCanCesTor(con, table, id, sbmc, xggym, scsym); 
					if(error>0){
						theerror++;
					}else{
						endtj++;
						pp.outPrint(endtj);
					}
				}
				
				

				System.out.println("表  "+table+"  更新结束!");
				System.out.println("一共更新成功"+endtj+"次!");
				System.out.println("一共更新失败"+theerror+"次!");
				
				data = new java.util.Date();
				cal.setTime(data);//设置时间
				long  timeTwo=cal.getTimeInMillis();
				long  daysapart = (timeTwo-timeOne)/(1000*60);//分钟
				long  daysaparts = (timeTwo-timeOne)/1000%60;//获得总秒数后取除以60的余数即为秒数
				System.out.println("结束时间为:"+bartDateFormat.format(data)); 
				System.out.println("共花费时间为"+daysapart+"分"+daysaparts+"秒");

//				System.out.println("回车键继续!");
//				Scanner scan=new Scanner(System.in); 
//		   	 	int number1=scan.nextInt(); 
				r.close();
				st.close();
		      	con.close(); 
		}
	    		catch(Exception ex)
	    		{ 
	    			ex.printStackTrace(); 
	    		} 
		}

	  //表名,id,azwzm,tablezb
	  public static void updateLocations(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command="";
		  locationsid++;
		  locationsrowstamp++;
			try {
					command =
					"insert into " +
					"locations" +
					 " (" +
					 "Location," +
					 "Description," +
					 "Type," +
					 "ChangeBy," +
					 "ChangeDate," +
					 "Disabled," +
					 "SiteId," +
					 "Orgid," +
					 "Isdefault," +
					 "Status," +
					 "LoCatIOnSid," +
					 "UseInPopr," +
					 "Langcode," +
					 "Hasld," +
					 "Autowogen," +
					 "StatusDate," +
					 "RowStamp" +
					 " ) values( "  +			//插入数据
					 "'"+xggym +"',"+			//Location
					 "'"+sbmc +"',"+		//Description
					 "'OPERATING'," +				//Type
					 "'MAXADMIN'," +			//ChangeBy
					 "TO_DATE( '2011/11/27 16:16:16', 'YYYY-MM-DD HH24:MI:SS')," +		//ChangeDate
					 "0," +			//Disabled
					 "'"+allsiteid+"'," +			//SiteId
					 "'"+allorgid+"'," +				//Orgid
					 "0," +			//Isdefault	
					 "'OPERATING'," +			//Status
					 locationsid+"," +		//LoCatIOnSid为空即可
					 "0," +			//UseInPopr
					 "'ZH'," +			//Langcode
					 "0," +				//Hasld
					 "0," +			//Autowogen
					 "TO_DATE( '2011/11/27 16:16:16', 'YYYY-MM-DD HH24:MI:SS')," +		//StatusDate
					 "'" +locationsrowstamp+"'"+
					 " )";			//RowStamp为空即可
					if(mode){
						System.out.println(command+";");
					}
				PreparedStatement pst = con.prepareStatement(command);
  	        pst.execute();
  	        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}

	  public static void updateLocstatus(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command="";
		  locstatusid++;
		  locstatusrowstamp++;
			try {
				command =
					"insert into " +
					"LocstAtUs" +
					 " (" +
					  "Location,"+
					  "Status,"+
					  "ChangeBy,"+
					  "ChangeDate,"+
					  "Memo,"+
					  "SiteId,"+
					  "Orgid,"+
					  "LocStatusId,"+
					  "RowStamp"+
					 " ) values( "  +			
					 "'"+xggym+"',"+		//Location
					  "'OPERATING',"+		//Status
					  "'MAXADMIN',"+		//ChangeBy
					  "TO_DATE( '2011/11/27 16:16:16', 'YYYY-MM-DD HH24:MI:SS')," +	//ChangeDate
					  "null,"+			//Memo 为空即可	
						 "'"+allsiteid+"'," +			//SiteId
						 "'"+allorgid+"'," +				//Orgid
						 locstatusid+ ","+		//LocStatusId值为空即可
					 "'"+locstatusrowstamp+"'"+//RowStamp
					 ")";			
				if(mode){
					System.out.println(command+";");
				}
				PreparedStatement pst = con.prepareStatement(command);
				pst.execute();
				pst.close();//更新后关闭此线程,不然更新数据多了就会异常
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}

	  public static void updateLoCoper(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command ="";
		  locoperid++;
		  locoperrowstamp++;
			try {
				command =
					"insert into " +
					"LoCoper" +
					 " (" +
					 "Location," +
					 "SiteId," +
					 "Orgid," +
					 "locoperid," +
					 "rowstamp"+
					 " ) values( "  +			//插入数据
					 "'"+xggym +"',"+			//Location
					 "'"+allsiteid+"'," +			//SiteId
					 "'"+allorgid+"'," +				//Orgid
					 locoperid+//locoperid
					 ",'"+locoperrowstamp+"'"+	//locoperrowstamp
					 " )";			//
				if(mode){
					System.out.println(command+";");
				}
				PreparedStatement pst = con.prepareStatement(command);
				pst.execute();
				pst.close();//更新后关闭此线程,不然更新数据多了就会异常
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}

	  public static void updateLocHiEraRchy(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command ="";
		  lochierarchyid++;
		  lochierarchyrowstamp++;
		  int 	Children=0;//父子标识,如果XGGYM为子值,则为0,父值则为1
			try {
				Statement thissa = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	  	        String thiscommand=
	  	        	"select ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
	  	        	"from " +
	  	        	table +
	  	        	" WHERE scsym ='"+xggym+"' ";
				if(mode){
					System.out.println(thiscommand+";");
				}
				ResultSet thisa = thissa.executeQuery(thiscommand);
				thisa.last();
	  	        int  thisc=thisa.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0 
	  	        thisa.close();//更新后关闭此线程,不然更新数据多了就会异常
	  	        
        		thiscommand=
	  	        	"select location,parent " +
	  	        	"from lochierarchy WHERE parent ='"+xggym+"' ";
    			if(mode){
    				System.out.println(thiscommand+";");
    			}
        		thisa.last();
        		int thisd=thisa.getRow();
	  	        thisa.close();//更新后关闭此线程,不然更新数据多了就会异常
	  	        if(thisc>0||thisd>0){
	  	        	Children=1;
	  	        }
	  	        thissa.close();//更新后关闭此线程,不然更新数据多了就会异常
				
				
				command =
					"insert into " +
					"LocHiEraRchy" +
					 " (" +
					 "Location," +
					 "Parent,"+
					 "SystemId,"+
					 "Children,"+		//父子标识,如果XGGYM为子值,则为0,父值则为1
					 "SiteId," +
					 "Orgid," +
					 "lochierarchyid," +
					 "rowstamp"+
					 " ) values( "  +			//插入数据
					 "'"+xggym +"',"+			//Location
					 "'"+scsym +"',"+
					 "'PRIMARY',"+
					 Children+","+
					 "'"+allsiteid+"'," +			//SiteId
					 "'"+allorgid+"'," +				//Orgid
					 lochierarchyid+			//lochierarchyid
					 ",'"+lochierarchyrowstamp+"'"+//lochierarchyrowstamp
					 " )";			//
				if(mode){
					System.out.println(command+";");
				}
				PreparedStatement pst = con.prepareStatement(command);
				pst.execute();
				pst.close();//更新后关闭此线程,不然更新数据多了就会异常
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}

	  public static void updateLoCanCesTor(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command ="";
		  String ancestor=xggym;//相关工艺码的索引,第一次写入的的索引跟相关工艺码相同
			try {
				while(1==1){
					  locancestorid++;
					  locancestorrowstamp++;
					command =
						"insert into " +
						"LoCanCesTor" +
						 " (" +
						 "Location," +
						 "Ancestor,"+
						 "SystemId,"+
						 "SiteId," +
						 "Orgid," +
						 "locancestorid," +
						 "rowstamp"+
						 " ) values( "  +			//插入数据
						 "'"+xggym +"',"+			//Location
						 "'"+ancestor +"',"+
						 "'PRIMARY',"+
						 "'"+allsiteid+"'," +			//SiteId
						 "'"+allorgid+"'," +				//Orgid
						 locancestorid+			//locancestorid
						 ",'"+locancestorrowstamp+"'"+
						 " )";			//
					if(mode){
						System.out.println(command);
					}
					PreparedStatement pst = con.prepareStatement(command);
			        pst.execute();
			        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
					
					Statement thissa = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  	        String thiscommand=
		  	        	"select ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
		  	        	"from " +
		  	        	table +
		  	        	" WHERE xggym ='"+ancestor+"' ";
					if(mode){
						System.out.println(thiscommand);
					}
					ResultSet thisa = thissa.executeQuery(thiscommand);
					thisa.last();
		  	        int  thisc=thisa.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0 
		  	        String thisscsym="";
		  	        if(thisc!=0){
			  	        thisscsym=thisa.getString(6);
			  	        ancestor=thisscsym.substring(0,thisscsym.length());
		  	        }
		  	        thisa.close();//更新后关闭此线程,不然更新数据多了就会异常	
		  	        if(thisc==0){
		        		thiscommand=
			  	        	"select location,parent " +
			  	        	"from lochierarchy WHERE location ='"+ancestor+"' ";
						if(mode){
							System.out.println(thiscommand);
						}
		        		thisa = thissa.executeQuery(thiscommand);
		        		thisa.last();
		        		int thisd=thisa.getRow();
		        		if(thisd!=0){
			        		thisscsym=thisa.getString(2);
				  	        ancestor=thisscsym.substring(0,thisscsym.length());
		        		}
		        		thisa.close();//更新后关闭此线程,不然更新数据多了就会异常
		        		if(thisd==0){
		        			break;
		        		}
		  	        }
		  	        thissa.close();//更新后关闭此线程,不然更新数据多了就会异常

		  	        
		  	        if(ancestor.equals("里彦电厂")){
		  	        	break;
		  	        }
		  	        if(ancestor.equals(xggym)){
		  	        	System.out.println("进入死循环,上层索引码与相关工艺码相同!   "+ancestor);
			  	      	System.out.println(thisc);
		  	        }
				}
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}
	  public static void setIdRowstamp(Connection con) throws SQLException{
		  String thiscommand="";
		  try {
			//locancestor
			Statement sql = con.createStatement();
  	        thiscommand=
  	        	"select locancestorid from " +
  	        	"locancestor order by locancestorid desc  ";
			System.out.println(thiscommand+";");
			ResultSet rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				locancestorid=rs.getInt(1);
			}
			thiscommand=
  	        	"select rowstamp from " +
  	        	"locancestor order by rowstamp desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				locancestorrowstamp=rs.getInt(1);
			}
			//locoper
			thiscommand=
  	        	"select locoperid from " +
  	        	"locoper order by locoperid desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				locoperid=rs.getInt(1);
			}
			thiscommand=
  	        	"select rowstamp from " +
  	        	"locoper order by rowstamp desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				locoperrowstamp=rs.getInt(1);
			}
			//lochierarchy
			thiscommand=
  	        	"select lochierarchyid from " +
  	        	"lochierarchy order by lochierarchyid desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				lochierarchyid=rs.getInt(1);
			}
			thiscommand=
  	        	"select rowstamp from " +
  	        	"lochierarchy order by rowstamp desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				lochierarchyrowstamp=rs.getInt(1);
			}
			//locstatus
			thiscommand=
  	        	"select locstatusid from " +
  	        	"locstatus order by locstatusid desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				locstatusid=rs.getInt(1);
			}
			thiscommand=
  	        	"select rowstamp from " +
  	        	"locstatus order by rowstamp desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				locstatusrowstamp=rs.getInt(1);
			}
			//locations
			thiscommand=
  	        	"select locationsid from " +
  	        	"locations order by locationsid desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				locationsid=rs.getInt(1);
			}
			thiscommand=
  	        	"select rowstamp from " +
  	        	"locations order by rowstamp desc  ";
			System.out.println(thiscommand+";");
			rs = sql.executeQuery(thiscommand);
			if(rs.next()){
				locationsrowstamp=rs.getInt(1);
			}
			System.out.println("locancestor 表的locancestorid,locancestorrowstamp的起始值为 " +
					locancestorid+","+locancestorrowstamp);
			//locancestor
			//locoper
			System.out.println("locoper 表的locoperid,locoperrowstamp的起始值为 " +
					locoperid+","+locoperrowstamp);
			//lochierarchy
			System.out.println("lochierarchy 表的lochierarchyid,lochierarchyrowstamp的起始值为 " +
					lochierarchyid+","+lochierarchyrowstamp);
			//locstatus
			System.out.println("locstatus 表的locstatusid,locstatusrowstamp的起始值为 " +
					locstatusid+","+locstatusrowstamp);
			//locations
			System.out.println("locations 表的locationsid,locationsrowstamp的起始值为 " +
					locationsid+","+locationsrowstamp);
		  } catch (Exception e) {
			  System.out.println(thiscommand);
				// TODO: handle exception
		}
	  }
}


