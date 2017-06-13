package org.tomaximo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class KksUse {
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
			String tablename="test";
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

	        int intddcsm =100;
//	        String ddcsm = String.valueOf(intddcsm);//整数转换为字符串
//	        String ddcsm =Integer.toString(intddcsm);//整数转换为字符串
	        String ddcsm =""+intddcsm;//整数转换为字符串
			System.out.println(ddcsm);
	        String xggymbak="";//相关工艺码对比字符
	        int error = 0;//记录数据更新失败记录
	        
	      while (r.next()) {
	    	//
	    	int id=r.getInt(1);
	        s = r.getString(2);
	        String xggym =r.getString(3);
//	        ddcsm = String.valueOf(intddcsm);//整数转换为字符串
//	        ddcsm =Integer.toString(intddcsm);//整数转换为字符串
	        ddcsm =""+intddcsm;
	        if(!xggym.equals(xggymbak))//比较字符内容
	        {
//	        	System.out.println(xggym+"\t"+xggymbak+"\t"+xggym.equals(xggymbak));//调试专用
		        intddcsm++;
		        ddcsm =""+intddcsm;
//		        System.out.println("编码加一");//调试专用
	        }
	        try {
		        PreparedStatement pst = con.prepareStatement(
		            	"UPDATE " +
		            	tablename +
		            	" SET DDCSM = "+ddcsm+",ID ="+id+" WHERE   ID="+id);
		    	      //pst.setString(2,s);  //用此句则异常
		    	        pst.execute();
		    	        pst.close();//更新后关闭此线程,不然更新数据多了就会异常
		    	        /*
		    	         * 对齐
		    	         */
//		    	        System.out.println("写入完成\t"+xggym+"\t"+xggymbak+"\t"+xggym.equals(xggymbak)+"\t"+intddcsm);
		    	        System.out.println("写入完成\t");
		    	        
		    	        System.out.println(xggym+"\t"+xggymbak+"\t"+xggym.equals(xggymbak)+"\t"+intddcsm);
				
			} catch (Exception e) {
				e.printStackTrace(); 
				error++;
				// TODO: handle exception
			}
			xggymbak="";
        	xggymbak=xggym.substring(0,xggym.length());
	      	}

	      r.close();
	      st.close();
	      con.close(); 
			System.out.println("查询结束!");
			System.out.println("一共更新失败"+error+"次!");
			
		}
		
		
		catch(Exception ex)
		{ 
			ex.printStackTrace(); 
		} 
	}

	private static String toString(int intddcsm) {
		// TODO Auto-generated method stub
		return null;
	} 
}


