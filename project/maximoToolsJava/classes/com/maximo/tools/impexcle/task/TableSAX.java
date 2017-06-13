package com.maximo.tools.impexcle.task;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler;
import org.apache.poi.xssf.eventusermodel.XSSFSheetXMLHandler.SheetContentsHandler;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.*;
import org.dom4j.io.SAXReader;
import org.dom4j.tree.DefaultCDATA;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import bsh.EvalError;
import bsh.Interpreter;

import com.alibaba.druid.pool.DruidDataSource;
import com.maximo.app.MTException;
import com.maximo.app.MessageOnTerminal;
import com.maximo.app.OutMessage;
import com.maximo.app.config.ReadDruidDataSourceKonnfigu;
import com.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class TableSAX extends TaskSAX{
	public static final String TABLE="TABLE";
	public static final String INCLUDEATTS="INCLUDEATTS";
	public static final String EXCLUDEATTS="EXCLUDEATTS";
	public static final String JDBCNAME="JDBCNAME";
	public static final String EXCLE="EXCLE";
	
	/**
	 * Bsh引入的包名
	 */
	public static final String BSHIMPORT=""+
										" import com.maximo.tools.impexcle.task.*;" +"\n"+
										" import com.maximo.tools.impexcle.*;"+"\n"+
										" import java.sql.Connection;"+"\n"+
										" import java.sql.PreparedStatement;"+"\n"+
										" import java.sql.ResultSet;"+"\n"+
										" import java.sql.SQLException;"+"\n"+
										"";
	
	/**
	 * 错误标识,如果出现error则置1,因为SAX解析无法中途报错停止
	 */
	boolean error=false;
	
	
	Map<String,String> dataMap=null;
	long rownum=0L;
	Element root=null;
	Document doc=null;
	private TriggerBeanShellSAX triggerBeanShell=null;
	private Interpreter triggerBeanShellI = new Interpreter();  // TRIGGERBEANSHELL 使用的 bsh主類 ,因為實例化需要話費10多毫秒
	private ExcleDataSAXSAX excleDataSax=null;
	
	OracleSqlDetabese osd=null;
	PreparedStatement ps =null;
	
	//要插入的字段
	List<String> incolumns=new ArrayList<String>();
	private int titleCount;
	private int includeRowCount=0;
	private int excludeRowCount=0;
	
	public TableSAX(Connection con,OutMessage om) throws MTException {
		this.om=om;
		
	}
	
	
	public TableSAX(String xmlName) throws MTException {
		// TODO Auto-generated constructor stub
		readXml(xmlName);
		
	}
	public void execution(String xmlName) throws DocumentException, MTException, SQLException{
		//改變工作目錄為xml所在目錄
		File file = new File(xmlName);
		try {
			file = file.getCanonicalFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
	}
		System.setProperty("user.dir",file.getParent());
		readXml(xmlName);
		closeFile();	

	}
	public void readXml(String xmlName) throws MTException{
		// 读取XML文件
		
		SAXReader reader= new SAXReader();
		try {
			System.out.println(xmlName);
		
			doc = reader.read(new File(xmlName));
//			doc = reader.read(xmlName);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			throw new MTException("讀取xml文件失敗:",e);
		}
		// 获取XML根元素
		 root = doc.getRootElement();
		 setElement(root);
		 setTaskName("RESULTS");
		 parseElement();
	}
	
	@Override
	public void parseElement() throws MTException {
		// TODO Auto-generated method stub
		try {
			super.parseElement();
			String rootTable=getName();
			String rootIncludeAtts = getProperty("INCLUDEATTS");
			String rootExcludeAtts = getProperty("EXCLUDEATTS");
			String jdbcname = getProperty("JDBCNAME");
			om.info("主表名為:" + rootTable);
			om.info("jdbc鏈接名稱為:" +jdbcname);
			om.info("插入的字段有:"+rootIncludeAtts);
			om.info("忽略的字段有:"+rootExcludeAtts);
			ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(om);
			DruidDataSource druid = rddsk.getDruidDataSource(jdbcname);
			setConnection(druid.getConnection());

			con.setAutoCommit(false);
			setTable(this);
			while (hasNextKoElement()) {
				TaskSAX koTask = parseNextKoElement();
				if(koTask instanceof TriggerBeanShellSAX){
					this.triggerBeanShell=(TriggerBeanShellSAX) koTask;
				}else if(koTask instanceof ExcleDataSAXSAX){
					this.excleDataSax=(ExcleDataSAXSAX) koTask;
					titleCount=excleDataSax.getTitleCount();
				}
			}
			try {
				formatSQL();
			} catch (Exception e) {
				// TODO: handle exception
				throw new MTException(e);
			}
			if(excleDataSax==null){
				om.info("未找到 ExcleDataSAXSAX 节点信息,无法进行导入操作");
				return;
			}
			om.info("正在解析 Excle,文件名:"+excleDataSax.getName()+",標題行數:"+excleDataSax.getTitleCount()+",標籤頁(sheet):"+excleDataSax.getExcleSheetAt());
			insertToDetaesu();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			throw new MTException(e);
		}
		
	}
	
	public void formatSQL() throws MTException, SQLException{
		//插入的主表
		String rootTable=getName();
		String rootIncludeAtts = getProperty("INCLUDEATTS");
		if(rootIncludeAtts!=null&&!rootIncludeAtts.trim().isEmpty()){
			for(String attName:rootIncludeAtts.trim().toUpperCase().split(",")){
				if(!attName.trim().isEmpty()){
					attName=attName.trim().toUpperCase();
					incolumns.add(attName);
					om.debug("插入的字段名稱:"+attName);
				}
			}
		}
		StringBuffer insertSql =null;
		StringBuffer valuesSql=null;
		for(String column:incolumns){
			if(insertSql==null){
				insertSql=new StringBuffer("insert into ").append(rootTable).append("(").append(column);
				valuesSql=new StringBuffer("?");
			}else{
				insertSql.append(",").append(column);
				valuesSql.append(",?");
			}
		}
		insertSql.append(") values(").append(valuesSql).append(")");
		om.info("插入sql語句為:"+insertSql);
		osd = new OracleSqlDetabese(con);
		osd.setSql(insertSql.toString());
		ps = osd.prepareStatement();
		
	}


	public String getName() {
		return propertys.get(TABLE);
	} 


	public void closeFile(){
		doc.clearContent();
	}


	/** 插入数据至数据库
	 * @throws MTException
	 * @throws SQLException 
	 */
	public  void insertToDetaesu() throws MTException, SQLException   {
		OPCPackage container;
		try {
			container = OPCPackage.open(new File(excleDataSax.getName()).getCanonicalPath());
			ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(
					container);
			XSSFReader xssfReader = new XSSFReader(container);
			StylesTable styles = xssfReader.getStylesTable();
			//			XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader .getSheetsData();
			InputStream stream =xssfReader.getSheet("rId"+excleDataSax.getExcleSheetAt());
			processSheet(styles, strings, stream);
			stream.close();
			
			if(error){
				getConnection().rollback();
			}else{
				om.info(getName()+" 表共計行數:"+(includeRowCount+excludeRowCount));
				om.info(getName()+" 表插入行數:"+includeRowCount);
				om.info(getName()+" 表過濾行數:"+excludeRowCount);
				om.info("提交'"+getName()+"'表的數據");
				ps.executeBatch();
				osd.close();
				getConnection().commit();
			}
		} catch (Exception e) {
			om.error(om.getTrace(e));
			getConnection().rollback();
		}

	}

	protected void processSheet(StylesTable styles,
			ReadOnlySharedStringsTable strings, InputStream sheetInputStream)
					throws Exception {
		InputSource sheetSource = new InputSource(sheetInputStream);
		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader sheetParser = saxParser.getXMLReader();
		ContentHandler handler = new XSSFSheetXMLHandler(styles, strings,
				new SheetContentsHandler() {

			@Override
			public void startRow(int rowNum) {
				rownum=rowNum+1;
				dataMap=new HashMap<String,String>(); 
				replaceLineMessageInTerminal("正在解析第["+new DecimalFormat("00000000").format(rownum-titleCount)+"]行數據");
//				om.info("rowNum="+rowNum);
			}


			@Override
			public void endRow() {
				if(error||rownum<=titleCount)return;
				boolean canAdd=true;
				try {
				//執行 TRIGGERBEANSHELL 標籤的bsh腳本
				if(triggerBeanShell!=null&&triggerBeanShell.getCdata()!=null&&!triggerBeanShell.getCdata().isEmpty()&&triggerBeanShell.isBshDataIsEnabled()){
					Object eval=null;
					try {
						triggerBeanShellI.set("datamap", dataMap);
						triggerBeanShellI.set("table", table);
						triggerBeanShellI.set("om", om);
						triggerBeanShellI.eval(BSHIMPORT);
						eval = triggerBeanShellI.eval(triggerBeanShell.getCdata());
						if(eval!=null){
							try {
								if(!(boolean) eval){
									canAdd=false;
									om.debug("表 "+getName()+"第"+(rownum-titleCount)+"行數據取消插入.");
									excludeRowCount++;	
								}
							} catch (Exception e) {
								// TODO: handle exception
								om.warn("(TRIGGERBEANSHELL)beanshell腳本錯誤,無效的返回值,如果指定返回值,則必須返回 boolean 類型");
							}
						}
					} catch (EvalError e) {
						// TODO Auto-generated catch block
						error=true;
						new MTException("表 "+getName()+" 的 (TRIGGERBEANSHELL)beanshell腳本錯誤[位於第"+(rownum-titleCount)+"行數據]:",e).printStackTrace();
					}
				}
				if(canAdd){
					for (String attName : incolumns) {
						ps.setObject(incolumns.indexOf(attName)+1, dataMap.get(attName));
					}
					includeRowCount++;
					ps.addBatch();
					if(includeRowCount%1000==0){
						ps.executeBatch();
					}
				}
//					om.info(rownum-titleCount);
					//om.info("endRow");
				} catch (SQLException e) {
					error=true;
					new MTException("第"+rownum+"行出错",e).printStackTrace();
				}
			}

			@Override
			public void cell(String cellReference,
					String formattedValue) {
				String col=cellReference.replaceAll(rownum+"", "");
				String value=formattedValue;
				if(value!=null){
					value=value.trim();
				}
				dataMap.put(excleDataSax.ccMap.get(col), value);
//				om.info("cellReference="+cellReference);
//				om.info("formattedValue="+value);
			}

			@Override
			public void headerFooter(String text, boolean isHeader,
					String tagName) {
			}
		}, false);
		sheetParser.setContentHandler(handler);
		sheetParser.parse(sheetSource);

	}



	/** 替換該行信息,支線linux終端
	 * @param s
	 */
	public void replaceLineMessageInTerminal(String s){
		tcc.moveToFirstLine();
		tcc.clearToEndOfLine();
		tcc.print(s);
	}

//	public static void main(String[] args) throws MTException {
//		ReadExcleDataSAX red = new ReadExcleDataSAX("F:/SVN/CRPXZ/7 配置开发/導入数据/徐州项目/kks/shoukaiseki_insert_kks.xml");
//		red.init();
//		red.closeFile();
//
//	}

}
