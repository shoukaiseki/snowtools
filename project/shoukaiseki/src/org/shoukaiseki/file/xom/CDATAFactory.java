package org.shoukaiseki.file.xom;

import java.io.IOException;
import java.io.StringReader;

import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.ParsingException;
import nu.xom.Text;

/**
 * 创建CDATA段的辅助类
 */
public class CDATAFactory {
	private static final Text PROTOTYPE; // this is really an instance of
											// CDATASection

	static {
		Text temp = null;

		try {
//			 XOM preserves existing CDATA's so start with a doc that has one
//			<text></text>作为Document读取时的父节点,可以随便起名
			String docWithCDATA = "<text><![CDATA[prototype]]></text>";

			Builder builder = new Builder();
			
			//在Document中才能setValue只改变[]中的值
			Document document = builder.build(new StringReader(docWithCDATA));

			// grab the resulting CDATASection and keep it around as a prototype
			temp = (Text) document.getRootElement().getChild(0);
			//分离出<![CDATA[]]作为父节点标识,不然会出现  [nu.xom.CDATASection: prototype] child already has a parent.
			temp.detach();
		} catch (IOException e) {
			// not worried about IOExceptions just reading a string
		} catch (ParsingException e) {
			// already know this document is valid and will parse
		}

		PROTOTYPE = temp;
	}

	public static Text makeCDATASection(String value) {
		// use copy and setValue to get a brand new CDATA section
		Text result = (Text) PROTOTYPE.copy();
		result.setValue(value);
		return result;
	}

}


