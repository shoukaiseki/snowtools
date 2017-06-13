package com.tomaximo.workorder;
//工作票,写入到3个表,关联内容
//select * from HAZARD ;
//select * from HAZARDPREC;
//select * from PRECAUTION ;
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

public class WorkOrderall {
 
	public static  int error = 0;//记录数据更新失败记录
	public static  int endtj=0;//统计总更新数据条数
	public static  int bfb=0;//统计总进度百分比
	public static  int allbfb=0;//需要更新的总条数
	public static  DecimalFormat df=new DecimalFormat("0.0000"); //" "内写格式的模式 如保留2位就用"0.00"即可
	public static  DecimalFormat format = new DecimalFormat("0000");//输出整数格式化方式001,002


	public static  String neirong="";
	
	public static  int precautionid=0;//precaution
	public static  int precautionrowstamp=0;//rowstamp
	public static  int hazardprecid=0;//hazardprec
	public static  int hazardprecrowstamp=0;//rowstamp
	
	public static  String allsiteid="里彦电厂";
	public static  String allorgid="LYDC";
	
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
				String ssupername="testworkorder";//主表名,不更改


	        	Statement tablebfb = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        	command=
						"select   * " +
		    		     "from " +
		    		     ssupername +
		      			" WHERE KIGENN IS NOT NULL ORDER BY NEIRONG,KIGENN";
				System.out.println(command);
				ResultSet ablebr = tablebfb.executeQuery(command);
				ablebr.last();
	  	        int  tablebc=ablebr.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0 
	        	allbfb=tablebc;//需要更新的总条数
	        	bfb = tablebc/100;//每百分一的条数
	        	System.out.println("需要更新的总记录数总共:"+tablebc);
	        	System.out.println("每百分一的条数:"+bfb);
	        	//百分比
//				int supernameint=20;//子表编号
				Statement st = con.createStatement();
				
				System.out.println(command);
				ResultSet r = st.executeQuery(command);

				error = 0;//记录数据更新失败记录
				endtj=0;//统计总更新数据条数
				
				SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal =Calendar.getInstance();
				java.util.Date data = new java.util.Date();
				cal.setTime(data);//设置时间为当前时间
				long  timeOne=cal.getTimeInMillis();
				System.out.println("开始时间为:"+bartDateFormat.format(data));

				
		        String neirongbak="";//内容比较文本
				String kigennbak="";
		        int xuhao=1000;//序号
		        String testxuhao="";//所属专业+预防类型编码
				int theerror=0;
				while (r.next()) {
//					int id=r.getInt(1);//id
					neirong=r.getString(3) ;//内容去首尾空后重新写入
			        String kigenn = r.getString(4);//预防类型
//					String sszy =r.getString(5);//所属专业

					
//					System.out.println(sszykigenn+"=="+sszykigennbak);
			        if(neirong.equals(neirongbak)&&kigenn.equals(kigennbak)){
			        	continue;//如果内容危险类型都相同则跳过此次循环
			        }
			        if(!neirong.equals(neirongbak))//比较字符内容
			        {
			        	xuhao++;//与上次的内容不同则序号+1,而且插入到表PRECAUTION
			        	updatePrecaution(con, xuhao);
			        }
			        kigennbak="";
			        kigennbak=kigenn.substring(0,kigenn.length());
			        neirongbak="";
			        neirongbak=neirong.substring(0,neirong.length());
			        testxuhao=format.format(xuhao);//写入的值
			        updateHazardprec(con, kigenn, xuhao);//向表插入数据

					if(error>0){
						theerror++;
					}else{
						endtj++;
						
						//适合在控制台运行,显示百分比START
						float a=(float)endtj/allbfb*100;
						String b="当前进度为:       ["+String.valueOf(df.format(a))+"%]  "+String.valueOf(endtj);
						for (int k = 0; k < b.length(); k++) {
							System.out.print( "\b\b\b\b");
						}
					    System.out.print( "\b\b" + b +"  ");
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

	
	  //表名,id,azwzm,tablezb
	  public static void updateHazardprec(Connection con,String kigenn,int xuhao) 
		throws SQLException
		{
		  String command ="";
		  hazardprecid++;
		  hazardprecrowstamp++;
			try {
				command =
					"insert into HAZARDPREC(" +
					"HAZARDID," +
					"PRECAUTIONID," +
					"ORGID," +
					"SITEID," +
					"HAZARDPRECID," +
					"ROWSTAMP"+
					")values("
					+"'"+kigenn+"',"		//:HAZARDID,	//危险类型名
					+"'"+xuhao+"',"			//:PRECAUTIONID,//预防措施新编号
					+"'"+allorgid+"',"  	//:ORGID,
					+"'"+allsiteid+"',"  	//:SITEID,
					+"'"+hazardprecid+"',"  	//:HAZARDPRECID,
					+hazardprecrowstamp
					+")";
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
				System.out.println(command);
				System.out.println(error);
				// TODO: handle exception
			}
		}

//表名,id,azwzm,tablezb
public static void updatePrecaution(Connection con,int xuhao) 
	throws SQLException
	{
	  String command ="";
	  precautionid++;
	  precautionrowstamp++;
		try {
			command =
				"insert into PRECAUTION(" +
				"PRECAUTIONID," +
				"THISDESCRIPTION," +
				"PREC10," +
				"ORGID," +
				"SITEID," +
				"PRECAUTIONUID," +
				"LANGCODE," +
				"HASLD," +
				"ROWSTAMP"+
				")values("
				+"'"+xuhao+"',"			//:PRECAUTIONID,//预防措施新编号
				+"'"+neirong+"',"  		//:DESCRIPTION,//内容
				+"'"+0+"',"  			//:PREC10,
				+"'"+allorgid+"',"  	//:ORGID,
				+"'"+allsiteid+"',"  	//:SITEID,
				+"'"+precautionid+"',"   //:PRECAUTIONUID,
				+"'ZH',"  				//:LANGCODE,
				+"'0',"					//:HASLD
				+precautionrowstamp
				+")";
//			System.out.println(command);
			PreparedStatement pst = con.prepareStatement(command);
	        pst.execute();
	        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
	        
	        
	         // 输出表信息
//	        System.out.print(id+"\t\t");
//	        System.out.print(stringLength(s,60));//加字符总长度为60
//	        System.out.print(stringLength(xggym,20));
//	        System.out.print(stringLength(scsym,20));
//	        System.out.print(theerror+"\t\t");
//	    	System.out.println(ssupername);

		} catch (Exception e) {
			e.printStackTrace();
			error++;
			System.out.println(command);
			System.out.println(error);
			// TODO: handle exception
		}
	}
}


