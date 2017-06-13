package com.tomaximo.repoto;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.shoukaiseki.file.excel.ExcelStaticHouhou;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;
import com.shoukaiseki.sql.oracle.SqlFormat;

/**解析Excel 
 * @author 蒋カイセキ    Japan-Tokyo  2012-5-24
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class ExcelKaiketuToOngxt {
	
	static Connection con = null;
	static Statement sm = null;
	static ResultSet rs = null;
	static String tableName = null;
	static String cName = null;
	static String result = null;
	static String url = "jdbc:oracle:thin:@127.0.0.1:1521:orcl";
	// private String url = "jdbc:oracle:thin:@10.59.67.22:1521:test22";
	static String driver = "oracle.jdbc.driver.OracleDriver";
	static String user = "maximo75";
	static String password = "maximo75";
	OracleSqlDetabese osd =null;
	String[] source={"DG30","AA31","AI31","BJ31","BR31","CS31","DA31","DD31","DE31","DF31","DG31","AA32","AI32","BJ32","BR32","CS32","DA32","DD32","DE32","DF32","DG32","AA33","AI33","BJ33","BR33","CS33","DA33","DD33","DE33","DF33","DG33","AA34","AI34","CS34","DA34","DD34","DE34","DF34","DG34","BJ35","BR35","CS35","DA35","DD35","DE35","DF35","DG35","AA37","AI37","BJ37","BR37","CS37","DA37","DD37","DE37","DF37","DG37","AA39","AI39","BJ39","BR39","CS39","DA39","AA41","AI41","BJ41","BR41","CS41","DA41","DD41","DE41","DF41","DG41","AA43","AI43","BJ43","BR43","CS43","DA43","DE43","DG43","AA46","AI46","BJ7","BR7","CS7","DA7","DD7","DE7","DF7","DG7","AA8","AI8","BJ8","BR8","CS8","DA8","DD8","DE8","DE2","DG2","DD9","DE9","DF9","DG9","AA10","AI10","BJ10","BR10","CS10","AA5","AI5","BR5","CS5","DA5","AA6","AI6","BJ6","BR6","CS6","DA6","DD6","DE6","DF6","DG6","AA7","AI7","BR28","DD28","DE28","DF28","DG28","AA29","AI29","BR29","CS29","DA29","DD29","DE29","DF29","DG29","AA30","AI30","BJ30","BR30","CS30","DA30","DD30","DE30","DF30","DF15","DG15","AA16","AI16","BJ16","BR16","CS16","DA16","DD16","DE16","DF16","DG16","AA17","DF8","DG8","AA9","AI9","BJ9","BR9","CS9","DA9","DA10","DD10","DE10","DF10","DG10","AA11","AI11","BJ11","BR11","CS11","DA11","DD11","DE11","DF11","DG11","AA12","DF22","DG22","AA23","AI23","BJ23","BR23","DD23","DE23","DF23","DG23","AA24","AI24","BJ24","BR24","DD24","DE24","DF24","DG24","AA25","AI25","BJ25","BR25","CS25","DA25","DD25","DE25","DF25","DG25","AA26","AI26","BJ26","BR26","CS26","DA26","DD26","DE26","DF26","DG26","BJ27","BR27","CS27","DA27","DD27","DE27","DF27","DG27","AA28","AI28","BJ28","AI17","BJ17","BR17","CS17","DA17","DD17","DE17","DF17","DG17","AA18","AI18","BR18","CS18","DA18","DD18","DE18","DF18","DG18","AA19","AI19","BJ19","BR19","CS19","DA19","DD19","DE19","DF19","DG19","AA20","AI20","BJ20","BR20","CS20","DA20","DD20","DE20","DF20","DG20","AA21","AI21","AI12","BJ12","BR12","CS12","DA12","DD12","DE12","DF12","DG12","AA13","AI13","BJ13","BR13","CS13","DA13","DD13","DE13","DF13","DG13","AA14","AI14","BJ21","BR21","CS21","DA21","DD21","DE21","DF21","DG21","AA22","AI22","BJ22","BR22","CS22","DA22","DD22","DE22","BJ46","BR46","CS46","DA46","CS47","DA47","AA48","AI48","AA49","AI49","AA50","AI50","AA51","AI51","AA52","AI52","AA53","AI53","BJ14","BR14","CS14","DA14","DD14","DE14","DF14","DG14","AA15","AI15","BJ15","BR15","CS15","DA15","DD15","DE15"};
	public ExcelKaiketuToOngxt() throws ClassNotFoundException, SQLException {
		// TODO Auto-generated constructor stub

		url = "jdbc:oracle:thin:@10.59.67.22:1521:test22";
		user = "orclzhjq";
		password = "orclzhjq";
		url = "jdbc:oracle:thin:@127.0.0.1:1521:maximo75";
		user = "luyang";
		password = "luyang";
		url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.6)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.7)(PORT=1521)))(CONNECT_DATA=(SERVER=dedicated)(SERVICE_NAME=eam)))";
		user = "maximo";
		password = "maximo";
		
		
        String fileName = "C:/Documents and Settings/Administrator/桌面/レポート(repoto)[报告][报表]/maiNitiRepotoKeikatu.voom";
//        fileName = "C:/Documents and Settings/Administrator/桌面/レポート(repoto)[报告][报表]/maiGetuRepotoKeikatu.voom";
		PropertiesLoadTest(fileName);
		
		osd = new OracleSqlDetabese(url, user, password, driver);
		source=code.split(",");
		
		String s = "SUM(D51:AH51)";
		s="SUM(D31:AH31)";
		s="AA32+AA33";
		
		
		SqlFormat sqlFormat = osd.setTableRoot("SHOUKAISEKI_ONGXT_BIRT");
		sqlFormat.setWhere(pwhere);
		ResultSet rs = osd.executeQuery(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
		
		rs=osd.executeQueryUpdate();
		while(rs.next()){
			String from = rs.getString("ATAI");
//			System.out.println(from);
			String to = replaceAuto(from);
			rs.updateString("SIKI", to);
			rs.updateRow();
//			System.out.println(to);
		}
		osd.getConnection().commit();
		osd.close();
		
		
//		System.out.println(kaiketu(s));
	}
	
	public ExcelKaiketuToOngxt(String s){
		
			String to = replaceAuto(s);
	}
	
	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		new ExcelKaiketuToOngxt();
		String string = "IF(AI7=0,0,AI7/(DC2*24))*10";
		string = "AI7/(DC2*24)*10";
		string = "IF(AV14+AV15+AV17=0,0,(AV14+AV15)/(C3*24)*100)";
		System.out.println(string);
//		new ExcelKaiketuToOngxt(string);
		
	}
	
	public String replaceAuto(String from){
		String to=from.toUpperCase();
		String siki="";
		if(to.matches("IF\\(.*")){
//			System.out.println("IF语句"+to);
			int hidari = to.indexOf("IF(");
			int migi = getKakkoKonntenntu(to, hidari+3);
			String s = to.substring(hidari+3, migi);
//			System.out.println(migi);
//			System.out.println(to.length());
//			System.out.println(s);
			String s1 = kaiketuIf(s);
			System.out.println(migi+"\t"+to.length());
			String last="";
			if(migi+1<=to.length())
				last=to.substring(migi+1,to.length());
			System.out.println("------"+last);
			System.out.println("s1=="+s1);
			
			System.out.println("to0="+to);
			to=s1+replaceAuto(last);
			System.out.println("to1="+to);
//			to=replaceAuto(s1);
//			to+=to.substring(migi);
//			System.out.println(s1);
		}else if(to.matches("SUM\\(.*")){
//			System.out.println("IF语句"+to);
			int hidari = to.indexOf("SUM(");
			int migi = getKakkoKonntenntu(to, hidari+4);
			String s = to.substring(hidari+4, migi);
//			System.out.println(s);
			String s1 = kaiketuSum(s);
//			System.out.println(s1);
			to=s1+to.substring(migi);
			siki=to;
		}else if(to.matches("MAX\\(.*")){
			
		}else if(to.matches("MIN\\(.*")){
			
		}else{
			to=kaiketuEnnzannsi(to);
		}
		System.out.println("to="+to);
		System.out.println("\n");
		return to;
	}
	
	public String kaiketuEnnzannsi(String from){
		String to=from;
		int index=0;
		String s="";
		System.out.println("演算子from="+from);
		while(1==1) {
			int min=-1;
			System.out.println("index="+index);
			int purasu=to.indexOf("+",index);
			int mainasu = to.indexOf("-",index);
			min=minIndex(purasu, mainasu);
			int kaeru = to.indexOf("*",index);
			System.out.println("kaeru="+kaeru);
			min=minIndex(kaeru,min);
			int nozoku = to.indexOf("/",index);
			min=minIndex(nozoku,min);
			int hidari=to.indexOf("(",index);
			min=minIndex(hidari,min);
			System.out.println("min="+min);
			if(min==-1){
				System.out.println(index+"\t"+to.length()+"\t"+s);
				if(index<=to.length()){
					System.out.println(index+"\t"+to.length()+"\t"+s);
					String konntenntu = to.substring(index, to.length());
					s+=formatAtai(konntenntu);
					System.out.println("s="+s);
				}
				break;
			}
			String konntenntu = to.substring(index, min);
			s+=formatAtai(konntenntu)+to.substring(min, min+1);
			System.out.println("s="+index+"\t"+s);
			if(min==hidari){
				int migi=getKakkoKonntenntu(to, index+1);
				konntenntu = to.substring(hidari+1, migi);
				s+="("+replaceAuto(konntenntu)+")";
				index=migi+1;
				System.out.println("migi="+migi+",leng="+to.length());
			}else{
				index=min+1;
			}
		}
		to=s;

		return to;
	}
	
	/**格式化值
	 * @param from
	 * @return
	 */
	public String formatAtai(String from){
		String to=from;
		int[] i = ExcelStaticHouhou.rowCoumnIntArray(from);
//		System.out.println(i[0]+"\t"+i[1]);
		if(i[0]!=-1&&i[1]!=-1){
			to="i.get(\""+from+"\")";
		}
		return to;
		
	}
	
	/**返回最小有效索引号
	 * @param i
	 * @param j
	 * @return
	 */
	public int minIndex(int i,int j){
		int min=-1;
		if(i==-1){
			min=j;
		}else{
			if(j==-1||i<j){
				min=i;
			}else{
				min=j;
			}
		}
		return min;
	}
	
	/**解決sum
	 * @param from
	 * @return
	 */
	public String kaiketuSum(String from){
		String to=from;
		int hidari=getSeparetaIndex(from,":", 0);
		String left=to.substring(0,hidari);
		String right=to.substring(hidari+1,from.length());
		int[] i = ExcelStaticHouhou.rowCoumnIntArray(left);
		int[] j =ExcelStaticHouhou.rowCoumnIntArray(right);
		if(i[1]==j[1]){
			String s1=null;
			if(i[0]>j[0]){
				int sm=i[0];
				i[0]=j[0];
				j[0]=i[0];
			}
			for(String s:source){
				int[] cou = ExcelStaticHouhou.rowCoumnIntArray(s);
				if(cou[1]==i[1]&&cou[0]>i[0]&&cou[0]<j[0]){
					s1=s;
//					System.out.println("s1="+s1);
					break;
				}
			}
			int count=Math.abs(i[0]-j[0]);
//			System.out.println(count);
			if(count==31||count==30){
				//月统计
				to="i.sum(\""+s1+"\", \"m\")";
			}else if(count==12||count==11){
				to="i.sum(\""+s1+"\", \"y\")";
			}
		}
		return to;
	}
	
	/**解決if
	 * @param from
	 * @return
	 */
	public String kaiketuIf(String from){
		String to=from;
//		System.out.println("from="+from);
		int jyokenn=getSeparetaIndex(from,",", 0);
		String s=to.substring(0, jyokenn);
		int sinn=getSeparetaIndex(from,",", jyokenn+1);
		String s1=to.substring(jyokenn+1,sinn);
		String s2=to.substring(sinn+1,from.length());
		
//		System.out.println("1="+s+"  "+jyokenn);
//		System.out.println("2="+s1+"  "+sinn);
//		System.out.println("3="+s2);
		if(s1.equals("0"))
			return replaceAuto(s2);
		return from;
	}
	
	/**提取次のセパレーターイにンデックス
	 * @param from
	 * @return
	 */
	public int getSeparetaIndex(String from,String separeta,int douhaoIndex){
		int left=0;//第几个左括号
		int right=0;//第几个右括号
		int index=douhaoIndex+1;
		while(1==1){
			int douhao=from.indexOf(separeta,index);
			int hidari=from.indexOf("(",index);
			int migi=from.indexOf(")",index);
//			System.out.println("douhao="+douhao+" migi="+migi+"hi");
			if(douhao==-1){
				return douhaoIndex;
			}else if(douhao<hidari&&douhao<migi){
				index=douhao;
				if(left==right){
					return index;
				}
			}else if(hidari<migi||migi==-1){
				if(hidari==-1){
					index=douhao;
					if(left==right){
						return index;
					}
				}else{
					index=hidari;
					left++;
				}
			}else{
				if(migi==-1){
					index=douhao;
					if(left==right){
						return index;
					}
				}else{
					index=migi;
					right++;
				}
			}
			index++;
		}
	}
	
	/**配对括弧インデックスを得るします
	 * @param from
	 * @return
	 */
	public int getKakkoKonntenntu(String from,int kakkoIndex){
		int left=1;//第几个左括号
		int right=0;//第几个右括号
		int index=kakkoIndex+1;
		while(left!=right){
			int hidari=from.indexOf("(",index+1);
			int migi=from.indexOf(")",index+1);
			if(migi==-1){
				return kakkoIndex;
			}else if(migi<hidari||hidari==-1){
				right++;
				index=migi;
//				System.out.println(hidari+"   "+migi+"  "+left+"  "+right);
			}else {
				left++;
				index=hidari;
//				System.out.println(hidari+"   "+migi+"  "+left+"  "+right);
			}
		}
		
		return index;
	}
	
	public String kaiketu(String from){
		String to=from;
		for (String s : source) {
			String siki="(\\(|:|\\+|^)"+s+"(\\)|:|$|\\+)";
			siki="(^|(^.*[\\(|:|\\+]))"+s+"($|([\\):(\\+)].*))";
//			System.out.println(siki);
			if(from.matches(siki)){
				to=to.replaceAll(s,"i.get(\""+s+"\")");
				System.out.println("s="+s);
			}
		}
		return to;
	}
	
	String pwhere =null;
	String ptable =null;
	String code =null;
    public void PropertiesLoadTest(String filename) {
        Properties prop = new Properties();
 
        try {
            InputStream stream = new FileInputStream(filename);
//            prop.loadFromXML(stream);
            prop.load(stream);
            stream.close();
            ptable=  prop.getProperty("table");
            pwhere=  prop.getProperty("where");
            code=prop.getProperty("code");
            url=prop.getProperty("url");
            driver=prop.getProperty("driver");
            user=prop.getProperty("user");
            password=prop.getProperty("password");
            System.out.println("System.getProperty = "+System.getProperty("file.encoding"));
            prop.list(System.out); 
            
        } catch (IOException ex) { 
            ex.printStackTrace();
        }
    }
}


