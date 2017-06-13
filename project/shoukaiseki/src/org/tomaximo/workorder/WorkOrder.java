package org.tomaximo.workorder;
//工件票
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

public class WorkOrder {

	public static  int error = 0;//记录数据更新失败记录
	public static  int endtj=0;//统计总更新数据条数
	public static  int bfb=0;//统计总进度百分比
	public static  int allbfb=0;//需要更新的总条数
	public static  DecimalFormat df=new DecimalFormat("0.0000"); //" "内写格式的模式 如保留2位就用"0.00"即可
	public static  DecimalFormat format = new DecimalFormat("000");//输出整数格式化方式001,002

	public static  int bianhao=0;//重新编号
	public static  String neirong="";//内容去首尾空后重新写入
	public static  String weix="";//危险类型,内容去首尾空后重新写入
	public static  String xgzy="";//相关专业,内容去首尾空后重新写入
	
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
		      			" WHERE KIGENN IS NOT NULL ORDER BY ID";
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

				bianhao=0;
		        String sszykigennbak="";//相关工艺码对比字符
		        int xuhao=0;//序号
		        String sszykigenn="";//所属专业+预防类型编码
				while (r.next()) {
					int id=r.getInt(1);//id
					neirong=r.getString(3) ;//内容去首尾空后重新写入
					if(neirong!=null){
						neirong=neirong.trim();//字段不为空时去首尾空
					}
			        String kuigenn = r.getString(4);//预防类型
					String sszy =r.getString(5);//所属专业


					Statement thistable=con.createStatement();
					String thiscommand="SELECT * FROM NEIRONG WHERE BIANHAO ='" +
					kuigenn+"' OR BIANHAO='"+sszy+"'";
					ResultSet thisr=thistable.executeQuery(thiscommand);
					weix="";
					xgzy="";
					while(thisr.next()){
						String j=thisr.getString(2);
						String t=thisr.getString(3);
						if(j.equals(kuigenn)){
							weix=t;
							if(t!=null){
								weix=weix.trim();
							}
						}
						if(j.equals(sszy)){
							xgzy=t;
							if(t!=null){
								xgzy=xgzy.trim();
							}
						}
					}
					thisr.close();
					thistable.close();
					sszykigenn=sszy+kuigenn;
					xuhao++;
					bianhao++;//重新编号
//					System.out.println(sszykigenn+"=="+sszykigennbak);
			        if(!sszykigenn.equals(sszykigennbak))//比较字符内容
			        {
			        	xuhao=1;//与上次的所属专业和危险分类不同则序号置1
			        }
			        sszykigennbak="";
			        sszykigennbak=sszykigenn.substring(0,sszykigenn.length());
			        sszykigenn=sszykigenn+"C"+format.format(xuhao);//写入的值
							update(con, ssupername, id, sszykigenn);//更新表数据
			        }
					

				System.out.println("表  "+ssupername+"  更新结束!");
				System.out.println("一共更新成功"+endtj+"次!");
				System.out.println("一共更新失败"+error+"次!");
				
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
	  public static void update(Connection con,String table,int id,String xuhao) 
		throws SQLException
		{
		  String command ="";
			try {
				command =
					"UPDATE " +
					table +
					" SET  id= "+bianhao
					+",neirong='"+neirong+"'"
					+",weix='"+weix+"'"
					+",xgzy='"+xgzy+"'"
					+",xuhao = '"+ xuhao+"'"
					+" WHERE   id="+id;
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
				endtj++;//统计总更新数据条数
				

				//适合在控制台运行,显示百分比START
				float a=(float)endtj/allbfb*100;
				String b="当前进度为:       ["+String.valueOf(df.format(a))+"%]  "+String.valueOf(endtj);
				for (int k = 0; k < b.length(); k++) {
					System.out.print( "\b\b\b\b");
				}
			    System.out.print( "\b\b" + b +"  ");
			  //适合在控制台运行END
			    
			    //适合在Eclipse下运行
//				if(bfb!=0)
//				{
//					if(endtj%bfb==0&&endtj/bfb<101){
//						System.out.println("当前进度为: "+endtj/bfb+"%");//每一个百分点显示一次进度
//					}
//				}
			} catch (Exception e) {
				e.printStackTrace();
				error++;
				System.out.println(command);
				System.out.println(error);
				// TODO: handle exception
			}
		}
}


