package org.tomaximo;
//��KKSд�����ű�
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

public class KksLocationsid {

	public static  int error = 0;//��¼��ݸ���ʧ�ܼ�¼
	public static  int endtj=0;//ͳ���ܸ����������
	public static  int bfb=0;//ͳ���ܽ�Ȱٷֱ�
	public static  int allbfb=0;//��Ҫ���µ�������
	//locancestor
	public static  int locancestorid=360000;
	//locoper
	public static  int locoperid=260000;
	//lochierarchy
	public static  int lochierarchyid=260000;
	//locstatus
	public static  int locstatusid=260000;
	//locations
	public static  int locationsid=260000;
	
	public static  String allsiteid="����糧";
	public static  String allorgid="LYDC";
	
	public static void main(String[] args) 
	{ 
		DecimalFormat df=new DecimalFormat("0.0000"); //" "��д��ʽ��ģʽ �籣��2λ����"0.00"����
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
				String table="testtheallbakbak";//������,�����

				
	        	Statement tablebfb = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	        	command=
	        			"select   ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
	        			"  from " +
	        			table+
	        			" where tablezb=60 order by xggym";
				System.out.println(command);
				ResultSet ablebr = tablebfb.executeQuery(command);
				ablebr.last();
	  	        int  tablebc=ablebr.getRow(); // ��õ�ǰ�кţ��˴���Ϊ����¼�� ,��ЧʱΪ0 
	        	ablebr.close();
	        	tablebfb.close();
	        	allbfb=tablebc;//��Ҫ���µ�������
	        	bfb = tablebc/100;//ÿ�ٷ�һ������
	        	System.out.println("��Ҫ���µ��ܼ�¼���ܹ�:"+tablebc);
	        	//�ٷֱ�
//				int supernameint=20;//�ӱ���
				Statement st = con.createStatement();
				
				System.out.println(command);
				ResultSet r = st.executeQuery(command);
				error = 0;//��¼��ݸ���ʧ�ܼ�¼
				
				SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Calendar cal =Calendar.getInstance();
				java.util.Date data = new java.util.Date();
				cal.setTime(data);//����ʱ��Ϊ��ǰʱ��
				long  timeOne=cal.getTimeInMillis();
				System.out.println("��ʼʱ��Ϊ:"+bartDateFormat.format(data));

				int theerror=0;
				while(r.next()){
					String sbmc = r.getString(2);//�豸���
					String xggym = r.getString(3);//��ع�����
					String scsym = r.getString(6);//�ϲ�������
					int id=r.getInt(10);//ID
					error=0;
		        	updateLocations(con, table, id, sbmc, xggym,scsym);
		        	updateLocstatus(con, table, id, sbmc, xggym,scsym);
		        	updateLoCoper(con, table, id, sbmc, xggym,scsym);
		        	updateLocHiEraRchy(con, table, id, sbmc, xggym, scsym);
					updateLoCanCesTor(con, table, id, sbmc, xggym, scsym); 
					if(error>0){
						theerror++;
					}else{
						endtj++;
						
						//�ʺ��ڿ���̨����,��ʾ�ٷֱ�START
						float a=(float)endtj/allbfb*100;
						String b="��ǰ���Ϊ:       ["+String.valueOf(df.format(a))+"%]  "+String.valueOf(endtj);
						for (int k = 0; k < b.length(); k++) {
							System.out.print( "\b\b\b\b");
						}
					    System.out.print( "\b\b" + b +"  ");
					  //�ʺ��ڿ���̨����END
					    
					    //�ʺ���Eclipse������
//						if(endtj%bfb==0&&endtj/bfb<101){
//							System.out.println("��ǰ���Ϊ: "+endtj/bfb+"%");//ÿһ���ٷֵ���ʾһ�ν��
//						}
					}
				}
				
				

				System.out.println("��  "+table+"  ���½���!");
				System.out.println("һ�����³ɹ�"+endtj+"��!");
				System.out.println("һ������ʧ��"+theerror+"��!");
				
				data = new java.util.Date();
				cal.setTime(data);//����ʱ��
				long  timeTwo=cal.getTimeInMillis();
				long  daysapart = (timeTwo-timeOne)/(1000*60);//����
				long  daysaparts = (timeTwo-timeOne)/1000%60;//����������ȡ����60������Ϊ����
				System.out.println("����ʱ��Ϊ:"+bartDateFormat.format(data)); 
				System.out.println("������ʱ��Ϊ"+daysapart+"��"+daysaparts+"��");

//				System.out.println("�س������!");
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

	  //����,id,azwzm,tablezb
	  public static void updateLocations(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command="";
		  locationsid++;
			try {
					command =
					"insert into " +
					"locations" +
					 " (" +
					 "Location," +
					 "Description," +
					 "Type," +
					 "ChangeBy," +
					 "ChangeDate," +
					 "Disabled," +
					 "SiteId," +
					 "Orgid," +
					 "Isdefault," +
					 "Status," +
					 "LoCatIOnSid," +
					 "UseInPopr," +
					 "Langcode," +
					 "Hasld," +
					 "Autowogen," +
					 "StatusDate," +
					 "RowStamp" +
					 " ) values( "  +			//�������
					 "'"+xggym +"',"+			//Location
					 "'"+sbmc +"',"+		//Description
					 "'OPERATING'," +				//Type
					 "'MAXADMIN'," +			//ChangeBy
					 "TO_DATE( '2011/11/27 16:16:16', 'YYYY-MM-DD HH24:MI:SS')," +		//ChangeDate
					 "0," +			//Disabled
					 "'"+allsiteid+"'," +			//SiteId
					 "'"+allorgid+"'," +				//Orgid
					 "0," +			//Isdefault	
					 "'OPERATING'," +			//Status
					 locationsid+"," +		//LoCatIOnSidΪ�ռ���
					 "0," +			//UseInPopr
					 "'ZH'," +			//Langcode
					 "0," +				//Hasld
					 "0," +			//Autowogen
					 "TO_DATE( '2011/11/27 16:16:16', 'YYYY-MM-DD HH24:MI:SS')," +		//StatusDate
					 "null )";			//RowStampΪ�ռ���
//				System.out.println(command);
				PreparedStatement pst = con.prepareStatement(command);
  	        pst.execute();
  	        pst.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}

	  public static void updateLocstatus(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command="";
		  locstatusid++;
			try {
				command =
					"insert into " +
					"LocstAtUs" +
					 " (" +
					  "Location,"+
					  "Status,"+
					  "ChangeBy,"+
					  "ChangeDate,"+
					  "Memo,"+
					  "SiteId,"+
					  "Orgid,"+
					  "LocStatusId,"+
					  "RowStamp"+
					 " ) values( "  +			
					 "'"+xggym+"',"+		//Location
					  "'OPERATING',"+		//Status
					  "'MAXADMIN',"+		//ChangeBy
					  "TO_DATE( '2011/11/27 16:16:16', 'YYYY-MM-DD HH24:MI:SS')," +	//ChangeDate
					  "null,"+			//Memo Ϊ�ռ���	
						 "'"+allsiteid+"'," +			//SiteId
						 "'"+allorgid+"'," +				//Orgid
						 locstatusid+ ","+		//LocStatusIdֵΪ�ռ���
					 "null )";			//RowStampΪ�ռ���
//				System.out.println(command);
				PreparedStatement pst = con.prepareStatement(command);
				pst.execute();
				pst.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}

	  public static void updateLoCoper(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command ="";
		  locoperid++;
			try {
				command =
					"insert into " +
					"LoCoper" +
					 " (" +
					 "Location," +
					 "SiteId," +
					 "Orgid," +
					 "locoperid"+
					 " ) values( "  +			//�������
					 "'"+xggym +"',"+			//Location
					 "'"+allsiteid+"'," +			//SiteId
					 "'"+allorgid+"'," +				//Orgid
					 locoperid+//locoperid
					 " )";			//
//				System.out.println(command);
				PreparedStatement pst = con.prepareStatement(command);
				pst.execute();
				pst.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}

	  public static void updateLocHiEraRchy(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command ="";
		  lochierarchyid++;
		  int 	Children=0;//���ӱ�ʶ,���XGGYMΪ��ֵ,��Ϊ0,��ֵ��Ϊ1
			try {
				Statement thissa = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
	  	        String thiscommand=
	  	        	"select ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
	  	        	"from " +
	  	        	table +
	  	        	" WHERE scsym ='"+xggym+"' ";
//	  	      System.out.println(thiscommand);
				ResultSet thisa = thissa.executeQuery(thiscommand);
				thisa.last();
	  	        int  thisc=thisa.getRow(); // ��õ�ǰ�кţ��˴���Ϊ����¼�� ,��ЧʱΪ0 
//	  	      System.out.println(thisc);
	  	        if(thisc>0){
	  	        	Children=1;
	  	        }
	  	        thisa.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
	  	        thissa.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
				
				
				command =
					"insert into " +
					"LocHiEraRchy" +
					 " (" +
					 "Location," +
					 "Parent,"+
					 "SystemId,"+
					 "Children,"+		//���ӱ�ʶ,���XGGYMΪ��ֵ,��Ϊ0,��ֵ��Ϊ1
					 "SiteId," +
					 "Orgid," +
					 "lochierarchyid"+
					 " ) values( "  +			//�������
					 "'"+xggym +"',"+			//Location
					 "'"+scsym +"',"+
					 "'PRIMARY',"+
					 Children+","+
					 "'"+allsiteid+"'," +			//SiteId
					 "'"+allorgid+"'," +				//Orgid
					 lochierarchyid+			//lochierarchyid
					 " )";			//
//				System.out.println(command);
				PreparedStatement pst = con.prepareStatement(command);
				pst.execute();
				pst.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}

	  public static void updateLoCanCesTor(Connection con,String table,int id,String sbmc,String xggym,String scsym) 
		throws SQLException
		{
		  String command ="";
		  String ancestor=xggym;//��ع����������,��һ��д��ĵ��������ع�������ͬ
			try {
				while(1==1){
					  locancestorid++;
					command =
						"insert into " +
						"LoCanCesTor" +
						 " (" +
						 "Location," +
						 "Ancestor,"+
						 "SystemId,"+
						 "SiteId," +
						 "Orgid," +
						 "locancestorid"+
						 " ) values( "  +			//�������
						 "'"+xggym +"',"+			//Location
						 "'"+ancestor +"',"+
						 "'PRIMARY',"+
						 "'"+allsiteid+"'," +			//SiteId
						 "'"+allorgid+"'," +				//Orgid
						 locancestorid+			//locancestorid
						 " )";			//
//					System.out.println(command);
					PreparedStatement pst = con.prepareStatement(command);
			        pst.execute();
			        pst.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
					
					Statement thissa = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
		  	        String thiscommand=
		  	        	"select ID,SBMC,XGGYM,AZWZM,DDCSM,SCSYM,ERROR,TABLEZB,ERRORBAK,IDBAK " +
		  	        	"from " +
		  	        	table +
		  	        	" WHERE xggym ='"+ancestor+"' ";
//		  	      	System.out.println(thiscommand);
					ResultSet thisa = thissa.executeQuery(thiscommand);
					thisa.last();
		  	        int  thisc=thisa.getRow(); // ��õ�ǰ�кţ��˴���Ϊ����¼�� ,��ЧʱΪ0 
		  	        String thisscsym=thisa.getString(6);
		  	        thisa.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
		  	        thissa.close();//���º�رմ��߳�,��Ȼ������ݶ��˾ͻ��쳣
//		  	      	System.out.println(thisc);
		  	        if(thisc==0){
		  	        	break;
		  	        }
		  	        ancestor=thisscsym.substring(0,thisscsym.length());
		  	        if(ancestor.equals("����糧")){
		  	        	break;
		  	        }
		  	        if(ancestor.equals(xggym)){
		  	        	System.out.println("������ѭ��,�ϲ�����������ع�������ͬ!   "+ancestor);
			  	      	System.out.println(thisc);
		  	        }
				}
			} catch (Exception e) {
				System.out.println(command);
				error++;
				e.printStackTrace();
				System.out.println(error);
				// TODO: handle exception
			}
		}
}


