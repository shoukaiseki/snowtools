package com.tomaximo;




/*	last方法使用 时需要构造器,执行下面这句即可
Statement sa = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
 *   Statement = Connection.createStatement(int resultSetType,int resultSetConcurrency)
resultSetType(结果集类型)包括:
    ResultSet.TYPE_FORWARD_ONLY 该常量指示光标只能向前移动的 ResultSet 对象的类型。
    ResultSet.TYPE_SCROLL_INSENSITIVE 该常量指示可滚动但通常不受 ResultSet 底层数据更改影响的 ResultSet 对象的类型。
    ResultSet.TYPE_SCROLL_SENSITIVE 该常量指示可滚动并且通常受 ResultSet 底层数据更改影响的ResultSet 对象的类型。
resultSetConcurrency(并发类型)包括:
    ResultSet.CONCUR_READ_ONLY 该常量指示不可以更新的 ResultSet 对象的并发模式。
    ResultSet.CONCUR_UPDATABLE 该常量指示可以更新的 ResultSet 对象的并发模式。
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class KksZfq {

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
			 * 查询数据
			 */
			String tablename="testtheall";//主表名,不更改
//			int supernameint=20;//子表编号
			
			String ssupername="";//H0LK的父级名,如果
	      Statement st = con.createStatement();
	      ResultSet r = st.executeQuery(
	    		  "select DISTINCT azwzm " +
	    		     "from " +
	    		     tablename +
	      			" ");
	        String scsym="";//相关工艺码的父级编号
	        int error = 0;//记录数据更新失败记录
	        int endtj=0;//统计总更新数据条数
	        int allint=0;//统计总数据数
	        int nosuper=0;//无父值的数据条数
	      while (r.next()) {
	    	//
	    	  allint++;
		    scsym=r.getString(1);//上层索引码,相关工艺码的父级编号
	        try {
	  	        Statement sa = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	  	        String aasString=
	  	    		  "select xggym " +
	  	    		     "from " +
	  	    		     tablename +
	  	      			" where xggym = '"+scsym+"'";
	  	      	ResultSet b = sa.executeQuery(aasString);
	  	      	
	  	      	b.last();
	  	        int  c=b.getRow(); // 获得当前行号：此处即为最大记录数 ,无效时为0  
//	  	      System.out.println("c= "+c);
	  	      if (c>0) {
	  	    	  if(!b.getString(1).equals(scsym)){
	  	    		  System.out.println(b.getString(1).equals(scsym)+"\t\t"+scsym);
	  	    		  System.out.println("无父值的a: "+scsym+"\t\t"+allint);
	  	    		  nosuper++;
		  	    		noSuper(con,nosuper,scsym) ;
	  	    		  
	  	    	  }
	  	      }else {
  	    		  System.out.println("b无父值的: "+scsym+"\t\t"+allint);
  	    		  nosuper++;
	  	    		noSuper(con,nosuper,scsym) ;
	  	      }
  	    	  b.close();
  	    	  sa.close();
    	        
   	         // 输出表信息
//   	        System.out.print(id+"\t\t");
//   	        System.out.print(stringLength(s,60));//加字符总长度为60
//   	        System.out.print(stringLength(xggym,20));
//   	        System.out.print(stringLength(scsym,20));
//   	        System.out.print(theerror+"\t\t");
//   	    	System.out.println(ssupername);
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
			System.out.println("无父值的数据一共有"+nosuper+"次!");
			
//			System.out.println("回车键继续!");
//			Scanner scan=new Scanner(System.in); 
//		    int number1=scan.nextInt(); 
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
	  
	  public static void noSuper(Connection con,int id,String xggym) 
	  			throws SQLException
	  {
		  PreparedStatement pst = con.prepareStatement(
					 "insert into " +
					 "nosuper" +
					 "(ID," +
					 "XGGYM" +
			 		 ") values(?,?)");
			 pst.setInt(1, id);
			 pst.setString(2,xggym);
	    	 pst.execute();
	    	 pst.close();//更新后关闭此线程,不然更新数据多了就会异常
	  }
}


