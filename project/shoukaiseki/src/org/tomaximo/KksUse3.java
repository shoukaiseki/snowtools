package org.tomaximo;
/*	批量使用的
 * 	相关工艺码相同的自动编号
 * 	
192.168.2.166:1521:orcl
String user="asus";
String password="asus";



 CREATE TABLE testrglyrgbm  --创建表
  (
  Id int,          --Id 列的数据类型是 int，包含整数。
  sbmc varchar(255),  --数据类型是 varchar，最大长度为 255 个字符。
  xggym varchar(255),  
  azwzm varchar(255),
  ddcsm varchar(255),
  scsym varchar(255),
  error int
  );
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class KksUse3 {

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
			String tablename="testkyjxt";//表名
	      Statement st = con.createStatement();
	      ResultSet r = st.executeQuery(
	    		  "select * " +
	    		     "from " +
	    		     tablename +
	    		     " a " +
	    		    "where exists(select * " +
	    		    	"from " +
	    		    	tablename+
	    		    	" b " +
	    		       "where b.xggym=a.xggym " +
	      				  "AND a.id<>b.id)" +
	      			"ORDER BY xggym");

	        int intddcsm =10000;
	        int intddcsmbak=10000;
	        String xggymbak="";//相关工艺码对比字符
	        String sbmcbak="";
	        int error = 0;//记录数据更新失败记录
	        int endtj=0;//统计总更新数据条数
	      while (r.next()) {
	    	//
	    	int id=r.getInt(1);
	        s = r.getString(2);
	    	if (s==null) {
	    		s ="";
			}
	        String xggym =r.getString(3).replaceAll(" ", "");//去空格常用replaceAll来替换空格
	    	if (xggym==null) {
	    		xggym ="";
			}
	        String ddcsm =r.getString(7);
	    	if (ddcsm==null) {
	    		ddcsm ="";
			}
	        if(!xggym.equals(xggymbak))//比较字符内容
	        {
//	        	System.out.println(xggym+"\t"+xggymbak+"\t"+xggym.equals(xggymbak));
		        intddcsm++;
//		        System.out.println("编码加一");
	        }
	    	intddcsmbak=intddcsm;
	        if (s.equals(sbmcbak)) {
		        intddcsmbak=intddcsm+100000;
			}
	        try {
		        PreparedStatement pst = con.prepareStatement(
		            	"UPDATE " +
		            	tablename +
		            	" SET error = "+intddcsmbak+",ID ="+id+" WHERE   ID="+id);
		    	      //pst.setString(2,s);  //用此句则异常
		    	        pst.execute();
		    	        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
		    	        /*
		    	         * 输出xggym对比信息
		    	         */
//		    	        System.out.println("写入完成\t"
//		    	        		+stringLength(xggym,40)//字符长度为40
//		    	        		+stringLength(xggymbak,40)//字符长度为40
//		    	        		+xggym.equals(xggymbak)+"\t"+intddcsm);
		    	        /*
		    	         * 输出表信息
		    	         */
		    	        System.out.print(id+"\t\t");
		    	        System.out.print(stringLength(s,60));//加字符总长度为60
		    	        System.out.print(stringLength(xggym,20));
		    	        System.out.println(ddcsm+"\t"+intddcsmbak);
		    	        endtj++;//统计总更新数据条数
			} catch (Exception e) {
				e.printStackTrace(); 
				error++;
				// TODO: handle exception
			}
			xggymbak="";
        	xggymbak=xggym.substring(0,xggym.length());
        	sbmcbak="";
        	sbmcbak=s.substring(0,s.length());
	      	}

	      r.close();
	      st.close();
	      con.close(); 
			System.out.println("表  "+tablename+"  更新结束!");
			System.out.println("一共更新成功"+endtj+"次!");
			System.out.println("一共更新失败"+error+"次!");
			
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
}


