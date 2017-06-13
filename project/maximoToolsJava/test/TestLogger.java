//import java.io.PrintWriter;

import java.io.PrintWriter;

import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.varia.LevelMatchFilter;

import org.maximo.app.MTException;


public class TestLogger {
	public static void main(String[] args) {
		Logger logger=null;
//		logger=Logger.getLogger(TestLogger.class);
		 logger=Logger.getLogger("printLogs");
		 //新建一個控制台輸出點
		 org.apache.log4j.ConsoleAppender consoleAppender = new org.apache.log4j.ConsoleAppender();
		 consoleAppender.setName("syso");
//		 consoleAppender.setLayout(new PatternLayout("%-4r [%t] %-5p %c %x - %m%n"));
		 consoleAppender.setLayout(new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n"));
		 consoleAppender.setLayout(new PatternLayout("%d{ABSOLUTE} %5p %c{1}:%L - %m%n"));
		 consoleAppender.setThreshold(Level.DEBUG);
		 consoleAppender.setWriter(new PrintWriter(System.out));
		 
		 logger.addAppender(consoleAppender);
//		 logger.callAppenders(loggingEvent);
//		 logger=Logger.getLogger(TestLogger.class);
		 //配置文件為DEBUG,可以設置為INFO,只能能設置比配置文件更低的級別
//		 logger.setLevel(Level.INFO);
		 //如果配置文件為INFO級別,則無法設為DEBUG
		 logger.setLevel(Level.DEBUG);
		 logger.fatal("致命級別");
		logger.warn("測試警告");
		logger.error("測試錯誤");
		logger.info("asus");
		logger.info("asus",new MTException("測試 info 報錯"));
		logger.debug("linux");
		System.out.println("asus");
		psdi.util.MXApplicationException ass;
	}

}
