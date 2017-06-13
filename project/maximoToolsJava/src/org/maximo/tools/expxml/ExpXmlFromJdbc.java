package org.maximo.tools.expxml;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;

import org.dom4j.DocumentException;
import org.dom4j.Element;

import com.alibaba.druid.pool.DruidDataSource;
import org.maximo.app.MTException;
import org.maximo.app.OutMessage;
import org.maximo.app.config.ReadDruidDataSourceKonnfigu;
import org.shoukaiseki.sql.oracle.OracleSqlDetabese;

public class ExpXmlFromJdbc extends ExpXmlTemplate{
	
	
	public ExpXmlFromJdbc(OutMessage om) {
		// TODO Auto-generated constructor stub
		super(om);
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
		readDatabaseConfigureXML(xmlName);
		closeFile();	
	}

	/**
	 * @param xmlName
	 * @throws DocumentException
	 * @throws MTException 
	 * @throws SQLException 
	 */
	private void readDatabaseConfigureXML(String xmlName) throws DocumentException, MTException, SQLException {
		super.readXml(xmlName);
		parseElement();
	}
	
	
	
	public void parseElement() throws MTException{
		Element sqlE = root.element("SQL");
		String outFile = getElementAttribute(sqlE,"OUTFILE");
		String sql=sqlE.getText();
		String jdbcName=getElementAttribute(sqlE,"JDBCNAME");
		//分组（该组名称不同时采用新标签页）
		String groupTable=getElementAttribute(sqlE,"GROUPTABLE");
		ReadDruidDataSourceKonnfigu rddsk = new ReadDruidDataSourceKonnfigu(om);
		DruidDataSource druid = rddsk .getDruidDataSource(jdbcName);
		OracleSqlDetabese osd=null;
		om.info("jdbcName="+jdbcName);
		om.info("outfile="+outFile);
		om.debug("sql="+sql);
		MakeExcleSXSSF me=null;
		try {
			om.info("正在創建數據庫鏈接");
			osd = new OracleSqlDetabese(druid.getConnection());
			om.info("已鏈接至數據庫");
			osd.setSql(sql);
			//数据行多的时候使用 r.last会卡死
			ResultSet r = osd.executeQuery(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			om.info("开始运行sql语句");
			me=new MakeExcleSXSSF(om, r);
			me.setGroupTable(groupTable);
			
			
			om.info("正在添加數據至excle");
			try {
				int rownum=1;
				while(r.next()){
					me.addRow();
					replaceLineMessageInTerminal("正在插入第["+new DecimalFormat("00000000").format(rownum++)+"]行數據");
				}
				if(me!=null){
					me.save(outFile);
					om.info("已成功導出.");
				}
			} catch (MTException e) {
				// TODO Auto-generated catch block
				om.error("",e);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			om.error("",e);
		}
		
		if(osd!=null){
			osd.close();
		}
		if(druid!=null){
			druid.close();
		}
	}
	
	/**
	 * @param sqlE
	 * @param attName
	 * @return 如果无此属性则为空
	 */
	public String getElementAttribute(Element sqlE,String attName){
		String s="";
		if(sqlE.attribute(attName)!=null){
			s=sqlE.attribute(attName).getValue();
		}
		return s;
	}
	
	/** 替換該行信息,支線linux終端
	 * @param s
	 */
	public void replaceLineMessageInTerminal(String s){
		tcc.moveToFirstLine();
		tcc.clearToEndOfLine();
		tcc.print(s);
	}

}