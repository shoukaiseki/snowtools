package org.maximo.app.config;
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
 * classpath加载顺序
 * dom4j-1.6.1.jar
 * stax-api-1.0.1.jar
 * xmlbeans-2.3.0.jar
 * poi*.jar
 *****************************************************************************/

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;

import com.alibaba.druid.pool.DruidDataSource;
import org.maximo.app.MTException;
import org.maximo.app.MessageOnTerminal;
import org.maximo.app.OutMessage;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;
/**
 * 
 * @author 蒋カイセキ
 *
 */
public class TestExportToExcle
{

    /**
     * @param args
     * @throws IOException 
     * @throws InvalidFormatException 
     */
    public static void main(String[] args) throws Exception 
    { 
//    	read();
//    	write();
//    	dbExpExcle();
    	dbExpExcleOne();
    }
    
    	static HSSFCellStyle cellStyle = null; 
    private static void dbExpExcleOne() throws SQLException, MTException {
    	
			OutMessage om=new MessageOnTerminal();
		ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(om);
			String refName="localhost_gbkorcl" ;
			refName="crpxz_101_shoukaiseki" ;
			DruidDataSource druid = rddsk .getDruidDataSource(refName);
			OracleSqlDetabese osd = new OracleSqlDetabese(druid.getConnection());
			osd.setSql(" select wonum 工單號, description 描述, worktype 工單類型, woa3 專業, WOA4 機組  from workorder order by wonum");
//			osd.setSql("select wosafetylink.wonum 工單號,wosafetylink.hazardid 危險編號,wohazard.description 危險內容 from wosafetylink,wohazard where WOSAFETYLINK.HAZARDID=WOHAZARD.HAZARDID and wosafetylink.wonum=wohazard.wonum and WOHAZARD.HAZARDTYPE in('ELECTRICAL','HEALTH')");
//			osd.setSql("select wosafetylink.wonum 工單號,wosafetylink.hazardid 危險編號,WOTAGOUT.tagoutid 安全標記編號,WOTAGOUT.description 安全標記內容 from wotagout,wosafetylink where WOTAGOUT.TAGOUTID=WOSAFETYLINK.TAGOUTID and WOTAGOUT.WONUM=WOSAFETYLINK.WONUM");
//			osd.setSql("select wotaglock.wonum 工單號,wotaglock.tagoutid  安全標記編號,wolockout.devicedescription 设备描述,wolockout.requiredstate 要求状态  from wotaglock,wolockout where WOLOCKOUT.LOCKOUTID=WOTAGLOCK.LOCKOUTID and WOLOCKOUT.WONUM=WOTAGLOCK.WONUM");
			ResultSet r = osd.executeQuery();
			ResultSetMetaData rsmd = r.getMetaData();
		
		// TODO Auto-generated method stub
    	 // 创建新的Excel 工作簿 
        HSSFWorkbook workbook = new HSSFWorkbook(); 
       
        // 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称 
        HSSFSheet sheet = workbook.createSheet(); 
        // HSSFSheet sheet = workbook.createSheet("SheetName"); 
       
        // 用于格式化单元格的数据 
        HSSFDataFormat format = workbook.createDataFormat(); 
       
        // 设置单元格类型 
         cellStyle = workbook.createCellStyle(); 
//        cellStyle.setFont(font); 
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中 
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平布局：居中 
        cellStyle.setWrapText( true ); 
       
        // 添加单元格注释 
        // 创建HSSFPatriarch对象,HSSFPatriarch是所有注释的容器. 
        HSSFPatriarch patr = sheet.createDrawingPatriarch(); 
       
			int column = rsmd.getColumnCount();
			String[] strs = new String[column];
			short rowNum=0;
			short cellNum=0;
			HSSFRow row = sheet.createRow(rowNum ++); 
			setCellData(workbook,row,cellNum++ , "表名稱" );
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				om.print(rsmd.getColumnName(i+1)+"\t");
				strs[i] = rsmd.getColumnName(i + 1);
        // 创建单元格 
        setCellData(workbook, row, cellNum++, rsmd.getColumnName(i+1) );
			}
			OracleSqlDetabese safeOsd=new OracleSqlDetabese(osd);
			safeOsd.setSql(new StringBuffer( "select wosafetylink.wonum 工單號,wosafetylink.hazardid 危險編號,wohazard.description 危險內容 from wosafetylink,wohazard " +
							"where WOSAFETYLINK.HAZARDID=WOHAZARD.HAZARDID and wosafetylink.wonum=wohazard.wonum" +
					" and  wosafetylink.tagoutid IS NULL AND EXISTS ( SELECT * FROM wohazard WHERE wonum= wosafetylink.wonum AND hazardid= wosafetylink.hazardid AND tagoutenabled= 1 ) and WOHAZARD.WONUM=:1")
					.toString());
			PreparedStatement safePs = safeOsd.prepareStatement();
			OracleSqlDetabese tgOsd=new OracleSqlDetabese(osd);
			tgOsd.setSql("select wosafetylink.wonum 工單號,wosafetylink.hazardid 危險編號,WOTAGOUT.tagoutid 安全標記編號,WOTAGOUT.description 安全標記內容 from wotagout,wosafetylink where WOTAGOUT.TAGOUTID=WOSAFETYLINK.TAGOUTID and WOTAGOUT.WONUM=WOSAFETYLINK.WONUM and WOTAGOUT.WONUM=:1 and wosafetylink.hazardid=:2");
			PreparedStatement tgPs = tgOsd.prepareStatement();
			OracleSqlDetabese loOsd=new OracleSqlDetabese(osd);
			loOsd.setSql("select wotaglock.wonum 工單號,wotaglock.tagoutid  安全標記編號,wolockout.devicedescription 设备描述,wolockout.requiredstate 要求状态  from wotaglock,wolockout where WOLOCKOUT.LOCKOUTID=WOTAGLOCK.LOCKOUTID and WOLOCKOUT.WONUM=WOTAGLOCK.WONUM and WOTAGLOCK.WONUM=:1 and WOTAGLOCK.TAGOUTID=:2");
			PreparedStatement loPs = loOsd.prepareStatement();
			while(r.next()){
				row = sheet.createRow(rowNum ++); 
				cellNum=0;
//				setCellData(workbook,row,cellNum++ , "WORKORDER" );
				setCellData(workbook,row,cellNum++ , "工單" );
        // 创建新行(row),并将单元格(cell)放入其中. 行号从0开始计算. 
				for (int i = 0; i < column; i++) {
					String field = rsmd.getColumnName(i + 1);
					if (rsmd.getColumnType(i + 1) == Types.DATE) {
						Timestamp time = r.getTimestamp(i + 1);
						om.println(field + "(TimeStamp)= " + time);
					}else{
						String value = r.getString(i + 1);
						om.println(field + " = " + value);
						setCellData(workbook, row, cellNum, value);
					}
					cellNum++;
				}
				safePs.setString(1, r.getString("工單號"));
				ResultSet safeRs = safePs.executeQuery();
				while(safeRs.next()){
					row = sheet.createRow(rowNum++ ); 
					cellNum=0;
//					setCellData(workbook,row,cellNum++ , "WOSAFETYLINK" );
					setCellData(workbook,row,cellNum++ , "危險" );
					cellNum++;
					setCellData(workbook,row,cellNum++ , safeRs.getString("危險編號") );
					setCellData(workbook,row,cellNum++ , safeRs.getString("危險內容") );
					tgPs.setString(1, r.getString("工單號"));
					tgPs.setString(2, safeRs.getString("危險編號"));
					ResultSet tgRs = tgPs.executeQuery();
					while(tgRs.next()){
						row = sheet.createRow(rowNum++ ); 
						cellNum=0;
//						setCellData(workbook,row,cellNum++ , "WOTAGOUT" );
						setCellData(workbook,row,cellNum++ , "安全標記" );
						cellNum++;
						cellNum++;
						setCellData(workbook,row,cellNum++ , tgRs.getString("安全標記編號") );
						setCellData(workbook,row,cellNum++ , tgRs.getString("安全標記內容") );
						loPs.setString(1,r.getString("工單號"));
						loPs.setString(2,tgRs.getString("安全標記編號"));
						ResultSet loRs = loPs.executeQuery();
						while(loRs.next()){
							row = sheet.createRow(rowNum++ ); 
							cellNum=0;
//							setCellData(workbook,row,cellNum++ , "WOLOCKOUT" );
							setCellData(workbook,row,cellNum++ , "隔離任務" );
							cellNum++;
							cellNum++;
							cellNum++;
							setCellData(workbook,row,cellNum++ , loRs.getString("设备描述") );
							setCellData(workbook,row,cellNum++ , loRs.getString("要求状态") );
						}
						loRs.close();
					}
					tgRs.close();
				}
				safeRs.close();
			}
			loOsd.close();
			tgOsd.close();
			safeOsd.close();
			for (int i = 0; i < column; i++) {
        sheet.autoSizeColumn(( short ) i ); // 调整第一列宽度 
			}
			
       
       

         try  { 
//			FileOutputStream fileOut = new FileOutputStream(new StringBuffer("/tmp/log/").append(new Date().getTime()).append(".xlsx").toString());
			FileOutputStream fileOut = new FileOutputStream(new StringBuffer("/tmp/log/asus.xlsx").toString());
            workbook.write(fileOut); 
            fileOut.close(); 
         } catch (Exception e)  { 
            System.out.println(e.toString()); 
        } 		
			osd.close();
			druid.close();
	}
    public static void setCellData(HSSFWorkbook workbook,HSSFRow row, short cellNum, String value ){
        // 设置单元格类型 
    	//        cellStyle.setFont(font); 
    	//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中 
    	cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平布局：居中 
    	cellStyle.setWrapText( true ); 
    	HSSFCell cell = row.createCell(cellNum ); 
    	HSSFRichTextString hssfString = new HSSFRichTextString( value ); 
    	cell.setCellValue(hssfString); // 设置单元格内容 
    	cell.setCellStyle(cellStyle); // 设置单元格样式 
    	cell.setCellType(HSSFCell.CELL_TYPE_STRING); // 指定单元格格式：数值、公式或字符串 
    }
    
    
    private static void dbExpExcle() throws SQLException, MTException {
    	
			OutMessage om=new MessageOnTerminal();
		ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(om);
			String refName="localhost_gbkorcl" ;
			refName="crpxz_101_shoukaiseki" ;
			DruidDataSource druid = rddsk .getDruidDataSource(refName);
			OracleSqlDetabese osd = new OracleSqlDetabese(druid.getConnection());
//			osd.setSql(" select wonum 工單號, worktype 工單類型, description 描述, woa3 專業, WOA4 機組  from workorder ");
//			osd.setSql("select wosafetylink.wonum 工單號,wosafetylink.hazardid 危險編號,wohazard.description 危險內容 from wosafetylink,wohazard where WOSAFETYLINK.HAZARDID=WOHAZARD.HAZARDID and wosafetylink.wonum=wohazard.wonum and WOHAZARD.HAZARDTYPE='ELECTRICAL'");
//			osd.setSql("select wosafetylink.wonum 工單號,wosafetylink.hazardid 危險編號,WOTAGOUT.tagoutid 預防措施編號,WOTAGOUT.description 預防措施內容 from wotagout,wosafetylink where WOTAGOUT.TAGOUTID=WOSAFETYLINK.TAGOUTID and WOTAGOUT.WONUM=WOSAFETYLINK.WONUM");
			osd.setSql("select wotaglock.wonum 工單號,wotaglock.tagoutid  安全標記編號,wolockout.devicedescription 设备描述,wolockout.requiredstate 要求状态  from wotaglock,wolockout where WOLOCKOUT.LOCKOUTID=WOTAGLOCK.LOCKOUTID and WOLOCKOUT.WONUM=WOTAGLOCK.WONUM");
			ResultSet r = osd.executeQuery();
			ResultSetMetaData rsmd = r.getMetaData();
		
		// TODO Auto-generated method stub
    	 // 创建新的Excel 工作簿 
        HSSFWorkbook workbook = new HSSFWorkbook(); 
       
        // 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称 
        HSSFSheet sheet = workbook.createSheet(); 
        // HSSFSheet sheet = workbook.createSheet("SheetName"); 
       
        // 用于格式化单元格的数据 
        HSSFDataFormat format = workbook.createDataFormat(); 
       
        // 设置单元格类型 
        HSSFCellStyle cellStyle = workbook.createCellStyle(); 
//        cellStyle.setFont(font); 
//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中 
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平布局：居中 
        cellStyle.setWrapText( true ); 
       
        // 添加单元格注释 
        // 创建HSSFPatriarch对象,HSSFPatriarch是所有注释的容器. 
        HSSFPatriarch patr = sheet.createDrawingPatriarch(); 
       
			int column = rsmd.getColumnCount();
			String[] strs = new String[column];
			short rowNum=0;
			short cellNum=0;
			HSSFRow row = sheet.createRow(rowNum ); 
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				om.print(rsmd.getColumnName(i+1)+"\t");
				strs[i] = rsmd.getColumnName(i + 1);
        // 创建单元格 
        HSSFCell cell = row.createCell(cellNum ); 
        HSSFRichTextString hssfString = new HSSFRichTextString( rsmd.getColumnName(i+1) ); 
        cell.setCellValue(hssfString); // 设置单元格内容 
        cell.setCellStyle(cellStyle); // 设置单元格样式 
        cell.setCellType(HSSFCell.CELL_TYPE_STRING); // 指定单元格格式：数值、公式或字符串 
				cellNum++;
			}
			while(r.next()){
				rowNum++;
				row = sheet.createRow(rowNum ); 
				cellNum=0;
        // 创建新行(row),并将单元格(cell)放入其中. 行号从0开始计算. 
				for (int i = 0; i < column; i++) {
					String field = rsmd.getColumnName(i + 1);
					if (rsmd.getColumnType(i + 1) == Types.DATE) {
						Timestamp time = r.getTimestamp(i + 1);
						om.println(field + "(TimeStamp)= " + time);
					}else{
						String value = r.getString(i + 1);
						om.println(field + " = " + value);
						HSSFCell cell = row.createCell(cellNum ); 
						HSSFRichTextString hssfString = new HSSFRichTextString( value ); 
						cell.setCellValue(hssfString); // 设置单元格内容 
						cell.setCellStyle(cellStyle); // 设置单元格样式 
						cell.setCellType(HSSFCell.CELL_TYPE_STRING); // 指定单元格格式：数值、公式或字符串 
					}
					cellNum++;
				}
			}
			for (int i = 0; i < column; i++) {
        sheet.autoSizeColumn(( short ) i ); // 调整第一列宽度 
			}
			
       
       

         try  { 
			FileOutputStream fileOut = new FileOutputStream(new StringBuffer("/tmp/log/").append(new Date().getTime()).append(".xlsx").toString());
//			FileOutputStream fileOut = new FileOutputStream(new StringBuffer("/tmp/log/asus.xlsx").toString());
            workbook.write(fileOut); 
            fileOut.close(); 
         } catch (Exception e)  { 
            System.out.println(e.toString()); 
        } 		
			osd.close();
			druid.close();
	}

	private static void write() throws InvalidFormatException, IOException {
		// TODO Auto-generated method stub
    	
    	 // 创建新的Excel 工作簿 
        HSSFWorkbook workbook = new HSSFWorkbook(); 
       
        // 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称 
        HSSFSheet sheet = workbook.createSheet(); 
        // HSSFSheet sheet = workbook.createSheet("SheetName"); 
       
        // 用于格式化单元格的数据 
        HSSFDataFormat format = workbook.createDataFormat(); 
       
        // 创建新行(row),并将单元格(cell)放入其中. 行号从0开始计算. 
        HSSFRow row = sheet.createRow(( short ) 1 ); 

        // 设置字体 
        HSSFFont font = workbook.createFont(); 
        font.setFontHeightInPoints(( short ) 20 ); // 字体高度 
        font.setColor(HSSFFont.COLOR_RED); // 字体颜色 
        font.setFontName( " 黑体 " ); // 字体 
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD); // 宽度 
        font.setItalic( true ); // 是否使用斜体 
//        font.setStrikeout(true); // 是否使用划线 

        // 设置单元格类型 
        HSSFCellStyle cellStyle = workbook.createCellStyle(); 
        cellStyle.setFont(font); 
        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中 
        cellStyle.setWrapText( true ); 
       
        // 添加单元格注释 
        // 创建HSSFPatriarch对象,HSSFPatriarch是所有注释的容器. 
        HSSFPatriarch patr = sheet.createDrawingPatriarch(); 
        // 定义注释的大小和位置,详见文档 
        HSSFComment comment = patr.createComment( new HSSFClientAnchor( 0 , 0 , 0 , 0 , ( short ) 4 , 2 , ( short ) 6 , 5 )); 
        // 设置注释内容 
        comment.setString( new HSSFRichTextString( " 可以在POI中添加注释！ " )); 
        // 设置注释作者. 当鼠标移动到单元格上是可以在状态栏中看到该内容. 
        comment.setAuthor( " Xuys. " ); 
       
        // 创建单元格 
        HSSFCell cell = row.createCell(( short ) 1 ); 
        HSSFRichTextString hssfString = new HSSFRichTextString( " Hello World! " ); 
        cell.setCellValue(hssfString); // 设置单元格内容 
        cell.setCellStyle(cellStyle); // 设置单元格样式 
        cell.setCellType(HSSFCell.CELL_TYPE_STRING); // 指定单元格格式：数值、公式或字符串 
        cell.setCellComment(comment); // 添加注释 

        // 格式化数据 
        row = sheet.createRow(( short ) 2 ); 
        cell = row.createCell(( short ) 2 ); 
        cell.setCellValue( 11111.25 ); 
        cellStyle = workbook.createCellStyle(); 
        cellStyle.setDataFormat(format.getFormat( " 0.0 " )); 
        cell.setCellStyle(cellStyle); 

        row = sheet.createRow(( short ) 3 ); 
        cell = row.createCell(( short ) 3 ); 
        cell.setCellValue( 9736279.073 ); 
        cellStyle = workbook.createCellStyle(); 
        cellStyle.setDataFormat(format.getFormat( " #,##0.0000 " )); 
        cell.setCellStyle(cellStyle); 
       
       
        sheet.autoSizeColumn(( short ) 0 ); // 调整第一列宽度 
        sheet.autoSizeColumn(( short ) 1 ); // 调整第二列宽度 
        sheet.autoSizeColumn(( short ) 2 ); // 调整第三列宽度 
        sheet.autoSizeColumn(( short ) 3 ); // 调整第四列宽度 

         try  { 
            FileOutputStream fileOut = new FileOutputStream( "/tmp/log/new.xlsx" ); 
            workbook.write(fileOut); 
            fileOut.close(); 
         } catch (Exception e)  { 
            System.out.println(e.toString()); 
        } 		
	}

	private static void read() {
		// TODO Auto-generated method stub
        try  
        {
        InputStream inp;
//            inp = new FileInputStream("w:/月报.xlsx");
            inp = new FileInputStream("/tmp/test.xlsx");
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheetAt(0);
            for (Iterator rit = sheet.rowIterator(); rit.hasNext();)
            {
                // 迭代行
                Row row = (Row) rit.next();
                
                // 迭代单元格
                for (Iterator cit = row.cellIterator(); cit.hasNext();)
                {
                    Cell cell = (Cell) cit.next();
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
                    	taipu="default";
                    }
                    String value=getMergedRegionValue(sheet, cell);
                    if(value!=null&&!value.isEmpty()){
                    	System.out.print(cell.getRowIndex()+1 + ":" + columnEgo(cell.getColumnIndex()+1)+"\t");
                        System.out.println(value);
                    }
                }
                    	if(row.getFirstCellNum()!=row.getLastCellNum())
                    	System.out.println(row.getPhysicalNumberOfCells());
            }
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
					return value;
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

	/**将列号変換为excle A-Z AA-AZ,BA-BZ格式
     * @param col 要转换的数字
     * @return
     */
    public static String columnEgo(int col){
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
}




















