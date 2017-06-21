package org.tomaximo.kksadd;
/*第三步执行的:
 * KKS索引码全部自动生成,取两边空后,取字符第一位到倒数第一个空格,进行查表,如果未有匹配则减少字符数,直至减少到5位,如果还未找到先置空
 * 先查找本表,再查找主表有无匹配
 */
//http://hi.baidu.com/jiangxinyi21/blog/item/556f2a2c98c49930d507421c.html
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.shoukaiseki.math.PrintPercent;

import oracle.jdbc.driver.DatabaseError;

public class KksIndex03 {

	//统计输出百分比对象
	public static PrintPercent pp=new PrintPercent(true);//输出为控制台模式
	//
	public static  int error = 0;//记录数据更新失败记录
	public static  int endtj=0;//统计总更新数据条数
	public static  DecimalFormat df=new DecimalFormat("0.0000"); //" "内写格式的模式 如保留2位就用"0.00"即可
	public static void main(String[] args) 
	{ 
		Connection con=null; 
		Statement sm=null; 
		String command=null; 
		ResultSet rs=null; 
		String tableName=null; 
		String cName=null; 
		String result=null; 
		String url="jdbc:oracle:thin:@127.0.0.1:1521:orcl";
//		mxe.db.driver=oracle.jdbc.OracleDriver
		String user="asus";
		String password="asus";
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
	    char[] carray = new char[1000];
//	    Arrays.fill(carray, "");
	    String s = new String(carray);
		try 
		{ 
				String ssupername="testtheallbakbak";//主表名,不更改
				//百分比
	        	Statement tablebfb = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        	command=
					"select ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
					"from " +
    		     	ssupername +
    		     	" WHERE xggym is not null  "+
					" ORDER BY xggym";
	        	System.out.println(command);
				ResultSet ablebr = tablebfb.executeQuery(command);
				ablebr.last();
	  	        int  tablebc=ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0 

	  	        pp.setTotal(tablebc);//需要更新的总条数
	        	System.out.println("需要更新的总记录数总共:"+tablebc);
	        	//百分比
//				int supernameint=20;//子表编号
				Statement st = con.createStatement();
				
				System.out.println(command);
				ResultSet r = st.executeQuery(command);
				String scsym="";//相关工艺码的父级编号
				error = 0;//记录数据更新失败记录
				endtj=0;//统计总更新数据条数
				
				SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal =Calendar.getInstance();
				java.util.Date data = new java.util.Date();
				cal.setTime(data);//设置时间为当前时间
				long  timeOne=cal.getTimeInMillis();
				System.out.println("开始时间为:"+bartDateFormat.format(data));
				int theerror=0;
				while (r.next()) {
					int idbak=r.getInt(10);//id
					String xggym =r.getString(3);//相关工艺码
					scsym=xggym.substring(0,xggym.length());//上层索引码,先置于于工艺码相同
//					int theerror = r.getInt(7);//错误标识
					int tablezb = r.getInt(8);//专业表
					Boolean theok=false;//如果为真,保留索引名,为假则置空字符
					if(scsym.length()>2){
						scsym=xggym.substring(0,xggym.length()-2);//索引的父值比子值至少小于两位以上
					}
					if(xggym.length()<8)
					{
						scsym=xggym.substring(0,1);//工艺码小于8位则取父值首字母
						theok=true;
					}else{
						Statement thissa = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			  	    	for (int i = 1; scsym.length()>2; i++) { String thiscommand=
				  	        	"select ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
				  	        	"from " +
				  	        	ssupername +
				  	        	" WHERE xggym ='"+scsym+"' ";
//				  	    	System.out.println(thiscommand);
							ResultSet thisa = thissa.executeQuery(thiscommand);
							thisa.last();
				  	        int  thisc=thisa.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0 
				  	      	thisa.close();//更新后关闭此线程,不然更新数据多了就会异常					
			        		thiscommand=
				  	        	"select * " +
				  	        	"from locations WHERE location ='"+scsym+"' ";
			        		thisa = thissa.executeQuery(thiscommand);
			        		thisa.last();
			        		int thisd=thisa.getRow();
			        		thisa.close();//更新后关闭此线程,不然更新数据多了就会异常	
				  	        if(thisc>0){
				  	        	theok=true;
				  	        	break;
				  	        }
				  	        if(thisd>0){
				  	        	theok=true;
				  	        	break;
				  	        }
				  	        
							scsym=xggym.substring(0,xggym.length()-i);
			  	    	}
			  	    	thissa.close();//更新后关闭此线程,不然更新数据多了就会异常
					}
					if (theok==false) {
						scsym="xxxxxxx";
					}
					update(con, ssupername, idbak, scsym, tablezb);//更新表数据
					if(error>0){
						theerror++;
					}else{
						endtj++;
						pp.outPrint(endtj);
					}
				}

				System.out.println("表  "+ssupername+"  更新结束!");
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

	/* 参数为stringLength(字符串,输出总长度)
	 * 对齐方法2,返回只包含空格的字符串
	 */
	public static String stringLength(String s,int count) {
		  String str=s;
		  int j=0;
		  int i=0;
	        if (str!=null) {//字符为空则长度为零
	        	j=length(str);
			}
		  for ( i = 0; i < count-j; i++) {
		   str+=" ";
		  }
		  
		  return str;
	}
	 
	 /** 
	  *   判断一个字符是Ascill字符还是其它字符（如汉，日，韩文字符） 
	  *   @param   char   c,   需要判断的字符 
	  *   @return   boolean,   返回true,Ascill字符 
	  */ 
	  public static boolean isLetter(char c)   { 
		  int k = 0x80; 
		  return c/k == 0?true:false; 
	  } 
	  public static int length(String s){ 
		  char[] c=s.toCharArray(); 
		  int len=0; 
		  for(int i=0;i<c.length;i++) 
		  { 
			  len++; 
			  if(!isLetter(c[i])) 
			  { 
				  len++; 
			  } 
		  } 
		  return   len; 
	  } 
	  //表名,id,azwzm,tablezb
	  public static void update(Connection con,String table,int idbak,String azwzm,int tablezb) 
		throws SQLException
		{
			try {
				String command =
					"UPDATE " +
					table +
					" SET  "
					+"scsym = '"+ azwzm+"' "
					+" WHERE   IDbak="+idbak;
//				System.out.println(command);
				PreparedStatement pst = con.prepareStatement(command);
    	        pst.execute();
    	        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
    	        
    	        
    	         // 输出表信息
//    	        System.out.print(id+"\t\t");
//    	        System.out.print(stringLength(s,60));//加字符总长度为60
//    	        System.out.print(stringLength(xggym,20));
//    	        System.out.print(stringLength(scsym,20));
//    	        System.out.print(theerror+"\t\t");
//    	    	System.out.println(ssupername);
				
			} catch (Exception e) {
				e.printStackTrace();
				error++;
				System.out.println(error);
				// TODO: handle exception
			}
		}
}


