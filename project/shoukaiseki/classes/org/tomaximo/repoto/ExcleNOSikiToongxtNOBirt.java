

package org.tomaximo.repoto;

//第一步


/******************************************************************************
 * 演示使用POI 读取 Excel 2007
 * 
 * 关键字：Java Excel POI POI-HSSF POI-XSSF
 * 
 * 作者：高宏伟(DukeJoe)
 * 时间：2009-10-22 16:12:25
 * 地点：黑龙江省哈尔滨市道里区通达街291号
 * 注释：http://blog.donews.com/dukejoe/archive/2009/10/22/1567379.aspx
 * 
 * 开发环境详细说明：
 * 1、java version "1.6.0_14"
 * 2、Java(TM) SE Runtime Environment (build 1.6.0_14-b08)
 * 3、Java HotSpot(TM) Client VM (build 14.0-b16, mixed mode, sharing)
 * 4、Microsoft Excel 2007
 * 5、Windows XP Home Edition Service Pack 3
 * 6、Apache POI 3.5
 * 
 * classpath加载顺序
 * dom4j-1.6.1.jar
 * stax-api-1.0.1.jar
 * xmlbeans-2.3.0.jar
 * poi*.jar
 *****************************************************************************/
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import org.shoukaiseki.file.SousaFile;
/**
 * 
 * @author 蒋カイセキ
 *
 */
public class ExcleNOSikiToongxtNOBirt
{

	ExcelNoSiki excelNoSiki;
	public String maigetuRepotoName;
	public String[] koukaColumn;
	static String insertSql="insert into  SHOUKAISEKI_ONGXT_BIRT" +
			"(OBUJEKUTO,SERU,SERUNAME,ATAI,TAIPU,SETUMEI,YUNITTO,FULLSERUNAME,CODE)" +
			" values('生产经营综合月报表',?,?,?,?,?,?,?,?) ";
	public ExcleNOSikiToongxtNOBirt() throws SQLException{
//	 insertSql="insert into  EXCLE_SIKITO_ONGXT_BIRT(OBUJEKUTO,SERU,ATAI,TAIPU,SETUMEI)" +
//			" values('asus',?,?,?,?) ";
    	setDefuorutoConnection();   	
		sakuseiTable();
    	PreparedStatement pst=con.prepareStatement(insertSql);
        try  
        {
        InputStream inp;
        	String fileName = "w:/月报.xlsx";
        	maigetuRepotoName = "C:/Documents and Settings/Administrator/桌面/レポート(repoto)[报告][报表]/maigetuRepoto.voom";
//        	fileName = "w:/asus.xlsx";
        	maigetuRepotoName = "C:/Documents and Settings/Administrator/桌面/レポート(repoto)[报告][报表]/maigetuRepoto2.voom";
        	fileName = "w:/日报.xlsx";
        	maigetuRepotoName = "C:/Documents and Settings/Administrator/桌面/レポート(repoto)[报告][报表]/maiNitiRepoto.voom";
        	fileName = "w:/负荷曲线设计—修改v5.16.4.xlsx";
        	maigetuRepotoName = "C:/Documents and Settings/Administrator/桌面/レポート(repoto)[报告][报表]/生产经营综合月报表Repoto.voom";
        	excelNoSiki = readFileToSetMergedName();
            inp = new FileInputStream(fileName);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            for (Iterator rit = sheet.rowIterator(); rit.hasNext();)
            {
                // 迭代行
                Row row = (Row) rit.next();
               if( row.getZeroHeight()){
            	   
            	   continue;
               }
                // 迭代单元格
               Vector vector=new Vector();
               for (String string : koukaColumn) {
            	   vector.add(string);
               }
               for (Object object : vector) {
				System.out.println(object);
               }
               
                for (Iterator cit = row.cellIterator(); cit.hasNext();)
                {
                    Cell cell = (Cell) cit.next();
                    //効果の列
//                    koukaColumn=new String[]{"A","B","C","D","E","M","P","Q","R","S","AC","AF","AG","AH","AI","AS","AV"};
//                    koukaColumn=new String[]{"M","AC","AS"};
                    
                    int[] koukaRow={};
                    if(vector.indexOf(columnEgo(cell.getColumnIndex()))==-1){
                    	continue;
                    }
                    System.out.println("sssssss");
                    // 开始操作单元格
                    // 在每一行的输出都打印如 "5:6 例子字符串"，5:6代表第5行，第6列
                    // 注意行和列是基于0索引的
//                    System.out.print(cell.getRowIndex() + ":" + columnEgo(cell.getColumnIndex()+1)+"\t");
                    // 打印单元格内的数据
                    String taipu="";
                    switch (cell.getCellType())
                    {
                    case Cell.CELL_TYPE_STRING:
                        taipu="string";
                        break;
                    case Cell.CELL_TYPE_NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell))
                        {
                            taipu="date";
                        } else
                        {
                            taipu="double";
                        }
                        break;
                    case Cell.CELL_TYPE_BOOLEAN:
                        taipu="boolean";
                        break;
                    case Cell.CELL_TYPE_FORMULA://公式
                    	
                        taipu="formula";//获取公式
//                            konntenntu=""+(cell.getNumericCellValue());//获取公式执行结果
                        
                        break;
                    default:
                    	if(isMergedRegion(sheet, cell)){
                    		taipu="merged";
                    	}else{
                    		taipu="default";
                    	}
                    }
//			System.out.println("--cell="+cell.toString());
                    String value=getMergedRegionValue(sheet, cell);
                    String merged = columnEgo(cell.getColumnIndex())+(cell.getRowIndex()+1);
                    	//SERU,ATAI,TAIPU,SETUMEI
                    if(value!=null){
                    	value=value.replaceAll("\n", "");
                    	value=value.replaceAll("\t", "");
                    	value=value.replaceAll(" ", "");
                    }
                    String seruName = getMergedName(sheet,row, cell);	
                    String[] name = getYunittoName(seruName);
//                    if(value!=null&&!value.isEmpty()){
                    	System.out.print(cell.getRowIndex()+1 + ":" + columnEgo(cell.getColumnIndex())+"\t");
                        System.out.println(seruName+"\t"+name[0]+"\t"+name[1]);
//                    }
                    pst.setString(1, merged);	
                    pst.setString(2, name[0]);	
                    pst.setString(3, value);	
                    pst.setString(4, taipu);	
                    pst.setString(5, null);	
                    pst.setString(6, name[1]);	
                    pst.setString(7, seruName);	
                    pst.setString(8, "HN_SCJY_"+merged);	
                    pst.addBatch();
                }
                pst.executeBatch();
                pst.clearBatch();
//					System.out.println("   正在将一批命令提交给数据库来执行!");
            }
            pst.close();
            con.commit();
            System.out.println(fileName);
            System.out.println("全部完了します");
            return;
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        } catch (InvalidFormatException e)
        {
            e.printStackTrace();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        con.rollback();
		
	}
	
    public String[] getYunittoName(String seruName) {
		String[] yunittoName={null,null};
		// TODO Auto-generated method stub
		String atarasiiSeruName=seruName.replaceAll("（", "(").replaceAll("）", ")");
		int lastMigiE = atarasiiSeruName.lastIndexOf(")");
		int lastHidariE = atarasiiSeruName.lastIndexOf("(");
//		int lastMigiE = seruName.lastIndexOf(")");
//		int lastMigiT = seruName.lastIndexOf("）");
//		int lastHidariE = seruName.lastIndexOf("(");
//		int lastHidariT = seruName.lastIndexOf("（");
		if(lastMigiE!=-1&&lastHidariE!=-1){
			yunittoName[0]=atarasiiSeruName.substring(0, lastHidariE)+atarasiiSeruName.substring(lastMigiE+1);
			yunittoName[1]=atarasiiSeruName.substring(lastHidariE+1, lastMigiE);
		}else{
			yunittoName[0]=seruName;
		}
		return yunittoName;
	}

	/**
     * @param args
     * @throws SQLException 
     */
    public static void main(String[] args) throws SQLException 
    { 
    	new ExcleNOSikiToongxtNOBirt();
    }
    
	public String getMergedName(Sheet sheet, Row row, Cell cell) {
		String mergedName = "";
		LinkedList<ExcelNoSikiObujekuto> source = excelNoSiki
				.getSource(columnEgo(cell.getColumnIndex()));
		if (source != null) {
			String separeta = null;
			for (ExcelNoSikiObujekuto eso : source) {
				if(eso.taipu.equals(ExcelNoSiki.separeta)){
					separeta=eso.atai;
				}
			}
			
//			System.out.println("key.length=" + key.length);
			for (ExcelNoSikiObujekuto eso : source) {
				System.out.println(eso.toString());
				System.out.println("======" + (row.getRowNum()) + ":"
						+ columnEgo(cell.getColumnIndex()));
					String value = eso.atai;
					String s = null;
					switch (excelNoSiki.getTaipu(eso.taipu)) {
					case ExcelNoSiki.column:
						int columnInt = columnInt((String) value);
						System.out.println("----===" + columnInt+value);
						
						Cell cell2 = row.getCell(columnInt);
						s = getMergedRegionValue(sheet, cell2);
//						System.out.println("----===" + s);
						break;
					case ExcelNoSiki.row:
						break;
					case ExcelNoSiki.cell:
						break;
					case ExcelNoSiki.string:
						s = value;
						break;
					default:

					}
					if (!(s == null || s.isEmpty())) {
						if (separeta != null) {
							if (!(mergedName.isEmpty())) {
								mergedName += separeta;
							}
						}
						s = s.replaceAll("\n", "");
						s = s.replaceAll("\t", "");
						s = s.replaceAll(" ", "");
						mergedName += s;
					}
			}
		}
		return mergedName;
	}

	public ExcelNoSiki readFileToSetMergedName(){
		ExcelNoSiki excelNoSiki = new ExcelNoSiki();
		SousaFile sf=new SousaFile();
		System.out.println(maigetuRepotoName);
		String readFileToString = sf.readFileToString(maigetuRepotoName);
		String[] lf = readFileToString.split("\n");
		for (String string : lf) {
			String[] ht = string.split("\t");
			if(ht.length==1){
				koukaColumn=ht[0].split(",");
			}else if(ht.length==3)
				excelNoSiki.addSource(ht[0], ht[1], ht[2]);
		}
		return excelNoSiki;
	}
	
	
	/**
	 * 如果为合并单元格则返回合并的内容
	 * 
	 * @param sheet
	 * @param cell
	 * @return
	 */
	public static String getMergedRegionValue(Sheet sheet, Cell cell) {
		String value = null;
		if(cell==null)
			return null;
		// 得到一个sheet中有多少个合并单元格
		int sheetMergeddRegionCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetMergeddRegionCount; i++) {
			// 得出具体的合并单元格
			CellRangeAddress ca = sheet.getMergedRegion(i);
			// 得到合并单元格的起始行, 结束行, 起始列, 结束列
			int firstColumn = ca.getFirstColumn();
			int lastColumn = ca.getLastColumn();
			int firstRow = ca.getFirstRow();
			int lastRow = ca.getLastRow();
			// 判断该单元格是否在合并单元格范围之内, 如果是, 则返回 true
			int Mcell = firstColumn;
			if (cell.getColumnIndex() <= lastColumn
					&& cell.getColumnIndex() >= Mcell) {
				if (cell.getRowIndex() <= lastRow
						&& cell.getRowIndex() >= firstRow) {
					sheet.getCellComment(Mcell, firstRow);
					Row row = sheet.getRow(firstRow);
					Cell cell2 = row.getCell(firstColumn);
					value = getCellValueToString(cell2);
					if(cell.getColumnIndex()==firstColumn){
						return value;
					}else{
						return null;
					}
				}
			}
		}

		value = getCellValueToString(cell);

		return value;
	}
	
	public static String getCellValueToString(Cell cell) {
		// TODO Auto-generated method stub
		String value = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			value = (cell.getRichStringCellValue().getString());
			break;
		case Cell.CELL_TYPE_NUMERIC:
			if (DateUtil.isCellDateFormatted(cell)) {
				value = "" + (cell.getDateCellValue());
			} else {
				value = "" + (cell.getNumericCellValue());
			}
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			value = "" + (cell.getBooleanCellValue());
			break;
		case Cell.CELL_TYPE_FORMULA:// 公式

			value = "" + (cell.getCellFormula());// 获取公式
			// konntenntu=""+(cell.getNumericCellValue());//获取公式执行结果

			break;
		default:
			value = cell.getStringCellValue();
		}
		return value;
	}

	/**合并的セル(seru)[单元格]ですか?
	 * @param sheet
	 * @param cell
	 * @return
	 */
	public static boolean isMergedRegion(Sheet sheet, Cell cell) {
		// 得到一个sheet中有多少个合并单元格
		int sheetmergerCount = sheet.getNumMergedRegions();
		for (int i = 0; i < sheetmergerCount; i++) {
			// 得出具体的合并单元格
			CellRangeAddress ca = sheet.getMergedRegion(i);
			// 得到合并单元格的起始行, 结束行, 起始列, 结束列
			int firstC = ca.getFirstColumn();
			int lastC = ca.getLastColumn();
			int firstR = ca.getFirstRow();
			int lastR = ca.getLastRow();
			// 判断该单元格是否在合并单元格范围之内, 如果是, 则返回 true
			if (cell.getColumnIndex() <= lastC
					&& cell.getColumnIndex() >= firstC) {
				if (cell.getRowIndex() <= lastR && cell.getRowIndex() >= firstR) {
					return true;
				}
			}
		}
		return false;
	}

	/**将excle A-Z AA-AZ,BA-BZ格式转换为列号,0开始
	 * @param object
	 * @return
	 */
	public int columnInt(String object) {
		// TODO Auto-generated method stub
		int iti=0;
		for (int i = 0; i < 1000; i++) {
			String string = columnEgo(i);
//			System.out.println(string+"====="+object+"    "+string.equalsIgnoreCase(object));
//			StringToASCII.toASCII(object);
			if (string.equalsIgnoreCase(object)) {
				return i;
			}
		}
		
		return iti;
	}
	/**将列号変換为excle A-Z AA-AZ,BA-BZ格式
     * @param col 要转换的数字
     * @return
     */
    public static String columnEgo(int column){
    	int col=column+1;
    	if(col<1)return "復帰(ふっき)[返回]null";
    	int iti=col/26;
    	if(col%26==0){
    		iti--;
    	}
    	char saishou=(char) (iti+65-1);
    	int ninn=col%26;
    	ninn=ninn==0?26:ninn;
    	char nii=(char)(ninn+64);
    	if(col<27){
    		return ""+nii;
    	}else{
    		return ""+saishou+nii;
    	}
    }

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

	public static  void setDefuorutoConnection() {
		try {
			String url = "jdbc:oracle:thin:@10.59.67.22:1521:test22";
			user="orclzhjq";
			password="orclzhjq";
//			url = "jdbc:oracle:thin:@127.0.0.1:1521:maximo75";
//			user="luyang";
//			password="luyang";
			url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.6)(PORT=1521))(ADDRESS=(PROTOCOL=TCP)(HOST=10.59.67.7)(PORT=1521)))(CONNECT_DATA=(SERVER=dedicated)(SERVICE_NAME=eam)))";
			user = "maximo";
			password = "maximo";
//			driver="oracle.jdbc.OracleDriver";
			// （1）装载并注册数据库的JDBC驱动程序
			// 载入JDBC驱动：oracle安装目录下的jdbc\lib\classes12.jar
			println("正在试图加载驱动程序 " + driver);
			Class.forName(driver);
			println("驱动程序已加载");
			// 注册JDBC驱动：有些地方可不用此句
			println("url=" + url);
			println("user=" + user);
			println("password=" + password);
			println("正在试图连接数据库--------");
			java.sql.DriverManager
					.registerDriver(new oracle.jdbc.driver.OracleDriver());

			con = DriverManager.getConnection(url, user, password);

			println("OK,成功连接到数据库");
			/**
			 * maximo.properties放在bin目录
			 */
			/**
			 * 关闭自动更新
			 */
			con.setAutoCommit(false);
		} catch (Exception ex) {
			ex.printStackTrace();
			println(ex.getMessage(), true);
		}
				
	}

	/**
	 * フォームを生成します
	 */
	public static void sakuseiTable() {
		String sql=""
				+"CREATE TABLE SHOUKAISEKI_ONGXT_BIRT\n"
				+"  (\n"
				+"    obujekuto VARCHAR2(100),  --对象\n"
				+"    seru      VARCHAR2(10),   --单元格\n"
				+"    CODE      VARCHAR2(30),   --单元格\n"
				+"    fullseruname  VARCHAR2(1000),  --单元格名称\n"
				+"    seruname  VARCHAR2(1000),  --单元格名称\n"
				+"    yunitto	VARCHAR2(100),  --单位\n"
				+"    atai      VARCHAR2(500),  --单元格值\n"
				+"    taipu     VARCHAR2(10),   --单元格类型\n"
				+"    setumei   VARCHAR2(1000), --描述\n"
				+"    jizu      VARCHAR2(500),  --机组\n"
				+"    tmp02     VARCHAR2(500),\n"
				+"    tmp03     VARCHAR2(500)\n"
				+"  )\n" ;
		System.out.println(sql);
		
		println(sql);
		try {
			PreparedStatement pst = con.prepareStatement(sql);

			pst.execute();
			pst.close();// 更新后关闭此线程,不然更新数据多了就会异常
			println( "表创建成功!");
		} catch (Exception e) {
			println("已有此表");
			// TODO: handle exception
			return;
		}
	}
	
	

	public static void println() {

	}

	public static void println(String s, boolean b) {
		System.out.println(s);
	}

	public static void println(String s) {
		System.out.println(s);
	}
}






















