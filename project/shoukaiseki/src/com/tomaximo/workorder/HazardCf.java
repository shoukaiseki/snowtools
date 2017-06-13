package com.tomaximo.workorder;
/*删除重复的Hazard表中的数据,并重新编号
 *删除Description字段首尾空
 *注意:须将Hazard表关联的数据插入testworkorder表中,不然会出现之后关联错误
*/
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

public class HazardCf {

	public static  int error = 0;//记录数据更新失败记录
	public static  int endtj=0;//统计总更新数据条数
	public static  int bfb=0;//统计总进度百分比
	public static  int allbfb=0;//需要更新的总条数
	public static  DecimalFormat df=new DecimalFormat("0.0000"); //" "内写格式的模式 如保留2位就用"0.00"即可
	public static  DecimalFormat format = new DecimalFormat("0000");//输出整数格式化方式001,002


	public static  String neirong="";
	
	public static  int precautionid=1;//precaution
	public static  int hazardprecid=1;//hazardprec
	public static  String allsiteid="里彦电厂";
	public static  String allorgid="LYDC";
	
	public static void main(String[] args)  
	{ 
		Connection con=null; 
		String command=null; 
		String url="jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		String user="asus";
		String password="asus";
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
				String ssupername="HAZARD";//主表名,不更改


	        	Statement tablebfb = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        	command=
						"select   * " +
		    		     "from " +
		    		     ssupername +
		      			" WHERE hazardid IS NOT NULL ORDER BY hazardid";
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

				Boolean thisupdate;
		        int xuhao=5000;//序号
				int theerror=0;
				while (r.next()) {
					neirong=r.getString(2) ;//内容去首尾空后重新写入
			        String kigenn = r.getString(1);//预防类型,编号
//					String sszy =r.getString(5);//所属专业
			        thisupdate=true;
			        
			        Statement thistable = con.createStatement();
		        	String thiscommand="" +
		        	"select   * " +
	    		     "from " +
	    		     ssupername +
	      			" WHERE HAZARDID!='"+kigenn+
	      			"' AND DESCRIPTION ='" +neirong+
	      			"' ORDER BY hazardid";
		        		;
//						System.out.println(thiscommand);
		        	ResultSet thisr = thistable.executeQuery(thiscommand);
		        		while(thisr.next()){

		        			String thiskigenn=thisr.getString(1);//预防类型,编号
		        			int xuh=Integer.parseInt(thiskigenn.substring(1,thiskigenn.length()));
		        			int xuha=Integer.parseInt(kigenn.substring(1,kigenn.length()));
//							System.out.println("updateTestworkorder  xuh="+xuh+"  xuhao="+xuha);
		        			if(xuha>xuh){
		        				updateTestworkorder(con, thiskigenn,kigenn );
//								System.out.println("updateTestworkorder");
		        				deleteHAZARD(con, kigenn);
//								System.out.println("deleteHAZARD");
		        				thisr.close();
		        				thistable.close();
		        				thisupdate=false;
		        				break;
		        			}
		        		}
		        	if(kigenn.equals("K5888")){
		        		thisupdate=false;//K5888的编号不更新
		        		System.out.println("/n"+kigenn+"/n");
		        	}
		        	if(thisupdate){
		        		//更新标志为真,需要更新
						xuhao++;
						thisr.close();
						thistable.close();
				        updateHazard(con, kigenn, xuhao);//向表插入数据
				        updateTestworkorder(con, "K"+xuhao,kigenn );//更新更改后的到workorder表
		        	}

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
	  public static void updateHazard(Connection con,String kigenn,int xuhao) 
		throws SQLException
		{
		  String command ="";
		  hazardprecid++;
			try {
				command =
					"update HAZARD SET HAZARDID='K" +
					xuhao +"' WHERE HAZARDID='" +kigenn+"'";
//				System.out.println(command);
				PreparedStatement pst = con.prepareStatement(command);
    	        pst.execute();
    	        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
    	        
			} catch (Exception e) {
				e.printStackTrace();
				error++;
				System.out.println(command);
				System.out.println(error);
				// TODO: handle exception
			}
		}

//表名,id,azwzm,tablezb
public static void updateTestworkorder(Connection con,String xuhao,String xuh) 
	throws SQLException
	{
	  String command ="";
		try {
			command =
				"UPDATE TESTWORKORDER SET KIGENN='" +
				xuhao+"' WHERE KIGENN='" +xuh+
				"'";
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
			System.out.println(command);
			error++;
			System.out.println(error);
			// TODO: handle exception
		}
	}
public static void deleteHAZARD(Connection con,String xuhao) 
throws SQLException
{
  String command ="";
	try {
		command =
			"DELETE FROM  HAZARD  WHERE HAZARDID='" +xuhao+"'";
//		System.out.println(command);
		PreparedStatement pst = con.prepareStatement(command);
        pst.execute();
        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
        

         // 输出表信息
//        System.out.print(id+"\t\t");
//        System.out.print(stringLength(s,60));//加字符总长度为60
//        System.out.print(stringLength(xggym,20));
//        System.out.print(stringLength(scsym,20));
//        System.out.print(theerror+"\t\t");
//    	System.out.println(ssupername);

	} catch (Exception e) {
		e.printStackTrace();
		System.out.println(command);
		error++;
		System.out.println(error);
		// TODO: handle exception
	}
}
}


