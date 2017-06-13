package com.maximo.tools.expxml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.maximo.app.MTException;
import com.maximo.app.OutMessage;

public class MakeExcle {
	ResultSet r=null;
	OutMessage om=null;
	HSSFWorkbook workbook=null;
	HSSFSheet sheet =null;
	// 设置单元格类型 
	static HSSFCellStyle cellStyle = null; 
	int rowNum=1;
	int cellNum=0;
	//數據庫列樹
	int columnCount=0;
	ResultSetMetaData rsmd =null;
	private String groupTable;
	private HashMap<String,ExcleGroup> groupTableMap=null;
	public MakeExcle(OutMessage om,ResultSet r) throws MTException {
		this.om=om;
		om.info("正在初始化excle文件模板");
		// 创建新的Excel 工作簿 
		workbook = new HSSFWorkbook(); 
		om.info("excle文件模板初始化完成");
		this.r=r;
	}
	
	/** 创建表头
	 * @param sheetName
	 * @throws MTException 
	 */
	public HSSFSheet  createSheet(String sheetName) throws MTException{
		// 在Excel工作簿中建一工作表，其名为缺省值, 也可以指定Sheet名称 
		om.info("sheetName="+sheetName);
		if(sheetName!=null){
			sheet = workbook.createSheet(sheetName); 
		}else{
			sheet = workbook.createSheet(); 
		}
		// TODO Auto-generated constructor stub
		cellStyle = workbook.createCellStyle(); 
		//        cellStyle.setFont(font); 
		//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中 
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平布局：居中 
		cellStyle.setWrapText( true ); 
		HSSFRow row = sheet.createRow(0); 
		om.info("正在創建excle標題");
		try {
			rsmd = r.getMetaData();
			columnCount = rsmd.getColumnCount();
			String[] strs = new String[columnCount];
			for (int i = 0; i < rsmd.getColumnCount(); i++) {
				om.debug(rsmd.getColumnName(i+1)+"\t");
				strs[i] = rsmd.getColumnName(i + 1);
				// 创建单元格 
				setCellData(workbook, row, cellNum++, rsmd.getColumnName(i+1) );
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
		
		return sheet;
	}
	
	public void addRow() throws MTException{
		try {
			HSSFRow row =null;
			if(groupTable!=null){
				String sheetName = r.getString(groupTable);
				if(sheetName==null){
					sheetName="分组字段内容为空";
				}
				ExcleGroup excleGroup = groupTableMap.get(sheetName);
				if(excleGroup==null){
					excleGroup=new ExcleGroup();
					cellNum=0;
					sheet =createSheet(sheetName);
					excleGroup.setSheet(sheet);
					excleGroup.setSheetName(sheetName);
					groupTableMap.put(sheetName, excleGroup);
				}else{
					sheet=excleGroup.getSheet();
				}
				row = sheet.createRow(excleGroup.getNextRowNum()); 
			}else{
				if(sheet==null){
					cellNum=0;
					createSheet(null);
				}
				row = sheet.createRow(rowNum ++); 
			}
			cellNum=0;
			for (int i = 0; i < columnCount; i++) {
				String field = rsmd.getColumnName(i + 1);
				if (rsmd.getColumnType(i + 1) == Types.DATE) {
					Timestamp time = r.getTimestamp(i + 1);
//					om.debug(field + "(TimeStamp)= " + time);
				}else{
					String value = r.getString(i + 1);
//					om.debug(field + " = " + value);
					setCellData(workbook, row, cellNum, value);
				}
				cellNum++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
	}
	
	
	/**
	 * 调整列宽為自動適應
	 */
	public void setAutoSizeColumn(){
		for (int i = 0; i < columnCount; i++) {
			sheet.autoSizeColumn(( short ) i ); // 调整第一列宽度 
		}
	}

	public  void setCellData(HSSFWorkbook workbook,HSSFRow row, int cellNum, String value ){
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
	
	public void save(String fileName) throws MTException{
			try {
				FileOutputStream fileOut = new FileOutputStream(new StringBuffer(fileName).toString());
				workbook.write(fileOut);
				fileOut.close(); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				throw new MTException(e);
				
			} 
		
	}

	public void setGroupTable(String groupTable) {
		// TODO Auto-generated method stub
		this.groupTable=groupTable;
			groupTableMap=new HashMap<String, ExcleGroup> ();
	}
	
	
	class ExcleGroup{
		int rowNum=0;
		private HSSFSheet sheet=null;
		private String sheetName=null;
		public ExcleGroup() {
			// TODO Auto-generated constructor stub
		}
		
		public int getNextRowNum() {
			// TODO Auto-generated method stub
			return ++rowNum;
		}

		public int getRowNum(){
			return rowNum;
		}
		
		public String getSheetName() {
			return sheetName;
		}

		public void setSheetName(String sheetName) {
			this.sheetName = sheetName;
		}

		public HSSFSheet getSheet() {
			return sheet;
		}

		public void setSheet(HSSFSheet sheet) {
			this.sheet = sheet;
		}
		
	}
}
