package com.maximo.tools.impxml.task;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.maximo.app.MTException;

public class TESTSAVEExcle {
	HSSFWorkbook workbook=null;
	HSSFSheet sheet =null;
	// 设置单元格类型 
	static HSSFCellStyle cellStyle = null; 
	
	public TESTSAVEExcle() {
		// TODO Auto-generated constructor stub
		
		workbook = new HSSFWorkbook(); 
		sheet = workbook.createSheet("分组字段内容为空"); 
		cellStyle = workbook.createCellStyle(); 
		//        cellStyle.setFont(font); 
		//        cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER); // 水平布局：居中 
		cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 水平布局：居中 
		cellStyle.setWrapText( true ); 
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
	
	public static void main(String[] args) throws MTException {
		TESTSAVEExcle t=new TESTSAVEExcle();
		t.save("Z:/1.xls");
	}
}
