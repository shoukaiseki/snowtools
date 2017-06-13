package org.tomaximo;
//装45个表导入一个总表
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class KksUse5 {

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
			/*
			 * 插入数据
			 */
//			 PreparedStatement pst = con.prepareStatement(
//	          "insert into test12(ID,SBMC) values(?,?)");
//				System.out.println("要5");
//	      pst.setString(1, "131");
//			System.out.println("要4");
//
//			System.out.println("要2");
////	      pst.setCharacterStream(2,
////	                             new InputStreamReader(new ByteArrayInputStream(s.
////	          getBytes())), s.length());
//	      pst.setString(2,"有机会");
//
//	      //pst.setString(2,s);  //用此句则异常
//	      pst.execute();

			/*
			 * 查询数据
			 */
			String tableallname="TESTTHEALLBAKBAK";//主表名,不更改
//			int supernameint=20;//子表编号
			
			//从第一张表开始
			for(int supernameint=1;supernameint<46;supernameint++)
			{
			String tablename=Tablename(supernameint);//取子表名
			String ssupername=""+supernameint;//H0LK的父级名,如果
	      Statement st = con.createStatement();
	      ResultSet r = st.executeQuery(
	    		  "select * " +
	    		     "from " +
	    		     tablename +
	      			" ORDER BY ID");
	        String scsym="";//相关工艺码的父级编号
	        int error = 0;//记录数据更新失败记录
	        int endtj=0;//统计总更新数据条数
	      while (r.next()) {
	    	//
	    	int id=r.getInt(1);//id
	        s = r.getString(2);//设备名称
		    String xggym =r.getString(3);//相关工艺码
		    scsym=r.getString(4);//上层索引码,相关工艺码的父级编号
		    int theerror = r.getInt(7);//错误标识

	        try {
				 PreparedStatement pst = con.prepareStatement(
						 "insert into " +
						 tableallname +
						 "(ID," +
						 "SBMC," +
						 "XGGYM," +
						 "AZWZM," +
						 "ERROR," +
				 		 "TABLEZB) values(?,?,?,?,?,?)");
				 pst.setInt(1, id);
				 pst.setString(2, s);
				 pst.setString(3, xggym);
				 pst.setString(4, scsym);
				 pst.setInt(5, theerror);
				 pst.setString(6, ssupername);
		    	      //pst.setString(2,s);  //用此句则异常
		    	        pst.execute();
		    	        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
		    	        
		    	         // 输出表信息
//		    	        System.out.print(id+"\t\t");
//		    	        System.out.print(stringLength(s,60));//加字符总长度为60
//		    	        System.out.print(stringLength(xggym,20));
//		    	        System.out.print(stringLength(scsym,20));
//		    	        System.out.print(theerror+"\t\t");
//		    	    	System.out.println(ssupername);
		    	        endtj++;//统计总更新数据条数
			} catch (Exception e) {
				e.printStackTrace(); 
				
				error++;
				System.out.println(error);
				// TODO: handle exception
			}
	      	}

			System.out.println("表  "+tablename+"  更新结束!");
			System.out.println("一共更新成功"+endtj+"次!");
			System.out.println("一共更新失败"+error+"次!");
			System.out.println("此表的编码为: "+supernameint);
			
//			System.out.println("回车键继续!");
//			Scanner scan=new Scanner(System.in); 
//		    int number1=scan.nextInt(); 
		      r.close();
		      st.close();
			}
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
	  /** 
	  *   得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1 
	  *   @param   String   s   ,需要得到长度的字符串 
	  *   @return   int,   得到的字符串长度 
	  */ 
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
		public static String Tablename(int ssupername)
		{
		 	String kyo="";
			switch(ssupername)
			{				
				case 1:
					kyo="testkyjxt";
					break;
				case 2:
					kyo="test56dcc";//#5#6电除尘机械部分KKS编码表
					break;
				case 3:
					kyo="test14chz";	
					break;
				case 4:
					kyo="test12tl";
					break;
				case 5:
					kyo="test34tl";
					break;
				case 6:
					kyo="test56shsss";
					break;
				case 7:
					kyo="test3qj";
					break;
				case 8:
					kyo="test4qj";
					break;
				case 9:
					kyo="test34qjgy";
					break;
				case 10:
					kyo="test5qj";
					break;
				case 11:
					kyo="test6qj";
					break;
				case 12:
					kyo="test56qjgy";
					break;
				case 13:
					kyo="test34hx";
					break;
				case 14:
					kyo="test56hx";
					break;
				case 15:
					kyo="testwscl";
					break;
				case 16:
					kyo="testsm";
					break;
				case 17:
					kyo="testsm1";
					break;
				case 18:
					kyo="test12dcc";//#1#2电除尘机械部分KKS编码表
					break;
				case 19:
					kyo="test34dcc";//#3#4电除尘机械部分KKS编码表
					break;
				case 20:
					kyo="test34glall";//3、4锅炉专业KKS编码汇总表
					break;
				case 21:
					kyo="test34glall4";
					break;
				case 22:
					kyo="test34glallgg";
					break;
				case 23:
					kyo="test56gl";
					break;
				case 24:
					kyo="test56gl5";
					break;
				case 25:
					kyo="test56glgg";
					break;
				case 26:
					kyo="test56glall";
					break;
				case 27:
					kyo="test56glallgys";
					break;
				case 28:
					kyo="testrg34glall";
					break;
				case 29:
					kyo="testrg34glall4";
					break;
				case 30:
					kyo="testrg34glallgg";
					break;
				case 31:
					kyo="testrg34gl";
					break;
				case 32:
					kyo="testrg34gl3";
					break;
				case 33:
					kyo="testrg34glgg";
					break;
				case 34:
					kyo="testrghxdccpd";
					break;
				case 35:
					kyo="testrghxdccpd34scl";
					break;
				case 36:
					kyo="testrghxdccpd56scl";
					break;
				case 37:
					kyo="testrghxdccpd34hxsq";
					break;
				case 38:
					kyo="testrghxdccpd5dcc";
					break;
				case 39:
					kyo="testrghxdccpd6dcc";
					break;
				case 40:
					kyo="testrghxdccpdpd";
					break;
				case 41:
					kyo="testrglyrgbm";
					break;
				case 42:
					kyo="testrglyrgbm6dzj";
					break;
				case 43:
					kyo="testrglyrgbm5jz";
					break;
				case 44:
					kyo="testrglyrgbm4djz";
					break;
				case 45:
					kyo="testrglyrgbm3djz";
					break;
			}
			return kyo;
		}
}


