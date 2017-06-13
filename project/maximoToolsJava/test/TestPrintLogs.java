import com.maximo.app.MTException;
import com.shoukaiseki.tuuyou.logger.PrintLogs;


public class TestPrintLogs {
	public static void main(String[] args) {
		PrintLogs logger=new PrintLogs();
		
		 logger.fatal("致命級別");
		logger.warn("測試警告");
		logger.error("測試錯誤");
		logger.info("asus");
		logger.info("asus",new MTException("測試 info 報錯"));
		logger.debug("linux");
	}

}
