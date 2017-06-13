package org.app;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import org.shoukaiseki.characterdetector.CharacterEncoding;
import org.shoukaiseki.constantlib.CharacterEncodingName;
import org.shoukaiseki.file.FindFile;
import org.shoukaiseki.string.ConCatLineBreaks;

/**
 *com.app.Iconv	編碼批量轉換
 * @author 蒋カイセキ    Japan-Tokyo 2012-6-26
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class Iconv {
	
	public static final String VERSION = "v1.0,2012-06-21 21:29:12";
	public static final String UTF_8 = "UTF-8";
	public static final String GBK = "GBK";
	public static ConCatLineBreaks content = null;// 显示到jtextpane的内容
	/**
	 * 换行符,因UTF8与GBK编码换行符不同
	 */
	public static final String LINE_BREAKS = "\r\n";
	/**
	 * 配置文件目录
	 */
	public static final String EXTENSION_PATH = "./Insert_SyntaxHighlighter/";
	public static int zenkai = 0;// 前回输出的字符数
	public static SimpleDateFormat bartDateFormat = new SimpleDateFormat(
			"yyyy年MM月dd日 HH:mm:ss ");
	public static Calendar cal = Calendar.getInstance();
	public static Date date = new Date();
	public static Date rundate = new Date();// 开始正常运行时间

	public static String urlstring = "";
	public static File file = new File("./Insert_SyntaxHighlighter");
	public static File txtfile = new File(
			"./Insert_SyntaxHighlighter/AutoSyntaxHighlighter.ora");

	public static String extension = "";
	public static String sourceCode = "";
	public static String outCode = "";
	//調試模式將輸出所有詳細信息
	public static boolean debug=false;

	public static void help() {
		System.out.println("蒋カイセキ    Iconv version " + VERSION);
		System.out.println("ブログ http://shoukaiseki.blog.163.com/");
		System.out.println("E-メール jiang28555@Gmail.com");
		System.out.println("使用說明:輸出文件格式爲UTF-8");
		System.out.println("用法:Iconv [選項] 源目錄 目標目錄");
		System.out.println("长选项必须使用的参数对于短选项时也是必需使用的。");
		System.out.println("  -h, --help                使用說明");
		System.out.println("  -name flename             指定文件名,支持通配符模式");
		System.out.println("  -debug                    調試模式,詳細輸出");
		System.out.println("  --version                 顯示版本號");
	}

	public static void main(String ages[]) throws IOException {

		sourceCode = "./";
		extension = "*";
		outCode = "./";
		if (ages.length < 2) {
			help();
			System.exit(0);
		}

		for (int i = 0; i < ages.length; i++) {
			String option = ages[i];
			if (option.equals("-h") || option.equals("--help")) {
				help();
				System.exit(0);
			}
			if (option.equals("-name")) {
				i++;
				if (i >= ages.length) {
					System.out.println(colorString("Error:")
							+ "例如 -name *.java");
					System.exit(0);
				}
				extension = ages[i];
				continue;
			}
			if (option.equals("-version")) {
				System.out.println("蒋カイセキ    Iconv version " + VERSION);
				System.out.println("ブログ http://shoukaiseki.blog.163.com/");
				System.out.println("E-メール jiang28555@Gmail.com");
				continue;
			}
			if (option.equals("--debug")) {
				debug=true;
				continue;
			}
			sourceCode = option;
			i++;
			if (i >= ages.length) {
				System.out.println(colorString("Error:") + "例如 ./src ./srcbak");
				System.exit(0);
			}
			outCode = ages[i];
		}
		
		System.out.print("源目錄爲 "+sourceCode);
		System.out.print("   目標目錄爲 "+outCode);
		System.out.println("   文件名爲 "+extension);

		new Iconv();
		try {
			FindFile star = new FindFile();
			File[] files = star.getFiles(sourceCode, extension);// 查找sourceCode目录下的extension后缀名的文件
			for (int i = 0; i < files.length; i++) {
				System.out.println(files[i].getPath());
				outTo(files[i]);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("目录下无任何文件或无此目录!");
		}
	}

	public static File newMkDir(String fileName) {
		File htmlFile = new File(fileName);
		File folder = new File(htmlFile.getParent());
		try {
			if (!folder.isDirectory()) {
				if (!folder.mkdirs()) {
					System.out.println("####创建目录" + folder + "失败,可能为权限不足!####");
				}
			}
			if (!htmlFile.exists()) {
				if (!htmlFile.createNewFile()) {
					System.out.println("####创建文件" + htmlFile
							+ "失败,可能为权限不足!####");
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return htmlFile;
	}

	public static void mkDir() {
		if (!file.isDirectory()) {
			file.mkdir();
			println("成功创建一个文件夹" + file + "！");
		}
	}

	public static void newFile() throws IOException {
		// 新建文件
		if (!txtfile.exists()) {
			if (txtfile.createNewFile()) {
				println("配置文件创建成功!");
			} else {
				System.out.println(colorString("创建新文件失败!"));
			}
		} else {
			println("发现配置文件" + txtfile + "!");
		}
	}

	public static void outTo(File thisfile) {
		ConCatLineBreaks writeConten = new ConCatLineBreaks();
		String fileName = thisfile.getPath();
		// 将\\替换为/,//也替换为/
		fileName = fileName.replace("\\", "/").replace("//", "/");
		String htmlFileName = fileName.replace(sourceCode, outCode);
		String extensionName = fileName.substring(
				fileName.lastIndexOf(".") + 1, fileName.length());
		newMkDir(htmlFileName);
		// System.out.println(htmlFileName);

		// System.out.println(extensionContent);

		/**
		 * 读取源码内容
		 */
		String s = readFile(fileName);
		writeConten.addLastLine(s);
		writeConten.addLastLineBreaks();

		wrFile(new File(htmlFileName), writeConten.getContent());
	}

	public static void wrFile(File file, String age0) {
		println("正在创建文件" + file.getPath());
		try {
			// 常量类:各编码名称
			CharacterEncodingName ce = new CharacterEncodingName();
			FileOutputStream o = new FileOutputStream(file);
			// 采用UTF-8编码格式输出
			OutputStreamWriter out = new OutputStreamWriter(o, ce.UTF_8);
			out.write(age0);
			println("文件创建写入成功");
			out.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
	}

	/**
	 * 读入文件,自动编码转换,返回文件内容
	 * 
	 * @param fileName
	 *            完整文件名
	 * @return content 文件内容
	 */

	public static String readFile(String fileName) {
		String content = "";
		try {
			FileInputStream in = new FileInputStream(fileName);
			println("文件字数为=" + in.available());

			byte[] bytes = new byte[in.available()];
			while ((in.read(bytes)) != -1)
				;
			String code = CharacterEncoding.getLocalteFileEncode(fileName);
			println("文件编码格式为" + code);

			content = new String(bytes, code);// 按照文件编码格式进行转换为系统编码
			// System.out.println("文件内容为\n"+content);
//			System.out.println(content);
			in.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return content;
	}

	public static String colorString(String from) {
		String to = "\033[1;30;31m" + from + "\033[0m";
		to = "\033[1;30;31m" + from + "\033[0m";
		return to;
	}
	public static void println(String s){
		if(debug){
			System.out.println(s);
		}
	}

}
