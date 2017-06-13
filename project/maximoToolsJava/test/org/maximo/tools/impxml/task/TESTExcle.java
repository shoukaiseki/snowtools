package org.maximo.tools.impxml.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.apache.log4j.Level;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.dom4j.*;
import org.dom4j.tree.DefaultCDATA;

import bsh.EvalError;
import bsh.Interpreter;

import org.maximo.app.MTException;
import org.maximo.app.MessageOnTerminal;
import org.shoukaiseki.tuuyou.logger.PrintLogs;

public class TESTExcle {
	
	private static final String TITLECOUNT = "TITLECOUNT";
	private static final String NAME = "NAME";
	private static final String SHEET = "SHEET";
	/**
	 * 存放excle cell與 column 的 映射,row设置值按照 映射後的column名称设置
	 * Map<CellName,ColumnName>
	 */
	Map<String,String> ccMap=new HashMap<String,String>();
	
	/**
	 *  存放excle cell與 column 的 映射,row设置值按照 映射後的column名称设置
	 *  Map<CellName,EDColumn>
	 */
	Map<String,EDColumn> edMap=new HashMap<String, EDColumn>();
	
	Sheet sheet=null;
	Iterator<?> rowsIt=null;
	InputStream inp=null;
	private int titleCount;
	public TESTExcle() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		try{
//			inp = new FileInputStream(new File("/media/develop/antProject/javaant/javaProject/maximoToolsJava/samples/impexp/inventory1.xls"));
			String file="/media/develop/antProject/javaant/javaProject/maximoToolsJava/samples/impexp/inventory1.xls";
//			file="Z:/1.xls";
			Workbook wb = WorkbookFactory.create(new File(file));
			sheet = wb.getSheetAt(getExcleSheetAt());
			rowsIt=sheet.iterator();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
	}
	
	public boolean hasNext(){
		return  rowsIt.hasNext();
	}
	
	
	public org.maximo.tools.impxml.task.Row next() throws MTException {
			Row row = (Row) rowsIt.next();
			for (Iterator cit = row.cellIterator(); cit.hasNext();){
				Cell cell = (Cell) cit.next();
				if(cell.getRowIndex()<getTitleCount()){
					return null;
				}
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
					break;
				default:
					taipu="default";
				}
				String value=getMergedRegionValue(sheet, cell);
				if(value!=null&&!value.isEmpty()){
					String attName=formatColumnName(cell.getColumnIndex());
					System.out.println(attName+"="+value);
				}
			}
			
			return null;
	}
	
	public String formatColumnName(int index){
		String s=ccMap.get(columnEgo(index+1));
		if(s==null){
			s=columnEgo(index+1);
		}
		return s;
	}
	
	public void addColumn(Element rowEle,String name,String value){
		Element reference = rowEle.addElement("COLUMN");
		reference.addAttribute("NAME", name);
		reference.add(new DefaultCDATA(value));
		
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
	

	
	public int getTitleCount() {
		return titleCount;
	}
	
	private void setTitleCount(String tc){
		if(IXFormat.isNullOrTrimEmpty(tc)){
			titleCount=1;
		}else{
			titleCount= Integer.parseInt(tc);	
		}
	}
	
	public int getExcleSheetAt(){
		return 0;
	}
	public static void main(String[] args) throws MTException {
		System.out.println("start");
		PrintLogs logger=new PrintLogs();
		logger.setConsoleAppenderLevel(Level.INFO );
		long l=new Date().getTime();
		TESTExcle te=new TESTExcle();
		te.parseElement();
		while(te.hasNext()){
			te.next();
		}
		long end=new Date().getTime();
		String toukeiJikann = logger.toukeiJikann(l, end);
		logger.info("共消耗時間:"+toukeiJikann);
		System.out.println("end");
	}

}
