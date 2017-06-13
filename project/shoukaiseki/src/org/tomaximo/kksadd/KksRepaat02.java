package org.tomaximo.kksadd;
//第二步执行的:添加Kks编码时的重复数据的第二个开始进行重新编号,在最后数字编号中进行+1,如果检索本表和主表都无重复编码时写入
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
//第一个执行的,本表编码相同的ERRORBAK

public class KksRepaat02 {

	//统计输出百分比对象
	public static PrintPercent pp=new PrintPercent(true);//输出为控制台模式
	//
	public static  DecimalFormat df=new DecimalFormat("0.0000"); //" "内写格式的模式 如保留2位就用"0.00"即可
	public static  int error = 0;//记录数据更新失败记录
	public static  int endtj=0;//统计总更新数据条数
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
		try 
		{ 
				String ssupername="testtheallbakbak";//主表名,不更改
				
	        	Statement tablebfb = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        	command=
						"select   ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
		    		     "from " +
		    		     ssupername +
		    		     " a " +
		    		    "where exists(select * " +
		    		    	"from " +
		    		    	ssupername+
		    		    	" b " +
		    		       "where b.xggym=a.xggym " +
		      				  "AND a.idbak<>b.idbak)" +
		      			" ORDER BY XGGYM,sbmc";
				System.out.println(command);
				ResultSet ablebr = tablebfb.executeQuery(command);
				ablebr.last();
	  	        int  tablebc=ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0 
	  	        
	  	        pp.setTotal(tablebc);//需要更新的总条数
	        	System.out.println("需要更新的总记录数总共:"+tablebc);
	        	System.out.println("每百分一的条数:"+pp.getPercent());
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

		        int intddcsm =10000;
		        int intddcsmbak=10000;
		        int tablezbbak=0;
		        String xggymbak="";//相关工艺码对比字符
		        String sbmcbak="";
				int theerror=0;
				while (r.next()) {
					Boolean repaat=true;
					int id=r.getInt(10);//id
			        String sbmc = r.getString(2);//设备名称
					String xggym =r.getString(3);//相关工艺码 

//					int theerror = r.getInt(7);//错误标识
					int tablezb = r.getInt(8);//专业表
			        if(!xggym.equals(xggymbak))//比较字符内容
			        {//与上次的相关工艺码不同,而且所在表不同,则编码+1
				        intddcsm++;
			        	intddcsmbak=intddcsm;
			        }else{
			        	String thisxggym=xggym;
			        	Statement thissa = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
			        	while (true) {	
			        		thisxggym=addxggym(thisxggym);					
			        		String thiscommand=
					  	        	"select * " +
					  	        	"from " +
					  	        	ssupername +
					  	        	" WHERE xggym ='"+thisxggym+"' "+
					  	        	" AND  idbak <> "+id;
							ResultSet thisa = thissa.executeQuery(thiscommand);
							thisa.last();
							int thisj=thisa.getRow();
							thisa.close();//更新后关闭此线程,不然更新数据多了就会异常					
				        		thiscommand=
					  	        	"select * " +
					  	        	"from locations WHERE location ='"+thisxggym+"' ";
						  	thisa = thissa.executeQuery(thiscommand);
							thisa.last();
							int thisk=thisa.getRow();
							thisa.close();//更新后关闭此线程,不然更新数据多了就会异常	
				  	        if(thisj==0&&thisk==0){
						    	break;
				  	        }
							
						}
			  	        	thissa.close();//更新后关闭此线程,不然更新数据多了就会异常
							updatexggym(con, ssupername, id, thisxggym);//更新表数据
			        }
					
					xggymbak="";
		        	xggymbak=xggym.substring(0,xggym.length());
		        	sbmcbak="";
		        	if (sbmc!=null){
			        	sbmcbak=sbmc.substring(0,sbmc.length());
		        	}
		        	tablezbbak=tablezb;
		        	
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
	  public static void updatexggym(Connection con,String table,int id,String xggym) 
		throws SQLException
		{
			try {
				String command =
					"UPDATE " +
					table +
					" SET  "
					+"xggym = '"+ xggym+"'"
					+" WHERE   idbak="+id;
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

		public static String addxggym(String xggym) {
			// 相关工艺码+1
			char[] s = xggym.toCharArray();
			String sstart = "";// 相关工艺码的首字符部分
			String snum = "";// 相关工艺码数字部分
			String send = "";// 相关工艺码的末字符部分
			int i = 0;// 相关工艺码编号部分
			int ibits = 0;// 编号部分的位数
			Boolean k = true;
			for (int j = s.length - 1; j >= 0; j--) {
				if (k) {
					if ((int) s[j] >= 48 && (int) s[j] <= 57) {
						int j2 = j;
						for (j2 = j; j2 > 0; j2--) {
							if ((int) s[j2] >= 48 && (int) s[j2] <= 57) {
								snum = s[j2] + snum;
								ibits++;
							} else {
								break;
							}
						}
						k = false;
						j = j2+1;
					} else {
						send = s[j] + send;
					}
				} else {
					sstart = s[j] + sstart;
				}
			}
			i = Integer.parseInt(snum);
			i++;
			snum = "";
			for (int j = 0; j < ibits; j++) {
				snum += "0";
			}
			DecimalFormat df = new DecimalFormat(snum); // " "内写格式的模式
														// 如保留2位就用"0.00"即可
			return sstart + df.format(i) + send;

		}
}

		


