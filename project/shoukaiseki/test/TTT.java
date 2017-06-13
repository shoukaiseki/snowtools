import org.shoukaiseki.tuuyou.logger.config.ConfigPath;

public class TTT {
	public static void main(String[] args) {
		System.out.println("hello");
		System.out.println("hello".substring(0,1));
		String path=ConfigPath.CONFIG_PATH;
		System.out.println("path="+path);
	}

}
