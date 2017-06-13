
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultCDATA;
import org.shoukaiseki.tuuyou.logger.PrintLogs;


public class TestXml extends PrintLogs{
//	public static String fileName =  "/tmp/del/testxml/database-configure.xml";
	public static String fileName =  "Z:/database-configure.xml";
	public TestXml() {
		// TODO Auto-generated constructor stub
		level=7;
		mkDatabaseConfigureXML();
	}

	public void mkDatabaseConfigureXML() {
		debug("mkDatabaseConfigureXML filename=" + fileName);
		error("mkDatabaseConfigureXML filename=" + fileName);
		String encoding = "UTF-8";
		// encoding="GBK";
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(encoding);
		Element references = document.addElement("ROW");
		Element reference = references.addElement("COLUMN");
		reference.addAttribute("name", "localhost_orcl");
		reference.add(new DefaultCDATA("asus"));
		System.out.println(document.getRootElement().asXML());
	}

	public void addStringRefAddr(Element refAddresses, String addrType,
			String Contents) {
		Element stringRefAddr = refAddresses.addElement("StringRefAddr");
		stringRefAddr.addAttribute("addrType", addrType);
		Element elementContents = stringRefAddr.addElement("Contents");
		elementContents.addText(Contents);
	}

	public static void main(String[] args) {
		new TestXml();
		System.out.println("完成:"+fileName);
	}

}
