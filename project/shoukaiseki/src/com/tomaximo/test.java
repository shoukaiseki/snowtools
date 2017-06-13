package com.tomaximo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class test {

	public static  int error = 0;//��¼��ݸ���ʧ�ܼ�¼
	public static  int endtj=0;//ͳ���ܸ����������
	public static  int bfb=0;//ͳ���ܽ�Ȱٷֱ�
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
			//��1��װ�ز�ע����ݿ��JDBC�����    
			//����JDBC��oracle��װĿ¼�µ�jdbc\lib\classes12.jar
			Class.forName("oracle.jdbc.driver.OracleDriver"); 
			System.out.println("������Ѽ���"); 
			//ע��JDBC����Щ�ط��ɲ��ô˾�
			java.sql.DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, user, password);
			System.out.println("OK,�ɹ����ӵ���ݿ�"); 
			
		}
		catch(Exception ex)
		{ 
			ex.printStackTrace(); 
		} 
		try 
		{ 
				String ssupername="testtheallbakbak";//������,�����

	        	Statement tablebfb = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        	command=
	        			"select * from testtheallbakbak " +
	        			"a where xggym LIKE '4%' AND " +
	        			" exists (select * from testtheallbakbak b where b.xggym = a.xggym " +
	        			" and a.id <> b.id and xggym is not null) order by xggym,sbmc";
				System.out.println(command);
				int  tablebc=1;
				while(tablebc>0){
					ResultSet ablebr = tablebfb.executeQuery(command);
					ablebr.last();
		  	         tablebc=ablebr.getRow(); // ��õ�ǰ�кţ��˴���Ϊ����¼�� ,��ЧʱΪ0 
		        	System.out.println("��Ҫ���µ��ܼ�¼���ܹ�:"+tablebc);
		        	  try
		              {
		                Thread.sleep(10000);//������
		                 } 
		               catch(InterruptedException e)
		               {
		                System.out.println("�����쳣!");
		               }
		            
		        	ablebr.close();
				}
	        	tablebfb.close();
		      	con.close(); 
		}
	    		catch(Exception ex)
	    		{ 
	    			ex.printStackTrace(); 
	    		} 
		}
}


