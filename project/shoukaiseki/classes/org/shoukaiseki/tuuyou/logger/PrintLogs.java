package org.shoukaiseki.tuuyou.logger;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.*;

import org.shoukaiseki.tuuyou.logger.config.ConfigPath;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;



/**
 * 輸出日誌 org.shoukaiseki.tuuyou.logger.PrintLogs
 * 
 * @author 蒋カイセキ Japan-Tokyo 2012-6-29
 * @ブログ http://shoukaiseki.blog.163.com/
 * @E-メール jiang28555@Gmail.com
 */
public class PrintLogs {
	public static final int KERN_EMESG = 0; // 致命级
	public static final int KERN_ALERT = 1; // 警戒级
	public static final int KERN_CRIT = 2; // 临界级
	public static final int KERN_ERR = 3; // 错误级
	public static final int KERN_WARN = 4; // 告警级
	public static final int KERN_NOTICE = 5; // 注意级
	public static final int KERN_INFO = 6; // 通知级
	public static final int KERN_DEBUG = 7; // 调试级
	public static final String USER_CONFIG=ConfigPath.CONFIG_PATH;
	

	/**
	 * 不同類日誌級別
	 */
	public static	Properties classLogLevel = null;
	/**
	 * log4j.properties文件
	 */
	public static Properties log4j = new Properties();
	private static boolean autoReloadLog4j=false;
	private static long log4jDelay=0L;
	/**
	 * 是否自動重載classLogLevel文件
	 */
	public static boolean autoReadLogLevelProperties=false;
	private static final String classLogLevelName=USER_CONFIG+"classLogLevel.properties";
	private static final String webClassLogLevelName=USER_CONFIG+"classLogLevel.properties";
	private static File classLogLevelFile=null;
	private static long logLeveTime=0L;
	private static String configFilename=null;
	public static Logger logger = null;
	public int level = 5;//設置調試級別,默認爲5
	private  boolean debugTime=false;
	/**
	 * logger輸出控制台的 Appender
	 */
	public static ConsoleAppender consoleAppender=null;

	public PrintLogs() {
		// TODO Auto-generated constructor stub
		//		System.setProperty("HOME", "/home/fedora");
//		System.out.println(System.getProperty("java.version"));
//		System.out.println("java.security.policy="+System.getProperty("java.security.policy"));
//		System.setProperty("java.security.policy","/media/linux/data/opt/java/jdk1.7.0_04/jre/lib/security/java.policy");
//		System.out.println("java.security.policy="+System.getProperty("java.security.policy"));
		classLogLevelFile=new File(classLogLevelName);
		configFilename=USER_CONFIG+"log4j.properties";
		init();
		loadLog4j();
		
	}
	
	public void setLevel(int level){
		this.level=level;
	}
	
	public static void init(){
		logLeveTime=classLogLevelFile.lastModified();
	}

	public void refreshLogger(){
		System.out.println("-----------------重載"+configFilename+"----------------");
		PropertyConfigurator.configure(log4j);
		logger=Logger.getLogger("printLogs");
	}

	public void loadLog4j() {
		InputStream stream =null;
		try {
			if(logger==null){
				System.out.println("-----------------加載l"+configFilename+"----------------");
				stream = new FileInputStream(configFilename);
				log4j.load(stream);
				if(autoReloadLog4j){
					if(log4jDelay==0L){
						PropertyConfigurator.configureAndWatch(configFilename);
					}else{
						PropertyConfigurator.configureAndWatch(configFilename,1L);
					}
				}else{
					PropertyConfigurator.configure(log4j);
				}
				logger=Logger.getLogger("printLogs");
				consoleAppender = new ConsoleAppender();
				consoleAppender.setName("syso");
//				consoleAppender.setLayout(new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n"));
				consoleAppender.setLayout(new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n"));
				consoleAppender.setThreshold(Level.DEBUG);
				consoleAppender.setWriter(new PrintWriter(System.out));
				logger.addAppender(consoleAppender);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setClass(Class cls){
//		logger = Logger.getLogger(cls);
	}
	
	/**調試級別輸出
	 * @param message
	 * @param level
	 */
	public void debug(Object message,int level){
//		logger.debug("InLevel="+level+";level="+level);
		if(this.level>=level){
			autoReadClassLogLevelProperties();
			logger.debug(message);
		}
	}
	
	/**調試級別輸出
	 * @param message
	 * @param level
	 */
	public void debug(Object message,Throwable t){
//		logger.debug("InLevel="+level+";level="+level);
			autoReadClassLogLevelProperties();
			logger.debug(message,t);
	}
	
	/**輸出消息,前台也能看到
	 * @param message
	 */
	public void info(Object message){
		autoReadClassLogLevelProperties();
		info(message,null);
	}
	
	/**輸出消息,前台也能看到
	 * @param message
	 * @param t
	 */
	public void info(Object message,Throwable t){
		autoReadClassLogLevelProperties();
		logger.info(message,t);
	}
	
	/** 致命錯誤
	 * @param message
	 */
	public void fatal(Object message){
		autoReadClassLogLevelProperties();
		fatal(message,null);
	}
	
	/** 致命錯誤
	 * @param message
	 */
	public void fatal(Object message,Throwable t){
		autoReadClassLogLevelProperties();
		logger.fatal(message,t);
	}
	
	public void setDebugTime(boolean debugTime){
		this.debugTime=debugTime;
	}
	
	/**測試代碼執行效率
	 * @param message
	 */
	public void debugTime(Object message){
		if(debugTime){
			autoReadClassLogLevelProperties();
			logger.debug(message);
		}
	}
	
	public void debug(Object message) {
		autoReadClassLogLevelProperties();
		logger.debug(message);
	}

	public void error(Object message) {
		autoReadClassLogLevelProperties();
		logger.error(message);
	}
	
	public void println(){
		System.out.println();
	}
	
	public void println(Object message){
		System.out.println(message);
	}
	
	 public static String getTrace(Throwable t) {
	        StringWriter stringWriter= new StringWriter();
	        PrintWriter writer= new PrintWriter(stringWriter);
	        t.printStackTrace(writer);
	        StringBuffer buffer= stringWriter.getBuffer();
	        return buffer.toString();
	    }


	public void error(Object message, Throwable e) {
		// TODO Auto-generated method stub
		autoReadClassLogLevelProperties();
		logger.error(message, e);
	}
	
	/**設置日誌輸出到控制台的級別,默認為 Level.DEBUG
	 * @param level
	 */
	public void setConsoleAppenderLevel(Level level){
		if(consoleAppender!=null)
			consoleAppender.setThreshold(level);
	}
	
	/**設置日誌輸出到控制台的Layout,默認為 new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n")
	 * @param layout
	 */
	public void setConsoleAppenderLayout(PatternLayout layout){
		if(consoleAppender!=null)
				consoleAppender.setLayout(layout);
	}
	
	public void warn(Object message){
		autoReadClassLogLevelProperties();
		logger.warn(message);
	}
	
	public void warn(Object message,Throwable t){
		autoReadClassLogLevelProperties();
		logger.warn(message, t);
	}
	
	
	/**時間相減,獲取時間差,返回字符串爲  n分n秒毫秒
	 * @param timeOne
	 * @param timeTwo
	 * @return
	 */
	public String toukeiJikann(long timeOne,long timeTwo){
		long daysapart = (timeTwo - timeOne) / (1000 * 60);// 分钟
		long daysaparts = (timeTwo - timeOne) / 1000 % 60;// 获得总秒数后取除以60的余数即为秒数
		long daysapartms = (timeTwo - timeOne) % 1000;// 获得毫秒数
		String s=( daysapart + "分" + daysaparts + "秒"+daysapartms+"毫秒");
		return s;
	}
	
	public void reloadLogLevelProperties() throws IOException{
		classLogLevel=null;
		autoReadClassLogLevelProperties();
	}
	
	/**執行的自動判斷後加載加載 classLogLevel.properties 方法
	 * @throws IOException
	 */
	private static void autoReadClassLogLevelProperties() {
			if(autoReadLogLevelProperties||classLogLevel==null){
				if(classLogLevel==null||logLeveTime!=classLogLevelFile.lastModified()){
					classLogLevel=new Properties();
					logLeveTime=classLogLevelFile.lastModified();
					InputStream stream =null;
					try {
						stream = new FileInputStream(classLogLevelFile);
						classLogLevel.load(stream);
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					initAllFlag();
					System.out.println("-----------------加載/重載"+classLogLevelName+"----------------");
				}
			}
	}
	
	private static void initAllFlag(){
		autoReadLogLevelProperties=true;
		String str = classLogLevel.getProperty("auto.reload.Log4j");
		autoReloadLog4j=str!=null&&str.equals("true")?true:false;
		str=classLogLevel.getProperty("auto.reload.Log4j.delay");
		try {
			log4jDelay=Long.parseLong(str);
		} catch (Exception e) {
			// TODO: handle exception
			log4jDelay=0L;
		}
		str=classLogLevel.getProperty("auto.reload.classLogLevel");
		autoReloadLog4j=str!=null&&str.equals("true")?true:false;
	}
	
	static{
		classLogLevelFile=new File(classLogLevelName);
		configFilename=USER_CONFIG+"log4j.properties";
		try {
			init();
			autoReadClassLogLevelProperties();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}