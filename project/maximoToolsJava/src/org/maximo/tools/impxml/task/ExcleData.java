package org.maximo.tools.impxml.task;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


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

/** org.maximo.tools.impxml.task.ExcleData
 * @author 蒋カイセキ    Japan-Tokyo  2014年5月27日
 * ブログ http://shoukaiseki.blog.163.com/
 * E-メール jiang28555@Gmail.com
 */
public class ExcleData extends Task{
	
	private static final String TITLECOUNT = "TITLECOUNT";
	private static final String NAME = "NAME";
	private static final String SHEET = "SHEET";
	/**
	 * 存放excle cell與 column 的 映射,row设置值按照 映射後的column名称设置
	 * Map<CellName,ColumnName>
	 */
	Map<String,String> ccMap=new HashMap<String,String>();
	
	/**
	 *  存放excle cell與 column 的 映射,row设置值按照 映射後的column名称设置:
	 *  Map<CellName,EDColumn>
	 */
	Map<String,EDColumn> edMap=new HashMap<String, EDColumn>();
	
	Sheet sheet=null;
	Iterator<?> rowsIt=null;
	InputStream inp=null;
	private int titleCount;
	Interpreter i = new Interpreter();  // Construct an interpreter
	public ExcleData() {
		// TODO Auto-generated constructor stub
		super();
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		super.parseElement();
		while (hasNextKoElement()) {
			Task koTask = parseNextKoElement();
			if(koTask instanceof EDColumn){
				EDColumn edc=(EDColumn) koTask;
				ccMap.put(edc.getCellName(),edc.getColumnName());
				edMap.put(edc.getCellName(),edc);
			}
			
		}
		setTitleCount(propertys.get(TITLECOUNT));
		om.info("正在解析 Excle,文件名:"+getName()+",標題行數:"+getTitleCount()+",標籤頁(sheet):"+getExcleSheetAt());
		try{
			inp = new FileInputStream(new File(getName()).getCanonicalPath());
			Workbook wb = WorkbookFactory.create(inp);
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
	
	public org.maximo.tools.impxml.task.Row createRow(){
		org.maximo.tools.impxml.task.Row row=new org.maximo.tools.impxml.task.Row();
		row.setConnection(con);
		row.setTable(table);
		row.setOutMessage(om);
		row.init();
		return row;
	}
	
	public org.maximo.tools.impxml.task.Row next() throws MTException {
		// TODO Auto-generated method stub
			org.maximo.tools.impxml.task.Row ixrow=createRow();
//			Date d1=new Date();
			
//	Document	document = DocumentHelper.createDocument();
//		document.setXMLEncoding("UTF-8");
//			Element rowEle = document.addElement("ROW");
//			Date d2=new Date();
//			om.info("話費時間"+(d1.getTime()-d2.getTime())+"ms");
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
					//                            konntenntu=""+(cell.getNumericCellValue());//获取公式执行结果
					break;
				default:
					taipu="default";
				}
//				om.info("單元格類型:"+taipu);
				Object value=getMergedRegionValue(sheet, cell);
				//bsh
				String attName=columnEgo(cell.getColumnIndex()+1);
				EDColumn edColumn = edMap.get(attName);
				//						om.info("attName="+attName);
				//						om.info("edColumn="+edColumn);
				//						om.info("value="+value);
				if(edColumn!=null&&!IXFormat.isNullOrTrimEmpty(edColumn.getCdata())){
					try {
						i.set("value", value);
						i.set("ixf", new IXFormat());
						i.set("om", om);
						Object eval = i.eval(edColumn.getCdata());
						if(eval!=null){
							value= eval;
						}
					} catch (EvalError e) {
						// TODO Auto-generated catch block
						throw new MTException("表 ["+ixrow.getTable().getName()+"],頁["+getExcleSheetAt()+"],列["+attName+"] 的 (DEFAULTSET.COLUMN)beanshell腳本錯誤[位於第"+row.getRowNum()+"行數據]:",e);
					}


				}
				
				if(value!=null&&(!value.toString().isEmpty())){
//					om.info("excle(行號:列號):"+(cell.getRowIndex()+1) + ":" + columnEgo(cell.getColumnIndex()+1)+"\t");
//					om.info("excle單元格值:"+value);
					
						attName=formatColumnName(cell.getColumnIndex());
//						addColumn(rowEle,attName , value);
//					ixrow.setValue(ccMap.get(columnEgo(cell.getColumnIndex()+1)), value);
						
//					om.info("attName="+attName);
//					om.info(IXFormat.trimUpperCase(ixrow.getDBNameAuto(attName)));
					ixrow.setValueAuto(attName, value);
					ixrow.setValue(attName, value);
					Column column = ixrow.getColumns().get(IXFormat.trimUpperCase(ixrow.getDBNameAuto(attName)));
//					om.info("column1="+column);
					if(column!=null){
						column.setBeginValue(value);
					}
					column = ixrow.getColumns().get(attName);
//					om.info("column2="+column);
					if(column!=null){
						column.setBeginValue(value);
					}
//					om.info(ixrow.getStringAuto(ixrow.getDBName(columnEgo(cell.getColumnIndex()))));
				}
			}
//			ixrow.setElement(rowEle);
//			om.info(rowEle.asXML());
//			ixrow.parseElement();
			
			return ixrow;
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
	
	public String getName() {
		// TODO Auto-generated method stub
		return IXFormat.trim(propertys.get(NAME));
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
		String s=propertys.get(SHEET);
		if(!IXFormat.isNullOrTrimEmpty(s)){
			return Integer.parseInt(s);
		}
		return 0;
	}

}
